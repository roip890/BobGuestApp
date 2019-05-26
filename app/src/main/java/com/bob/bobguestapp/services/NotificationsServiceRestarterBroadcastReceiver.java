package com.bob.bobguestapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class NotificationsServiceRestarterBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //start service
        Intent serviceIntent = new Intent(context, NotificationsService.class);
        serviceIntent.putExtra("guestId", 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }

        //        context.startService(new Intent(context, NotificationsService.class));
    }
}
