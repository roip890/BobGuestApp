package com.bob.bobguestapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.RemoteViews;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.intro.fragments.login.LoginFragment;
import com.bob.bobguestapp.activities.main.fragments.notifications.NotificationsFragment;
import com.bob.bobguestapp.tools.database.MyDBUtilsManager;
import com.bob.bobguestapp.tools.database.objects.GuestAppNotification;
import com.bob.bobguestapp.tools.notifications.NotificationsUtilsManager;
import com.bob.toolsmodule.GeneralUtilsManager;
import com.bob.toolsmodule.parsing.GsonParser;
import com.bob.uimodule.image.svg.SvgSoftwareLayerSetter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.NotificationTarget;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class NotificationsService extends Service {

    private static final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    private static final String FOREGROUND_NOTIFICATION_CHANNEL_ID = "my_channel_id_02";

    private WebSocketClient webSocketClient;
    private NotificationManager notificationManager;

    @Override
    public void onCreate(){
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            builder.setVisibility(Notification.VISIBILITY_SECRET);
            startMyOwnForeground();
        }
        else {
            startForeground(2, new Notification());
        }
    }

    private void startMyOwnForeground(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

//            String NOTIFICATION_CHANNEL_ID = "com.bob.bobguestapp";
//            String channelName = "My Background Service";
//            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
//            chan.setLightColor(Color.BLUE);
//            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            assert manager != null;
//            manager.createNotificationChannel(chan);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.no_height_notification_layout);
            builder.setContent(remoteViews)
                    .setOngoing(true)
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                builder.setVisibility(NotificationCompat.VISIBILITY_SECRET);

//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//            Notification notification = notificationBuilder.setOngoing(true)
//                    .setSmallIcon(R.mipmap.ic_launcher_bob_round)
//                    .setContentTitle("App is running in background")
//                    .setPriority(NotificationManager.IMPORTANCE_MIN)
//                    .setCategory(Notification.CATEGORY_SERVICE)
//                    .build();
            startForeground(2, builder.build());

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //init notifications manager
        this.initNotificationManager();

        if (this.webSocketClient == null || !this.webSocketClient.isOpen()) {
            this.connectWebSocket();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        if (this.webSocketClient != null && this.webSocketClient.isOpen()) {
            this.webSocketClient.close();
        }

        Intent broadcastIntent = new Intent(this, NotificationsServiceRestarterBroadcastReceiver.class);

        sendBroadcast(broadcastIntent);

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        if (this.webSocketClient != null && this.webSocketClient.isOpen()) {
            this.webSocketClient.close();
        }

        Intent broadcastIntent = new Intent(this, NotificationsServiceRestarterBroadcastReceiver.class);

        sendBroadcast(broadcastIntent);

    }

    private void initNotificationManager() {

        NotificationsService.this.notificationManager = NotificationsUtilsManager.get(BOBGuestApplication.get()).getNotificationManager();

    }

    private void connectWebSocket() {

        URI notificationsUri = this.buildNotificationWebSocketUri();

        if (notificationsUri != null) {

            this.webSocketClient = new WebSocketClient(notificationsUri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {

                    NotificationsService.this.onOpenNotificationWebSocket();

                }

                @Override
                public void onMessage(String message) {

                    NotificationsService.this.onMessageNotificationWebSocket(message);

                }

                @Override
                public void onClose(int i, String s, boolean b) {

                    NotificationsService.this.onCloseNotificationWebSocket(i, s, b);

                }

                @Override
                public void onError(Exception e) {

                    NotificationsService.this.onErrorNotificationWebSocket(e);

                }
            };

            this.webSocketClient.connect();

        }
    }

    private URI buildNotificationWebSocketUri() {

        //init uri
        URI uri = null;

        //build uri
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestId")
                && BOBGuestApplication.get().getSecureSharedPreferences().contains("hotelId")) {

            long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1L);
            long hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("hotelId", -1L);

            try {
                uri = new URI("ws://159.65.87.128:8080/NotificationServices/guest/"+guestId+"/"+hotelId);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }

        return uri;

    }

    private void onOpenNotificationWebSocket() {

        webSocketClient.send("Hello from " + Build.MANUFACTURER + ", " + Build.MODEL);

    }

    private void onMessageNotificationWebSocket(String message) {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//            }
//        }).start();

        try {
            JSONObject jsonMessage = new JSONObject(message);

            if (jsonMessage.has("notification")) {

                //save new notification on db
                String appNotificationString = jsonMessage.getJSONObject("notification").toString();
                GuestAppNotification appNotification = GsonParser.getParser().create().fromJson(appNotificationString, GuestAppNotification.class);
                appNotification.setTimeStamp(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(System.currentTimeMillis())
                );
                MyDBUtilsManager.get().insertNotificationToDB(appNotification);

                //add notification to the notifications page
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(NotificationsFragment.GET_NOTIFICATIONS_OF_GUEST);
                broadcastIntent.putExtra("notificationId", appNotification.getId());
                sendBroadcast(broadcastIntent);

                //make new notification
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(NotificationsService.this, NOTIFICATION_CHANNEL_ID);


                RemoteViews notificationRemoteViews = NotificationsService.this.initGuestNotificationView(appNotification);
                Notification notification = notificationBuilder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher_bob_round)
                        .setContentTitle(appNotification.getContent())
                        .setContentText(appNotification.getContent())
                        .setContent(notificationRemoteViews).build();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        NotificationsService.this.initGuestNotificationViewIcon(notificationRemoteViews, appNotification, notification);
                    }
                });


//                                .setSmallIcon(R.mipmap.ic_launcher_bob_round)
//                                .setTicker("BOB Guest")
//                                .setContentTitle(notification.getContent())
//                                .setContentText(notification.getContent())
//                                .setContentInfo("Info");

                NotificationsService.this.notificationManager.notify((int) appNotification.getId(), notificationBuilder.build());

            }

        } catch (JSONException e) {
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

    private void initGuestNotificationViewIcon(RemoteViews notificationRemoteView, GuestAppNotification appNotification, Notification notification) {

        NotificationTarget notificationTarget = new NotificationTarget(
                this,
                R.id.guest_request_notification_icon,
                notificationRemoteView,
                notification,
                (int)appNotification.getId());

        Glide.with(this.getApplicationContext())
                .asBitmap()
                .load(appNotification.getIconUrl())
                .apply(RequestOptions.fitCenterTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(notificationTarget);//

    }

    private void onErrorNotificationWebSocket(Exception e) {

        e.printStackTrace();

    }

    private void onCloseNotificationWebSocket(int i, String s, boolean b) {

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
