package com.bob.bobguestapp.activities.main.fragments.notifications;

import android.content.Context;
import android.content.Intent;

public interface NotificationsBroadcastReceiverListener {
    void onReceive(Context context, Intent intent);
}
