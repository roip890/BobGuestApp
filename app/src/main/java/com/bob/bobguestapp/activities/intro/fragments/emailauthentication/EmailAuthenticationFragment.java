package com.bob.bobguestapp.activities.intro.fragments.emailauthentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.intro.connector.IntroConnector;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementFragment;
import com.bob.uimodule.views.textviews.MyButton;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class EmailAuthenticationFragment extends ManagementFragment {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //login url
    private static final String LOGIN_URL = BOB_SERVER_MOBILE_SERVICES_URL + "/login/login";

    //sms verification
    private static final String SMS_VERIFY_URL = BOB_SERVER_MOBILE_SERVICES_URL +"/MobileRegister/Verify";

    //sms resend code
    private static final String SMS_RESEND_VERIFICATION_CODE_URL = BOB_SERVER_MOBILE_SERVICES_URL +"/MobileRegister/ResendVerificationCode";

    //get hotels url
    private static final String GET_ALL_HOTELS = BOB_SERVER_MOBILE_SERVICES_URL + "/login/getHotelsAndIcons";

    //screen states
    public static final int EMAIL_AUTHENTICATION = 10;

    //intro commands
    private IntroConnector introConnector;

    //email authentication
    private RelativeLayout emailAuthenticationLayout;
    private MyButton emailAuthenticationButton;
    private TextView emailAuthenticationTextView;

    public EmailAuthenticationFragment() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.EMAIL_AUTHENTICATION_FRAGMENT_SKIN);

    }

    public static EmailAuthenticationFragment newInstance() {
        EmailAuthenticationFragment fragment = new EmailAuthenticationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //get arguments
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }


    }


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //init management skin
        this.managementViewContainer.setScreenSkin(this.screenSkin);

        //view
        View view = inflater.inflate(R.layout.fragment_email_authentication, container, false);

        //init requests list layouts
        this.initEmailAuthenticationLayout(view);

        this.managementViewContainer.setScreenState(EMAIL_AUTHENTICATION);

        return view;

    }

    //main view screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.emailAuthenticationLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case EMAIL_AUTHENTICATION:
                this.emailAuthenticationLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //intro commands
    public void setIntroConnector(IntroConnector introConnector) {
        this.introConnector = introConnector;
    }

    //email authentication
    private void initEmailAuthenticationLayout(View view) {

        //email authentication layout
        this.initEmailAuthenticationMainLayout(view);

        //email authentication text view
        this.initEmailAuthenticationTextView(view);

        //email authentication button
        this.initEmailAuthenticationButton(view);

    }

    private void initEmailAuthenticationMainLayout(View view) {

        this.emailAuthenticationLayout = (RelativeLayout) view.findViewById(R.id.email_authentication_fragment_email_authentication_layout);

    }

    private void initEmailAuthenticationTextView(View view) {

        this.emailAuthenticationTextView = (TextView) view.findViewById(R.id.email_authentication_fragment_email_authentication_text_view);
        MyAppThemeUtilsManager.get().initTextView(this.emailAuthenticationTextView, "A confirmation link\nhas sent to your\nemail address.", this.getContext(), this.screenSkin);
        this.emailAuthenticationTextView.setMaxLines(5);
        this.emailAuthenticationTextView.setTextSize((int) this.getResources().getDimension(R.dimen.default_field_info_text_size));
        this.emailAuthenticationTextView.setTextColor(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_TEXT_COLOR, this.screenSkin));


    }

    private void initEmailAuthenticationButton(View view) {

        this.emailAuthenticationButton = (MyButton) view.findViewById(R.id.email_authentication_fragment_email_authentication_button);
        MyAppThemeUtilsManager.get().initMyButton(this.emailAuthenticationButton, "Confirm", this.getContext(), this.screenSkin);
        this.emailAuthenticationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EmailAuthenticationFragment.this.onEmailAuthenticationButtonClick();

            }
        });
        this.emailAuthenticationButton.setEnabled(false);
        this.emailAuthenticationButton.setWidth((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 260));
        this.emailAuthenticationButton.setButtonBorderRadius((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 30));

    }

    private void onEmailAuthenticationButtonClick(){

    }

    //tools
    // TODO: 04/10/2018 write this function
    private boolean isValidHotelName(String text) {
        return true;
    }

        @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        this.mListener = null
    }


    //back pressed handler
    public void onBackPressed() {

    }
}
