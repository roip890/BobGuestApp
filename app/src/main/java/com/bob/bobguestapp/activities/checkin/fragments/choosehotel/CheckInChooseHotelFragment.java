package com.bob.bobguestapp.activities.checkin.fragments.choosehotel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.RegexUtils;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.checkin.connector.CheckInConnector;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.toolsmodule.http.serverbeans.hotelautocomplete.HotelAutocompleteEntry;
import com.bob.toolsmodule.validators.Validator;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.hotelautocomplete.HotelAutocompleteAdapter;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.views.loadingcontainer.ManagementFragment;
import com.bob.uimodule.views.textviews.MyAutoCompleteEditText;
import com.bob.uimodule.views.textviews.MyButton;
import com.google.gson.Gson;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.LOADING;

public class CheckInChooseHotelFragment extends ManagementFragment {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //get hotels url
    private static final String GET_ALL_HOTELS = BOB_SERVER_MOBILE_SERVICES_URL + "/login/getHotelsAndIcons";

    //screen states
    public static final int CHECK_IN_CHOOSE_HOTEL = 10;

    //check in connector
    private CheckInConnector checkInConnector;

    //check in choose hotel
    private RelativeLayout checkInChooseHotelLayout;
    private MyAutoCompleteEditText checkInChooseHotelHotelEditText;
    private TextView checkInChooseHotelErrorTextView;
    private HotelAutocompleteAdapter hotelAutocompleteAdapter;
    private MyButton checkInChooseHotelQRCheckInButton;

    public CheckInChooseHotelFragment() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.AUTHENTICATION_FRAGMENT_SKIN);

    }

    public static CheckInChooseHotelFragment newInstance() {
        CheckInChooseHotelFragment fragment = new CheckInChooseHotelFragment();
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
        this.initCheckInChooseHotelLayout(view);

        this.managementViewContainer.setScreenState(CHECK_IN_CHOOSE_HOTEL);

        //get all hotels for autocomplete text view
        this.makeGetAllHotelsRequest();

        return view;

    }

    //main view screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.checkInChooseHotelLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case CHECK_IN_CHOOSE_HOTEL:
                this.checkInChooseHotelLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //check in connector
    public void setCheckInConnector(CheckInConnector checkInConnector) {
        this.checkInConnector = checkInConnector;
    }

    //choose hotel
    private void initCheckInChooseHotelLayout(View view) {

        //check in choose hotel hotel field
        this.initCheckInChooseHotelHotelField(view);

        //check in choose hotel error field
        this.initCheckInChooseHotelErrorField(view);

        //check in choose hotel button
        this.initCheckInChooseHotelButton(view);

    }

    private void initCheckInChooseHotelHotelField(View view) {

        this.checkInChooseHotelHotelEditText = (MyAutoCompleteEditText) view.findViewById(R.id.check_in_choose_hotel_fragment_hotel_edit_text);
        MyAppThemeUtilsManager.get().initMyEditText(this.checkInChooseHotelHotelEditText, "Hotel",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"gmd_location_city"), this.getContext(), this.screenSkin);

        this.checkInChooseHotelHotelEditText.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.checkInChooseHotelHotelEditText.setValidator(new Validator<String>() {
            @Override
            public String validate(String text) {
                if (RegexUtils.isMatch("^[\\p{L} .'-]+$", text) || text.equals("")) {
                    return null;
                } else {
                    return "Please enter hotel name";
                }
            }
        });

        this.checkInChooseHotelHotelEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                CheckInChooseHotelFragment.this.checkCheckInChooseHotelFields();
            }
        });


        ArrayList<HotelAutocompleteEntry> hotels = new ArrayList<HotelAutocompleteEntry>();
        this.hotelAutocompleteAdapter = new HotelAutocompleteAdapter(this.getContext(), hotels);
        this.checkInChooseHotelHotelEditText.getAutoCompleteTextView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager inputMethodManager = (InputMethodManager) CheckInChooseHotelFragment.this.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                if (inputMethodManager.isAcceptingText()) {
                    View currentFocus = CheckInChooseHotelFragment.this.getActivity().getCurrentFocus();
                    if (currentFocus != null) {
                        inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                    }
                }

                Object item = parent.getItemAtPosition(position);
                if (item instanceof HotelAutocompleteEntry){
                    CheckInChooseHotelFragment.this.checkInChooseHotelHotelEditText.setText(((HotelAutocompleteEntry) item).getHotelName());
                }
            }
        });


    }

    private void initCheckInChooseHotelErrorField(View view) {

        this.checkInChooseHotelErrorTextView = (TextView) view.findViewById(R.id.check_in_choose_hotel_fragment_error_text_view);
        MyAppThemeUtilsManager.get().initTextView(this.checkInChooseHotelErrorTextView, "", this.getContext(), this.screenSkin);
        this.checkInChooseHotelErrorTextView.setTextSize((int) this.getResources().getDimension(R.dimen.default_field_info_text_size));
        this.checkInChooseHotelErrorTextView.setTextColor(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_ERROR_COLOR, this.screenSkin));
        this.checkInChooseHotelErrorTextView.setMaxLines(5);

    }

    private void initCheckInChooseHotelButton(View view) {

        this.checkInChooseHotelQRCheckInButton = (MyButton) view.findViewById(R.id.check_in_choose_hotel_fragment_qr_check_in_button);
        MyAppThemeUtilsManager.get().initMyButton(this.checkInChooseHotelQRCheckInButton, "Check In", this.getContext(), this.screenSkin);
        this.checkInChooseHotelQRCheckInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //check in hotel

            }
        });
        this.checkInChooseHotelQRCheckInButton.setEnabled(false);
        this.checkInChooseHotelQRCheckInButton.setWidth((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 260));
        this.checkInChooseHotelQRCheckInButton.setButtonBorderRadius((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 30));

    }

    private void checkCheckInChooseHotelFields() {
        if (((this.checkInChooseHotelHotelEditText.getErrorText() != null) && (!this.checkInChooseHotelHotelEditText.getErrorText().toString().equals("")))
                || this.checkInChooseHotelHotelEditText.getText().toString().equals("")) {
            this.checkInChooseHotelQRCheckInButton.setEnabled(false);
        } else {
            this.checkInChooseHotelQRCheckInButton.setEnabled(true);
        }
    }

    private void setCheckInChooseHotelError(String error) {

        //go to main view
        this.managementViewContainer.setScreenSkin(CHECK_IN_CHOOSE_HOTEL);

        //set the error message
        this.checkInChooseHotelErrorTextView.setText(error);

    }

    //get all hotels for autocomplete text view
    private void makeGetAllHotelsRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {
                CheckInChooseHotelFragment.this.managementViewContainer.setScreenState(LOADING);
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
                    CheckInChooseHotelFragment.this.hotelAutocompleteAdapter.setHotels(Arrays.asList(hotelsInfoArray));
                    CheckInChooseHotelFragment.this.checkInChooseHotelHotelEditText.setAdapter(CheckInChooseHotelFragment.this.hotelAutocompleteAdapter);
                    CheckInChooseHotelFragment.this.checkInChooseHotelHotelEditText.getAutoCompleteTextView().setThreshold(1);
                    CheckInChooseHotelFragment.this.managementViewContainer.setScreenState(CHECK_IN_CHOOSE_HOTEL);
                } catch (Exception e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    CheckInChooseHotelFragment.this.setCheckInChooseHotelError(message);
                } else {
                    CheckInChooseHotelFragment.this.setCheckInChooseHotelError("Getting Hotels List Error");
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

    }


}
