package com.bob.bobguestapp.tools.http.serverbeans.guestrequest;

import com.bob.toolsmodule.database.objects.GuestRequest;
import com.bob.toolsmodule.database.objects.GuestRequestItem;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.toolsmodule.http.serverbeans.Guest;

public class CreateGuestRequestResponse {

    private ApplicativeResponse statusResponse;

    public CreateGuestRequestResponse() {

    }

    public ApplicativeResponse getStatusResponse() {
        return statusResponse;
    }

    public void setStatusResponse(ApplicativeResponse statusResponse) {
        this.statusResponse = statusResponse;
    }
}
