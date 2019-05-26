package com.bob.bobguestapp.tools.comparators.guestbooking;

import com.bob.bobguestapp.activities.main.fragments.profile.changeprofilepicture.ProfilePictureChangingOption;
import com.bob.bobguestapp.tools.database.objects.GuestBooking;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

public class GuestBookingCheckInComparator implements Comparator<GuestBooking> {

    @Override
    public int compare(GuestBooking guestBooking1, GuestBooking guestBooking2) {

        Date date1 = Timestamp.valueOf(guestBooking1.getCheckIn());
        Date date2 = Timestamp.valueOf(guestBooking2.getCheckIn());

        if (date1.before(date2)) {
            return 1;
        } else if (date1.after(date2)) {
            return -1;
        }
        return 0;

    }

}
