package com.bob.bobguestapp.activities.main.fragments.requests.requestslist;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestchangestatus.RequestChangeStatusDialogListener;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestchangestatus.RequestsChangeStatusDialogView;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestchangestatus.statuseslist.GuestRequestStatus;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestdetails.RequestDetailsDialogView;
import com.bob.bobguestapp.tools.database.MyRealmController;
import com.bob.bobguestapp.tools.myFinals;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.database.objects.GuestRequest;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.finals;
import com.bob.uimodule.views.circleprogressbar.CircleProgressViewTimer;
import com.bumptech.glide.Glide;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

/**
 * Created by User on 07/09/2018.
 */

public class RequestsListViewHolder extends RecyclerView.ViewHolder{


    private static final int REQUESTS_ITEM_DROPDOWN_MENU_SHOW_ID = 0;
    private static final int REQUESTS_ITEM_DROPDOWN_MENU_CHANGE_STATUS_ID = 1;

    //app theme
    private int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    private int screenSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;
    private int requestMenuSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;
    private int showRequestDialogSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;
    private int changeRequestStatusDialogSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;

    //views
    private ImageView requestIconView, requestMoreDetailsIconView,
            requestFavoriteIconView;
    private View requestStatusView;
    private TextView requestTitleView, requestTimestampView;
    private GuestRequest guestRequest;
    protected Context context;
    protected View view;

    //request menu item
    PowerMenuItem showRequestMenuItem;
    PowerMenuItem changeRequestStatusMenuItem;

    //request details dialog
    private MaterialDialog requestDetailsMaterialDialog;

    //request change status pop up
    private MaterialDialog requestChangeStatusMaterialDialog;

    //timer
    private CircleProgressView requestTimerView;
    private CircleProgressViewTimer requestTimer;
    private SimpleDateFormat dateFormat;
    private long finishTime;
    private long jobTime;

    public RequestsListViewHolder(Context context, View view) {
        super(view);

        this.finishTime = 0;
        this.context = context;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_SKIN);
        this.requestMenuSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUEST_LIST_ITEM_MENU_ITEM_SKIN);
        this.showRequestDialogSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUEST_LIST_ITEM_REQUEST_DETAILS_DIALOG_SKIN);
        this.changeRequestStatusDialogSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUEST_LIST_ITEM_CHANGE_STATUS_DIALOG_SKIN);

        this.initView(view);
    }

    public void onResume() {

        if (this.requestTimer != null) {

            this.requestTimer.startTimer(100);

        }


    }

    public void onPause() {

        if (this.requestTimer != null) {

            this.requestTimer.stopTimer();

        }

    }

    //guest request
    public void setGuestRequest(GuestRequest guestRequest) {

        this.guestRequest = guestRequest;

        this.updateRequest();

    }

    //general
    protected void initView(View view) {

        this.view = view;

        //request icon
        this.initRequestIcon();

        //title
        this.initRequestTitle();

        //timestamp
        this.initRequestTimestamp();

        //timer view
        this.initRequestTimerView();
        this.requestTimerView.setVisibility(View.GONE);

        //status icon
        this.initRequestStatusIcon();

        //details icon
        this.initRequestDetailsIcon();

        //update request
        this.updateRequest();

    }

    private void initRequestIcon() {

        this.requestIconView = (ImageView) view.findViewById(R.id.request_icon);

    }

    private void initRequestTitle() {

        this.requestTitleView = (TextView) view.findViewById(R.id.request_title);

    }

    private void initRequestTimestamp() {

        this.requestTimestampView = (TextView) view.findViewById(R.id.request_timestamp);

    }

    private void initRequestStatusIcon() {

        this.requestStatusView = view.findViewById(R.id.request_status_icon);

    }

    private void initRequestDetailsIcon() {

        this.requestMoreDetailsIconView = (ImageView) view.findViewById(R.id.more_details_icon);

        //show request menu item
        this.initShowRequestMenuItem();

        //change status menu item
        this.initChangeRequestStatusMenuItem();

        this.requestMoreDetailsIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestsListViewHolder.this.onRequestDetailsIconClick();

            }
        });


    }

    private void initShowRequestMenuItem() {

        this.showRequestMenuItem = new PowerMenuItem("Show", false);
        this.showRequestMenuItem.setTag(REQUESTS_ITEM_DROPDOWN_MENU_SHOW_ID);


    }

    private void initChangeRequestStatusMenuItem() {

        this.changeRequestStatusMenuItem = new PowerMenuItem("Change Status", false);
        this.changeRequestStatusMenuItem.setTag(REQUESTS_ITEM_DROPDOWN_MENU_CHANGE_STATUS_ID);


    }

    private void onRequestDetailsIconClick() {


        PowerMenu powerMenu = new PowerMenu.Builder(context)
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
                .addItem(this.showRequestMenuItem)
                .addItem(this.changeRequestStatusMenuItem)
                .setMenuRadius(UIUtilsManager.get().convertDpToPixels(this.context, 10))
                .setMenuShadow(UIUtilsManager.get().convertDpToPixels(this.context, 10))
                .setTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_MENU_ITEM_TEXT_COLOR, RequestsListViewHolder.this.requestMenuSkin))
                .setSelectedTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_MENU_ITEM_PRESSED_TEXT_COLOR, RequestsListViewHolder.this.requestMenuSkin))
                .setMenuColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_MENU_ITEM_BACKGROUND_COLOR_PRIMARY, RequestsListViewHolder.this.requestMenuSkin))
                .setSelectedMenuColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_MENU_ITEM_BACKGROUND_PRESSED_COLOR_PRIMARY, RequestsListViewHolder.this.requestMenuSkin))
                .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {

                        RequestsListViewHolder.this.onRequestDetailsIconMenuItemClick(position, item);

                    }
                })
                .build();


        powerMenu.showAsDropDown(this.requestMoreDetailsIconView);

    }

    private void onRequestDetailsIconMenuItemClick(int position, PowerMenuItem item) {

        if (item.getTag().equals(REQUESTS_ITEM_DROPDOWN_MENU_SHOW_ID)) {

            RequestsListViewHolder.this.onRequestItemShowClick();

        } else if (item.getTag().equals(REQUESTS_ITEM_DROPDOWN_MENU_CHANGE_STATUS_ID)) {

            RequestsListViewHolder.this.onRequestChangeStatusClick();

        }

    }

    public void updateRequest() {

        if (this.guestRequest != null) {

            //request icon
            this.updateRequestIcon();

            //title
            this.updateRequestTitle();

            //timestamp
            this.updateRequestTimestamp();

            //timer view
            this.initRequestTimerView();

            //status icon
            this.updateRequestStatusIcon();

            //more details icon
            this.updateRequestMoreDetailsIcon();

        }

    }

    private void updateRequestIcon() {

        Glide.with(this.context)
                .asBitmap()
                .load(this.guestRequest.getIconUrl())
                .into(this.requestIconView);

    }

    private void updateRequestStatusIcon() {

        int statusColor = R.color.waiting_status_color;
        if (myFinals.requestStatusColors.get(this.guestRequest.getStatus()) != null) {
            statusColor = myFinals.requestStatusColors.get(this.guestRequest.getStatus().toLowerCase());
        }

        GradientDrawable greenGradientDrawable = new GradientDrawable();
        greenGradientDrawable.setShape(GradientDrawable.OVAL);
        greenGradientDrawable.setColor(ContextCompat.getColor(this.context, statusColor));

        this.requestStatusView.setBackground(greenGradientDrawable);

    }

    private void updateRequestMoreDetailsIcon() {

        Glide.with(this.context)
                .asDrawable()
                .load(ContextCompat.getDrawable(this.context, R.drawable.ic_more_horiz_black_24dp))
                .into(this.requestMoreDetailsIconView);

    }

    private void updateRequestTitle() {

        this.requestTitleView.setText(this.guestRequest.getTitle());

        this.requestTitleView.setTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_TEXT_COLOR, this.screenSkin));

    }

    private void updateRequestTimestamp() {

        this.requestTimestampView.setText(this.guestRequest.getTimeStamp());

        this.requestTimestampView.setTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_TEXT_COLOR, this.screenSkin));

    }

    public void setScreenSkin(int screenSkin) {
        this.screenSkin = screenSkin;

        this.updateRequest();

    }

    //request details
    protected void onRequestItemShowClick() {

        this.requestDetailsMaterialDialog = new MaterialDialog.Builder(this.context)
//                .title(this.requestTitleView.getText())
                .titleGravity(finals.dialogGravity.get("center"))
                .customView(this.initRequestDetailsDialogView(), true)
                .backgroundColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_DIALOG_BACKGROUND_COLOR_PRIMARY, this.showRequestDialogSkin))
                .show();

        //round corners
        View dialogView = this.requestDetailsMaterialDialog.getWindow().getDecorView();
        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_DIALOG_BACKGROUND_COLOR_PRIMARY, this.showRequestDialogSkin));
        shapeDrawable.setCornerRadius(UIUtilsManager.get().convertDpToPixels(this.context, 10));
        dialogView.setBackground(shapeDrawable);

        //set width
        ViewGroup.LayoutParams params = this.requestDetailsMaterialDialog.getWindow().getAttributes();
        params.width =  (int) this.context.getResources().getDimension(R.dimen.guest_code_list_item_show_guest_code_dialog_width);
        this.requestDetailsMaterialDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    }

    protected View initRequestDetailsDialogView() {

        RequestDetailsDialogView requestDetailsDialogView = new RequestDetailsDialogView(this.context);

        requestDetailsDialogView.setGuestRequest(this.guestRequest);

        return requestDetailsDialogView;

    }

    //change status
    protected void onRequestChangeStatusClick() {
        this.requestChangeStatusMaterialDialog = new MaterialDialog.Builder(this.context)
                .title("Change Status:")
                .titleGravity(finals.dialogGravity.get("center"))
                .customView(this.initChangeStatusDialogView(), true)
                .backgroundColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_DIALOG_BACKGROUND_COLOR_PRIMARY, this.changeRequestStatusDialogSkin))
                .show();

        //round corners
        View dialogView = this.requestChangeStatusMaterialDialog.getWindow().getDecorView();
        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_DIALOG_BACKGROUND_COLOR_PRIMARY, this.changeRequestStatusDialogSkin));
        shapeDrawable.setCornerRadius(UIUtilsManager.get().convertDpToPixels(this.context, 10));
        dialogView.setBackground(shapeDrawable);

        //set width
        ViewGroup.LayoutParams params = this.requestChangeStatusMaterialDialog.getWindow().getAttributes();
        params.width =  (int) this.context.getResources().getDimension(R.dimen.guest_code_list_item_show_guest_code_dialog_width);
        this.requestChangeStatusMaterialDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    private View initChangeStatusDialogView() {

        RequestsChangeStatusDialogView requestsChangeStatusDialogView = new RequestsChangeStatusDialogView(this.context);

        //guest request
        requestsChangeStatusDialogView.setGuestRequest(this.guestRequest);

        //listener
        requestsChangeStatusDialogView.setRequestChangeStatusDialogListener(new RequestChangeStatusDialogListener() {
            @Override
            public void onRequestStatusChange(GuestRequestStatus guestRequestStatus) {

                MyRealmController.get().getRealm().beginTransaction();
                RequestsListViewHolder.this.guestRequest.setStatus(guestRequestStatus.getStatus());
                MyRealmController.get().getRealm().commitTransaction();

                RequestsListViewHolder.this.updateRequestStatusIcon();

            }
        });

        return requestsChangeStatusDialogView;


    }


    //timer
    private void initRequestTimerView() {

        this.requestTimerView = (CircleProgressView) view.findViewById(R.id.request_timer);

        //date format
        this.initSimpleDateFormat();

        //times
        this.initRequestTimerTimes();

        //timer
        this.initRequestTimer();

        //colors
        this.updateRequestTimerViewColors();

    }

    private void initSimpleDateFormat() {

        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    }

    private void initRequestTimerTimes() {

        this.initRequestTimerJobTime();

        this.initRequestTimerFinishTime();

    }

    private void initRequestTimerJobTime() {

        this.jobTime = TimeUnit.SECONDS.toMillis(70);

    }

    private void initRequestTimerFinishTime() {

        //COMMENT THIS
        Date currentDate = Calendar.getInstance().getTime();
        this.finishTime = currentDate.getTime() + this.jobTime;

        //UNCOMMENT THIS!!
//        try {
//            this.finishTime = this.dateFormat.parse(this.guestRequest.getTimeStamp()).getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

    }

    private void initRequestTimer() {

        this.requestTimerView.setDelayMillis(100);
        this.requestTimerView.setTextMode(TextMode.TEXT);
        this.requestTimerView.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float value) {

                Date currentDate = Calendar.getInstance().getTime();
                RequestsListViewHolder.this.requestTimerView.setTextMode(TextMode.TEXT);
                RequestsListViewHolder.this.requestTimerView.setAutoTextSize(true);

                if (currentDate.getTime() < RequestsListViewHolder.this.finishTime) {
                    RequestsListViewHolder.this.requestTimerView.setText(RequestsListViewHolder.this.timerFormat(RequestsListViewHolder.this.finishTime - currentDate.getTime()));
                } else {
                    RequestsListViewHolder.this.requestTimerView.setText("Time Off!");
                }

            }
        });


//        if (this.context instanceof Activity) {
//            this.requestTimer = new CircleProgressViewTimer((Activity) this.context, this.requestTimerView, new GetProgressValueListener() {
//
//                @Override
//                public int getProgressValue() {
//                    Date currentDate = Calendar.getInstance().getTime();
//                    RequestsListViewHolder.this.requestTimerView.setTextMode(TextMode.TEXT);
//                    RequestsListViewHolder.this.requestTimerView.setAutoTextSize(true);
//                    if (currentDate.getTime() < RequestsListViewHolder.this.finishTime) {
//                        return 100 - (int) (((float)(RequestsListViewHolder.this.finishTime - currentDate.getTime()) / (float)RequestsListViewHolder.this.jobTime) * 100.0);
//                    } else {
//                        return 0;
//                    }
//                }
//            });
//            requestTimer.startTimer(100);
//        }


    }

    private void updateRequestTimerViewColors() {

        this.requestTimerView.setFillCircleColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_CIRCLE_PROGRESS_TIMER_FILL_CIRCLE_COLOR, this.screenSkin));
        this.requestTimerView.setRimColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_CIRCLE_PROGRESS_TIMER_RIM_COLOR, this.screenSkin));
        this.requestTimerView.setTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_CIRCLE_PROGRESS_TIMER_TEXT_COLOR, this.screenSkin));
        this.requestTimerView.setUnitColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_CIRCLE_PROGRESS_TIMER_UNIT_COLOR, this.screenSkin));
        this.requestTimerView.setInnerContourColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_CIRCLE_PROGRESS_TIMER_INNER_COLOR, this.screenSkin));
        this.requestTimerView.setOuterContourColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_CIRCLE_PROGRESS_TIMER_OUTER_COLOR, this.screenSkin));
        this.requestTimerView.setSpinBarColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_CIRCLE_PROGRESS_TIMER_SPIN_BAR_COLOR, this.screenSkin));
        this.requestTimerView.setBarColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_CIRCLE_PROGRESS_TIMER_BAR_COLOR, this.screenSkin));
        this.requestTimerView.setBarWidth((int) this.context.getResources().getDimension(R.dimen.request_view_holder_circle_progress_timer_bar_width));
        this.requestTimerView.setRimWidth((int) this.context.getResources().getDimension(R.dimen.request_view_holder_circle_progress_timer_rim_width));

    }

    private String timerFormat(long milliseconds) {
        if (TimeUnit.MILLISECONDS.toDays(milliseconds) >= 1) {
            return String.format(Locale.getDefault(), "%02dd %02dh", TimeUnit.MILLISECONDS.toDays(milliseconds), TimeUnit.MILLISECONDS.toHours(milliseconds) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(milliseconds)));
        } else if (TimeUnit.MILLISECONDS.toHours(milliseconds) >= 1) {
            return String.format(Locale.getDefault(), "%02d:%02d", TimeUnit.MILLISECONDS.toHours(milliseconds), TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)));
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(milliseconds), TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
        }
    }


}