package com.bob.bobguestapp.activities.codes.codeslist.comperators;

import com.bob.toolsmodule.database.objects.GuestCode;

import java.util.Comparator;

/**
 * Created by User on 14/09/2018.
 */

public class GuestCodeCodeComparator implements Comparator<GuestCode> {

    @Override
    public int compare(GuestCode guestCode1, GuestCode guestCode2) {

        int compare = guestCode1.getCode().compareToIgnoreCase(guestCode2.getCode());
        if (compare < 0) {
            return -1;
        } else if (compare > 0) {
            return 1;
        }
        return 0;

    }

}
