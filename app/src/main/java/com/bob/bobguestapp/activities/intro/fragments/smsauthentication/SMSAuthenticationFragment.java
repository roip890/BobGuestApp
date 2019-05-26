package com.bob.bobguestapp.activities.intro.fragments.smsauthentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.bob.uimodule.views.textviews.MyEditText;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class SMSAuthenticationFragment extends ManagementFragment {

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
    public static final int SMS_AUTHENTICATION = 10;

    //intro commands
    private IntroConnector introConnector;

    //sms authentication
    private RelativeLayout smsAuthenticationLayout;
    private TextView smsAuthenticationTextView;
    private RelativeLayout smsAuthenticationCodeLayout;
    private MyEditText smsAuthenticationCodeFirstEditText, smsAuthenticationCodeSecondEditText,
            smsAuthenticationCodeThirdEditText, smsAuthenticationCodeFourthEditText;
    private MyButton smsAuthenticationButton;

    public SMSAuthenticationFragment() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.SMS_AUTHENTICATION_FRAGMENT_SKIN);

    }

    public static SMSAuthenticationFragment newInstance() {
        SMSAuthenticationFragment fragment = new SMSAuthenticationFragment();
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
        View view = inflater.inflate(R.layout.fragment_sms_authentication, container, false);

        //init requests list layouts
        this.initSMSAuthenticationLayout(view);

        this.managementViewContainer.setScreenState(SMS_AUTHENTICATION);

        return view;

    }

    //main view screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.smsAuthenticationLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case SMS_AUTHENTICATION:
                this.smsAuthenticationLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //intro commands
    public void setIntroConnector(IntroConnector introConnector) {
        this.introConnector = introConnector;
    }

    //sms authentication
    private void initSMSAuthenticationLayout(View view) {

        //sms authentication main layout
        this.initSMSAuthenticationMainLayout(view);

        //sms authentication text view
        this.initSMSAuthenticationTextView(view);

        //sms authentication code layout
        this.initSMSAuthenticationCodeLayout(view);

        //sms authentication button
        this.initSMSAuthenticationButton(view);

    }

    private void initSMSAuthenticationMainLayout(View view) {

        this.smsAuthenticationLayout = (RelativeLayout) view.findViewById(R.id.sms_authentication_fragment_sms_authentication_layout);

    }

    private void initSMSAuthenticationTextView(View view) {

        this.smsAuthenticationTextView = (TextView) view.findViewById(R.id.sms_authentication_fragment_sms_authentication_text_view);
        MyAppThemeUtilsManager.get().initTextView(this.smsAuthenticationTextView, "A confirmation code\nhas sent to your\nphone number.", this.getContext(), this.screenSkin);
        this.smsAuthenticationTextView.setTextSize((int) this.getResources().getDimension(R.dimen.default_field_info_text_size));
        this.smsAuthenticationTextView.setTextColor(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_TEXT_COLOR, this.screenSkin));
        this.smsAuthenticationTextView.setMaxLines(5);

    }

    private void initSMSAuthenticationCodeLayout(View view) {

        this.smsAuthenticationCodeLayout = (RelativeLayout) view.findViewById(R.id.sms_authentication_fragment_sms_authentication_code_layout);

        //sms authentication code first edit text
        this.initSMSAuthenticationCodeFirstEditText(view);

        //sms authentication code second edit text
        this.initSMSAuthenticationCodeSecondEditText(view);

        //sms authentication code third edit text
        this.initSMSAuthenticationCodeThirdEditText(view);

        //sms authentication code fourth edit text
        this.initSMSAuthenticationCodeFourthEditText(view);

    }

    private void initSMSAuthenticationCodeFirstEditText(View view) {

        this.smsAuthenticationCodeFirstEditText = (MyEditText) view.findViewById(R.id.sms_authentication_fragment_sms_authentication_first_code_edit_text);

        MyAppThemeUtilsManager.get().initMySMSCodeEditText(this.smsAuthenticationCodeFirstEditText, this.getContext(), this.screenSkin);

        this.smsAuthenticationCodeFirstEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SMSAuthenticationFragment.this.smsAuthenticationCodeSecondEditText.getEditText().requestFocus();
                SMSAuthenticationFragment.this.checkSMSAuthFields();
            }
        });

    }

    private void initSMSAuthenticationCodeSecondEditText(View view) {

        this.smsAuthenticationCodeSecondEditText = (MyEditText) view.findViewById(R.id.sms_authentication_fragment_sms_authentication_second_code_edit_text);

        MyAppThemeUtilsManager.get().initMySMSCodeEditText(this.smsAuthenticationCodeSecondEditText, this.getContext(), this.screenSkin);

        this.smsAuthenticationCodeSecondEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SMSAuthenticationFragment.this.smsAuthenticationCodeThirdEditText.getEditText().requestFocus();
                SMSAuthenticationFragment.this.checkSMSAuthFields();
            }
        });

    }

    private void initSMSAuthenticationCodeThirdEditText(View view) {

        this.smsAuthenticationCodeThirdEditText = (MyEditText) view.findViewById(R.id.sms_authentication_fragment_sms_authentication_third_code_edit_text);

        MyAppThemeUtilsManager.get().initMySMSCodeEditText(this.smsAuthenticationCodeThirdEditText, this.getContext(), this.screenSkin);

        this.smsAuthenticationCodeThirdEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SMSAuthenticationFragment.this.checkSMSAuthFields();
            }
        });

    }

    private void initSMSAuthenticationCodeFourthEditText(View view) {

        this.smsAuthenticationCodeFirstEditText = (MyEditText) view.findViewById(R.id.sms_authentication_fragment_sms_authentication_first_code_edit_text);

        MyAppThemeUtilsManager.get().initMySMSCodeEditText(this.smsAuthenticationCodeFirstEditText, this.getContext(), this.screenSkin);

        this.smsAuthenticationCodeFirstEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SMSAuthenticationFragment.this.smsAuthenticationCodeSecondEditText.getEditText().requestFocus();
                SMSAuthenticationFragment.this.checkSMSAuthFields();
            }
        });

    }

    private void initSMSAuthenticationButton(View view) {

        this.smsAuthenticationButton = (MyButton) view.findViewById(R.id.sms_authentication_fragment_sms_authentication_button);

        MyAppThemeUtilsManager.get().initMyButton(this.smsAuthenticationButton, "Confirm", this.getContext(), this.screenSkin);

        this.smsAuthenticationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSAuthenticationFragment.this.onSMSAuthenticationButtonClick();
            }
        });

        this.smsAuthenticationButton.setEnabled(false);
        this.smsAuthenticationButton.setWidth((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 260));
        this.smsAuthenticationButton.setButtonBorderRadius((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 30));

    }

    private void onSMSAuthenticationButtonClick(){

    }

    private void checkSMSAuthFields() {
        if ((this.smsAuthenticationCodeFirstEditText.getText().toString().equals(""))
                || (this.smsAuthenticationCodeSecondEditText.getText().toString().equals(""))
                || (this.smsAuthenticationCodeThirdEditText.getText().toString().equals(""))
                || (this.smsAuthenticationCodeFourthEditText.getText().toString().equals(""))) {
            this.smsAuthenticationButton.setEnabled(false);
        } else {
            this.smsAuthenticationButton.setEnabled(true);
        }
    }

    private void resetSMSAuthFields() {
        this.smsAuthenticationCodeFirstEditText.setText("");
        this.smsAuthenticationCodeSecondEditText.setText("");
        this.smsAuthenticationCodeThirdEditText.setText("");
        this.smsAuthenticationCodeFourthEditText.setText("");
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
