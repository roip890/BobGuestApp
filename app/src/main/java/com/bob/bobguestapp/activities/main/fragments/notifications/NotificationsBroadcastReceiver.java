package com.bob.bobguestapp.activities.main.fragments.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationsBroadcastReceiver extends BroadcastReceiver {

    NotificationsBroadcastReceiverListener notificationsBroadcastReceiverListener;

    public NotificationsBroadcastReceiver() {
        super();
    }


    public NotificationsBroadcastReceiver(NotificationsBroadcastReceiverListener notificationsBroadcastReceiverListener) {
        this.notificationsBroadcastReceiverListener = notificationsBroadcastReceiverListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (this.notificationsBroadcastReceiverListener != null) {
            this.notificationsBroadcastReceiverListener.onReceive(context, intent);
        }
    }
}
