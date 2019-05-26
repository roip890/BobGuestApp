package com.bob.bobguestapp.activities.main.fragments.requests.requestslist.comperators;


import com.bob.toolsmodule.database.objects.GuestRequest;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by User on 14/09/2018.
 */

public class GuestRequestStatusComparator implements Comparator<GuestRequest> {

    private static final int DONE = 0;
    private static final int PENDING = 1;
    private static final int WAITING = 2;
    private static final int DEFAULT = 3;


    private static final HashMap<String, Integer> guestStatus;
    static {
        guestStatus = new HashMap<String, Integer>();
        guestStatus.put("waiting", WAITING);
        guestStatus.put("in_progress", PENDING);
        guestStatus.put("done", DONE);
    }


    @Override
    public int compare(GuestRequest guestRequest1, GuestRequest guestRequest2) {


        int status1 = getGuestStatusRepresentation(guestRequest1.getStatus());
        int status2 = getGuestStatusRepresentation(guestRequest2.getStatus());

        if (status1 < status2) {
            return -1;
        } else if (status1 > status2) {
            return 1;
        }
        return 0;
    }

    private int getGuestStatusRepresentation(String guestStatus) {

        Integer guestStatusRepresentation;

        if ((guestStatusRepresentation = GuestRequestStatusComparator.guestStatus.get(guestStatus.toLowerCase())) != null) {
            return guestStatusRepresentation;
        } else {
            return GuestRequestStatusComparator.DEFAULT;
        }
    }

}
