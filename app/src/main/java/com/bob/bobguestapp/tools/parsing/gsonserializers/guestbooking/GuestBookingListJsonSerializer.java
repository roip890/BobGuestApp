package com.bob.bobguestapp.tools.parsing.gsonserializers.guestbooking;


import com.bob.bobguestapp.tools.database.objects.GuestBooking;
import com.bob.toolsmodule.parsing.gsonserializers.BaseListJsonSerializer;

public class GuestBookingListJsonSerializer extends BaseListJsonSerializer<GuestBooking> {

    @Override
    protected void initObjectSerializer() {
        this.objectSerializer = new GuestBookingJsonSerializer();
    }

    @Override
    protected GuestBooking[] getArrayOfType(int arraySize) {
        return new GuestBooking[arraySize];
    }

}
