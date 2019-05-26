package com.bob.bobguestapp.tools.database.objects;

import io.realm.annotations.RealmModule;

@RealmModule(library = true, classes = {
        GuestAppNotification.class,
        GuestBooking.class})
public class BOBGuestAppRealmModule {
}
