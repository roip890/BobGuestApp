package com.bob.bobguestapp.tools.http.serverbeans.smsverification;

import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;

public class GuestSMSVerificationResponse {

    private ApplicativeResponse statusResponse;

    public GuestSMSVerificationResponse() {

    }

    public ApplicativeResponse getStatusResponse() {
        return statusResponse;
    }

    public void setStatusResponse(ApplicativeResponse statusResponse) {
        this.statusResponse = statusResponse;
    }
}
