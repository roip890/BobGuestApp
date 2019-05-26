package com.bob.bobguestapp.activities.checkin;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.checkin.connector.CheckInConnector;
import com.bob.bobguestapp.activities.checkin.fragments.choosehotel.CheckInChooseHotelFragment;
import com.bob.bobguestapp.activities.checkin.fragments.code.CheckInCodeFragment;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.uimodule.others.BackgroundColorTimer;
import com.bob.uimodule.views.loadingcontainer.ManagementActivity;
import com.bob.uimodule.views.viewpager.DynamicHeightRtlViewPager;
import com.bob.uimodule.views.viewpager.DynamicHeightViewPagerAdapter;
import com.bob.uimodule.views.viewpager.RtlViewPager;
import com.bob.uimodule.views.viewpager.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import static android.view.View.VISIBLE;

/**
 * Created by user on 27/09/2017.
 */


public class CheckInActivity extends ManagementActivity implements CheckInConnector {

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

    //app theme
    private int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    private int screenSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;

    //screen states
    private static final int MAIN_VIEW = 3;
    private static final int CHOOSE_HOTEL = 4;
    private static final int CODE = 5;

    //tab indices
    private static final int CHOOSE_HOTEL_TAB_INDEX = 0;
    private static final int CODE_TAB_INDEX = 0;

    //background
    private ConstraintLayout backgroundLayout;
    private int backgroundColorPrimary, backgroundColorLight;
    private BackgroundColorTimer backgroundColorTimer;

    //main view
    private RelativeLayout mainViewLayout;
    private TabLayout tabLayout;
    private RtlViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    //choose hotel
    private CheckInChooseHotelFragment checkInChooseHotelFragment;

    //code
    private CheckInCodeFragment checkInCodeFragment;

    public CheckInActivity() {

    }

    @Override
    protected View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this).getSkin(appTheme, MyAppThemeUtilsManager.INTRO_ACTIVITY_SKIN);

        //background
        this.initBackgroundAnimation();

        //view
        View view = inflater.inflate(R.layout.activity_check_in, container, true);

        //layouts
        this.initMainViewLayout(view);

        //status bar color
        this.initStatusBarColor();

        return view;

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.backgroundColorTimer.startTimer();

        this.managementViewContainer.setScreenState(this.managementViewContainer.getScreenState());
//        this.getLastRequestedFocus();
    }

    @Override
    protected void onPause() {
        this.backgroundColorTimer.stopTimer();

        //        this.saveLastRequestedFocus();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //management
    private void initBackgroundAnimation() {

        this.backgroundColorPrimary = MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_SCREEN_BACKGROUND_COLOR_PRIMARY, this.screenSkin);
        this.backgroundColorLight = MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_SCREEN_BACKGROUND_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN);

        this.backgroundColorTimer = new BackgroundColorTimer(this.backgroundLayout, this.backgroundColorPrimary, this.backgroundColorPrimary);
        this.backgroundColorTimer.setColorIntervals(1, 0, 0, 1, 0);
        this.backgroundColorTimer.setColorValuesRange(1, 0, 0, 30, 0);

    }

    private void initStatusBarColor() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(this.getResources().getColor(R.color.transparent));
                window.setNavigationBarColor(MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_STATUS_BAR_BACKGROUND_COLOR_PRIMARY, this.screenSkin));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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
            case CHOOSE_HOTEL:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setChooseHotelState();
                break;
            case CODE:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setCodeState();
                break;
            default:
                break;

        }

    }

    protected void setChooseHotelState() {

        if (this.viewPagerAdapter == null) {

            this.viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        }

        if (!this.viewPager.getAdapter().equals(this.viewPagerAdapter)) {

            this.viewPager.setAdapter(this.viewPagerAdapter);

        }

        this.viewPagerAdapter.clearFragments();

        this.viewPagerAdapter.addFragment(this.checkInChooseHotelFragment, "Choose Hotel");

        this.viewPagerAdapter.notifyDataSetChanged();

        this.viewPager.setCurrentItem(CHOOSE_HOTEL_TAB_INDEX);

    }

    protected void setCodeState() {

        if (this.viewPagerAdapter == null) {

            this.viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        }

        if (!this.viewPager.getAdapter().equals(this.viewPagerAdapter)) {

            this.viewPager.setAdapter(this.viewPagerAdapter);

        }

        this.viewPagerAdapter.clearFragments();

        this.viewPagerAdapter.addFragment(this.checkInCodeFragment, "Code");

        this.viewPagerAdapter.notifyDataSetChanged();

        this.viewPager.setCurrentItem(CODE_TAB_INDEX);

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
        this.mainViewLayout = (RelativeLayout) view.findViewById(R.id.intro_activity_main_layout);

        //main view background animation
        this.initMainViewBackgroundDrawable();

    }

    private void initMainViewBackgroundDrawable() {

        GradientDrawable shapeDrawable = new GradientDrawable();
//        shapeDrawable.setColors(new int[]{
//                MyAppThemeUtilsManager.get().getColor(ThemeUtilsManager.DEFAULT_BASE_COLOR, MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN),
//                MyAppThemeUtilsManager.get().getColor(ThemeUtilsManager.DEFAULT_BASE_COLOR, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
//        });
        shapeDrawable.setColors(new int[]{
                Color.parseColor("#2193b0"),
                Color.parseColor("#6dd5ed")
//                MyAppThemeUtilsManager.get().getColor(ThemeUtilsManager.DEFAULT_BASE_COLOR, MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN),
//                MyAppThemeUtilsManager.get().getColor(ThemeUtilsManager.DEFAULT_BASE_COLOR, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
        });
        shapeDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        shapeDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        shapeDrawable.setCornerRadius(0f);
        this.backgroundLayout.setBackground(shapeDrawable);
    }

    private void initFragments() {

        //login
        this.checkInChooseHotelFragment = new CheckInChooseHotelFragment();
        this.checkInChooseHotelFragment.setCheckInConnector(this);

        //register
        this.checkInCodeFragment = new CheckInCodeFragment();
        this.checkInCodeFragment.setCheckInConnector(this);

    }

    private void initTabLayout(View view) {

        this.tabLayout = (TabLayout) view.findViewById(R.id.intro_activity_tabs);

        this.tabLayout.post(new Runnable() {
            @Override
            public void run() {

                CheckInActivity.this.tabLayout.setTabTextColors(
                        MyAppThemeUtilsManager.get(CheckInActivity.this).getColor(MyAppThemeUtilsManager.DEFAULT_TAB_TEXT_COLOR, CheckInActivity.this.screenSkin),
                        MyAppThemeUtilsManager.get(CheckInActivity.this).getColor(MyAppThemeUtilsManager.DEFAULT_TAB_TEXT_COLOR, CheckInActivity.this.screenSkin)
                );
                CheckInActivity.this.tabLayout.setSelectedTabIndicatorColor(
                        ContextCompat.getColor(CheckInActivity.this, R.color.transparent)
                );

                ViewGroup tabViewGroup = (ViewGroup) CheckInActivity.this.tabLayout.getChildAt(0);
                int tabsCount = tabViewGroup.getChildCount();
                for (int j = 0; j < tabsCount; j++) {
                    ViewGroup vgTab = (ViewGroup) tabViewGroup.getChildAt(j);
                    int tabChildCount = vgTab.getChildCount();
                    for (int i = 0; i < tabChildCount; i++) {
                        View tabViewChild = vgTab.getChildAt(i);
                        if (tabViewChild instanceof TextView) {
                            ((TextView) tabViewChild).setTextSize(CheckInActivity.this.getResources().getDimension(R.dimen.text_size_large));
                        }
                    }
                }

            }
        });

    }

    private void initViewPager(View view) {

        this.viewPager = (RtlViewPager) view.findViewById(R.id.intro_activity_view_pager);

        //adapter
        this.viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        this.viewPager.setAdapter(this.viewPagerAdapter);

        /* the ViewPager requires a minimum of 1 as OffscreenPageLimit */
        int limit = (this.viewPagerAdapter.getCount() > 1 ? this.viewPagerAdapter.getCount() - 1 : 1);
        this.viewPager.setOffscreenPageLimit(limit);

        this.tabLayout.post(new Runnable() {
            @Override
            public void run() {

                CheckInActivity.this.tabLayout.setupWithViewPager(CheckInActivity.this.viewPager);

            }
        });

    }

}
