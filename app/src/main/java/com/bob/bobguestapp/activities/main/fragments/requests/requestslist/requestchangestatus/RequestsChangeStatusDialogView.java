package com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestchangestatus;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestchangestatus.statuseslist.GuestRequestStatus;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestchangestatus.statuseslist.GuestRequestStatusesListAdapter;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestchangestatus.statuseslist.OnChangeRequestStatusListener;
import com.bob.bobguestapp.tools.database.MyRealmController;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.database.objects.GuestRequest;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.uimodule.views.loadingcontainer.ManagementLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestsChangeStatusDialogView extends ManagementLayout {

    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //change status
    private static final String CHANGE_STATUS = BOB_SERVER_WEB_SERVICES_URL + "/wishes/update";

    //main view screen states
    public static final int REQUEST_STATUSES = 10;

    //views
    private RelativeLayout requestChangeStatusDialogLayout;
    private RecyclerView guestRequestStatusesListRecyclerView;
    private GuestRequestStatusesListAdapter guestRequestStatusesListAdapter;

    //listener
    private RequestChangeStatusDialogListener requestChangeStatusDialogListener;

    //guest request
    private GuestRequest guestRequest;

    //constructors
    public RequestsChangeStatusDialogView(Context context) {
        this(context, null);
    }

    public RequestsChangeStatusDialogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RequestsChangeStatusDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin= MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.REQUEST_LIST_ITEM_CHANGE_STATUS_DIALOG_SKIN);

        this.initMainView();
    }

    @Override
    protected View onCreateMainView() {


        View view = this.initChangeStatusDialogLayout();

        this.setScreenState(REQUEST_STATUSES);

        return view;

    }

    @Override
    protected void setMainViewScreenState(int screenState) {

        this.requestChangeStatusDialogLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case REQUEST_STATUSES:
                this.requestChangeStatusDialogLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }
    }

    //view
    protected View initChangeStatusDialogLayout() {

        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View view = inflater.inflate(R.layout.dialog_request_change_status, new RelativeLayout(this.getContext()));

        this.requestChangeStatusDialogLayout = (RelativeLayout) view.findViewById(R.id.request_change_status_dialog_statuses_layout);

        this.initGuestRequestStatusesListRecyclerView(view);

        return view;
    }

    private void initGuestRequestStatusesListRecyclerView(View view) {

        this.guestRequestStatusesListRecyclerView = (RecyclerView) view.findViewById(R.id.request_change_status_dialog_statuses_recycler_view);

        //init adapter
        this.initGuestRequestStatusesListAdapter();

        //set adapter
        this.setGuestRequestStatusesListAdapter();

        //set columns to 1
        this.guestRequestStatusesListRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
        this.guestRequestStatusesListRecyclerView.setAdapter(guestRequestStatusesListAdapter);
        guestRequestStatusesListAdapter.notifyDataSetChanged();

    }

    private void initGuestRequestStatusesListAdapter() {

        this.guestRequestStatusesListAdapter = new GuestRequestStatusesListAdapter(this.getContext());

        ArrayList<GuestRequestStatus> guestRequestStatuses = new ArrayList<GuestRequestStatus>();
        guestRequestStatuses.add(new GuestRequestStatus("waiting", R.color.waiting_status_color));
        guestRequestStatuses.add(new GuestRequestStatus("in_progress", R.color.in_progress_status_color));
        guestRequestStatuses.add(new GuestRequestStatus("done", R.color.done_status_color));

        this.guestRequestStatusesListAdapter.setGuestRequestStatuses(guestRequestStatuses);

        this.guestRequestStatusesListAdapter.setOnChangeRequestStatusListener(new OnChangeRequestStatusListener() {
            @Override
            protected void onRequestStatusChange(GuestRequestStatus guestRequestStatus) {

                RequestsChangeStatusDialogView.this.makeChangeRequestStatusRequest(
                        RequestsChangeStatusDialogView.this.guestRequest, guestRequestStatus);

            }
        });

    }

    private void setGuestRequestStatusesListAdapter() {

        if (this.guestRequestStatusesListRecyclerView != null) {

            this.guestRequestStatusesListRecyclerView.setAdapter(this.guestRequestStatusesListAdapter);

            if (this.guestRequestStatusesListRecyclerView.getAdapter() != null) {

                this.guestRequestStatusesListRecyclerView.getAdapter().notifyDataSetChanged();

            }
        }

    }

    //listener
    public void setRequestChangeStatusDialogListener(RequestChangeStatusDialogListener requestChangeStatusDialogListener) {

        this.requestChangeStatusDialogListener = requestChangeStatusDialogListener;

    }

    //change status request
    private void makeChangeRequestStatusRequest(GuestRequest guestRequest, GuestRequestStatus guestRequestStatus) {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {
                RequestsChangeStatusDialogView.this.setScreenState(LOADING);
            }

            @Override
            protected boolean requestCondition() {
                if (guestRequest != null) {
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
                    GuestRequest guestRequest = new GuestRequest();
                    guestRequest.setId(RequestsChangeStatusDialogView.this.guestRequest.getId());
                    guestRequest.setStatus(guestRequestStatus.getStatus());

                    JsonObject jsonObject = new JsonObject();

                    JsonObject jsonRequest = new JsonObject();
                    JsonElement jsonWish = MyGsonParser.getParser().create().toJsonTree(guestRequest);
                    jsonRequest.add("wish", jsonWish);
                    jsonObject.add("request", jsonRequest);

                    return new JSONObject( new Gson().toJson(jsonObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected String getRequestUrl() {
                return CHANGE_STATUS;
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

                MyRealmController.get().getRealm().beginTransaction();
                guestRequest.setStatus(guestRequestStatus.getStatus());
                MyRealmController.get().getRealm().commitTransaction();
//                        MyRealmController.get().insertOrUpdateGuestRequest(RequestsListViewHolder.this.guestRequest);
                if (RequestsChangeStatusDialogView.this.requestChangeStatusDialogListener != null) {

                    RequestsChangeStatusDialogView.this.requestChangeStatusDialogListener.onRequestStatusChange(guestRequestStatus);

                }

                RequestsChangeStatusDialogView.this.setMessage("Status Changed!", SUCCESS_MESSAGE);

            }

            @Override
            protected void onDefaultError(String message) {
                RequestsChangeStatusDialogView.this.setMessage("Error Changing Status!", FAILURE_MESSAGE);
            }

        };

        jsonServerRequest.makeRequest();


    }

    //guest request
    public void setGuestRequest(GuestRequest guestRequest) {

        this.guestRequest = guestRequest;
    }

}
