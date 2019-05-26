package com.bob.bobguestapp.tools.database.objects;

import com.bob.toolsmodule.http.serverbeans.Hotel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GuestBooking extends RealmObject {

    //fields
    @PrimaryKey
    private long id;

    private String bookerEmail;
    private String bookerPassport;
    private String bookerPhone;
    private String checkIn;
    private String checkOut;
    private boolean checkedIn;

    private Hotel hotel;
    private int users;
    private int room;

    //getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBookerEmail() {
        return bookerEmail;
    }

    public void setBookerEmail(String bookerEmail) {
        this.bookerEmail = bookerEmail;
    }

    public String getBookerPassport() {
        return bookerPassport;
    }

    public void setBookerPassport(String bookerPassport) {
        this.bookerPassport = bookerPassport;
    }

    public String getBookerPhone() {
        return bookerPhone;
    }

    public void setBookerPhone(String bookerPhone) {
        this.bookerPhone = bookerPhone;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

}
