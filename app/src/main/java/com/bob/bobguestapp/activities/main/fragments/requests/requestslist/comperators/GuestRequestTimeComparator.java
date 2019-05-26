package com.bob.bobguestapp.activities.main.fragments.requests.requestslist.comperators;


import com.bob.toolsmodule.database.objects.GuestRequest;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by User on 14/09/2018.
 */

public class GuestRequestTimeComparator implements Comparator<GuestRequest> {

    @Override
    public int compare(GuestRequest guestRequest1, GuestRequest guestRequest2) {

        Date date1 = Timestamp.valueOf(guestRequest1.getTimeStamp());
        Date date2 = Timestamp.valueOf(guestRequest2.getTimeStamp());

        if (date1.before(date2)) {
            return 1;
        } else if (date1.after(date2)) {
            return -1;
        }
        return 0;

    }

}
