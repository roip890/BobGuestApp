package com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestchangestatus.statuseslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuestRequestStatusesListAdapter extends RecyclerView.Adapter<GuestRequestStatusesListViewHolder> {

    //app theme
    protected int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    protected int screenSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;


    private final int STATUS_VIEW = 0;


    // The items to display in your RecyclerView
    private ArrayList<GuestRequestStatus> guestRequestStatuses;
    private GuestRequestStatusTitleComparator guestRequestStatusTitleComparator;
    private OnChangeRequestStatusListener onChangeRequestStatusListener;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public GuestRequestStatusesListAdapter(Context context) {
        this.context = context;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_SKIN);

        this.guestRequestStatusTitleComparator = new GuestRequestStatusTitleComparator();
        this.guestRequestStatuses = new ArrayList<GuestRequestStatus>();
        this.onChangeRequestStatusListener = null;
        this.setGuestRequestStatuses(this.guestRequestStatuses);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.guestRequestStatuses.size();
    }

    @Override
    public int getItemViewType(int position) {
        return STATUS_VIEW;
    }

    @Override
    public GuestRequestStatusesListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        GuestRequestStatusesListViewHolder guestRequestStatusesListViewHolder;

        switch (viewType) {
            case STATUS_VIEW:
                guestRequestStatusesListViewHolder = new GuestRequestStatusesListViewHolder(context, inflater.inflate(R.layout.list_item_change_status, viewGroup, false));
            default:
//                return null;
                guestRequestStatusesListViewHolder = new GuestRequestStatusesListViewHolder(context, inflater.inflate(R.layout.list_item_change_status, viewGroup, false));
        }

        guestRequestStatusesListViewHolder.setOnChangeRequestStatusListener(this.onChangeRequestStatusListener);

        return guestRequestStatusesListViewHolder;
    }

    @Override
    public void onBindViewHolder(GuestRequestStatusesListViewHolder viewHolder, int position) {
        GuestRequestStatus guestRequestStatus = this.guestRequestStatuses.get(position);
        viewHolder.configureRequestStatus(guestRequestStatus);
    }

    public void setGuestRequestStatuses(List<GuestRequestStatus> guestRequestStatuses) {
        if (guestRequestStatuses != null) {
            this.guestRequestStatuses.clear();
            this.guestRequestStatuses.addAll(guestRequestStatuses);
            Collections.sort(this.guestRequestStatuses, this.guestRequestStatusTitleComparator);
            this.notifyDataSetChanged();
        }
    }

    public void setScreenSkin(int screenSkin) {
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, screenSkin);
        this.notifyDataSetChanged();
    }

    public void setOnChangeRequestStatusListener(OnChangeRequestStatusListener onChangeRequestStatusListener) {
        this.onChangeRequestStatusListener = onChangeRequestStatusListener;
        this.notifyDataSetChanged();
    }

}
