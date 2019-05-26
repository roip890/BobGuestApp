package com.bob.bobguestapp.activities.main.fragments.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.connector.MainConnector;
import com.bob.bobguestapp.activities.main.fragments.notifications.notificaitonslist.AppNotificationsListAdapter;
import com.bob.bobguestapp.tools.database.MyDBUtilsManager;
import com.bob.bobguestapp.tools.database.MyRealmController;
import com.bob.bobguestapp.tools.database.objects.GuestAppNotification;
import com.bob.bobguestapp.tools.recyclerview.MyRecyclerView;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.toolsmodule.parsing.GsonParser;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.bob.bobguestapp.tools.recyclerview.MyRecyclerView.RECYCLER_VIEW;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.FAILURE_MESSAGE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.LOADING;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.NONE_MESSAGE;


public class NotificationsFragment extends ManagementFragment {

    private static final int NOTIFICATIONS_BATCH = 10;

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";
    private static String BOB_SERVER_NOTIFICATIONS_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/NotificationServices";

    //get notifications url
    public static final String GET_NOTIFICATIONS_OF_GUEST = BOB_SERVER_NOTIFICATIONS_SERVICES_URL + "/guest/notification/getPage";

    //main view screen states
    public static final int NOTIFICATIONS_LIST = 10;

    //main commands
    private MainConnector mainConnector;

    //service
    NotificationsBroadcastReceiver notificationsBroadcastReceiver;
    IntentFilter notificationsIntentFilter;

    //notifications layout
    private RelativeLayout notificationsLayout;
    private SwipeRefreshLayout notificationsRecyclerViewSwipeRefreshLayout;
    private MyRecyclerView notificationsRecyclerView;
    private AppNotificationsListAdapter notificationsListAdapter;
    private ArrayList<GuestAppNotification> notificationsList;

    public NotificationsFragment() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.NOTIFICATIONS_FRAGMENT_SKIN);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        this.notificationsRecyclerView.setScreenState(LOADING);
        this.makeRefreshNotificationsRequest();

        return view;

    }

    @Override
    public View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

        //init management skin
        this.managementViewContainer.setScreenSkin(this.screenSkin);

        //view
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        //init notifications layout
        this.initNotificationsLayout(view);

        //init broadcast receiver
        this.initBroadcastReceiver();

        //init intent filter
        this.initIntentFilter();

        //set screen state
        this.managementViewContainer.setScreenState(NOTIFICATIONS_LIST);

        return view;
    }

    //screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.notificationsLayout.setVisibility(INVISIBLE);

        switch (screenState) {
            case NOTIFICATIONS_LIST:
                this.notificationsLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //main commands
    public void setMainConnector(MainConnector mainConnector) {
        this.mainConnector = mainConnector;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.getActivity().registerReceiver(this.notificationsBroadcastReceiver, notificationsIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.getActivity().unregisterReceiver(this.notificationsBroadcastReceiver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //notifications
    private void initNotificationsLayout(View view) {

        //notifications list
        this.initNotificationsList();

        //notifications main layout
        this.initNotificationsMainLayout(view);

        //notifications swipe refresh layout
        this.initNotificationsRecyclerViewSwipeRefreshLayout(view);

    }

    private void initNotificationsList() {

        this.notificationsList = new ArrayList<GuestAppNotification>();

    }

    private void initNotificationsMainLayout(View view) {

        this.notificationsLayout = (RelativeLayout) view.findViewById(R.id.notifications_fragment_notifications_list_layout);

    }

    private void initNotificationsRecyclerViewSwipeRefreshLayout(View view) {

        this.notificationsRecyclerViewSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.notifications_fragment_notifications_list_recycler_view_swipe_refresh_layout);

        this.notificationsRecyclerViewSwipeRefreshLayout.setColorSchemeColors(MyAppThemeUtilsManager.get(this.getContext()).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_PRIMARY_COLOR,
                this.screenSkin
        ), MyAppThemeUtilsManager.get(this.getContext()).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_SECONDARY_COLOR,
                this.screenSkin
        ));

        this.notificationsRecyclerViewSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(MyAppThemeUtilsManager.get(this.getContext()).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_BACKGROUND_PRIMARY_COLOR,
                this.screenSkin
        ));

        this.notificationsRecyclerViewSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                NotificationsFragment.this.notificationsRecyclerView.setScreenState(LOADING);
                NotificationsFragment.this.makeRefreshNotificationsRequest();

            }
        });

        //notifications recycler view
        this.initNotificationsRecyclerView(view);

    }

    private void initNotificationsRecyclerView(View view) {

        this.notificationsRecyclerView = (MyRecyclerView) view.findViewById(R.id.notifications_fragment_notifications_list_recycler_view);

        this.notificationsRecyclerView.setRecyclerViewOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                NotificationsFragment.this.makeGetNotificationsRequest();

            }
        });


        this.notificationsRecyclerView.setScreenSkin(ThemeUtilsManager.LIGHT_COLOR_SKIN);

//        horizontal line divider between items
//        this.setHorizontalDivider();

        this.setAdapter();


    }

    private void setHorizontalDivider() {

        DividerItemDecoration horizontalDecorationOpacity = new DividerItemDecoration(this.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDividerOpacity = ContextCompat.getDrawable(getActivity(), R.drawable.horizontal_divider).mutate();
        horizontalDividerOpacity.setColorFilter(new PorterDuffColorFilter(
                MyAppThemeUtilsManager.get(this.getContext()).getColor(MyAppThemeUtilsManager.DEFAULT_RECYCLER_VIEW_SEPARATOR_COLOR, this.screenSkin)
                ,PorterDuff.Mode.SRC_ATOP));
        horizontalDecorationOpacity.setDrawable(horizontalDividerOpacity);
        this.notificationsRecyclerView.getRecyclerView().addItemDecoration(horizontalDecorationOpacity);

    }

    private void setAdapter() {

        if (this.notificationsList != null) {
            this.notificationsListAdapter = new AppNotificationsListAdapter(this.getActivity(), this.notificationsList);
        } else {
            this.notificationsListAdapter = new AppNotificationsListAdapter(this.getActivity(), new ArrayList<GuestAppNotification>());
        }
        this.notificationsRecyclerView.getRecyclerView().setLayoutManager(new GridLayoutManager(this.getActivity(), 1));
        this.notificationsRecyclerView.getRecyclerView().setAdapter(this.notificationsListAdapter);

    }

    private void makeRefreshNotificationsRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                NotificationsFragment.this.notificationsRecyclerView.setMessage("Loading...", NONE_MESSAGE);
                NotificationsFragment.this.notificationsRecyclerView.setLoadMore(true, "Loading...");
                NotificationsFragment.this.notificationsRecyclerView.setLoadingItems(true);

                NotificationsFragment.this.notificationsRecyclerViewSwipeRefreshLayout.setEnabled(false);
                NotificationsFragment.this.notificationsRecyclerViewSwipeRefreshLayout.setRefreshing(true);

            }

            @Override
            protected boolean requestCondition() {
                long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1L);
                long bookingId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("bookingId", -1L);
                if ((guestId != -1L) && (bookingId != -1L)) {
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

                String url = GET_NOTIFICATIONS_OF_GUEST;

                long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1L);
                url += "?guestId=" + Long.toString(guestId);

                long bookingId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("bookingId", -1L);
                url += "&booking=" + Long.toString(bookingId);

                if (NotificationsFragment.this.notificationsRecyclerView != null
                        && NotificationsFragment.this.notificationsRecyclerView.getRecyclerView() != null
                        && NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter() != null
                        && NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter() instanceof AppNotificationsListAdapter) {

                    AppNotificationsListAdapter adapter = ((AppNotificationsListAdapter)NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter());

                    //index & limit
                    url += "&offset=0";

                    url += "&amount=20";

                }


                return url;
//                return BOB_SERVER_WEB_SERVICES_URL + "/wishes/getAllByUser?user=15";
            }

            @Override
            protected ApplicativeResponse getApplicativeResponse(JSONObject response) {
                try {
                    Gson customGson = MyGsonParser.getParser().create();
                    GuestAppNotification[] appNotifications = null;
                    if (response.getJSONObject("response").optJSONArray("notifications") != null) {
                        String appNotificationsString = response.getJSONObject("response").getJSONArray("notifications").toString();
                        appNotifications = customGson.fromJson(appNotificationsString, GuestAppNotification[].class);
                    } else if (response.getJSONObject("response").optJSONObject("notifications") != null) {
                        String appNotificationsString = response.getJSONObject("response").getJSONObject("notifications").toString();
                        appNotifications = new GuestAppNotification[] {
                                customGson.fromJson(appNotificationsString, GuestAppNotification.class)
                        };
                    }

                    if (appNotifications != null) {
                        ApplicativeResponse statusResponse = new ApplicativeResponse();
                        statusResponse.setStatus("Success");
                        statusResponse.setCode(ApplicativeResponse.SUCCESS);
                        return statusResponse;
                    } else {
                        ApplicativeResponse statusResponse = new ApplicativeResponse();
                        statusResponse.setStatus("Failure");
                        statusResponse.setCode(ApplicativeResponse.FAILURE);
                        statusResponse.setMessage("error in parsing response");
                        return statusResponse;
                    }

//                    String statusResponse = response.getJSONObject("response").getJSONObject("statusResponse").toString();
//                    return customGson.fromJson(statusResponse, ApplicativeResponse.class);

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

                    if (response.getJSONObject("response").has("notifications")) {

                        GuestAppNotification[] appNotifications = new GuestAppNotification[0];
                        if (response.getJSONObject("response").optJSONArray("notifications") != null) {
                            String appNotificationsString = response.getJSONObject("response").getJSONArray("notifications").toString();
                            appNotifications = customGson.fromJson(appNotificationsString, GuestAppNotification[].class);
                        } else if (response.getJSONObject("response").optJSONObject("notifications") != null) {
                            String appNotificationsString = response.getJSONObject("response").getJSONObject("notifications").toString();
                            appNotifications = new GuestAppNotification[] {
                                    customGson.fromJson(appNotificationsString, GuestAppNotification.class)
                            };
                        }

                        if (appNotifications != null && appNotifications.length > 0) {

                            MyDBUtilsManager.get().clearNotificationsFromDB();

                            MyDBUtilsManager.get().insertNotificationsListToDB(appNotifications);

                            if (NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter() != null
                                    && NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter() instanceof AppNotificationsListAdapter) {

                                ArrayList<GuestAppNotification> notificationsFromServer = new ArrayList<GuestAppNotification>(Arrays.asList(appNotifications));
                                ((AppNotificationsListAdapter)NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter()).setObjects(notificationsFromServer);
                                NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter().notifyDataSetChanged();

                            }

                            NotificationsFragment.this.notificationsRecyclerView.setHasMoreItems(true);

                        } else {

                            NotificationsFragment.this.notificationsRecyclerView.setFinishLoading(true, "-End-");
                            NotificationsFragment.this.notificationsRecyclerView.setHasMoreItems(false);

                        }


                    } else {

                        NotificationsFragment.this.notificationsRecyclerView.setFinishLoading(true, "-End-");
                        NotificationsFragment.this.notificationsRecyclerView.setHasMoreItems(false);

                    }

                    NotificationsFragment.this.notificationsRecyclerView.setScreenState(RECYCLER_VIEW);

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    NotificationsFragment.this.notificationsRecyclerView.setMessage(message, FAILURE_MESSAGE);
                } else {
                    NotificationsFragment.this.notificationsRecyclerView.setMessage("Getting Notifications List Error!", FAILURE_MESSAGE);
                }
            }

            @Override
            protected void postRequest() {

                NotificationsFragment.this.notificationsRecyclerViewSwipeRefreshLayout.setRefreshing(false);
                NotificationsFragment.this.notificationsRecyclerViewSwipeRefreshLayout.setEnabled(true);

                NotificationsFragment.this.notificationsRecyclerView.setLoadMore(false);
                NotificationsFragment.this.notificationsRecyclerView.setLoadingItems(false);

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void makeGetNotificationsRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                NotificationsFragment.this.notificationsRecyclerView.setLoadingItems(true);
                NotificationsFragment.this.notificationsRecyclerView.setLoadMore(true, "Loading...");

            }

            @Override
            protected boolean requestCondition() {
                long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1L);
                return guestId != -1L;
            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected void setRequestParams(HashMap<String, String> params) {
                String guestEmail = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestEmail", null);
                String hotelName = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelName", null);
                params.put("guestEmail", guestEmail);
                params.put("hotelName", hotelName);
            }


            @Override
            protected String getRequestUrl() {

                String url = GET_NOTIFICATIONS_OF_GUEST;

                long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1L);
                url += "?guestId=" + Long.toString(guestId);

                long bookingId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("bookingId", -1L);
                url += "&booking=" + Long.toString(bookingId);

                if (NotificationsFragment.this.notificationsRecyclerView != null
                        && NotificationsFragment.this.notificationsRecyclerView.getRecyclerView() != null
                        && NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter() != null
                        && NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter() instanceof AppNotificationsListAdapter) {

                    AppNotificationsListAdapter adapter = ((AppNotificationsListAdapter)NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter());

                    //index & limit
                    url += "&offset=" + adapter.getAllObjects().size();

                    url += "&amount=20";

                }


                return url;

            }

            @Override
            protected ApplicativeResponse getApplicativeResponse(JSONObject response) {
                try {
                    Gson customGson = MyGsonParser.getParser().create();
                    GuestAppNotification[] appNotifications = null;
                    if (response.getJSONObject("response").optJSONArray("notifications") != null) {
                        String appNotificationsString = response.getJSONObject("response").getJSONArray("notifications").toString();
                        appNotifications = customGson.fromJson(appNotificationsString, GuestAppNotification[].class);
                    } else if (response.getJSONObject("response").optJSONObject("notifications") != null) {
                        String appNotificationsString = response.getJSONObject("response").getJSONObject("notifications").toString();
                        appNotifications = new GuestAppNotification[] {
                                customGson.fromJson(appNotificationsString, GuestAppNotification.class)
                        };
                    }

                    if (appNotifications != null) {
                        ApplicativeResponse statusResponse = new ApplicativeResponse();
                        statusResponse.setStatus("Success");
                        statusResponse.setCode(ApplicativeResponse.SUCCESS);
                        return statusResponse;
                    } else {
                        ApplicativeResponse statusResponse = new ApplicativeResponse();
                        statusResponse.setStatus("Failure");
                        statusResponse.setCode(ApplicativeResponse.FAILURE);
                        statusResponse.setMessage("error in parsing response");
                        return statusResponse;
                    }

//                    String statusResponse = response.getJSONObject("response").getJSONObject("statusResponse").toString();
//                    return customGson.fromJson(statusResponse, ApplicativeResponse.class);

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

                    if (response.getJSONObject("response").has("notifications")) {

                        GuestAppNotification[] appNotifications = new GuestAppNotification[0];
                        if (response.getJSONObject("response").optJSONArray("notifications") != null) {
                            String appNotificationsString = response.getJSONObject("response").getJSONArray("notifications").toString();
                            appNotifications = customGson.fromJson(appNotificationsString, GuestAppNotification[].class);
                        } else if (response.getJSONObject("response").optJSONObject("notifications") != null) {
                            String appNotificationsString = response.getJSONObject("response").getJSONObject("notifications").toString();
                            appNotifications = new GuestAppNotification[] {
                                    customGson.fromJson(appNotificationsString, GuestAppNotification.class)
                            };
                        }


                        if (appNotifications != null && appNotifications.length > 0) {

                            MyDBUtilsManager.get().insertNotificationsListToDB(appNotifications);

                            if (NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter() != null
                                    && NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter() instanceof AppNotificationsListAdapter) {

                                ArrayList<GuestAppNotification> notificationsToInsert = new ArrayList<GuestAppNotification>();
                                ArrayList<GuestAppNotification> notificationsFromServer = new ArrayList<GuestAppNotification>(Arrays.asList(appNotifications));
                                List<GuestAppNotification> allNotifications = ((AppNotificationsListAdapter)NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter()).getAllObjects();

                                ArrayList<Long> allNotificationsIds = new ArrayList<Long>();
                                for (GuestAppNotification notification : allNotifications) {
                                    if (notification != null) {
                                        allNotificationsIds.add(notification.getId());
                                    }
                                }

                                for (GuestAppNotification notificationFromServer : notificationsFromServer) {
                                    if (notificationFromServer != null && !allNotificationsIds.contains(notificationFromServer.getId())) {
                                        notificationsToInsert.add(notificationFromServer);
                                    }
                                }

                                ((AppNotificationsListAdapter)NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter()).addObjects(notificationsToInsert);
                                NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter().notifyDataSetChanged();

                            }

                            NotificationsFragment.this.notificationsRecyclerView.setHasMoreItems(true);

                        } else {

                            NotificationsFragment.this.notificationsRecyclerView.setFinishLoading(true, "-End-");
                            NotificationsFragment.this.notificationsRecyclerView.setHasMoreItems(false);

                        }


                    } else {

                        NotificationsFragment.this.notificationsRecyclerView.setFinishLoading(true, "-End-");
                        NotificationsFragment.this.notificationsRecyclerView.setHasMoreItems(false);

                    }

                    NotificationsFragment.this.notificationsRecyclerView.setScreenState(RECYCLER_VIEW);

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    NotificationsFragment.this.notificationsRecyclerView.setMessage(message, FAILURE_MESSAGE);
                } else {
                    NotificationsFragment.this.notificationsRecyclerView.setMessage("Getting Notifications List Error!", FAILURE_MESSAGE);
                }
            }

            @Override
            protected void postRequest() {

                NotificationsFragment.this.notificationsRecyclerView.setLoadMore(false);
                NotificationsFragment.this.notificationsRecyclerView.setLoadingItems(false);

            }

        };

        jsonServerRequest.makeRequest();

    }
    
    private void setAppNotifications() {

        long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1L);
        if (guestId != -1L) {
            List<GuestAppNotification> appNotifications = MyRealmController.with(BOBGuestApplication.get()).getAppNotificationsOfGuest(guestId);
            if ((appNotifications != null) && (appNotifications.size() >0)) {
                this.notificationsRecyclerView.getRecyclerView().setLayoutManager(new GridLayoutManager(this.getActivity(), 1));
                this.notificationsListAdapter.setNotificationsListFromDB();
                this.notificationsRecyclerView.getRecyclerView().setAdapter(this.notificationsListAdapter);
            } else {

            }

        }

    }

    public void refreshNotifications() {

        this.makeRefreshNotificationsRequest();

    }

    //broadcast receiver
    private void initBroadcastReceiver() {

        this.notificationsBroadcastReceiver = new NotificationsBroadcastReceiver(new NotificationsBroadcastReceiverListener() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GET_NOTIFICATIONS_OF_GUEST)) {

                    if (intent.hasExtra("notification")
                            && intent.getStringExtra("notification") != null) {

                        String appNotificationString = intent.getStringExtra("notification");
                        try {

                            //parse notification
                            GuestAppNotification appNotification = GsonParser.getParser().create().fromJson(appNotificationString, GuestAppNotification.class);

                            //save notification
                            MyDBUtilsManager.get().insertNotificationToDB(appNotification);

                            //add notification on notifications list
                            if (NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter() != null
                                    && NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter() instanceof AppNotificationsListAdapter) {

                                List<GuestAppNotification> allNotifications = ((AppNotificationsListAdapter)NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter()).getAllObjects();

                                ArrayList<Long> allNotificationsIds = new ArrayList<Long>();
                                for (GuestAppNotification notification : allNotifications) {
                                    if (notification != null) {
                                        allNotificationsIds.add(notification.getId());
                                    }
                                }

                                if (appNotification != null && !allNotificationsIds.contains(appNotification.getId())) {
                                    ((AppNotificationsListAdapter)NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter()).addObjects(new ArrayList<GuestAppNotification>(Arrays.asList(appNotification)));
                                    NotificationsFragment.this.notificationsRecyclerView.getRecyclerView().getAdapter().notifyDataSetChanged();
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
            }
        });
    }

    //intent filter
    private void initIntentFilter(){

        this.notificationsIntentFilter = new IntentFilter();
        this.notificationsIntentFilter.addAction(GET_NOTIFICATIONS_OF_GUEST);
        
    }

    //back pressed handler
    public void onBackPressed() {

    }

}
