package com.bob.bobguestapp.activities.intro.fragments.authentication;

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

public class AuthenticationFragment extends ManagementFragment {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //sms verification
    private static final String SMS_VERIFY_URL = BOB_SERVER_MOBILE_SERVICES_URL +"/MobileRegister/Verify";

    //sms resend code
    private static final String SMS_RESEND_VERIFICATION_CODE_URL = BOB_SERVER_MOBILE_SERVICES_URL +"/MobileRegister/ResendVerificationCode";

    //get hotels url
    private static final String GET_ALL_HOTELS = BOB_SERVER_MOBILE_SERVICES_URL + "/login/getHotelsAndIcons";

    //screen states
    public static final int AUTHENTICATION = 10;

    //intro commands
    private IntroConnector introConnector;

    //authentication
    private RelativeLayout authenticationLayout;
    private MyButton smsAuthenticationButton;
    private MyButton emailAuthenticationButton;
    private RelativeLayout authenticationButtonsIntermediate;
    private View authenticationButtonsIntermediateBeforeLine;
    private View authenticationButtonsIntermediateAfterLine;
    private TextView authenticationButtonsIntermediateTextView;

    public AuthenticationFragment() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.AUTHENTICATION_FRAGMENT_SKIN);

    }

    public static AuthenticationFragment newInstance() {
        AuthenticationFragment fragment = new AuthenticationFragment();
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
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);

        //init requests list layouts
        this.initAuthenticationLayout(view);

        this.managementViewContainer.setScreenState(AUTHENTICATION);

        return view;

    }

    //main view screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.authenticationLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case AUTHENTICATION:
                this.authenticationLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //intro commands
    public void setIntroConnector(IntroConnector introConnector) {
        this.introConnector = introConnector;
    }

    //authentication
    private void initAuthenticationLayout(View view) {

        //authentication main layout
        this.initAuthenticationMainLayout(view);

        //sms authentication button
        this.initSMSAuthenticationButton(view);

        //email authentication button
        this.initEmailAuthenticationButton(view);

        //authentication buttons intermediate
        this.initAuthenticationButtonsIntermediateView(view);

    }

    private void initAuthenticationMainLayout(View view) {
        this.authenticationLayout = (RelativeLayout) view.findViewById(R.id.authentication_fragment_authentication_layout);
    }

    private void initSMSAuthenticationButton(View view) {

        this.smsAuthenticationButton = (MyButton) view.findViewById(R.id.authentication_fragment_sms_authentication_button);
        MyAppThemeUtilsManager.get().initMyButton(this.smsAuthenticationButton, "Verify By Phone", this.getContext(), this.screenSkin);
        this.smsAuthenticationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AuthenticationFragment.this.onSMSAuthenticationButtonClick();

            }
        });
        this.smsAuthenticationButton.setEnabled(false);
        this.smsAuthenticationButton.setWidth((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 260));
        this.smsAuthenticationButton.setButtonBorderRadius((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 30));


    }

    private void initEmailAuthenticationButton(View view) {

        this.emailAuthenticationButton = (MyButton) view.findViewById(R.id.authentication_fragment_email_authentication_button);
        MyAppThemeUtilsManager.get().initMyButton(this.emailAuthenticationButton, "Verify By Email", this.getContext(), this.screenSkin);
        this.emailAuthenticationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AuthenticationFragment.this.onEmailAuthenticationButtonClick();

            }
        });
        this.emailAuthenticationButton.setEnabled(false);
        this.emailAuthenticationButton.setWidth((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 260));
        this.emailAuthenticationButton.setButtonBorderRadius((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 30));


    }

    private void initAuthenticationButtonsIntermediateView(View view) {

        this.initAuthenticationButtonsIntermediateLayout(view);

        this.initAuthenticationButtonsIntermediateLayoutBeforeLine(view);

        this.initAuthenticationButtonsIntermediateLayoutTextView(view);

        this.initAuthenticationButtonsIntermediateLayoutAfterLine(view);

    }

    private void initAuthenticationButtonsIntermediateLayout(View view) {

        this.authenticationButtonsIntermediate = (RelativeLayout) view.findViewById(R.id.authentication_fragment_authentication_buttons_intermediate);

    }

    private void initAuthenticationButtonsIntermediateLayoutBeforeLine(View view) {

        this.authenticationButtonsIntermediateBeforeLine = (View) view.findViewById(R.id.authentication_fragment_authentication_buttons_intermediate_before_line);

    }

    private void initAuthenticationButtonsIntermediateLayoutTextView(View view) {

        this.authenticationButtonsIntermediateTextView = (TextView) view.findViewById(R.id.authentication_fragment_authentication_buttons_intermediate_text);
        MyAppThemeUtilsManager.get().initTextView(this.authenticationButtonsIntermediateTextView, "OR", this.getContext(), this.screenSkin);
        this.authenticationButtonsIntermediateTextView.setMaxLines(1);
        this.authenticationButtonsIntermediateTextView.setTextSize((int) this.getResources().getDimension(R.dimen.default_field_info_text_size));
        this.authenticationButtonsIntermediateTextView.setTextColor(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_TEXT_COLOR, this.screenSkin));

    }

    private void initAuthenticationButtonsIntermediateLayoutAfterLine(View view) {

        this.authenticationButtonsIntermediateAfterLine = (View) view.findViewById(R.id.authentication_fragment_authentication_buttons_intermediate_after_line);

    }

    private void onSMSAuthenticationButtonClick(){

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
