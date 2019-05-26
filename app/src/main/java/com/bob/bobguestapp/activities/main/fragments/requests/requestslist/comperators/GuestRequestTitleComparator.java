package com.bob.bobguestapp.activities.main.fragments.requests.requestslist.comperators;

import com.bob.toolsmodule.database.objects.GuestRequest;

import java.util.Comparator;

/**
 * Created by User on 14/09/2018.
 */

public class GuestRequestTitleComparator implements Comparator<GuestRequest> {

    @Override
    public int compare(GuestRequest guestRequest1, GuestRequest guestRequest2) {

        int compare = guestRequest1.getTitle().compareToIgnoreCase(guestRequest2.getTitle());
        if (compare < 0) {
            return -1;
        } else if (compare > 0) {
            return 1;
        }
        return 0;
    }

}
