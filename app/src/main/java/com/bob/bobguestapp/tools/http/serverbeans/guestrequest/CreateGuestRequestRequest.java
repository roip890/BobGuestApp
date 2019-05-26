package com.bob.bobguestapp.tools.http.serverbeans.guestrequest;

import com.bob.toolsmodule.database.objects.GuestRequest;
import com.bob.toolsmodule.database.objects.GuestRequestItem;
import com.bob.toolsmodule.http.serverbeans.Guest;

public class CreateGuestRequestRequest {

    private Guest guest;
    private GuestRequest wish;
    private GuestRequestItem[] elements;

    public CreateGuestRequestRequest() {

    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public GuestRequest getWish() {
        return wish;
    }

    public void setWish(GuestRequest wish) {
        this.wish = wish;
    }

    public GuestRequestItem[] getElements() {
        return elements;
    }

    public void setElements(GuestRequestItem[] elements) {
        this.elements = elements;
    }
}
