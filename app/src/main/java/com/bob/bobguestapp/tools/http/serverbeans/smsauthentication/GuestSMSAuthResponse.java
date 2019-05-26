package com.bob.bobguestapp.tools.http.serverbeans.smsauthentication;

import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;

public class GuestSMSAuthResponse {

    private ApplicativeResponse statusResponse;

    public GuestSMSAuthResponse() {

    }

    public ApplicativeResponse getStatusResponse() {
        return statusResponse;
    }

    public void setStatusResponse(ApplicativeResponse statusResponse) {
        this.statusResponse = statusResponse;
    }
}
