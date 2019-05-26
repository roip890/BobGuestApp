package com.bob.bobguestapp.activities.intro.connector;

public interface IntroConnector {

    public void emailVerification(int guestId, String email);

    public void smsVerification(int guestId, String phone);

    public void login(String guestEmail, String guestPassword);

}
