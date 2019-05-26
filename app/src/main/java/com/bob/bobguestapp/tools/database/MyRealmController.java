package com.bob.bobguestapp.tools.database;

import android.app.Activity;
import android.app.Application;

import androidx.fragment.app.Fragment;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.tools.database.objects.GuestAppNotification;
import com.bob.bobguestapp.tools.database.objects.GuestBooking;
import com.bob.toolsmodule.database.RealmController;

import java.util.Collection;
import java.util.List;

import io.realm.Realm;

public class MyRealmController extends RealmController {

    protected MyRealmController(Application application) {
        super(application);
    }

    public static MyRealmController with(Fragment fragment) {
        if (instance == null) {
            instance = new MyRealmController(fragment.getActivity().getApplication());
        }
        return (MyRealmController)instance;
    }

    public static MyRealmController with(Activity activity) {
        if (instance == null) {
            instance = new MyRealmController(activity.getApplication());
        }
        return (MyRealmController)instance;
    }

    public static MyRealmController with(Application application) {
        if (instance == null) {
            instance = new MyRealmController(application);
        }
        return (MyRealmController)instance;
    }

    public static MyRealmController get() {
        if (instance == null) {
            instance = new MyRealmController(BOBGuestApplication.get());
        }
        return (MyRealmController)instance;
    }

    //app notification
    public List<GuestAppNotification> getAppNotifications() {
//        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        List<GuestAppNotification> guestAppNotifications = realm.where(GuestAppNotification.class).findAll();
        guestAppNotifications = guestAppNotifications != null ? realm.copyFromRealm(guestAppNotifications) : null;
        realm.close();
        return guestAppNotifications;
//        return realm.where(GuestAppNotification.class).findAll();
    }

    public GuestAppNotification getAppNotificationById(long appNotificationId) {
//        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        GuestAppNotification guestAppNotification = realm.where(GuestAppNotification.class).equalTo("id", appNotificationId).findFirst();
        guestAppNotification = guestAppNotification != null ? realm.copyFromRealm(guestAppNotification) : null;
        realm.close();
        return guestAppNotification;
//        return realm.where(GuestAppNotification.class).equalTo("id", appNotificationId).findFirst();
    }

    public void insertAppNotification(GuestAppNotification appNotification) {
//        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(appNotification);
        realm.commitTransaction();
        realm.close();
    }

    public void insertAppNotifications(Collection<GuestAppNotification> appNotifications) {
        for (GuestAppNotification appNotification:appNotifications) {
//            if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(appNotification);
        realm.commitTransaction();
        realm.close();
        }
    }

    public void insertOrUpdateAppNotification(GuestAppNotification appNotification) {
//        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(appNotification);
        realm.commitTransaction();
        realm.close();
    }

    public void insertOrUpdateAppNotifications(Collection<GuestAppNotification> appNotifications) {
        for (GuestAppNotification appNotification:appNotifications) {
//            if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        realm.beginTransaction();
            realm.copyToRealmOrUpdate(appNotification);
            realm.commitTransaction();
        realm.close();
        }
    }

    public void deleteAppNotificationById(long appNotificationId) {
//        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        realm.beginTransaction();
        realm.where(GuestAppNotification.class).equalTo("id", appNotificationId).findAll().deleteAllFromRealm();
        realm.commitTransaction();
        realm.close();
    }

    public void deleteAllAppNotifications() {
//        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        realm.beginTransaction();
        realm.delete(GuestAppNotification.class);
        realm.commitTransaction();
        realm.close();
    }

    public List<GuestAppNotification> getAppNotificationsOfGuest(long guestId) {
//        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        List<GuestAppNotification> guestAppNotifications = realm.where(GuestAppNotification.class).equalTo("guestId", guestId).findAll();
        guestAppNotifications = guestAppNotifications != null ? realm.copyFromRealm(guestAppNotifications) : null;
        realm.close();
        return guestAppNotifications;
//        return realm.where(GuestAppNotification.class).equalTo("guestId", guestId).findAll();
    }

    //guest booking
    public List<GuestBooking> getGuestBookings() {
//        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        List<GuestBooking> guestBookings = realm.where(GuestBooking.class).findAll();
        guestBookings = guestBookings != null ? realm.copyFromRealm(guestBookings) : null;
        realm.close();
        return guestBookings;
//        return realm.where(GuestBooking.class).findAll();
    }

    public GuestBooking getGuestBookingById(long guestBookingId) {
//        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        GuestBooking guestBooking = realm.where(GuestBooking.class).equalTo("id", guestBookingId).findFirst();
        guestBooking = guestBooking != null ? realm.copyFromRealm(guestBooking) : null;
        realm.close();
        return guestBooking;
//        return realm.where(GuestBooking.class).equalTo("id", guestBookingId).findFirst();
    }

    public void insertGuestBooking(GuestBooking guestBooking) {
//        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(guestBooking);
        realm.commitTransaction();
        realm.close();
    }

    public void insertGuestBookings(Collection<GuestBooking> guestBookings) {
        for (GuestBooking guestBooking:guestBookings) {
    //        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(guestBooking);
            realm.commitTransaction();
            realm.close();
        }
    }

    public void insertOrUpdateGuestBooking(GuestBooking guestBooking) {
//        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(guestBooking);
        realm.commitTransaction();
        realm.close();
    }

    public void insertOrUpdateGuestBookings(Collection<GuestBooking> guestBookings) {
        for (GuestBooking guestBooking:guestBookings) {
    //        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(guestBooking);
            realm.commitTransaction();
            realm.close();
        }
    }

    public void deleteGuestBookingById(long guestBookingId) {
        realm.where(GuestBooking.class).equalTo("id", guestBookingId).findAll().deleteAllFromRealm();
    }

    public void deleteAllGuestBookings() {
//        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        realm.beginTransaction();
        realm.delete(GuestBooking.class);
        realm.commitTransaction();
        realm.close();
    }

    public List<GuestBooking> getGuestBookingsOfGuest(long guestId) {
//        if (realm.isClosed()){
            realm = Realm.getDefaultInstance();
//        }
        List<GuestBooking> guestBookings = realm.where(GuestBooking.class).equalTo("guestId", guestId).findAll();
        guestBookings = guestBookings != null ? realm.copyFromRealm(guestBookings) : null;
        realm.close();
        return guestBookings;
//        return realm.where(GuestBooking.class).equalTo("guestId", guestId).findAll();
    }

}
