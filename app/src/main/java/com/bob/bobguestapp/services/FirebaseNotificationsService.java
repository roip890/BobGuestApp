package com.bob.bobguestapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.notifications.NotificationsFragment;
import com.bob.bobguestapp.tools.database.MyDBUtilsManager;
import com.bob.bobguestapp.tools.database.objects.GuestAppNotification;
import com.bob.bobguestapp.tools.notifications.NotificationsUtilsManager;
import com.bob.bobguestapp.tools.parsing.gsonserializers.appnotification.AppNotificationJsonSerializer;
import com.bob.bobguestapp.tools.parsing.gsonserializers.appnotification.AppNotificationListJsonSerializer;
import com.bob.bobguestapp.tools.session.SessionUtilsManager;
import com.bob.toolsmodule.parsing.GsonParser;
import com.bob.uimodule.views.menu.MyMenuNodeView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class FirebaseNotificationsService extends FirebaseMessagingService {

    private static final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    private static final String FOREGROUND_NOTIFICATION_CHANNEL_ID = "my_channel_id_02";

    private NotificationManager notificationManager;

    @Override
    public void onCreate(){
        super.onCreate();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                String token = instanceIdResult.getToken();
                System.out.println("Firebase Token3: " + token);
                SessionUtilsManager.get().saveFirebaseToken(token);

            }
        });

        //init notifications manager
        this.initNotificationManager();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null
                && remoteMessage.getData().containsKey("notification")
                && remoteMessage.getData().get("notification") != null) {

            this.onMessageNotification(remoteMessage.getData().get("notification"));

        } else if (remoteMessage.getNotification() != null
                && remoteMessage.getNotification().getBody() != null) {

            try {

                JsonElement jsonElement = new JsonParser().parse(remoteMessage.getNotification().getBody());

                JsonObject jsonObject = jsonElement.getAsJsonObject();

                JsonObject notificationJsonObject = jsonObject.getAsJsonObject("notification");

                this.onMessageNotification(notificationJsonObject.toString());

            } catch (Exception e) {

                e.printStackTrace();

            }


        }

    }


    @Override
    public void onNewToken(String token) {

        System.out.println("Firebase Token: " + token);
        SessionUtilsManager.get().saveFirebaseToken(token);

    }


    private void initNotificationManager() {

        FirebaseNotificationsService.this.notificationManager = NotificationsUtilsManager.get(BOBGuestApplication.get()).getNotificationManager();

    }

    private void onMessageNotification(String appNotificationString) {

        try {

            //save new notification on db
            GsonBuilder customGsonBuilder = new GsonBuilder();
            customGsonBuilder.registerTypeAdapter(GuestAppNotification.class, new AppNotificationJsonSerializer());
            customGsonBuilder.registerTypeAdapter(GuestAppNotification[].class, new AppNotificationListJsonSerializer());
            Gson customGson = customGsonBuilder.create();

            GuestAppNotification appNotification = customGson.fromJson(appNotificationString, GuestAppNotification.class);
            appNotification.setTimeStamp(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(System.currentTimeMillis())
            );

            //add notification to the notifications page
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(NotificationsFragment.GET_NOTIFICATIONS_OF_GUEST);
            broadcastIntent.putExtra("notification", customGson.toJson(appNotification, GuestAppNotification.class));
            sendBroadcast(broadcastIntent);

            //make new notification
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(FirebaseNotificationsService.this, NOTIFICATION_CHANNEL_ID);


            RemoteViews notificationRemoteViews = FirebaseNotificationsService.this.initGuestNotificationView(appNotification);
            Notification notification = notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher_bob_round)
                    .setContentTitle(appNotification.getContent())
                    .setContentText(appNotification.getContent())
                    .setContent(notificationRemoteViews).build();

            new Handler(Looper.getMainLooper()).post(new NotificationImageLoaderRunnable(
                    this,
                    notificationRemoteViews,
                    notification,
                    appNotification.getIconUrl(),
                    (int)appNotification.getId()
            ));

            FirebaseNotificationsService.this.notificationManager.notify((int) appNotification.getId(), notificationBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private RemoteViews initGuestNotificationView(GuestAppNotification appNotification) {

        RemoteViews notificationRemoteView = new RemoteViews(getPackageName(), R.layout.notification_layout_guest_request);

        //notification title
        this.initGuestNotificationViewTitle(notificationRemoteView, appNotification);

        //notification content
        this.initGuestNotificationViewContent(notificationRemoteView, appNotification);

        //notification timestamp
        this.initGuestNotificationViewTimestamp(notificationRemoteView, appNotification);

        return notificationRemoteView;

    }

    private void initGuestNotificationViewTitle(RemoteViews notificationRemoteView, GuestAppNotification appNotification) {

        notificationRemoteView.setTextViewText(R.id.guest_request_notification_title, appNotification.getContent());

    }

    private void initGuestNotificationViewContent(RemoteViews notificationRemoteView, GuestAppNotification appNotification) {

        notificationRemoteView.setTextViewText(R.id.guest_request_notification_content, appNotification.getContent());

    }

    private void initGuestNotificationViewTimestamp(RemoteViews notificationRemoteView, GuestAppNotification appNotification) {

        notificationRemoteView.setTextViewText(R.id.guest_request_notification_timestamp, appNotification.getTimeStamp());

    }

    public class NotificationImageLoaderRunnable implements Runnable {

        private Context context;
        private RemoteViews notificationRemoteView;
        private Notification notification;
        private String appNotificationIconUrl;
        private int appNotificationId;

        public NotificationImageLoaderRunnable(Context context, RemoteViews notificationRemoteView, Notification notification, String appNotificationIconUrl, int appNotificationId) {

            this.context = context;

            this.notificationRemoteView = notificationRemoteView;

            this.notification = notification;

            this.appNotificationIconUrl = appNotificationIconUrl;

            this.appNotificationId = appNotificationId;

        }

        @Override
        public void run() {

            NotificationTarget notificationTarget = new NotificationTarget(
                    this.context,
                    R.id.guest_request_notification_icon,
                    this.notificationRemoteView,
                    this.notification,
                    this.appNotificationId);

            Glide.get(this.context).clearMemory();

            Glide.with(this.context)
                    .asBitmap()
//                .load(R.mipmap.ic_launcher_bob_round)
                    .load(this.appNotificationIconUrl)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(notificationTarget);//

        }
    }

}
