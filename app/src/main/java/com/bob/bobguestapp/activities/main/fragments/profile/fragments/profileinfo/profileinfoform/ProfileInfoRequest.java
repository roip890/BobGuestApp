package com.bob.bobguestapp.activities.main.fragments.profile.fragments.profileinfo.profileinfoform;

import com.bob.toolsmodule.database.objects.RequestItem;
import com.bob.toolsmodule.http.serverbeans.Guest;

public class ProfileInfoRequest {

    private Guest guest;
    private RequestItem[] requestItems;

    public ProfileInfoRequest() {

    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public RequestItem[] getRequestItems() {
        return requestItems;
    }

    public void setRequestItems(RequestItem[] requestItems) {
        this.requestItems = requestItems;
    }
}
