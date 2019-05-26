package com.bob.bobguestapp.activities.main.fragments.notifications.notificaitonslist.comperators;

import com.bob.bobguestapp.tools.database.objects.GuestAppNotification;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by User on 14/09/2018.
 */

public class AppNotificationTimeComparator implements Comparator<GuestAppNotification> {

    @Override
    public int compare(GuestAppNotification appNotification1, GuestAppNotification appNotification2) {

        Date date1 = Timestamp.valueOf(appNotification1.getTimeStamp());
        Date date2 = Timestamp.valueOf(appNotification2.getTimeStamp());

        if (date1.before(date2)) {
            return 1;
        } else if (date1.after(date2)) {
            return -1;
        }
        return 0;
    }

}
