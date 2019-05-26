package com.bob.bobguestapp.activities.intro.fragments.register;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.RegexUtils;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.intro.IntroActivity;
import com.bob.bobguestapp.activities.intro.connector.IntroConnector;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.http.ServerRequest;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.http.serverbeans.Hotel;
import com.bob.toolsmodule.http.serverbeans.hotelautocomplete.HotelAutocompleteEntry;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.toolsmodule.validators.Validator;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.finals;
import com.bob.uimodule.hotelautocomplete.HotelAutocompleteAdapter;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.views.loadingcontainer.ManagementFragment;
import com.bob.uimodule.views.textviews.MyAutoCompleteEditText;
import com.bob.uimodule.views.textviews.MyButton;
import com.bob.uimodule.views.textviews.MyEditText;
import com.bob.uimodule.views.textviews.MyPhoneEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.IntToDoubleFunction;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.LOADING;

public class RegisterFragment extends ManagementFragment {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //register url
    private static final String REGISTER_URL = BOB_SERVER_MOBILE_SERVICES_URL + "/MobileRegister/Register";

    //get hotels url
    private static final String GET_ALL_HOTELS = BOB_SERVER_MOBILE_SERVICES_URL + "/login/getHotelsAndIcons";

    //app theme
    private int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    private int screenSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;

    //screen states
    public static final int REGISTER = 3;

    //intro commands
    private IntroConnector introConnector;

    //register
    private ConstraintLayout registerLayout;
    private MyButton registerButton;
    private TextView registerTitle, registerSignInMessageTextView, registerSignInLinkTextView, registerErrorTextView;
    private MyPhoneEditText registerPhoneEditText;
    private MyEditText registerUsernameEditText, registerFullNameEditText, registerEmailEditText,
            registerPasswordEditText, registerConfirmPasswordEditText;
    private MyAutoCompleteEditText registerHotelNameEditText;
    private HotelAutocompleteAdapter hotelAutocompleteAdapter;


    public RegisterFragment() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.REGISTER_FRAGMENT_SKIN);

    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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

//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;

    }


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //init management skin
        this.managementViewContainer.setScreenSkin(this.screenSkin);

        //view
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //init requests list layouts
        this.initRegisterLayout(view);

        this.managementViewContainer.setScreenState(REGISTER);

        //get all hotels for hotel field
        this.makeGetAllHotelsRequest();

        //COMMENT THIS!!
        this.registerUsernameEditText.setText("aaaaaa");
        this.registerEmailEditText.setText("aa@a.a");
        this.registerPhoneEditText.setText("0000000");
        this.registerPasswordEditText.setText("Aa111111");
        this.registerConfirmPasswordEditText.setText("Aa111111");
        this.registerFullNameEditText.setText("a b");

        return view;

    }

    //main view screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.registerLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case REGISTER:
                this.registerLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //intro commands
    public void setIntroConnector(IntroConnector introConnector) {
        this.introConnector = introConnector;
    }

    //register
    private void initRegisterLayout(View view) {

        //register main layout
        this.initLoginMainLayout(view);

        //register username field
        this.initRegisterUsernameField(view);

        //register email field
        this.initRegisterEmailField(view);

        //register password field
        this.initRegisterPasswordField(view);

        //register confirm password field
        this.initRegisterConfirmPasswordField(view);

        //register hotel field
        this.initRegisterHotelField(view);

        //register full name field
        this.initRegisterFullNameField(view);

        //register phone field
        this.initRegisterPhoneField(view);

        //register error
        this.initRegisterErrorField(view);

        //register button
        this.initRegisterButton(view);

    }

    private void initLoginMainLayout(View view) {
        this.registerLayout = (ConstraintLayout) view.findViewById(R.id.register_fragment_register_layout);
    }

    private void initRegisterUsernameField(View view) {


        //register username
        this.registerUsernameEditText = (MyEditText) view.findViewById(R.id.register_fragment_register_username_edit_text);
        MyAppThemeUtilsManager.get().initMyEditText(this.registerUsernameEditText, "Username",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"faw_user"), this.getContext(), this.screenSkin);

        this.registerUsernameEditText.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.registerUsernameEditText.setValidator(new Validator<String>() {
            @Override
            public String validate(String text) {
                if (RegexUtils.isUsername(text) || text.equals("")) {
                    return null;
                } else {
                    return "Please enter valid username";
                }
            }
        });

        this.registerUsernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RegisterFragment.this.checkRegisterFields();
            }
        });

    }

    private void initRegisterEmailField(View view) {

        //register email
        this.registerEmailEditText = (MyEditText) view.findViewById(R.id.register_fragment_register_email_edit_text);

        MyAppThemeUtilsManager.get().initMyEditText(this.registerEmailEditText, "Email",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"faw_envelope"), this.getContext(), this.screenSkin);

        this.registerEmailEditText.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.registerEmailEditText.setValidator(new Validator<String>() {
            @Override
            public String validate(String text) {
                if (RegexUtils.isEmail(text) || text.equals("")) {
                    return null;
                } else {
                    return "Please enter valid email";
                }
            }
        });

        this.registerEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RegisterFragment.this.checkRegisterFields();
            }
        });

    }

    private void initRegisterPasswordField(View view) {

        this.registerPasswordEditText = (MyEditText) view.findViewById(R.id.register_fragment_register_password_edit_text);

        MyAppThemeUtilsManager.get().initMyEditText(this.registerPasswordEditText, "Password",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"faw_lock"), this.getContext(), this.screenSkin);

        this.registerPasswordEditText.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.registerPasswordEditText.setTextInputType(finals.inputTypes.get("textPassword"));

        this.registerPasswordEditText.setValidator(new Validator<String>() {
            @Override
            public String validate(String text) {
                if (((RegexUtils.isMatch("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})", text))
                        && (text.length() >= 8) && (text.length() <= 15)) || text.equals("")) {
                    return null;
                } else {
                    return "Please enter valid password";
                }
            }
        });

        this.registerPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RegisterFragment.this.checkRegisterFields();
            }
        });

    }

    private void initRegisterConfirmPasswordField(View view) {

        this.registerConfirmPasswordEditText = (MyEditText) view.findViewById(R.id.register_fragment_register_confirm_password_edit_text);

        MyAppThemeUtilsManager.get().initMyEditText(this.registerConfirmPasswordEditText, "Confirm Password",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"faw_lock"), this.getContext(), this.screenSkin);

        this.registerConfirmPasswordEditText.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.registerConfirmPasswordEditText.setTextInputType(finals.inputTypes.get("textPassword"));

        this.registerConfirmPasswordEditText.setValidator(new Validator<String>() {
            @Override
            public String validate(String text) {
                if (text.equals(RegisterFragment.this.registerPasswordEditText.getText().toString())  || text.equals("")) {
                    return null;
                } else {
                    return "Passwords do not match";
                }
            }
        });

        this.registerConfirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RegisterFragment.this.checkRegisterFields();
            }
        });

    }

    private void initRegisterHotelField(View view) {

        this.registerHotelNameEditText = (MyAutoCompleteEditText) view.findViewById(R.id.register_fragment_register_hotel_name_edit_text);
        MyAppThemeUtilsManager.get().initMyEditText(this.registerHotelNameEditText, "Hotel",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"gmd_location_city"), this.getContext(), this.screenSkin);

        this.registerHotelNameEditText.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.registerHotelNameEditText.setValidator(new Validator<String>() {
            @Override
            public String validate(String text) {
                if (RegexUtils.isMatch("^[\\p{L} .'-]+$", text) || text.equals("")) {
                    return null;
                } else {
                    return "Please enter hotel name";
                }
            }
        });

        this.registerHotelNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RegisterFragment.this.checkRegisterFields();
            }
        });


        ArrayList<HotelAutocompleteEntry> hotels = new ArrayList<HotelAutocompleteEntry>();
        this.hotelAutocompleteAdapter = new HotelAutocompleteAdapter(this.getContext(), hotels);
        this.registerHotelNameEditText.getAutoCompleteTextView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager inputMethodManager = (InputMethodManager) RegisterFragment.this.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputMethodManager.isAcceptingText()) {
                    View currentFocus = RegisterFragment.this.getActivity().getCurrentFocus();
                    if (currentFocus != null) {
                        inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                    }
                }

                Object item = parent.getItemAtPosition(position);
                if (item instanceof HotelAutocompleteEntry){
                    RegisterFragment.this.registerHotelNameEditText.setText(((HotelAutocompleteEntry) item).getHotelName());
                }
            }
        });


    }

    private void initRegisterFullNameField(View view) {

        //register full name
        this.registerFullNameEditText = (MyEditText) view.findViewById(R.id.register_fragment_register_full_name_edit_text);

        MyAppThemeUtilsManager.get().initMyEditText(this.registerFullNameEditText, "Full Name",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"faw_user"), this.getContext(), this.screenSkin);

        this.registerFullNameEditText.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.registerFullNameEditText.setValidator(new Validator<String>() {
            @Override
            public String validate(String text) {
                if (RegexUtils.isMatch("^[\\p{L} .'-]+$", text) || text.equals("")) {
                    return null;
                } else {
                    return "Please enter full name";
                }
            }
        });

        this.registerFullNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RegisterFragment.this.checkRegisterFields();
            }
        });

    }

    private void initRegisterPhoneField(View view) {

        //register phone
        this.registerPhoneEditText = (MyPhoneEditText) view.findViewById(R.id.register_fragment_register_phone_edit_text);

        MyAppThemeUtilsManager.get().initMyPhoneEditText(this.registerPhoneEditText, "Phone", this.getContext(), this.screenSkin);

        this.registerConfirmPasswordEditText.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.registerPhoneEditText.setValidator(new Validator<String>() {
            @Override
            public String validate(String text) {
                if (RegexUtils.isMatch(
                        "(\\+\\d+|0)(\\d{9})$",
                        "+" + RegisterFragment.this.registerPhoneEditText.getCountryCode() + text)
                        || text.equals("")) {
                    return null;
                } else {
                    return "Please enter valid phone number";
                }
            }
        });

        this.registerPhoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RegisterFragment.this.checkRegisterFields();
            }
        });

    }

    private void initRegisterErrorField(View view) {

        this.registerErrorTextView = (TextView) view.findViewById(R.id.register_fragment_register_error_text_view);
        MyAppThemeUtilsManager.get().initTextView(this.registerErrorTextView, "", this.getContext(), this.screenSkin);
        this.registerErrorTextView.setTextSize((int) this.getResources().getDimension(R.dimen.default_field_info_text_size));
        this.registerErrorTextView.setTextColor(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_ERROR_COLOR, this.screenSkin));
        this.registerErrorTextView.setMaxLines(5);

    }

    private void initRegisterButton(View view) {

        this.registerButton = (MyButton) view.findViewById(R.id.register_fragment_register_sign_up_button);
        MyAppThemeUtilsManager.get().initMyButton(this.registerButton, "Sing Up", this.getContext(), this.screenSkin);
        this.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment.this.onRegisterButtonClick(
                        RegisterFragment.this.registerUsernameEditText.getText().toString(),
                        RegisterFragment.this.registerEmailEditText.getText().toString(),
                        "+" + RegisterFragment.this.registerPhoneEditText.getCountryCode() +
                                RegisterFragment.this.registerPhoneEditText.getText().toString(),
                        RegisterFragment.this.registerPasswordEditText.getText().toString(),
                        RegisterFragment.this.registerHotelNameEditText.getText().toString()
                );
            }
        });
        this.registerButton.setEnabled(false);
        this.registerButton.setWidth((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 260));
        this.registerButton.setButtonBorderRadius((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 30));

    }

    private void makeRegisterRequest(final String username, final String email, final String phoneNumber, final String password, final String hotelName) {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {
                RegisterFragment.this.managementViewContainer.setScreenState(LOADING);
            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {
                try {

                    //guest
                    Guest guest = new Guest();
                    guest.setName(username);
                    guest.setEmail(email);
                    guest.setPhone(phoneNumber);
                    guest.setPassword(password);
                    guest.setRoom(0);
                    JsonElement jsonGuest = MyGsonParser.getParser().create().toJsonTree(guest, Guest.class);

                    //hotel
                    Hotel hotel = new Hotel();
                    hotel.setName(hotelName);
                    JsonElement jsonHotel = MyGsonParser.getParser().create().toJsonTree(hotel, Hotel.class);

                    JsonObject jsonRequest = new JsonObject();
                    jsonRequest.add("guest", jsonGuest);
                    jsonRequest.add("hotel", jsonHotel);

                    JsonObject jsonRegisterRequest = new JsonObject();
                    jsonRegisterRequest.add("request", jsonRequest);

                    return new JSONObject( new Gson().toJson(jsonRegisterRequest));

                } catch (JSONException e) {

                    e.printStackTrace();
                    return null;

                }

            }

            @Override
            protected String getRequestUrl() {
                return REGISTER_URL;
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
                try {
                    String guestResponse = response.getJSONObject("response").getJSONObject("guest").toString();
                    Guest guest = MyGsonParser.getParser().create().fromJson(guestResponse, Guest.class);
                    if (guest != null) {
                        RegisterFragment.this.introConnector.login(guest.getEmail(), guest.getPassword());
                    } else {
                        RegisterFragment.this.introConnector.login(email, password);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    RegisterFragment.this.setRegisterError(message);
                } else {
                    RegisterFragment.this.setRegisterError("Register Error");
                }
                RegisterFragment.this.managementViewContainer.setScreenState(REGISTER);
            }

        };

        jsonServerRequest.makeRequest();

    }

    private void checkRegisterFields() {
        if (((this.registerUsernameEditText.getErrorText() != null) && (!this.registerUsernameEditText.getErrorText().toString().equals("")))
                || ((this.registerEmailEditText.getErrorText() != null) && (!this.registerEmailEditText.getErrorText().toString().equals("")))
                || ((this.registerHotelNameEditText.getErrorText() != null) && (!this.registerHotelNameEditText.getErrorText().toString().equals("")))
                || ((this.registerFullNameEditText.getErrorText() != null) && (!this.registerFullNameEditText.getErrorText().toString().equals("")))
                || ((this.registerPhoneEditText.getErrorText() != null) && (!this.registerPhoneEditText.getErrorText().toString().equals("")))
                || ((this.registerPasswordEditText.getErrorText() != null) && (!this.registerPasswordEditText.getErrorText().toString().equals("")))
                || ((this.registerConfirmPasswordEditText.getErrorText() != null) && (!this.registerConfirmPasswordEditText.getErrorText().toString().equals("")))
                || this.registerUsernameEditText.getText().toString().equals("")
                || this.registerEmailEditText.getText().toString().equals("")
                || this.registerHotelNameEditText.getText().toString().equals("")
                || this.registerFullNameEditText.getText().toString().equals("")
                || this.registerPhoneEditText.getText().toString().equals("")
                || this.registerPasswordEditText.getText().toString().equals("")
                || this.registerConfirmPasswordEditText.getText().toString().equals("")){
            this.registerButton.setEnabled(false);
        } else {
            this.registerButton.setEnabled(true);
        }
    }

    private void resetRegisterFields() {
        this.registerUsernameEditText.setText("");
        this.registerEmailEditText.setText("");
        this.registerHotelNameEditText.setText("");
        this.registerFullNameEditText.setText("");
        this.registerPhoneEditText.setText("");
        this.registerPhoneEditText.setCountryCodeByCode(972);
        this.registerPasswordEditText.setText("");
        this.registerConfirmPasswordEditText.setText("");
        this.registerErrorTextView.setText("");
        this.registerErrorTextView.setVisibility(View.GONE);

    }

    private void onRegisterButtonClick(String username, String email, String phoneNumber, String password, String hotelName){
        this.makeRegisterRequest(username, email, phoneNumber, password, hotelName);
    }

    private void setRegisterError(String error) {
        this.managementViewContainer.setScreenState(REGISTER);
        this.registerErrorTextView.setText(error);
    }


    //hotels names
    private void makeGetAllHotelsRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {
                RegisterFragment.this.managementViewContainer.setScreenState(LOADING);
            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {
                return null;
            }

            @Override
            protected String getRequestUrl() {
                return GET_ALL_HOTELS;
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
                try {
                    Gson customGson = MyGsonParser.getParser().create();

                    String hotelsInfo = response.getJSONObject("response").getJSONArray("hotelsInfo").toString();
                    HotelAutocompleteEntry[] hotelsInfoArray = customGson.fromJson(hotelsInfo, HotelAutocompleteEntry[].class);
                    RegisterFragment.this.hotelAutocompleteAdapter.setHotels(Arrays.asList(hotelsInfoArray));
                    RegisterFragment.this.registerHotelNameEditText.setAdapter(RegisterFragment.this.hotelAutocompleteAdapter);
                    RegisterFragment.this.registerHotelNameEditText.getAutoCompleteTextView().setThreshold(1);
                    RegisterFragment.this.managementViewContainer.setScreenState(REGISTER);
                } catch (Exception e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    RegisterFragment.this.setRegisterError(message);
                } else {
                    RegisterFragment.this.setRegisterError("Getting Hotels List Error");
                }
            }

        };

        jsonServerRequest.makeRequest();

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

        if (this.managementViewContainer.getManagementContainerScrollView().getVisibility() == VISIBLE) {

            this.managementViewContainer.setScreenState(REGISTER);

        }

    }
}
