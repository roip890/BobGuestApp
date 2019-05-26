package com.bob.bobguestapp.activities.main.fragments.notifications.notificaitonslist;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.PictureDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.tools.database.MyRealmController;
import com.bob.bobguestapp.tools.database.objects.GuestAppNotification;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.image.svg.SvgSoftwareLayerSetter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by User on 07/09/2018.
 */

public class AppNotificationsListViewHolder extends RecyclerView.ViewHolder{

    private static final int NOTIFICATIONS_ITEM_DROPDOWN_MENU_HIDE_ID = 0;
    private static final int NOTIFICATIONS_ITEM_DROPDOWN_MENU_READ_MARKED_ID = 1;

    //app theme
    private int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    private int screenSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;
    private int notificationMenuSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;

    //views
    private RelativeLayout notificationBackground;
    private ImageView notificationIconView;
    private ImageView notificationMenuIconView;
    private TextView notificationContentView;
    private TextView notificationTimestampView;
    private GuestAppNotification appNotification;
    private PowerMenu notificationPopUpMenu;
    protected Context context;
    protected View view;

    //notification menu items
    protected PowerMenuItem hideNotificationMenuItem;
    protected PowerMenuItem readMarkedNotificationMenuItem;

    public AppNotificationsListViewHolder(Context context, View view) {
        super(view);

        this.context = context;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.NOTIFICATIONS_FRAGMENT_SKIN);
        this.notificationMenuSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.NOTIFICATION_LIST_ITEM_MENU_ITEM_SKIN);

        this.initView(view);
    }

    //app notification
    public void setAppNotification(GuestAppNotification appNotification) {

        this.appNotification = appNotification;

        this.updateNotification();

    }

    //general
    protected void initView(View view) {

        this.view = view;

        //notification background
        this.initNotificationBackground(view);

        //notification icon
        this.initNotificationIcon(view);

        //content
        this.initNotificationContent(view);

        //timestamp
        this.initNotificationTimestamp(view);

        //menu icon
        this.initNotificationMenuIcon(view);

        //update notification
        this.updateNotification();

    }

    private void initNotificationBackground(View view) {

        this.notificationBackground = (RelativeLayout) view.findViewById(R.id.notification_background);

    }

    private void initNotificationIcon(View view) {

        this.notificationIconView = (ImageView) view.findViewById(R.id.notification_icon);

    }

    private void initNotificationContent(View view) {

        this.notificationContentView = (TextView) view.findViewById(R.id.notification_content);

    }

    private void initNotificationTimestamp(View view) {

        this.notificationTimestampView = (TextView) view.findViewById(R.id.notification_timestamp);

    }

    private void initNotificationMenuIcon(View view) {

        this.notificationMenuIconView = (ImageView) view.findViewById(R.id.more_details_icon);

        //hide notification menu item
        this.initHideNotificationMenuItem();

        //read marked notification menu item
        this.initReadMarkedNotificationMenuItem();

        this.notificationMenuIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppNotificationsListViewHolder.this.onNotificationMenuIconClick();

            }
        });


    }

    private void initHideNotificationMenuItem() {

        this.hideNotificationMenuItem = new PowerMenuItem("Hide", false);
        this.hideNotificationMenuItem.setTag(NOTIFICATIONS_ITEM_DROPDOWN_MENU_HIDE_ID);


    }

    private void initReadMarkedNotificationMenuItem() {

        this.readMarkedNotificationMenuItem = new PowerMenuItem("Mark As Read", false);
        this.readMarkedNotificationMenuItem.setTag(NOTIFICATIONS_ITEM_DROPDOWN_MENU_READ_MARKED_ID);

    }

    private void onNotificationMenuIconClick() {


        this.notificationPopUpMenu = new PowerMenu.Builder(context)
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT)
                .addItem(this.hideNotificationMenuItem)
                .addItem(this.readMarkedNotificationMenuItem)
                .setMenuRadius(UIUtilsManager.get().convertDpToPixels(this.context, 10))
                .setMenuShadow(UIUtilsManager.get().convertDpToPixels(this.context, 10))
                .setTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_MENU_ITEM_TEXT_COLOR, AppNotificationsListViewHolder.this.notificationMenuSkin))
                .setSelectedTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_MENU_ITEM_PRESSED_TEXT_COLOR, AppNotificationsListViewHolder.this.notificationMenuSkin))
                .setMenuColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_MENU_ITEM_BACKGROUND_COLOR_PRIMARY, AppNotificationsListViewHolder.this.notificationMenuSkin))
                .setSelectedMenuColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_MENU_ITEM_BACKGROUND_PRESSED_COLOR_PRIMARY, AppNotificationsListViewHolder.this.notificationMenuSkin))
                .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {

                        AppNotificationsListViewHolder.this.onNotificationMenuIconMenuItemClick(item);

                    }
                })
                .build();


        this.notificationPopUpMenu.showAsDropDown(this.notificationMenuIconView);

    }

    private void onNotificationMenuIconMenuItemClick(PowerMenuItem item) {

        if (item.getTag().equals(NOTIFICATIONS_ITEM_DROPDOWN_MENU_HIDE_ID)) {

            AppNotificationsListViewHolder.this.onNotificationHideClick();

        } else if (item.getTag().equals(NOTIFICATIONS_ITEM_DROPDOWN_MENU_READ_MARKED_ID)) {

            AppNotificationsListViewHolder.this.onNotificationReadMarkedClick();

        }

    }

    private void onNotificationHideClick() {

        if (this.notificationPopUpMenu != null
                && this.notificationPopUpMenu.isShowing()) {

            this.notificationPopUpMenu.dismiss();

        }

    }

    private void onNotificationReadMarkedClick() {

        MyRealmController.get().getRealm().beginTransaction();
        this.appNotification.setRead(!this.appNotification.isRead());
        MyRealmController.get().getRealm().commitTransaction();

        this.updateNotification();

        if (this.notificationPopUpMenu != null
                && this.notificationPopUpMenu.isShowing()) {

            this.notificationPopUpMenu.dismiss();

        }

    }

    public void updateNotification() {

        if (this.appNotification != null) {

            //notification background
            this.updateNotificationBackground();

            //notification icon
            this.updateNotificationIcon();

            //content
            this.updateNotificationContent();

            //timestamp
            this.updateNotificationTimestamp();

            //menu icon
            this.updateNotificationMenuIcon();

            //marked read menu item
            this.updateReadMarkedNotificationMenuItem();

        }

    }

    private void updateNotificationBackground() {

        if (this.appNotification.isRead()) {

            this.notificationBackground.setBackgroundColor(ContextCompat.getColor(this.context, R.color.light_primary_color));

        } else {

            this.notificationBackground.setBackgroundColor(ContextCompat.getColor(this.context, R.color.dark_primary_color_extra_opacity));

        }

    }

    private void updateNotificationIcon() {

        if (this.appNotification.getIconUrl().endsWith(".svg")) {
            Glide.with(context)
                    .as(PictureDrawable.class)
                    .transition(withCrossFade())
                    .apply(RequestOptions.fitCenterTransform())
                    .listener(new SvgSoftwareLayerSetter())
                    .load(this.appNotification.getIconUrl())
                    .into(this.notificationIconView);
        } else {
                Glide.with(this.context)
                .asBitmap()
                .load(this.appNotification.getIconUrl())
                .apply(RequestOptions.fitCenterTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(this.notificationIconView);//
        }

    }

    private void updateNotificationContent() {

        this.notificationContentView.setText(this.appNotification.getContent());

        this.notificationContentView.setTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_TEXT_COLOR, this.screenSkin));

        if (this.appNotification.getContent() != null) {

            SpannableString contentString = new SpannableString(this.appNotification.getContent());
            if (this.appNotification.isRead()) {

                contentString.setSpan(new StyleSpan(Typeface.NORMAL), 0, this.appNotification.getContent().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            } else {

                contentString.setSpan(new StyleSpan(Typeface.BOLD), 0, this.appNotification.getContent().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

            this.notificationContentView.setText(contentString);
        }

    }

    private void updateNotificationTimestamp() {

        this.notificationTimestampView.setText(this.appNotification.getTimeStamp());

        this.notificationTimestampView.setTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_FIELD_TEXT_COLOR, this.screenSkin));

        if (this.appNotification.getTimeStamp() != null) {

            SpannableString timestampString = new SpannableString(this.appNotification.getTimeStamp());
            if (this.appNotification.isRead()) {

                timestampString.setSpan(new StyleSpan(Typeface.ITALIC), 0, this.appNotification.getTimeStamp().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            } else {

                timestampString.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, this.appNotification.getTimeStamp().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

            this.notificationTimestampView.setText(timestampString);

        }

    }

    private void updateNotificationMenuIcon() {

        Glide.with(this.context)
                .asDrawable()
                .load(ContextCompat.getDrawable(this.context, R.drawable.ic_more_horiz_black_24dp))
                .into(this.notificationMenuIconView);

    }

    private void updateReadMarkedNotificationMenuItem() {

        if (this.appNotification != null) {

            if (this.appNotification.isRead()) {

                this.readMarkedNotificationMenuItem = new PowerMenuItem("Mark As Unread", false);
                this.readMarkedNotificationMenuItem.setTag(NOTIFICATIONS_ITEM_DROPDOWN_MENU_READ_MARKED_ID);

            } else {

                this.readMarkedNotificationMenuItem = new PowerMenuItem("Mark As Read", false);
                this.readMarkedNotificationMenuItem.setTag(NOTIFICATIONS_ITEM_DROPDOWN_MENU_READ_MARKED_ID);

            }
        }

    }

    public void setScreenSkin(int screenSkin) {
        this.screenSkin = screenSkin;

        this.updateNotification();

    }

}