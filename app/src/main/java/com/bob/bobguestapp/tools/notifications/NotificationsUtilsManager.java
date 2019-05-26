package com.bob.bobguestapp.tools.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

public final class NotificationsUtilsManager {

    public static final String NOTIFICATIONS_CHANNEL_ID = "NOTIFICATIONS_CHANNEL_0";

    protected static NotificationsUtilsManager instance;

    protected NotificationManager notificationManager;

    //singleton init
    public static NotificationsUtilsManager get(Context context) {
        if (instance == null) {
            instance = new NotificationsUtilsManager(context);
        }
        return instance;
    }

    //initialization
    protected NotificationsUtilsManager(Context context) {

        //init notifications manager
        this.initNotificationsManager(context);

        //init main notificaitons channel
        this.initMainNotificationsChannel();

    }

    private void initNotificationsManager(Context context) {

        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public void initMainNotificationsChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATIONS_CHANNEL_ID, "Main Notifications Channel", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("BOB Guest App Main Notifications Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            this.notificationManager.createNotificationChannel(notificationChannel);
        }

    }

    public NotificationManager getNotificationManager() {

        return this.notificationManager;

    }





}
