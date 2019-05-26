package com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestchangestatus.statuseslist;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.tools.myFinals;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;


public class GuestRequestStatusesListViewHolder extends RecyclerView.ViewHolder {


    //app theme
    protected int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    protected int screenSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;

    protected Context context;

    //views
    protected TextView statusTitle;
    protected View statusIcon;
    protected RelativeLayout statusLayout;

    //
    protected OnChangeRequestStatusListener onChangeRequestStatusListener;
    protected GuestRequestStatus guestRequestStatus;

    public GuestRequestStatusesListViewHolder(Context context, @NonNull View itemView) {
        super(itemView);

        this.context = context;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUEST_LIST_ITEM_CHANGE_STATUS_DIALOG_SKIN);

        //init views
        initView(itemView);

        //init listener
        this.initOnChangeStatusListener();

        //init status
        this.initStatus();

        //update views
        updateStatus();

    }

    //screen state
    public void setScreenSkin(int screenSkin) {

        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, screenSkin);

        this.updateStatus();

    }

    //views
    private void initView(View view) {

        //status layout
        this.initStatusLayout(view);

        //status title
        this.initStatusTitle(view);

        //status icon
        this.initStatusIcon(view);


    }

    private void initStatusLayout(View view) {

        this.statusLayout = (RelativeLayout) itemView.findViewById(R.id.list_item_change_status_background);

        //listener
        this.initStatusLayoutOnClickListener();

    }

    private void initStatusLayoutOnClickListener() {

        this.statusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChangeRequestStatusListener != null && guestRequestStatus != null) {
                    onChangeRequestStatusListener.onRequestStatusChange(guestRequestStatus);
                }
            }
        });

    }

    private void initStatusTitle(View view) {

        this.statusTitle = (TextView) itemView.findViewById(R.id.list_item_change_status_status_title);

    }

    private void initStatusIcon(View view) {

        this.statusIcon = (View) itemView.findViewById(R.id.list_item_change_status_status_icon);

    }

    private void initOnChangeStatusListener() {

        this.onChangeRequestStatusListener = null;

    }

    private void initStatus() {

        this.guestRequestStatus = null;

    }

    private void updateStatus() {

        //background
        this.updateStatusBackground();

        //title
        this.updateStatusTitle();

        //icon
        this.updateStatusIcon();


    }

    private void updateStatusBackground() {

        this.statusLayout.setBackgroundColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_VIEW_BACKGROUND_COLOR_PRIMARY, this.screenSkin));

    }

    private void updateStatusTitle() {

        this.statusTitle.setTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_BASE_TEXT_COLOR, this.screenSkin));

        if (guestRequestStatus != null && myFinals.requestStatusStrings.keySet().contains(guestRequestStatus.getStatus())) {
            this.statusTitle.setText(myFinals.requestStatusStrings.get(guestRequestStatus.getStatus()));

        }

    }

    private void updateStatusIcon(){

        if (this.guestRequestStatus != null
                && myFinals.requestStatusColors.values().contains(this.guestRequestStatus.getColor())) {

            int statusColor = this.guestRequestStatus.getColor();

            GradientDrawable greenGradientDrawable = new GradientDrawable();
            greenGradientDrawable.setShape(GradientDrawable.OVAL);
            greenGradientDrawable.setColor(ContextCompat.getColor(this.context, statusColor));

            this.statusIcon.setBackground(greenGradientDrawable);

        }

    }

    public void configureRequestStatus(GuestRequestStatus guestRequestStatus) {

        this.guestRequestStatus = guestRequestStatus;

        this.updateStatus();

    }

    public void setOnChangeRequestStatusListener(OnChangeRequestStatusListener onChangeRequestStatusListener) {

        this.onChangeRequestStatusListener = onChangeRequestStatusListener;

    }

}
