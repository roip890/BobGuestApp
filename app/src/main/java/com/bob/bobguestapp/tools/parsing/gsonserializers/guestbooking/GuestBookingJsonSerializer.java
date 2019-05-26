package com.bob.bobguestapp.tools.parsing.gsonserializers.guestbooking;

import com.bob.bobguestapp.tools.database.objects.GuestBooking;
import com.bob.toolsmodule.http.serverbeans.Hotel;
import com.bob.toolsmodule.parsing.GsonParser;
import com.bob.toolsmodule.parsing.gsonserializers.BaseJsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GuestBookingJsonSerializer extends BaseJsonSerializer<GuestBooking> {


    @Override
    public JsonObject createJsonObject(GuestBooking object) {

        JsonObject guestBookingJsonObject = new JsonObject();

        guestBookingJsonObject.addProperty("id", object.getId());
        guestBookingJsonObject.addProperty("bookerEmail", object.getBookerEmail());
        guestBookingJsonObject.addProperty("bookerPassport", object.getBookerPassport());
        guestBookingJsonObject.addProperty("bookerPhone", object.getBookerPhone());
        guestBookingJsonObject.addProperty("checkIn", object.getCheckIn());
        guestBookingJsonObject.addProperty("checkOut", object.getCheckOut());
        guestBookingJsonObject.addProperty("checkedIn", object.isCheckedIn());
        guestBookingJsonObject.addProperty("numberOfUsers", object.getUsers());
        guestBookingJsonObject.addProperty("room", object.getRoom());

        if (object.getHotel() != null) {
            JsonElement hotelJsonObject = GsonParser.getParser().create().toJsonTree(object.getHotel(), Hotel.class);
            guestBookingJsonObject.add("hotel", hotelJsonObject);
        }

        return guestBookingJsonObject;
    }

    @Override
    public GuestBooking createObject(JsonObject jsonObject) {

        GuestBooking guestBooking = new GuestBooking();

        if (jsonObject.get("id") != null) {
            guestBooking.setId(jsonObject.get("id").getAsInt());
        }
        if (jsonObject.get("bookerEmail") != null) {
            guestBooking.setBookerEmail(jsonObject.get("bookerEmail").getAsString());
        }
        if (jsonObject.get("bookerPassport") != null) {
            guestBooking.setBookerPassport(jsonObject.get("bookerPassport").getAsString());
        }
        if (jsonObject.get("bookerPhone") != null) {
            guestBooking.setBookerPhone(jsonObject.get("bookerPhone").getAsString());
        }
        if (jsonObject.get("checkIn") != null) {
            guestBooking.setCheckIn(jsonObject.get("checkIn").getAsString());
        }
        if (jsonObject.get("checkOut") != null) {
            guestBooking.setCheckOut(jsonObject.get("checkOut").getAsString());
        }
        if (jsonObject.get("checkedIn") != null) {
            guestBooking.setCheckedIn(jsonObject.get("checkedIn").getAsBoolean());
        }
        if (jsonObject.get("numberOfUsers") != null) {
            guestBooking.setUsers(jsonObject.get("numberOfUsers").getAsInt());
        }
        if (jsonObject.get("room") != null) {
            guestBooking.setRoom(jsonObject.get("room").getAsInt());
        }
        if (jsonObject.get("hotel") != null) {
            guestBooking.setHotel(
                    GsonParser.getParser().create().fromJson(jsonObject.get("hotel"), Hotel.class)
            );
        }

        return guestBooking;
    }

    @Override
    protected void initObjectSerializers() {

    }
}
