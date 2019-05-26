package com.bob.bobguestapp.activities.main.fragments.notifications.notificaitonslist.comperators;

import com.bob.bobguestapp.tools.database.objects.GuestAppNotification;

import java.util.Comparator;

/**
 * Created by User on 14/09/2018.
 */

public class AppNotificationTitleComparator implements Comparator<GuestAppNotification> {

    @Override
    public int compare(GuestAppNotification appNotification1, GuestAppNotification appNotification2) {

        int compare = appNotification1.getContent().compareToIgnoreCase(appNotification2.getContent());
        if (compare < 0) {
            return -1;
        } else if (compare > 0) {
            return 1;
        }
        return 0;
    }

}
