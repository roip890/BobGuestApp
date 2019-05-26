package com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestchangestatus.statuseslist;


import java.util.Comparator;

/**
 * Created by User on 14/09/2018.
 */

public class GuestRequestStatusTitleComparator implements Comparator<GuestRequestStatus> {

    @Override
    public int compare(GuestRequestStatus guestRequestStatus1, GuestRequestStatus guestRequestStatus2) {

        int compare = guestRequestStatus1.getStatus().compareToIgnoreCase(guestRequestStatus2.getStatus());
        if (compare < 0) {
            return -1;
        } else if (compare > 0) {
            return 1;
        }
        return 0;
    }

}
