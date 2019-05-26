package com.bob.bobguestapp.activities.intro.fragments.resetpassword;

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

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.RegexUtils;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.intro.connector.IntroConnector;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.validators.Validator;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.views.loadingcontainer.ManagementFragment;
import com.bob.uimodule.views.textviews.MyButton;
import com.bob.uimodule.views.textviews.MyEditText;
import com.mikepenz.iconics.IconicsDrawable;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class ResetPasswordFragment extends ManagementFragment {

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
    public static final int RESET_PASSWORD = 10;

    //intro commands
    private IntroConnector introConnector;

    //sms authentication
    private RelativeLayout resetPasswordLayout;
    private MyButton resetPasswordButton;
    private TextView resetPasswordTextView;
    private MyEditText resetPasswordEmailEditText;

    public ResetPasswordFragment() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.RESET_PASSWORD_FRAGMENT_SKIN);

    }

    public static ResetPasswordFragment newInstance() {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        //init requests list layouts
        this.initResetPasswordLayout(view);

        this.managementViewContainer.setScreenState(RESET_PASSWORD);

        return view;

    }

    //main view screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.resetPasswordLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case RESET_PASSWORD:
                this.resetPasswordLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //intro commands
    public void setIntroConnector(IntroConnector introConnector) {
        this.introConnector = introConnector;
    }

    //reset password
    private void initResetPasswordLayout(View view) {

        //reset password main layout
        this.initResetPasswordMainLayout(view);

        //reset password text view
        this.initResetPasswordTextView(view);

        //reset password email field
        this.initResetPasswordEmailField(view);

        //reset password button
        this.initResetPasswordButton(view);

    }

    private void initResetPasswordMainLayout(View view) {
        this.resetPasswordLayout = (RelativeLayout) view.findViewById(R.id.reset_password_fragment_reset_password_layout);
    }

    private void initResetPasswordTextView(View view) {

        this.resetPasswordTextView = (TextView) view.findViewById(R.id.reset_password_fragment_reset_password_text_view);
        MyAppThemeUtilsManager.get().initTextView(this.resetPasswordTextView, "Please enter your email address.", this.getContext(), this.screenSkin);
        this.resetPasswordTextView.setMaxLines(5);
        this.resetPasswordTextView.setTextSize((int) this.getResources().getDimension(R.dimen.default_field_info_text_size));
        this.resetPasswordTextView.setTextColor(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_TEXT_COLOR, this.screenSkin));

    }

    private void initResetPasswordEmailField(View view) {

        this.resetPasswordEmailEditText = (MyEditText) view.findViewById(R.id.reset_password_fragment_reset_password_email_edit_text);
        MyAppThemeUtilsManager.get().initMyEditText(this.resetPasswordEmailEditText, "Email",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"faw_user"), this.getContext(), this.screenSkin);
        this.resetPasswordEmailEditText.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.resetPasswordEmailEditText.setValidator(new Validator<String>() {
            @Override
            public String validate(String text) {
                if (RegexUtils.isEmail(text) || text.equals("")) {
                    return null;
                } else {
                    return "Please enter valid email";
                }
            }
        });
        this.resetPasswordEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ResetPasswordFragment.this.checkResetPasswordFields();
            }
        });

    }

    private void initResetPasswordButton(View view) {

        this.resetPasswordButton = (MyButton) view.findViewById(R.id.reset_password_fragment_reset_password_button);
        MyAppThemeUtilsManager.get().initMyButton(this.resetPasswordButton, "Reset", this.getContext(), this.screenSkin);
        this.resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ResetPasswordFragment.this.onResetPasswordButtonClick();

            }
        });
        this.resetPasswordButton.setEnabled(false);
        this.resetPasswordButton.setWidth((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 260));
        this.resetPasswordButton.setButtonBorderRadius((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 30));

    }

    private void onResetPasswordButtonClick(){

    }

    private void checkResetPasswordFields() {
        if (((this.resetPasswordEmailEditText.getErrorText() != null) && (!this.resetPasswordEmailEditText.getErrorText().toString().equals("")))
                || this.resetPasswordEmailEditText.getText().toString().equals("")) {
            this.resetPasswordButton.setEnabled(false);
        } else {
            this.resetPasswordButton.setEnabled(true);
        }
    }

    private void resetResetPasswordFields() {

        this.resetPasswordEmailEditText.setText("");

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
