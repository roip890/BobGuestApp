package com.bob.bobguestapp.tools.session;

import android.content.Context;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.tools.database.objects.GuestBooking;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.http.serverbeans.Hotel;
import com.bob.uimodule.theme.ThemeUtilsManager;

public class SessionUtilsManager {

    protected static SessionUtilsManager instance;

    //initialization
    protected SessionUtilsManager() {

    }

    public static SessionUtilsManager get() {
        if (instance == null) {
            instance = new SessionUtilsManager();
        }
        return instance;
    }

    //save
    public void saveGuest(Guest guest) {

        if (guest != null) {
            if (guest.getEmail() != null) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putString("guestEmail", guest.getEmail()).apply();
            }
            if (guest.getId() != -1) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putLong("guestId", guest.getId()).apply();
            }
            if (guest.getName() != null) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putString("guestName", guest.getName()).apply();
            }
            if (guest.getPassword() != null) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putString("guestPassword", guest.getPassword()).apply();
            }
            if (guest.getPhone() != null) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putString("guestPhone", guest.getPhone()).apply();
            }
            if (guest.getRoom() != -1) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putInt("guestRoom", guest.getRoom()).apply();
            }
        }

    }

    public void saveHotel(Hotel hotel) {

        if (hotel != null) {
            if (hotel.getId() != -1) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putLong("hotelId", hotel.getId()).apply();
            }
            if (hotel.getName() != null) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putString("hotelName", hotel.getName()).apply();
            }
            if (hotel.getImageUrl() != null) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putString("hotelImage", hotel.getImageUrl()).apply();
            }
        }

    }

    public void saveBooking(GuestBooking guestBooking) {

        if (guestBooking != null) {
            if (guestBooking.getId() != -1) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putLong("bookingId", guestBooking.getId()).apply();
            }
            if (guestBooking.getBookerEmail() != null) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putString("bookerEmail", guestBooking.getBookerEmail()).apply();
            }
            if (guestBooking.getBookerPassport() != null) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putString("bookerPassport", guestBooking.getBookerPassport()).apply();
            }
            if (guestBooking.getBookerPhone() != null) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putString("bookerPhone", guestBooking.getBookerPhone()).apply();
            }
            if (guestBooking.getCheckIn() != null) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putString("bookingCheckIn", guestBooking.getCheckIn()).apply();
            }
            if (guestBooking.getCheckOut() != null) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putString("bookingCheckOut", guestBooking.getCheckOut()).apply();
            }

            BOBGuestApplication.get().getSecureSharedPreferences().edit().putBoolean("bookingCheckedIn", guestBooking.isCheckedIn()).apply();

            if (guestBooking.getUsers() != -1) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putInt("bookingNumberOfUsers", guestBooking.getUsers()).apply();
            }
            if (guestBooking.getRoom() != -1) {
                BOBGuestApplication.get().getSecureSharedPreferences().edit().putInt("bookingRoom", guestBooking.getRoom()).apply();
            }
            if (guestBooking.getHotel() != null) {
                SessionUtilsManager.get().saveHotel(guestBooking.getHotel());
            }
        }

    }

    public void saveFirebaseToken(String firebaseToken) {

        if (firebaseToken != null && !firebaseToken.equals("")) {
            BOBGuestApplication.get().getSecureSharedPreferences().edit().putString("firebaseToken", firebaseToken).apply();
        }

    }

    //get
    public Guest getGuest() {

        Guest guest = new Guest();

        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestEmail")) {
            guest.setEmail(BOBGuestApplication.get().getSecureSharedPreferences().getString("guestEmail", null));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestId")) {
            guest.setId(BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1l));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestName")) {
            guest.setName(BOBGuestApplication.get().getSecureSharedPreferences().getString("guestName", null));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestPassword")) {
            guest.setPassword(BOBGuestApplication.get().getSecureSharedPreferences().getString("guestPassword", null));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestPhone")) {
            guest.setPhone(BOBGuestApplication.get().getSecureSharedPreferences().getString("guestPhone", null));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestRoom")) {
            guest.setRoom(BOBGuestApplication.get().getSecureSharedPreferences().getInt("guestRoom", -1));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestImageUrl")) {
            guest.setImageUrl(BOBGuestApplication.get().getSecureSharedPreferences().getString("guestImageUrl", null));
        }

        return guest;

    }

    public Hotel getHotel() {

        Hotel hotel = new Hotel();

        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("hotelId")) {
            hotel.setId(BOBGuestApplication.get().getSecureSharedPreferences().getInt("hotelId", -1));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("hotelName")) {
            hotel.setName(BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelName", null));
        }

        return hotel;

    }

    public GuestBooking getGuestBooking() {

        GuestBooking guestBooking = new GuestBooking();

        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("bookingId")) {
            guestBooking.setId(BOBGuestApplication.get().getSecureSharedPreferences().getInt("id", -1));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("bookerEmail")) {
            guestBooking.setBookerEmail(BOBGuestApplication.get().getSecureSharedPreferences().getString("bookerEmail", null));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("bookerPassport")) {
            guestBooking.setBookerPassport(BOBGuestApplication.get().getSecureSharedPreferences().getString("bookerPassport", null));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("bookerPhone")) {
            guestBooking.setBookerPhone(BOBGuestApplication.get().getSecureSharedPreferences().getString("bookerPhone", null));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("bookingCheckIn")) {
            guestBooking.setCheckIn(BOBGuestApplication.get().getSecureSharedPreferences().getString("checkIn", null));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("bookingCheckOut")) {
            guestBooking.setCheckOut(BOBGuestApplication.get().getSecureSharedPreferences().getString("checkOut", null));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("bookingCheckedIn")) {
            guestBooking.setCheckedIn(BOBGuestApplication.get().getSecureSharedPreferences().getBoolean("checkedIn", false));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("bookingNumberOfUsers")) {
            guestBooking.setUsers(BOBGuestApplication.get().getSecureSharedPreferences().getInt("numberOfUsers", -1));
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("bookingRoom")) {
            guestBooking.setRoom(BOBGuestApplication.get().getSecureSharedPreferences().getInt("room", -1));
        }

        Hotel hotel = SessionUtilsManager.get().getHotel();
        if (hotel != null) {
            guestBooking.setHotel(hotel);
        }

        return guestBooking;

    }

    public String getFirebaseToken() {

        String firebaseToken = null;

        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("firebaseToken")) {
            firebaseToken =  BOBGuestApplication.get().getSecureSharedPreferences().getString("firebaseToken", null);
        }

        return firebaseToken;

    }

    //clear
    public void clearSession() {

        //clear guest
        this.clearGuest();

        //clear hotel
        this.clearHotel();

    }

    public void clearGuest() {

        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestEmail")) {
            BOBGuestApplication.get().getSecureSharedPreferences().edit().remove("guestEmail").apply();
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestGuidValid")) {
            BOBGuestApplication.get().getSecureSharedPreferences().edit().remove("guestGuidValid").apply();
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestId")) {
            BOBGuestApplication.get().getSecureSharedPreferences().edit().remove("guestId").apply();
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestName")) {
            BOBGuestApplication.get().getSecureSharedPreferences().edit().remove("guestName").apply();
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestPassword")) {
            BOBGuestApplication.get().getSecureSharedPreferences().edit().remove("guestPassword").apply();
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestPhone")) {
            BOBGuestApplication.get().getSecureSharedPreferences().edit().remove("guestPhone").apply();
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestRoom")) {
            BOBGuestApplication.get().getSecureSharedPreferences().edit().remove("guestRoom").apply();
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestStatus")) {
            BOBGuestApplication.get().getSecureSharedPreferences().edit().remove("guestStatus").apply();
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestInsertTs")) {
            BOBGuestApplication.get().getSecureSharedPreferences().edit().remove("guestInsertTs").apply();
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("guestLmTs")) {
            BOBGuestApplication.get().getSecureSharedPreferences().edit().remove("guestLmTs").apply();
        }

    }

    public void clearHotel() {

        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("hotelId")) {
            BOBGuestApplication.get().getSecureSharedPreferences().edit().remove("hotelId").apply();
        }
        if (BOBGuestApplication.get().getSecureSharedPreferences().contains("hotelName")) {
            BOBGuestApplication.get().getSecureSharedPreferences().edit().remove("hotelName").apply();
        }

    }

}
