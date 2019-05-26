package com.bob.bobguestapp.activities.main.fragments.requests;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.intro.connector.IntroConnector;
import com.bob.bobguestapp.activities.main.connector.MainConnector;
import com.bob.bobguestapp.activities.main.fragments.requests.requestsfilter.OnRequestFilterButtonClickListener;
import com.bob.bobguestapp.activities.main.fragments.requests.requestsfilter.RequestsFilterDialogView;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.RequestsListAdapter;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.filter.GuestRequestsFilter;
import com.bob.bobguestapp.tools.database.MyDBUtilsManager;
import com.bob.bobguestapp.tools.database.MyRealmController;
import com.bob.bobguestapp.tools.recyclerview.MyRecyclerView;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.database.objects.GuestRequest;
import com.bob.toolsmodule.database.objects.RequestItem;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.finals;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.bob.bobguestapp.tools.recyclerview.MyRecyclerView.RECYCLER_VIEW;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.FAILURE_MESSAGE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.LOADING;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.NONE_MESSAGE;


public class RequestsFragment extends ManagementFragment {

    private static final int REQUESTS_BATCH = 10;
    private static final int REQUESTS_SORT_BUTTON_INDEX = 0;
    private static final int REQUESTS_FILTER_BUTTON_INDEX = 1;
    private static final int REQUESTS_REFRESH_BUTTON_INDEX = 2;

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //get all requests url
    private static final String GET_ALL_REQUESTS_URL = BOB_SERVER_MOBILE_SERVICES_URL + "/wishes/getAllByGuestAndHotel";

    //main view screen states
    public static final int REQUESTS_LIST = 10;

    //skins
    private int sortDialogSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;
    private int filterDialogSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;

    //intro commands
    private MainConnector mainConnector;

    //requests layout
    private RelativeLayout requestsLayout;
    private SwipeRefreshLayout requestsRecyclerViewSwipeRefreshLayout;
    private MyRecyclerView requestsRecyclerView;
    private RequestsListAdapter requestsListAdapter;
    private ArrayList<GuestRequest> requestsList;

    //tabs
    private TabLayout tabLayout;

    //request filter dialog layout
    private MaterialDialog requestsFilterMaterialDialog;

    //request sort dialog layout
    private MaterialDialog requestsSortMaterialDialog;


    public RequestsFragment() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_SKIN);
        this.sortDialogSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_SORT_DIALOG_SKIN);
        this.filterDialogSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_FILTER_DIALOG_SKIN);

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

        this.requestsRecyclerView.setScreenState(LOADING);
        this.makeRefreshRequestsRequest();


        return view;

    }

    @Override
    public View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //init management skin
        this.managementViewContainer.setScreenSkin(this.screenSkin);

        //view
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        //init requests list layouts
        this.initRequestsLayout(view);

        this.managementViewContainer.setScreenState(REQUESTS_LIST);

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.requestsLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case REQUESTS_LIST:
                this.requestsLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //main commands
    public void setMainConnector(MainConnector mainConnector) {
        this.mainConnector = mainConnector;
    }

    //requests list
    private void initRequestsLayout(View view) {

        //request list
        this.initRequestsList();

        //request main layout
        this.initRequestsMainLayout(view);

        //request buttons tabs
        this.initRequestsButtonsTabs(view);

        //request recycler view swipe refresh layout
        this.initRequestsRecyclerViewSwipeRefreshLayout(view);

    }

    private void initRequestsList() {
        this.requestsList = new ArrayList<GuestRequest>();
    }

    private void initRequestsMainLayout(View view) {

        this.requestsLayout = (RelativeLayout) view.findViewById(R.id.requests_fragment_requests_layout);

    }

    private void initRequestsRecyclerViewSwipeRefreshLayout(View view) {

        this.requestsRecyclerViewSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.requests_fragment_requests_list_recycler_view_swipe_refresh_layout);

        this.requestsRecyclerViewSwipeRefreshLayout.setColorSchemeColors(MyAppThemeUtilsManager.get(this.getContext()).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_PRIMARY_COLOR,
                this.screenSkin
        ), MyAppThemeUtilsManager.get(this.getContext()).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_SECONDARY_COLOR,
                this.screenSkin
        ));

        this.requestsRecyclerViewSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(MyAppThemeUtilsManager.get(this.getContext()).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_BACKGROUND_PRIMARY_COLOR,
                this.screenSkin
        ));

        this.requestsRecyclerViewSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                RequestsFragment.this.requestsRecyclerView.setScreenState(LOADING);
                RequestsFragment.this.makeRefreshRequestsRequest();

            }
        });

        //recycler view
        this.initRequestsRecyclerView(view);

    }

    private void initRequestsRecyclerView(View view) {

        this.requestsRecyclerView = (MyRecyclerView) view.findViewById(R.id.requests_fragment_requests_list_recycler_view);

        this.requestsRecyclerView.setRecyclerViewOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RequestsFragment.this.makeGetRequestsRequest();

            }
        });


        this.requestsRecyclerView.setScreenSkin(ThemeUtilsManager.LIGHT_COLOR_SKIN);



        //horizontal line divider between items
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
        this.requestsRecyclerView.getRecyclerView().addItemDecoration(horizontalDecorationOpacity);

        //        this.requestsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

    }

    private void setAdapter() {

        if (this.requestsList != null) {
            this.requestsListAdapter = new RequestsListAdapter(this.getActivity(), this.requestsList);
        } else {
            this.requestsListAdapter = new RequestsListAdapter(this.getActivity(), new ArrayList<GuestRequest>());
        }
        this.requestsListAdapter.setScreenSkin(this.screenSkin);
        this.requestsRecyclerView.getRecyclerView().setLayoutManager(new GridLayoutManager(this.getActivity(), 1));
        this.requestsRecyclerView.getRecyclerView().setAdapter(this.requestsListAdapter);

    }

    private void setGuestRequests() {
        long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1L);
        if (guestId != -1L) {
            List<GuestRequest> guestRequests = MyRealmController.with(BOBGuestApplication.get()).getGuestRequestsOfGuest(guestId);
            if ((guestRequests != null) && (guestRequests.size() > 0)) {
                this.requestsRecyclerView.getRecyclerView().setLayoutManager(new GridLayoutManager(this.getActivity(), 1));
                this.requestsListAdapter.setRequestsListFromDB();
                this.requestsRecyclerView.getRecyclerView().setAdapter(this.requestsListAdapter);
                this.requestsListAdapter.notifyDataSetChanged();
            } else {

            }

        }
    }

    private void initRequestsButtonsTabs(View view) {

        //tab layout
        this.tabLayout = (TabLayout) view.findViewById(R.id.requests_fragment_requests_buttons_tabs);

        //tab layout color
        this.tabLayout.setBackgroundColor(MyAppThemeUtilsManager.get(this.getContext()).getColor(MyAppThemeUtilsManager.DEFAULT_TAB_LAYOUT_BACKGROUND_COLOR_PRIMARY, this.screenSkin));

        //tab layout color
        this.initRequestsButtonsTabsButtons();

    }

    private void initRequestsButtonsTabsButtons() {

        //sort button
        Drawable sortIconDrawable = ContextCompat.getDrawable(RequestsFragment.this.getContext(),R.drawable.ic_sort_white_24dp).mutate();
        sortIconDrawable.setColorFilter(new PorterDuffColorFilter(MyAppThemeUtilsManager.get(RequestsFragment.this.getContext()).getColor(MyAppThemeUtilsManager.DEFAULT_TAB_ICON_COLOR, RequestsFragment.this.screenSkin), PorterDuff.Mode.SRC_IN));
        this.tabLayout.addTab(this.tabLayout.newTab().setIcon(sortIconDrawable), REQUESTS_SORT_BUTTON_INDEX);

        //filter button
        Drawable filterIconDrawable = ContextCompat.getDrawable(RequestsFragment.this.getContext(),R.drawable.ic_filter_list_white_24dp).mutate();
        filterIconDrawable.setColorFilter(new PorterDuffColorFilter(MyAppThemeUtilsManager.get(RequestsFragment.this.getContext()).getColor(MyAppThemeUtilsManager.DEFAULT_TAB_ICON_COLOR, RequestsFragment.this.screenSkin), PorterDuff.Mode.SRC_IN));
        this.tabLayout.addTab(this.tabLayout.newTab().setIcon(filterIconDrawable), REQUESTS_FILTER_BUTTON_INDEX);

        //refresh button
        Drawable refreshIconDrawable = ContextCompat.getDrawable(RequestsFragment.this.getContext(),R.drawable.ic_refresh_white_24dp).mutate();
        refreshIconDrawable.setColorFilter(new PorterDuffColorFilter(MyAppThemeUtilsManager.get(RequestsFragment.this.getContext()).getColor(MyAppThemeUtilsManager.DEFAULT_TAB_ICON_COLOR, RequestsFragment.this.screenSkin), PorterDuff.Mode.SRC_IN));
        this.tabLayout.addTab(this.tabLayout.newTab().setIcon(refreshIconDrawable), REQUESTS_REFRESH_BUTTON_INDEX);

        //set indicator color to be transparent
        this.tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.tabLayout.post(new Runnable() {
            @Override
            public void run() {

                RequestsFragment.this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        switch (tab.getPosition()) {
                            case REQUESTS_SORT_BUTTON_INDEX:
                                RequestsFragment.this.onRequestSortClick();
                                break;
                            case REQUESTS_FILTER_BUTTON_INDEX:
                                RequestsFragment.this.onRequestFilterClick();
                                break;
                            case REQUESTS_REFRESH_BUTTON_INDEX:
                                RequestsFragment.this.onRequestRefreshClick();
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        switch (tab.getPosition()) {
                            case REQUESTS_SORT_BUTTON_INDEX:
                                RequestsFragment.this.onRequestSortClick();
                                break;
                            case REQUESTS_FILTER_BUTTON_INDEX:
                                RequestsFragment.this.onRequestFilterClick();
                                break;
                            case REQUESTS_REFRESH_BUTTON_INDEX:
                                RequestsFragment.this.onRequestRefreshClick();
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });

    }

    private void onRequestSortClick() {

        this.initRequestSortDialog();

    }

    private void initRequestSortDialog() {

        this.requestsSortMaterialDialog = new MaterialDialog.Builder(this.getContext())
                .title("Sort By")
                .titleGravity(finals.dialogGravity.get("center"))
                .items("Date ▲", "Date ▼", "Status ▲", "Status ▼",
                        "Title ▲", "Title ▼")
                .itemsGravity(finals.dialogGravity.get("center"))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        if (text.equals("Date ▲")) {
                            RequestsFragment.this.requestsListAdapter.setSortingType("date");
                            RequestsFragment.this.requestsListAdapter.setAscending(true);
                        } else if (text.equals("Date ▼")) {
                            RequestsFragment.this.requestsListAdapter.setSortingType("date");
                            RequestsFragment.this.requestsListAdapter.setAscending(false);
                        } else if (text.equals("Status ▲")) {
                            RequestsFragment.this.requestsListAdapter.setSortingType("status");
                            RequestsFragment.this.requestsListAdapter.setAscending(true);
                        } else if (text.equals("Status ▼")) {
                            RequestsFragment.this.requestsListAdapter.setSortingType("status");
                            RequestsFragment.this.requestsListAdapter.setAscending(false);
                        } else if (text.equals("Title ▲")) {
                            RequestsFragment.this.requestsListAdapter.setSortingType("title");
                            RequestsFragment.this.requestsListAdapter.setAscending(true);
                        } else if (text.equals("Title ▼")) {
                            RequestsFragment.this.requestsListAdapter.setSortingType("title");
                            RequestsFragment.this.requestsListAdapter.setAscending(false);
                        }
                        RequestsFragment.this.requestsSortMaterialDialog.dismiss();
                    }
                })
                .backgroundColor(MyAppThemeUtilsManager.get(this.getContext()).getColor(MyAppThemeUtilsManager.DEFAULT_DIALOG_BACKGROUND_COLOR_PRIMARY, this.sortDialogSkin))
                .show();

        View dialogView = this.requestsSortMaterialDialog.getWindow().getDecorView();
        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setColor(MyAppThemeUtilsManager.get(this.getContext()).getColor(MyAppThemeUtilsManager.DEFAULT_SCREEN_BACKGROUND_COLOR_PRIMARY, this.sortDialogSkin));
        shapeDrawable.setCornerRadius(UIUtilsManager.get().convertDpToPixels(this.getContext(), 10));
        dialogView.setBackground(shapeDrawable);

        ViewGroup.LayoutParams params = this.requestsSortMaterialDialog.getWindow().getAttributes();
        params.width = (int) this.getContext().getResources().getDimension(R.dimen.requests_list_fragment_sort_requests_dialog_width);
        this.requestsSortMaterialDialog.getWindow().setAttributes((WindowManager.LayoutParams) params);

    }

    private void onRequestFilterClick() {

        this.initRequestFilterDialog();

    }

    private void initRequestFilterDialog(){

        this.requestsFilterMaterialDialog = new MaterialDialog.Builder(this.getContext())
                .title("Filter By")
                .titleGravity(finals.dialogGravity.get("center"))
                .customView(this.initFilterDialogView(), true)
                .backgroundColor(
                        MyAppThemeUtilsManager.get(this.getContext()).getColor(MyAppThemeUtilsManager.DEFAULT_DIALOG_BACKGROUND_COLOR_PRIMARY, this.filterDialogSkin)
                )
                .show();

        View dialogView = this.requestsFilterMaterialDialog.getWindow().getDecorView();
        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setColor(MyAppThemeUtilsManager.get(this.getContext()).getColor(MyAppThemeUtilsManager.DEFAULT_SCREEN_BACKGROUND_COLOR_PRIMARY, this.filterDialogSkin));
        shapeDrawable.setCornerRadius(UIUtilsManager.get().convertDpToPixels(this.getContext(), 10));
        dialogView.setBackground(shapeDrawable);

        ViewGroup.LayoutParams params = this.requestsFilterMaterialDialog.getWindow().getAttributes();
        params.width = (int) this.getContext().getResources().getDimension(R.dimen.requests_list_fragment_filter_requests_dialog_width);
        this.requestsFilterMaterialDialog.getWindow().setAttributes((WindowManager.LayoutParams) params);

    }

    private void onRequestRefreshClick() {

        this.requestsRecyclerView.setScreenState(LOADING);
        this.makeRefreshRequestsRequest();

    }

    private void makeRefreshRequestsRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                RequestsFragment.this.requestsRecyclerView.setMessage("Loading...", NONE_MESSAGE);
                RequestsFragment.this.requestsRecyclerView.setLoadMore(true, "Loading...");
                RequestsFragment.this.requestsRecyclerView.setLoadingItems(true);

                RequestsFragment.this.requestsRecyclerViewSwipeRefreshLayout.setEnabled(false);
                RequestsFragment.this.requestsRecyclerViewSwipeRefreshLayout.setRefreshing(true);

            }

            @Override
            protected boolean requestCondition() {
                String guestEmail = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestEmail", null);
                String hotelName = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelName", null);
                long bookingId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("bookingId", -1L);
                if ((guestEmail != null) && (hotelName != null) && (bookingId != -1L)) {
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
            protected void setRequestParams(HashMap<String, String> params) {
                String guestEmail = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestEmail", null);
                String hotelName = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelName", null);
                params.put("guestEmail", guestEmail);
                params.put("hotelName", hotelName);
            }


            @Override
            protected String getRequestUrl() {

                String url = GET_ALL_REQUESTS_URL;

                String hotelName = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelName", null);
                url += "?hotel=" + hotelName;

                String guestEmail = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestEmail", null);
                url += "&guest=" + guestEmail;

                long bookingId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("bookingId", -1L);
                url += "&booking=" + Long.toString(bookingId);


                if (RequestsFragment.this.requestsRecyclerView != null
                        && RequestsFragment.this.requestsRecyclerView.getRecyclerView() != null
                        && RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter() != null
                        && RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter() instanceof RequestsListAdapter
                        && ((RequestsListAdapter)RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter()).getFilter() != null) {

                    RequestsListAdapter adapter = ((RequestsListAdapter)RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter());

                    //index & limit
                    url += "&index=0";

                    url += "&limit=20";

                    //sorting type
                    String sortingType = adapter.getSortingType();
                    if (sortingType != null && !sortingType.equals("")) {

                        url += "&sort=" + adapter.getSortingType();

                    }

                    //filter
                    GuestRequestsFilter filter = adapter.getFilter();

                    if (filter.isFilterByMinDate()) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
                        url += "&minTs=" + dateFormat.format(filter.getMinDate());

                    }

                    if (filter.isFilterByMaxDate()) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
                        url += "&maxTs=" + dateFormat.format(filter.getMaxDate());

                    }

                    if (filter.isFilterByStatus()) {

                        for (String status : filter.getStatuses()) {

                            url += "&status=" + status;

                        }

                    }

                }


                return url;
//                return BOB_SERVER_WEB_SERVICES_URL + "/wishes/getAllByUser?user=15";
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

                    if (response.getJSONObject("response").has("requests")) {

                        GuestRequest[] guestRequests = new GuestRequest[0];
                        if (response.getJSONObject("response").optJSONArray("requests") != null) {
                            String guestRequestsString = response.getJSONObject("response").getJSONArray("requests").toString();
                            guestRequests = customGson.fromJson(guestRequestsString, GuestRequest[].class);
                        } else if (response.getJSONObject("response").optJSONObject("requests") != null) {
                            String guestRequestsString = response.getJSONObject("response").getJSONObject("requests").toString();
                            guestRequests = new GuestRequest[] {
                                    customGson.fromJson(guestRequestsString, GuestRequest.class)
                            };
                        }

                        //COMMENT THIS!!
                        long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", 1L);
                        for(GuestRequest guestRequest : guestRequests) {
                            if (guestRequest != null) {
                                guestRequest.setGuestId(guestId);
                                if (guestRequest.getRequestItems() != null) {
                                    for (RequestItem requestItem : guestRequest.getRequestItems()) {
                                        if (requestItem != null) {
                                            requestItem.setType("text_view");
                                        }
                                    }
                                }
                            }
                        }

                        if (guestRequests != null && guestRequests.length > 0) {

                            MyDBUtilsManager.get().clearRequestsFromDB();

                            MyDBUtilsManager.get().insertRequestsListToDB(guestRequests);

                            if (RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter() != null
                                    && RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter() instanceof RequestsListAdapter) {

                                ArrayList<GuestRequest> requestsFromServer = new ArrayList<GuestRequest>(Arrays.asList(guestRequests));
                                ((RequestsListAdapter)RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter()).setObjects(requestsFromServer);
                                RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter().notifyDataSetChanged();

                            }

                            RequestsFragment.this.requestsRecyclerView.setHasMoreItems(true);

                        } else {

                            RequestsFragment.this.requestsRecyclerView.setFinishLoading(true, "-End-");
                            RequestsFragment.this.requestsRecyclerView.setHasMoreItems(false);

                        }


                    } else {

                        RequestsFragment.this.requestsRecyclerView.setFinishLoading(true, "-End-");
                        RequestsFragment.this.requestsRecyclerView.setHasMoreItems(false);

                    }

                    RequestsFragment.this.requestsRecyclerView.setScreenState(RECYCLER_VIEW);

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    RequestsFragment.this.requestsRecyclerView.setMessage(message, FAILURE_MESSAGE);
                } else {
                    RequestsFragment.this.requestsRecyclerView.setMessage("Getting Requests List Error!", FAILURE_MESSAGE);
                }
            }

            @Override
            protected void postRequest() {

                RequestsFragment.this.requestsRecyclerViewSwipeRefreshLayout.setRefreshing(false);
                RequestsFragment.this.requestsRecyclerViewSwipeRefreshLayout.setEnabled(true);

                RequestsFragment.this.requestsRecyclerView.setLoadMore(false);
                RequestsFragment.this.requestsRecyclerView.setLoadingItems(false);

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void makeGetRequestsRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                RequestsFragment.this.requestsRecyclerView.setLoadingItems(true);
                RequestsFragment.this.requestsRecyclerView.setLoadMore(true, "Loading...");

            }

            @Override
            protected boolean requestCondition() {
                String guestEmail = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestEmail", null);
                String hotelName = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelName", null);
                if ((guestEmail != null) && (hotelName != null)) {
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
            protected void setRequestParams(HashMap<String, String> params) {
                String guestEmail = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestEmail", null);
                String hotelName = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelName", null);
                params.put("guestEmail", guestEmail);
                params.put("hotelName", hotelName);
            }


            @Override
            protected String getRequestUrl() {

                String url = GET_ALL_REQUESTS_URL;

                String hotelName = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelName", null);
                url += "?hotel=" + hotelName;

                String guestEmail = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestEmail", null);
                url += "&guest=" + guestEmail;

                long bookingId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("bookingId", -1L);
                url += "&booking=" + Long.toString(bookingId);

                if (RequestsFragment.this.requestsRecyclerView != null
                        && RequestsFragment.this.requestsRecyclerView.getRecyclerView() != null
                        && RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter() != null
                        && RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter() instanceof RequestsListAdapter
                        && ((RequestsListAdapter)RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter()).getFilter() != null) {

                    RequestsListAdapter adapter = ((RequestsListAdapter)RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter());

                    //index & limit
                    url += "&index=" + adapter.getAllObjects().size();

                    url += "&limit=20";

                    //sorting type
                    String sortingType = adapter.getSortingType();
                    if (sortingType != null && !sortingType.equals("")) {

                        url += "&sort=" + adapter.getSortingType();

                    }

                    //filter
                    GuestRequestsFilter filter = adapter.getFilter();

                    if (filter.isFilterByMinDate()) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
                        url += "&minTs=" + dateFormat.format(filter.getMinDate());

                    }

                    if (filter.isFilterByMaxDate()) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
                        url += "&maxTs=" + dateFormat.format(filter.getMaxDate());

                    }

                    if (filter.isFilterByStatus()) {

                        for (String status : filter.getStatuses()) {

                            url += "&status=" + status;

                        }

                    }

                }


                return url;
//                return BOB_SERVER_WEB_SERVICES_URL + "/wishes/getAllByUser?user=15";
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

                    if (response.getJSONObject("response").has("requests")) {

                        GuestRequest[] guestRequests = new GuestRequest[0];
                        if (response.getJSONObject("response").optJSONArray("requests") != null) {
                            String guestRequestsString = response.getJSONObject("response").getJSONArray("requests").toString();
                            guestRequests = customGson.fromJson(guestRequestsString, GuestRequest[].class);
                        } else if (response.getJSONObject("response").optJSONObject("requests") != null) {
                            String guestRequestsString = response.getJSONObject("response").getJSONObject("requests").toString();
                            guestRequests = new GuestRequest[] {
                                    customGson.fromJson(guestRequestsString, GuestRequest.class)
                            };
                        }

                        //COMMENT THIS!!
                        long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", 1L);
                        for(GuestRequest guestRequest : guestRequests) {
                            if (guestRequest != null) {
                                guestRequest.setGuestId(guestId);
                                if (guestRequest.getRequestItems() != null) {
                                    for (RequestItem requestItem : guestRequest.getRequestItems()) {
                                        if (requestItem != null) {
                                            requestItem.setType("text_view");
                                        }
                                    }
                                }
                            }
                        }

                        if (guestRequests != null && guestRequests.length > 0) {

                            MyDBUtilsManager.get().insertRequestsListToDB(guestRequests);

                            if (RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter() != null
                                    && RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter() instanceof RequestsListAdapter) {

                                ArrayList<GuestRequest> requestsToInsert = new ArrayList<GuestRequest>();
                                ArrayList<GuestRequest> requestsFromServer = new ArrayList<GuestRequest>(Arrays.asList(guestRequests));
                                List<GuestRequest> allRequests = ((RequestsListAdapter)RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter()).getAllObjects();

                                ArrayList<Long> allRequestsIds = new ArrayList<Long>();
                                for (GuestRequest request : allRequests) {
                                    if (request != null) {
                                        allRequestsIds.add(request.getId());
                                    }
                                }

                                for (GuestRequest requestFromServer : requestsFromServer) {
                                    if (requestFromServer != null && !allRequestsIds.contains(requestFromServer.getId())) {
                                        requestsToInsert.add(requestFromServer);
                                    }
                                }

                                ((RequestsListAdapter)RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter()).addObjects(requestsToInsert);
                                RequestsFragment.this.requestsRecyclerView.getRecyclerView().getAdapter().notifyDataSetChanged();

                            }

                            RequestsFragment.this.requestsRecyclerView.setHasMoreItems(true);

                        } else {

                            RequestsFragment.this.requestsRecyclerView.setFinishLoading(true, "-End-");
                            RequestsFragment.this.requestsRecyclerView.setHasMoreItems(false);

                        }


                    } else {

                        RequestsFragment.this.requestsRecyclerView.setFinishLoading(true, "-End-");
                        RequestsFragment.this.requestsRecyclerView.setHasMoreItems(false);

                    }

                    RequestsFragment.this.requestsRecyclerView.setScreenState(RECYCLER_VIEW);

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    RequestsFragment.this.requestsRecyclerView.setMessage(message, FAILURE_MESSAGE);
                } else {
                    RequestsFragment.this.requestsRecyclerView.setMessage("Getting Requests List Error!", FAILURE_MESSAGE);
                }
            }

            @Override
            protected void postRequest() {

                RequestsFragment.this.requestsRecyclerView.setLoadMore(false);
                RequestsFragment.this.requestsRecyclerView.setLoadingItems(false);

            }

        };

        jsonServerRequest.makeRequest();

    }

    public void refreshRequests() {

        this.makeRefreshRequestsRequest();

    }

    //requests filter view
    private View initFilterDialogView() {

        RequestsFilterDialogView requestsFilterDialogView = new RequestsFilterDialogView(this.getContext());

        //dialog
        requestsFilterDialogView.setOnRequestFilterButtonClickListener(new OnRequestFilterButtonClickListener() {
            @Override
            public void onRequestFilterButtonClick(GuestRequestsFilter guestRequestsFilter) {

                RequestsFragment.this.requestsListAdapter.setFilter(guestRequestsFilter);

                if (RequestsFragment.this.requestsFilterMaterialDialog != null) {

                    RequestsFragment.this.requestsFilterMaterialDialog.dismiss();

                }
            }
        });

        //guest requests filter
        requestsFilterDialogView.setGuestRequestsFilter(this.requestsListAdapter.getFilter());

        return requestsFilterDialogView;
    }

    //back pressed handler
    public void onBackPressed() {

    }

    //TEMP FUNCTION
//    private void testingRequests() {
//        //CLEAR DB
//        MyRealmController.with(this.getActivity()).deleteAllGuestRequests();
//        MyRealmController.with(this.getActivity()).deleteAllGuestRequestItems();
//
//        //PAGE
//        long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1L);
//        int requestId = 0;
//        this.createRequest(guestId, requestId, "done", "first_request_title", "first_request_content", "http://icons.iconarchive.com/icons/graphicloads/colorful-long-shadow/256/Man-icon.png", "1/1/2010");
//
//    }
//    void createRequest(long guestId, int id, String status, String title, String content, String iconUrl, String timeStamp) {
//        GuestRequest guestRequest = new GuestRequest();
//        guestRequest.setId(id);
//        guestRequest.setGuestId(guestId);
//        guestRequest.setStatus(status);
//        guestRequest.setIconUrl(iconUrl);
//        guestRequest.setTimeStamp(timeStamp);
//        guestRequest.setTitle(title);
//        MyRealmController.with(this.getActivity()).insertGuestRequest(guestRequest);
//    }
//    void createRequestItem(int id, int requestId, String type, String key, String value) {
//        RequestItem guestRequestItem = new RequestItem();
//        guestRequestItem.setId(id);
//        guestRequestItem.setType(type);
//        guestRequestItem.setKey(key);
//        guestRequestItem.setValue(value);
//        MyRealmController.with(this.getActivity()).insertGuestRequestItem(guestRequestItem);
//    }

}
