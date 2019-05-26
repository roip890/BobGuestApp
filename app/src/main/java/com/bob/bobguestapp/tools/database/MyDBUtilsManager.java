package com.bob.bobguestapp.tools.database;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.tools.database.objects.GuestAppNotification;
import com.bob.bobguestapp.tools.database.objects.GuestBooking;
import com.bob.toolsmodule.ToolsModuleManager;
import com.bob.toolsmodule.database.DBUtilsManager;
import com.bob.toolsmodule.database.RealmController;
import com.bob.toolsmodule.database.objects.GuestCode;
import com.bob.toolsmodule.database.objects.GuestRequest;

public class MyDBUtilsManager extends DBUtilsManager {


    protected MyDBUtilsManager(){
        super();
    }

    public static MyDBUtilsManager get() {
        if (instance == null) {
            instance = new MyDBUtilsManager();
        }
        return (MyDBUtilsManager)instance;
    }

    //notifications
    public void clearNotificationsFromDB() {

        MyRealmController.with(BOBGuestApplication.get()).deleteAllAppNotifications();

    }

    public void insertNotificationsListToDB(GuestAppNotification[] notifications) {

        for (GuestAppNotification notification : notifications) {
            MyRealmController.with(BOBGuestApplication.get()).insertOrUpdateAppNotification(notification);
        }

    }

    public void insertNotificationToDB(GuestAppNotification notification) {

        MyRealmController.with(BOBGuestApplication.get()).insertOrUpdateAppNotification(notification);

    }

    //codes
    public void clearCodesFromDB() {
        MyRealmController.with(BOBGuestApplication.get()).deleteAllGuestCodes();
    }

    public void insertCodesListToDB(GuestCode[] guestCodes) {

        for (GuestCode guestCode : guestCodes) {
            MyRealmController.with(BOBGuestApplication.get()).insertGuestCode(guestCode);
        }

    }

    public void insertGuestBookingsListToDB(GuestBooking[] guestBookings) {

        for (GuestBooking guestBooking : guestBookings) {
            MyRealmController.with(BOBGuestApplication.get()).insertOrUpdateGuestBooking(guestBooking);
        }

    }

    public void clearGuestBookingsFromDB() {
        MyRealmController.with(BOBGuestApplication.get()).deleteAllGuestBookings();
    }

}
