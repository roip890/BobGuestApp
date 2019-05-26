package com.bob.bobguestapp.activities.intro.fragments.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.RegexUtils;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.intro.connector.IntroConnector;
import com.bob.bobguestapp.activities.main.MainActivity;
import com.bob.bobguestapp.activities.main.fragments.requests.RequestsFragment;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.RequestsListAdapter;
import com.bob.bobguestapp.services.FirebaseNotificationsService;
import com.bob.bobguestapp.tools.comparators.guestbooking.GuestBookingCheckInComparator;
import com.bob.bobguestapp.tools.database.MyDBUtilsManager;
import com.bob.bobguestapp.tools.session.SessionUtilsManager;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.GeneralUtilsManager;
import com.bob.toolsmodule.database.objects.GuestRequest;
import com.bob.toolsmodule.database.objects.RequestItem;
import com.bob.toolsmodule.http.ServerRequest;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.http.serverbeans.Hotel;
import com.bob.toolsmodule.http.serverbeans.hotelautocomplete.HotelAutocompleteEntry;
import com.bob.toolsmodule.http.serverbeans.menu.RelationalMenuResponse;
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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.bob.bobguestapp.tools.recyclerview.MyRecyclerView.RECYCLER_VIEW;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.FAILURE_MESSAGE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.LOADING;

public class LoginFragment extends ManagementFragment {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";
    private static String BOB_SERVER_DESIGN_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_DESIGN_PORT;

    //guest bookings
    private static final String GET_ALL_GUEST_BOOKING_URL = BOB_SERVER_WEB_SERVICES_URL +"/bookings/findForGuest";

    //menu design
    private static final String GET_ALL_MENU_URL = BOB_SERVER_DESIGN_SERVICES_URL +"/design/getRelationalTree";

    //login url
    private static final String LOGIN_URL = BOB_SERVER_MOBILE_SERVICES_URL + "/login/login";

    //sms verification
    private static final String SMS_VERIFY_URL = BOB_SERVER_MOBILE_SERVICES_URL +"/MobileRegister/Verify";

    //sms resend code
    private static final String SMS_RESEND_VERIFICATION_CODE_URL = BOB_SERVER_MOBILE_SERVICES_URL +"/MobileRegister/ResendVerificationCode";

    //get hotels url
    private static final String GET_ALL_HOTELS = BOB_SERVER_MOBILE_SERVICES_URL + "/login/getHotelsAndIcons";

    //screen states
    public static final int LOGIN = 10;

    //intro commands
    private IntroConnector introConnector;

    //login
    private ConstraintLayout loginLayout;
    private MyButton loginButton;
    private TextView loginTitle, loginErrorTextView;
    private MyAutoCompleteEditText loginInHotelEditText;
    private HotelAutocompleteAdapter hotelAutocompleteAdapter;
    private MyEditText loginEmailEditText, loginPasswordEditText;

    public LoginFragment() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.LOGIN_FRAGMENT_SKIN);

    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //init requests list layouts
        this.initLoginLayout(view);

        this.managementViewContainer.setScreenState(LOGIN);

        //        //COMMENT THIS!!
        this.loginEmailEditText.setText("aa@a.a");
        this.loginPasswordEditText.setText("Aa111111");

//        this.managementViewContainer.getMainViewContainerFrameLayout().setBackgroundColor(Color.YELLOW);
//        this.managementViewContainer.getManagementContainerScrollView().setBackgroundColor(Color.BLACK);
//        this.managementViewContainer.getMessageLayout().setBackgroundColor(Color.GREEN);
//        this.managementViewContainer.getLoadingLayout().setBackgroundColor(Color.GREEN);
//        this.managementViewContainer.getProgressLayout().setBackgroundColor(Color.RED);
//        this.managementViewContainer.getBackgroundLayout().setBackgroundColor(Color.CYAN);

        return view;

    }

    //main view screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.loginLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case LOGIN:
                this.loginLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //intro commands
    public void setIntroConnector(IntroConnector introConnector) {
        this.introConnector = introConnector;
    }

    //login
    private void initLoginLayout(View view) {

        //login main layout
        this.initLoginMainLayout(view);

        //check in hotel field
        this.initLoginHotelField(view);

        //login email
        this.initLoginEmailField(view);

        //login password
        this.initLoginPasswordField(view);

        //login error
        this.initLoginErrorField(view);

        //login button
        this.initLoginButton(view);

    }

    private void initLoginMainLayout(View view) {
        this.loginLayout = (ConstraintLayout) view.findViewById(R.id.login_fragment_login_layout);
    }

    private void initLoginHotelField(View view) {

        this.loginInHotelEditText = (MyAutoCompleteEditText) view.findViewById(R.id.login_fragment_login_hotel_edit_text);
        MyAppThemeUtilsManager.get().initMyEditText(this.loginInHotelEditText, "Hotel",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"gmd_location_city"), this.getContext(), this.screenSkin);
        this.loginInHotelEditText.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.loginInHotelEditText.setValidator(new Validator<String>() {
            @Override
            public String validate(String text) {
                if (LoginFragment.this.isValidHotelName(text) || text.equals("")) {
                    return null;
                } else {
                    return "Please enter valid hotel name";
                }
            }
        });
        this.loginInHotelEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                LoginFragment.this.checkLoginFields();
            }
        });

        this.loginInHotelEditText.setVisibility(GONE);

        ArrayList<HotelAutocompleteEntry> hotels = new ArrayList<HotelAutocompleteEntry>();
//        hotels.add(new HotelAutocompleteEntry("Sheraton", "https://pbs.twimg.com/profile_images/666425946296074240/qaRwUoax.png"));
//        hotels.add(new HotelAutocompleteEntry("Hilton", "http://mastersida.com/wp-content/uploads/2015/08/HI_mk_logo_hiltonbrandlogo3.jpg"));
        this.hotelAutocompleteAdapter = new HotelAutocompleteAdapter(this.getContext(), hotels);
        this.loginInHotelEditText.getAutoCompleteTextView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager inputMethodManager = (InputMethodManager) LoginFragment.this.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputMethodManager.isAcceptingText()) {
                    View currentFocus = LoginFragment.this.getActivity().getCurrentFocus();
                    if (currentFocus != null) {
                        inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                    }
                }

                Object item = parent.getItemAtPosition(position);
                if (item instanceof HotelAutocompleteEntry){
                    LoginFragment.this.loginInHotelEditText.setText(((HotelAutocompleteEntry) item).getHotelName());
                }

            }
        });


    }

    private void initLoginEmailField(View view) {

        this.loginEmailEditText = (MyEditText) view.findViewById(R.id.login_fragment_login_email_edit_text);
        MyAppThemeUtilsManager.get().initMyEditText(this.loginEmailEditText, "Email",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"faw_user"), this.getContext(), this.screenSkin);
        this.loginEmailEditText.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.loginEmailEditText.setValidator(new Validator<String>() {
            @Override
            public String validate(String text) {
                if (RegexUtils.isEmail(text) || text.equals("")) {
                    return null;
                } else {
                    return "Please enter valid email";
                }
            }
        });
        this.loginEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LoginFragment.this.checkLoginFields();
            }
        });
    }

    private void initLoginPasswordField(View view) {

        this.loginPasswordEditText = (MyEditText) view.findViewById(R.id.login_fragment_login_password_edit_text);
        MyAppThemeUtilsManager.get().initMyEditText(this.loginPasswordEditText, "Password",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"faw_lock"), this.getContext(), this.screenSkin);
        this.loginPasswordEditText.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.loginPasswordEditText.setTextInputType(finals.inputTypes.get("textPassword"));
        this.loginPasswordEditText.setValidator(new Validator<String>() {
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
        this.loginPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                LoginFragment.this.checkLoginFields();
            }
        });

    }

    private void initLoginErrorField(View view) {

        this.loginErrorTextView = (TextView) view.findViewById(R.id.login_fragment_login_error_text_view);
        MyAppThemeUtilsManager.get().initTextView(this.loginErrorTextView, "", this.getContext(), this.screenSkin);
        this.loginErrorTextView.setMaxLines(5);
        this.loginErrorTextView.setTextSize((int) this.getResources().getDimension(R.dimen.default_field_info_text_size));
        this.loginErrorTextView.setTextColor(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_ERROR_COLOR, this.screenSkin));

    }

    private void initLoginButton(View view) {

        this.loginButton = (MyButton) view.findViewById(R.id.login_fragment_login_sign_in_button);
        MyAppThemeUtilsManager.get().initMyButton(this.loginButton, "Sign In", this.getContext(), this.screenSkin);
        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment.this.onLoginButtonClick(
                        LoginFragment.this.loginInHotelEditText.getText().toString(),
                        LoginFragment.this.loginEmailEditText.getText().toString(),
                        LoginFragment.this.loginPasswordEditText.getText().toString()
                );
            }
        });
        this.loginButton.setEnabled(false);
        this.loginButton.setWidth((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 260));
        this.loginButton.setButtonBorderRadius((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 30));


    }

    private void onLoginButtonClick(String hotel, String username, String password){
//        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
//        IntroActivity.this.startActivity(intent);
        this.makeLoginRequest(hotel, username, password);
    }

    private void makeLoginRequest(final String hotelName, final String email, final String password) {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {
                LoginFragment.this.managementViewContainer.setScreenState(LOADING);
            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {
                try {

                    Guest guest = new Guest();
                    guest.setEmail(email);
                    guest.setPassword(password);
                    JsonElement jsonGuest = MyGsonParser.getParser().create().toJsonTree(guest, Guest.class);

                    Hotel hotel = new Hotel();
                    hotel.setName(hotelName);
                    JsonElement jsonHotel = MyGsonParser.getParser().create().toJsonTree(hotel, Hotel.class);

                    JsonObject jsonRequest = new JsonObject();
                    jsonRequest.add("guest", jsonGuest);
                    jsonRequest.add("hotel", jsonHotel);

                    JsonObject jsonLoginRequest = new JsonObject();
                    jsonLoginRequest.add("request", jsonRequest);

                    return new JSONObject( new Gson().toJson(jsonLoginRequest));

                } catch (JSONException e) {

                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected String getRequestUrl() {
                return LOGIN_URL;
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

                    SessionUtilsManager.get().saveGuest(guest);

                    //start service
//                    if (GeneralUtilsManager.get().isServiceRunning(LoginFragment.this.getActivity(), NotificationsService.class)) {
//                        Intent serviceIntent = new Intent(LoginFragment.this.getActivity(), NotificationsService.class);
//                        LoginFragment.this.getActivity().stopService(serviceIntent);
//                    }
//                    Intent serviceIntent = new Intent(LoginFragment.this.getActivity(), NotificationsService.class);
//                    serviceIntent.putExtra("guestId", guest.getId());
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        LoginFragment.this.getActivity().startForegroundService(serviceIntent);
//                    } else {
//                        LoginFragment.this.getActivity().startService(serviceIntent);
//                    }

                    if (GeneralUtilsManager.get().isServiceRunning(LoginFragment.this.getActivity(), FirebaseNotificationsService.class)) {
                        Intent firebaseServiceIntent = new Intent(LoginFragment.this.getActivity(), FirebaseNotificationsService.class);
                        LoginFragment.this.getActivity().stopService(firebaseServiceIntent);
                    }
                    Intent firebaseServiceIntent = new Intent(LoginFragment.this.getActivity(), FirebaseNotificationsService.class);
                    firebaseServiceIntent.putExtra("guestId", guest.getId());
                    LoginFragment.this.getActivity().startService(firebaseServiceIntent);

                    Intent intent = new Intent(LoginFragment.this.getActivity(), MainActivity.class);
                    LoginFragment.this.startActivity(intent);
                    LoginFragment.this.getActivity().finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    LoginFragment.this.setLoginError(message);
                } else {
                    LoginFragment.this.setLoginError("Login Error");
                }
            }

            @Override
            protected void onException(Exception e) {
                e.printStackTrace();
                this.onDefaultError(e.toString());

//                Log.i("Firebase Service", e.toString());

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void checkLoginFields() {
        if (((this.loginEmailEditText.getErrorText() != null) && (!this.loginEmailEditText.getErrorText().toString().equals("")))
                || ((this.loginPasswordEditText.getErrorText() != null) && (!this.loginPasswordEditText.getErrorText().toString().equals("")))
                || this.loginEmailEditText.getText().toString().equals("")
                || this.loginPasswordEditText.getText().toString().equals("")) {
            this.loginButton.setEnabled(false);
        } else {
            this.loginButton.setEnabled(true);
        }
    }

    private void resetLoginFields() {
        this.loginEmailEditText.setText("");
        this.loginPasswordEditText.setText("");
        this.loginErrorTextView.setText("");
        this.loginErrorTextView.setVisibility(View.GONE);
    }

    private void setLoginError(String error) {
        this.managementViewContainer.setScreenState(LOGIN);
        this.loginErrorTextView.setText(error);
    }

    //menu
    private void makeGetAllMenuRequest() {
        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {
                LoginFragment.this.managementViewContainer.setScreenState(LOADING);
            }

            @Override
            protected boolean requestCondition() {
                String email = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestEmail", null);
                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", "1");
                if ((email != null) && (hotelId != null)) {
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
            protected String getRequestUrl() {
                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", "1");
                if (hotelId != null) {
                    return GET_ALL_MENU_URL + hotelId;
                } else {
                    return null;
                }
            }

            @Override
            protected ApplicativeResponse getApplicativeResponse(JSONObject response) {
                try {
                    Gson customGson = MyGsonParser.getParser().create();
                    String statusResponse = response.getJSONObject("statusResponse").toString();

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
                RelationalMenuResponse relationalMenuResponse = new Gson().fromJson(response.toString(), RelationalMenuResponse.class);
                MyDBUtilsManager.get().insertMenuToDB(relationalMenuResponse.getTree().getNodes(),
                        relationalMenuResponse.getTree().getNodesProperties(),
                        relationalMenuResponse.getTree().getBullets(),
                        relationalMenuResponse.getTree().getBulletsProperties());
                Intent intent = new Intent(LoginFragment.this.getActivity(), MainActivity.class);
                LoginFragment.this.startActivity(intent);
                LoginFragment.this.getActivity().finish();

            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    LoginFragment.this.managementViewContainer.setMessage(message, FAILURE_MESSAGE);
                } else {
                    LoginFragment.this.managementViewContainer.setMessage("Getting Menu Error", FAILURE_MESSAGE);
                }
            }

            @Override
            protected void onException(Exception e) {
                e.printStackTrace();
                this.onDefaultError(e.toString());
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

            this.managementViewContainer.setScreenState(LOGIN);

        }

    }
}
