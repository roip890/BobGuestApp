package com.bob.bobguestapp.activities.codes.codeslist.comperators;

import com.bob.toolsmodule.database.objects.GuestCode;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by User on 14/09/2018.
 */

public class GuestCodeStatusComparator implements Comparator<GuestCode> {

    private static final int CONNECTED = 0;
    private static final int DISCONNECTED = 1;
    private static final int DEFAULT = 2;


    private static final HashMap<String, Integer> guestStatus;
    static {
        guestStatus = new HashMap<String, Integer>();
        guestStatus.put("connected", CONNECTED);
        guestStatus.put("disconnected", DISCONNECTED);
    }


    @Override
    public int compare(GuestCode guestCode1, GuestCode guestCode2) {

        int status1 = getGuestStatusRepresentation(guestCode1.getStatus());
        int status2 = getGuestStatusRepresentation(guestCode2.getStatus());

        if (status1 < status2) {
            return -1;
        } else if (status1 > status2) {
            return 1;
        }
        return 0;

    }

    private int getGuestStatusRepresentation(String guestStatus) {

        Integer guestStatusRepresentation;

        if ((guestStatusRepresentation = GuestCodeStatusComparator.guestStatus.get(guestStatus.toLowerCase())) != null) {
            return guestStatusRepresentation;
        } else {
            return GuestCodeStatusComparator.DEFAULT;
        }
    }

}
