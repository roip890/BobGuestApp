package com.bob.bobguestapp.activities.main.fragments.requests.requestslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.tools.database.MyRealmController;
import com.bob.bobguestapp.tools.recyclerview.SortFilterAdapter;
import com.bob.toolsmodule.database.objects.GuestRequest;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.comperators.GuestRequestStatusComparator;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.comperators.GuestRequestTimeComparator;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.comperators.GuestRequestTitleComparator;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.filter.GuestRequestsFilter;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class RequestsListAdapter extends SortFilterAdapter<GuestRequest, RequestsListViewHolder, GuestRequestsFilter> {

    //app theme
    private int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    private int screenSkin = MyAppThemeUtilsManager.LIGHT_COLOR_SKIN;

    private static final int REQUEST_VIEW_TYPE = 0;
    private Context context;

    public RequestsListAdapter(Context context, List<GuestRequest> items) {
		this.context = context;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_SKIN);

        if (items != null) {
            this.allObjects = items;
        } else {
		    this.allObjects = new ArrayList<GuestRequest>();
        }
        this.objectsToShow = new ArrayList<GuestRequest>();
		this.objectsToShow.addAll(this.allObjects);

		this.comparators = new HashMap<String, Comparator<GuestRequest>>();
        this.comparators.put("status", new GuestRequestStatusComparator());
        this.comparators.put("date", new GuestRequestTimeComparator());
        this.comparators.put("title", new GuestRequestTitleComparator());

        this.filter = new GuestRequestsFilter();
		this.sortingType = "date";
		this.ascending = true;

	}

    public RequestsListAdapter(Context context) {

        this(context, new ArrayList<GuestRequest>());

    }

    public void setScreenSkin(int screenSkin) {
        this.screenSkin = screenSkin;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return REQUEST_VIEW_TYPE;
    }

    @Override
    public RequestsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        RequestsListViewHolder requestsListViewHolder = new RequestsListViewHolder(context, inflater.inflate(R.layout.list_item_request, viewGroup, false));
        requestsListViewHolder.setScreenSkin(this.screenSkin);
        return requestsListViewHolder;
    }

    @Override
    public void onBindViewHolder(RequestsListViewHolder viewHolder, int position) {

        GuestRequest request = this.objectsToShow.get(position);

        viewHolder.setGuestRequest(request);

    }
    
    @Override
    public int getItemCount() {
        return objectsToShow.size();
	}

    public void setRequestsListFromDB() {

        long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1L);

        this.setObjects(MyRealmController.get().with(BOBGuestApplication.get()).getGuestRequestsOfGuest(guestId));

        this.notifyDataSetChanged();

    }

}
