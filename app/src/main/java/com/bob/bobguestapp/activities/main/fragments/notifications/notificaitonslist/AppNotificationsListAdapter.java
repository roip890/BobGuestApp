package com.bob.bobguestapp.activities.main.fragments.notifications.notificaitonslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.notifications.notificaitonslist.comperators.AppNotificationTimeComparator;
import com.bob.bobguestapp.activities.main.fragments.notifications.notificaitonslist.comperators.AppNotificationTitleComparator;
import com.bob.bobguestapp.activities.main.fragments.notifications.notificaitonslist.filter.AppNotificationsFilter;
import com.bob.bobguestapp.tools.database.MyRealmController;
import com.bob.bobguestapp.tools.database.objects.GuestAppNotification;
import com.bob.bobguestapp.tools.recyclerview.SortFilterAdapter;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class AppNotificationsListAdapter extends SortFilterAdapter<GuestAppNotification, AppNotificationsListViewHolder, AppNotificationsFilter> {

    //app theme
    private int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    private int screenSkin = MyAppThemeUtilsManager.LIGHT_COLOR_SKIN;

    private static final int NOTIFICATION_VIEW_TYPE = 0;
    private Context context;

    public AppNotificationsListAdapter(Context context, List<GuestAppNotification> items) {
        this.context = context;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.NOTIFICATIONS_FRAGMENT_SKIN);

        if (items != null) {
            this.allObjects = items;
        } else {
            this.allObjects = new ArrayList<GuestAppNotification>();
        }
        this.objectsToShow = new ArrayList<GuestAppNotification>();
        this.objectsToShow.addAll(this.allObjects);

        this.comparators = new HashMap<String, Comparator<GuestAppNotification>>();
        this.comparators.put("date", new AppNotificationTimeComparator());
        this.comparators.put("title", new AppNotificationTitleComparator());

        this.filter = new AppNotificationsFilter();
        this.sortingType = "date";
        this.ascending = true;

	}

    public AppNotificationsListAdapter(Context context) {

        this(context, new ArrayList<GuestAppNotification>());

    }

    public void setScreenSkin(int screenSkin) {
        this.screenSkin = screenSkin;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return NOTIFICATION_VIEW_TYPE;
    }

    @Override
    public AppNotificationsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        AppNotificationsListViewHolder appNotificationsListViewHolder = new AppNotificationsListViewHolder(context, inflater.inflate(R.layout.list_item_notification, viewGroup, false));
        appNotificationsListViewHolder.setScreenSkin(this.screenSkin);
        return appNotificationsListViewHolder;

    }

    @Override
    public void onBindViewHolder(AppNotificationsListViewHolder viewHolder, int position) {

        GuestAppNotification appNotification = this.objectsToShow.get(position);

        viewHolder.setAppNotification(appNotification);

    }
    
    @Override
    public int getItemCount() {
        return this.objectsToShow.size();
	}

    public void setNotificationsListFromDB() {

        long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1L);

        this.setObjects(MyRealmController.get().with(BOBGuestApplication.get()).getAppNotificationsOfGuest(guestId));

        this.notifyDataSetChanged();

    }


}
