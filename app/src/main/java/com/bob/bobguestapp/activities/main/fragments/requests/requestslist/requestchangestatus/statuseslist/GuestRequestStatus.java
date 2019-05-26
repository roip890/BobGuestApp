package com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestchangestatus.statuseslist;

public class GuestRequestStatus {

    private String status;
    private int icon;
    private int color;


    public GuestRequestStatus(String status, int color) {

        this.status = status;

        this.color = color;

    }

    public GuestRequestStatus() {

    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


}
