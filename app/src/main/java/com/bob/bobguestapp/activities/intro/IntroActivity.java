package com.bob.bobguestapp.activities.intro;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.intro.connector.IntroConnector;
import com.bob.bobguestapp.activities.intro.fragments.authentication.AuthenticationFragment;
import com.bob.bobguestapp.activities.intro.fragments.emailauthentication.EmailAuthenticationFragment;
import com.bob.bobguestapp.activities.intro.fragments.login.LoginFragment;
import com.bob.bobguestapp.activities.intro.fragments.register.RegisterFragment;
import com.bob.bobguestapp.activities.intro.fragments.resetpassword.ResetPasswordFragment;
import com.bob.bobguestapp.activities.intro.fragments.smsauthentication.SMSAuthenticationFragment;
import com.bob.bobguestapp.activities.main.MainActivity;
import com.bob.bobguestapp.activities.main.fragments.profile.fragments.profilestats.ProfileStatsFragment;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.uimodule.others.BackgroundColorTimer;
import com.bob.uimodule.views.loadingcontainer.ManagementActivity;
import com.bob.uimodule.views.progressbar.MyProgressBar;
import com.bob.uimodule.views.progressbar.OnProgressIntervalListener;
import com.bob.uimodule.views.progressbar.ProgressBarTimer;
import com.bob.uimodule.views.viewpager.DynamicHeightRtlViewPager;
import com.bob.uimodule.views.viewpager.DynamicHeightViewPagerAdapter;
import com.bob.uimodule.views.viewpager.RtlViewPager;
import com.bob.uimodule.views.viewpager.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.view.View.VISIBLE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.LOADING;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.MESSAGE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.NONE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.PROGRESS;

/**
 * Created by user on 27/09/2017.
 */


public class IntroActivity extends ManagementActivity implements IntroConnector {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //check session url
    private static final String CHECK_SESSION_URL = BOB_SERVER_MOBILE_SERVICES_URL + "/login/CheckSession";

    //screen states
    private static final int MAIN_VIEW = 3;
    private static final int LOGIN = 4;
    private static final int REGISTER = 5;
    private static final int AUTHENTICATION = 6;
    private static final int EMAIL_AUTHENTICATION = 7;
    private static final int SMS_AUTHENTICATION = 8;
    private static final int RESET_PASSWORD = 9;

    //tab indices
    private static final int LOGIN_TAB_INDEX = 0;
    private static final int REGISTER_TAB_INDEX = 1;
    private static final int AUTHENTICATION_TAB_INDEX = 0;
    private static final int EMAIL_AUTHENTICATION_TAB_INDEX = 0;
    private static final int SMS_AUTHENTICATION_TAB_INDEX = 0;
    private static final int RESET_PASSWORD_TAB_INDEX = 0;

    //background
    private int backgroundColorPrimary, backgroundColorLight;
    private BackgroundColorTimer backgroundColorTimer;

    //main view
    private ConstraintLayout mainViewLayout;
    private TabLayout tabLayout;
    private DynamicHeightRtlViewPager viewPager;
    private DynamicHeightViewPagerAdapter viewPagerAdapter;

    //login
    private LoginFragment loginFragment;

    //register
    private RegisterFragment registerFragment;

    //authentication
    private AuthenticationFragment authenticationFragment;

    //email authentication
    private EmailAuthenticationFragment emailAuthenticationFragment;

    //sms authentication
    private SMSAuthenticationFragment smsAuthenticationFragment;

    //reset password
    private ResetPasswordFragment resetPasswordFragment;

    //check session
    private boolean checkingSession, isSessionValid;
    private ProgressBarTimer checkSessionProgressBarTimer;

    public IntroActivity() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this).getSkin(appTheme, MyAppThemeUtilsManager.INTRO_ACTIVITY_SKIN);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        this.managementViewContainer.setScreenState(PROGRESS);
//        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
//        IntroActivity.this.startActivity(intent);


    }


    @Override
    protected View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {


        //background
        this.initBackgroundAnimation();

        //view
        View view = inflater.inflate(R.layout.activity_intro, container, false);

        //layouts
        this.initMainViewLayout(view);

        //status bar color
        this.initStatusBarColor();

        //check session
        this.checkSession();


//        COMMENT THIS!!
//        BOBGuestApplication.get().getSecureSharedPreferences().edit().putInt("userId", 15).apply();
//        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
//        IntroActivity.this.startActivity(intent);

//        this.managementViewContainer.getManagementContainer().setVisibility(VISIBLE);
//        this.managementViewContainer.getLoadingLayout().setVisibility(VISIBLE);

        return view;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.backgroundColorTimer != null
                && !this.backgroundColorTimer.isRunning()
                && this.managementViewContainer.getManagementContainerScrollView().getVisibility() == VISIBLE) {
            this.backgroundColorTimer.startTimer();
        } else {
            this.initMainViewBackgroundDrawable();
        }
        if (this.checkSessionProgressBarTimer != null
                && !this.checkSessionProgressBarTimer.isRunning()
                && this.managementViewContainer.getScreenState() == PROGRESS
                && this.checkingSession) {
            this.checkSessionProgressBarTimer.startTimer();
        }
        this.managementViewContainer.setScreenState(this.managementViewContainer.getScreenState());
        IntroActivity.this.setBackground(this.managementViewContainer.getScreenState());
//        this.getLastRequestedFocus();
    }

    @Override
    protected void onPause() {

        if (this.backgroundColorTimer != null
                && this.backgroundColorTimer.isRunning()
                && this.managementViewContainer.getManagementContainerScrollView().getVisibility() == VISIBLE) {

            this.backgroundColorTimer.stopTimer();

        }

        if (this.checkSessionProgressBarTimer != null
                && this.checkSessionProgressBarTimer.isRunning()
                && this.managementViewContainer.getScreenState() == PROGRESS
                && this.checkingSession) {

            this.checkSessionProgressBarTimer.stopTimer();

        }
//        this.saveLastRequestedFocus();
        super.onPause();
    }

    //management
    private void initBackgroundAnimation() {

        this.backgroundColorPrimary = MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_SCREEN_BACKGROUND_COLOR_PRIMARY, this.screenSkin);
        this.backgroundColorLight = MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_SCREEN_BACKGROUND_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN);

        this.backgroundColorTimer = new BackgroundColorTimer(this.managementViewContainer.getBackgroundLayout(), this.backgroundColorPrimary, this.backgroundColorPrimary);
        this.backgroundColorTimer.setColorIntervals(1, 0, 0, 1, 0);
        this.backgroundColorTimer.setColorValuesRange(1, 0, 0, 30, 0);

    }

    private void initStatusBarColor() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_STATUS_BAR_BACKGROUND_COLOR_PRIMARY, this.screenSkin));
                window.setNavigationBarColor(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_NAVIGATION_BAR_BACKGROUND_COLOR_PRIMARY, this.screenSkin));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setMainViewScreenState(int screenState) {

        this.mainViewLayout.setVisibility(View.INVISIBLE);

        switch (screenState) {

            case MAIN_VIEW:
                this.mainViewLayout.setVisibility(VISIBLE);
                break;
            case LOGIN:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setLoginState();
                break;
            case REGISTER:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setRegisterState();
                break;
            case AUTHENTICATION:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setAuthenticationState();
                break;
            case EMAIL_AUTHENTICATION:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setEmailAuthenticationState();
                break;
            case SMS_AUTHENTICATION:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setSMSAuthenticationState();
                break;
            case RESET_PASSWORD:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setResetPasswordState();
                break;

        }

    }

    protected void setBackground(int backgroundType) {

        switch (backgroundType) {

            case LOADING: case MESSAGE: case PROGRESS: case NONE:
                if (this.backgroundColorTimer != null) {
                    this.backgroundColorTimer.startTimer();
                }
                break;
            case MAIN_VIEW: case LOGIN: case REGISTER: case AUTHENTICATION: case EMAIL_AUTHENTICATION: case SMS_AUTHENTICATION: case RESET_PASSWORD:
                if (this.backgroundColorTimer != null) {
                    this.backgroundColorTimer.stopTimer();
                }
                this.initMainViewBackgroundDrawable();
                break;
            default:
                if (this.backgroundColorTimer != null) {
                    this.backgroundColorTimer.stopTimer();
                }
                this.initMainViewBackgroundDrawable();
                break;
        }

    }

    protected void setLoginState() {

        if (this.viewPagerAdapter == null) {

            this.viewPagerAdapter = new DynamicHeightViewPagerAdapter(this.getSupportFragmentManager());

        }

        if (!this.viewPager.getAdapter().equals(this.viewPagerAdapter)) {

            this.viewPager.setAdapter(this.viewPagerAdapter);

        }

        this.viewPagerAdapter.clearFragments();

        this.viewPagerAdapter.addFragment(this.loginFragment, "Sign In");
        this.viewPagerAdapter.addFragment(this.registerFragment, "Sign Up");

        this.viewPager.setAdapter(this.viewPagerAdapter);

        this.viewPager.setCurrentItem(LOGIN_TAB_INDEX);

    }

    protected void setRegisterState() {

        if (this.viewPagerAdapter == null) {

            this.viewPagerAdapter = new DynamicHeightViewPagerAdapter(this.getSupportFragmentManager());

        }

        if (!this.viewPager.getAdapter().equals(this.viewPagerAdapter)) {

            this.viewPager.setAdapter(this.viewPagerAdapter);

        }

        this.viewPagerAdapter.clearFragments();

        this.viewPagerAdapter.addFragment(this.loginFragment, "Sign In");
        this.viewPagerAdapter.addFragment(this.registerFragment, "Sign Up");

        this.viewPager.setAdapter(this.viewPagerAdapter);

        this.viewPager.setCurrentItem(REGISTER_TAB_INDEX);

    }

    protected void setAuthenticationState() {

        if (this.viewPagerAdapter == null) {

            this.viewPagerAdapter = new DynamicHeightViewPagerAdapter(this.getSupportFragmentManager());

        }

        if (!this.viewPager.getAdapter().equals(this.viewPagerAdapter)) {

            this.viewPager.setAdapter(this.viewPagerAdapter);

        }

        this.viewPagerAdapter.clearFragments();

        this.viewPagerAdapter.addFragment(this.authenticationFragment, "Authentication");

        this.viewPager.setAdapter(this.viewPagerAdapter);

        this.viewPager.setCurrentItem(AUTHENTICATION_TAB_INDEX);

    }

    protected void setEmailAuthenticationState() {

        if (this.viewPagerAdapter == null) {

            this.viewPagerAdapter = new DynamicHeightViewPagerAdapter(this.getSupportFragmentManager());

        }

        if (!this.viewPager.getAdapter().equals(this.viewPagerAdapter)) {

            this.viewPager.setAdapter(this.viewPagerAdapter);

        }

        this.viewPagerAdapter.clearFragments();

        this.viewPagerAdapter.addFragment(this.emailAuthenticationFragment, "Email Authentication");

        this.viewPager.setAdapter(this.viewPagerAdapter);

        this.viewPager.setCurrentItem(EMAIL_AUTHENTICATION_TAB_INDEX);

    }

    protected void setSMSAuthenticationState() {

        if (this.viewPagerAdapter == null) {

            this.viewPagerAdapter = new DynamicHeightViewPagerAdapter(this.getSupportFragmentManager());

        }

        if (!this.viewPager.getAdapter().equals(this.viewPagerAdapter)) {

            this.viewPager.setAdapter(this.viewPagerAdapter);

        }

        this.viewPagerAdapter.clearFragments();

        this.viewPagerAdapter.addFragment(this.smsAuthenticationFragment, "Phone Authentication");

        this.viewPager.setAdapter(this.viewPagerAdapter);

        this.viewPager.setCurrentItem(SMS_AUTHENTICATION_TAB_INDEX);

    }

    protected void setResetPasswordState() {

        if (this.viewPagerAdapter == null) {

            this.viewPagerAdapter = new DynamicHeightViewPagerAdapter(this.getSupportFragmentManager());

        }

        if (!this.viewPager.getAdapter().equals(this.viewPagerAdapter)) {

            this.viewPager.setAdapter(this.viewPagerAdapter);

        }

        this.viewPagerAdapter.clearFragments();

        this.viewPagerAdapter.addFragment(this.resetPasswordFragment, "Reset Password");

        this.viewPager.setAdapter(this.viewPagerAdapter);

        this.viewPager.setCurrentItem(RESET_PASSWORD_TAB_INDEX);
        

    }

    //main view
    private void initMainViewLayout(View view) {

        //main view
        this.initMainViewBackground(view);

        //fragments
        this.initFragments();

        //tab layout
        this.initTabLayout(view);

        //view pager
        this.initViewPager(view);

    }

    private void initMainViewBackground(View view) {

        //main view background
        this.mainViewLayout = (ConstraintLayout) view.findViewById(R.id.intro_activity_main_layout);

        //main view background animation
        this.initMainViewBackgroundDrawable();

    }

    private void initMainViewBackgroundDrawable() {

        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setColors(new int[]{
                MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_ACTIVITY_BACKGROUND_COLOR_PRIMARY, this.screenSkin),
                MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_ACTIVITY_BACKGROUND_COLOR_SECONDARY, this.screenSkin)
        });

        shapeDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        shapeDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        shapeDrawable.setCornerRadius(0f);
        this.managementViewContainer.getBackgroundLayout().setBackground(shapeDrawable);

    }

    private void initFragments() {

        //login
        this.loginFragment = new LoginFragment();
        this.loginFragment.setIntroConnector(this);

        //register
        this.registerFragment = new RegisterFragment();
        this.registerFragment.setIntroConnector(this);

        //authentication
        this.authenticationFragment = new AuthenticationFragment();
        this.authenticationFragment.setIntroConnector(this);

        //email authentication
        this.emailAuthenticationFragment= new EmailAuthenticationFragment();
        this.emailAuthenticationFragment.setIntroConnector(this);

        //sms authentication
        this.smsAuthenticationFragment = new SMSAuthenticationFragment();
        this.smsAuthenticationFragment.setIntroConnector(this);

        //reset password
        this.resetPasswordFragment = new ResetPasswordFragment();
        this.resetPasswordFragment.setIntroConnector(this);


    }

    private void initTabLayout(View view) {

        this.tabLayout = (TabLayout) view.findViewById(R.id.intro_activity_tabs);

        this.tabLayout.post(new Runnable() {
            @Override
            public void run() {

                IntroActivity.this.tabLayout.setTabTextColors(
                        MyAppThemeUtilsManager.get(IntroActivity.this).getColor(MyAppThemeUtilsManager.DEFAULT_TAB_TEXT_COLOR, IntroActivity.this.screenSkin),
                        MyAppThemeUtilsManager.get(IntroActivity.this).getColor(MyAppThemeUtilsManager.DEFAULT_TAB_TEXT_COLOR, IntroActivity.this.screenSkin)
                );
                IntroActivity.this.tabLayout.setSelectedTabIndicatorColor(
                        ContextCompat.getColor(IntroActivity.this, R.color.transparent)
                );

                ViewGroup tabViewGroup = (ViewGroup) IntroActivity.this.tabLayout.getChildAt(0);
                int tabsCount = tabViewGroup.getChildCount();
                for (int j = 0; j < tabsCount; j++) {
                    ViewGroup vgTab = (ViewGroup) tabViewGroup.getChildAt(j);
                    int tabChildCount = vgTab.getChildCount();
                    for (int i = 0; i < tabChildCount; i++) {
                        View tabViewChild = vgTab.getChildAt(i);
                        if (tabViewChild instanceof TextView) {
                            ((TextView) tabViewChild).setTextSize(IntroActivity.this.getResources().getDimension(R.dimen.text_size_large));
                        }
                    }
                }

            }
        });

    }

    private void initViewPager(View view) {

        this.viewPager = (DynamicHeightRtlViewPager) view.findViewById(R.id.intro_activity_view_pager);

        //adapter
        this.viewPagerAdapter = new DynamicHeightViewPagerAdapter(this.getSupportFragmentManager());

        this.viewPagerAdapter.addFragment(this.loginFragment, "Sign In");
        this.viewPagerAdapter.addFragment(this.registerFragment, "Sign Up");

        this.viewPager.setAdapter(this.viewPagerAdapter);

        /* the ViewPager requires a minimum of 1 as OffscreenPageLimit */
        int limit = (this.viewPagerAdapter.getCount() > 1 ? this.viewPagerAdapter.getCount() - 1 : 1);
        this.viewPager.setOffscreenPageLimit(limit);

        this.tabLayout.post(new Runnable() {
            @Override
            public void run() {

                IntroActivity.this.tabLayout.setupWithViewPager(IntroActivity.this.viewPager);

            }
        });

    }

    //session
    private void initSessionState() {
        this.isSessionValid = false;
        this.checkingSession = false;
    }

    private void initCheckSessionProgressBarTimer() {

        this.checkSessionProgressBarTimer = new ProgressBarTimer(this.managementViewContainer.getProgressBar(), 1, 10, new OnProgressIntervalListener() {

            @Override
            public boolean OnProgressInterval(MyProgressBar progressBar, int progressInterval) {

                if (IntroActivity.this.isSessionValid) {
                    IntroActivity.this.checkSessionProgressBarTimer.stopTimer();
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    IntroActivity.this.startActivity(intent);
                } else {
                    if (IntroActivity.this.checkingSession) {
                        if (progressBar.getProgress() + progressInterval <= 100) {
                            progressBar.incrementProgressBy(progressInterval);
                        }
                    } else {
                        if (progressBar.getProgress() + progressInterval <= 100) {
                            progressBar.incrementProgressBy(progressInterval);
                            return true;
                        } else {
                            IntroActivity.this.managementViewContainer.setScreenState(LOGIN);
                            IntroActivity.this.setBackground(LOGIN);
                            return false;
                        }
                    }
                }

                return true;
            }
        });
    }

    private void checkSession() {

        //init check session state
        this.initSessionState();

        //check session request
        this.makeCheckSessionRequest();
    }

    private void makeCheckSessionRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {
                IntroActivity.this.isSessionValid = false;
                IntroActivity.this.checkingSession = true;
                IntroActivity.this.managementViewContainer.setScreenState(PROGRESS);
                IntroActivity.this.setBackground(PROGRESS);
                IntroActivity.this.initCheckSessionProgressBarTimer();
                IntroActivity.this.checkSessionProgressBarTimer.startTimer();
            }

            @Override
            protected boolean requestCondition() {
                String email = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestEmail", null);
                String guid = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                if ((guid != null) && (email != null)) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {
                try {

                    String email = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestEmail", null);
                    int id = BOBGuestApplication.get().getSecureSharedPreferences().getInt("guestId", -1);

                    Guest guest = new Guest();
                    guest.setEmail(email);
                    guest.setId(id);
                    JsonElement jsonGuest = MyGsonParser.getParser().create().toJsonTree(guest, Guest.class);


                    JsonObject jsonRequest = new JsonObject();
                    jsonRequest.add("guest", jsonGuest);

                    JsonObject jsonCheckSessionRequest = new JsonObject();
                    jsonCheckSessionRequest.add("request", jsonRequest);

                    return new JSONObject( new Gson().toJson(jsonCheckSessionRequest));

                } catch (JSONException e) {

                    e.printStackTrace();
                    return null;

                }

            }

            @Override
            protected String getRequestUrl() {
                return CHECK_SESSION_URL;
            }

            @Override
            protected ApplicativeResponse getApplicativeResponse(JSONObject response) {
                try {
                    Gson customGson = MyGsonParser.getParser().create();
                    String statusResponse = response.getJSONObject("response").getJSONObject("statusResponse").toString();

                    return customGson.fromJson(statusResponse, ApplicativeResponse.class);
                } catch (JSONException e) {
                    ApplicativeResponse statusResponse = new ApplicativeResponse();
                    statusResponse.setStatus("Failure");
                    statusResponse.setCode(ApplicativeResponse.FAILURE);
                    statusResponse.setMessage("error in parsing response");
                    return statusResponse;
                }
            }

            @Override
            protected void onSuccess(JSONObject response) {

                IntroActivity.this.checkingSession = false;
                IntroActivity.this.isSessionValid = false;
//                IntroActivity.this.managementViewContainer.setMessage("Yo Yo!!", SUCCESS_MESSAGE);
////                SWITCH TO THIS!!!!
////                IntroActivity.this.checkingSession = false;
////                IntroActivity.this.isSessionValid = true;
            }

            @Override
            protected void onDefaultError(String message) {
                IntroActivity.this.checkingSession = false;
                IntroActivity.this.isSessionValid = false;
//                IntroActivity.this.managementViewContainer.setMessage("Yo Yo!!", FAILURE_MESSAGE);
            }

        };

        jsonServerRequest.makeRequest();

    }

    @Override
    public void emailVerification(int guestId, String email) {

    }

    @Override
    public void smsVerification(int guestId, String phone) {

    }

    @Override
    public void login(String guestEmail, String guestPassword) {

    }


    @Override
    public void onBackPressed() {
        switch (this.viewPager.getCurrentItem()) {
            case LOGIN_TAB_INDEX:
                this.loginFragment.onBackPressed();
                break;
            case REGISTER_TAB_INDEX:
                this.registerFragment.onBackPressed();
                break;
            default:
                break;

        }
    }

}
