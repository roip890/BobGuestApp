package com.bob.bobguestapp.activities.main.fragments.notifications.notificaitonslist.filter;

import com.bob.bobguestapp.tools.database.objects.GuestAppNotification;
import com.bob.bobguestapp.tools.recyclerview.ListFilter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 14/09/2018.
 */

public class AppNotificationsFilter  extends ListFilter<GuestAppNotification> {

    private Date minDate;
    private Date maxDate;
    private String title;
    private String type;
    private boolean filterByMinDate;
    private boolean filterByMaxDate;
    private boolean filterByTitle;
    private boolean filterByType;

    public AppNotificationsFilter() {
        this.minDate = null;
        this.maxDate = null;
        this.title = null;
        this.type = null;
        this.filterByMinDate = false;
        this.filterByMaxDate = false;
        this.filterByTitle = false;
        this.filterByType = false;
    }

    public void filterByMinDate(boolean filterByMinDate) {
        this.filterByMinDate = filterByMinDate;
    }

    public void filterByMaxDate(boolean filterByMaxDate) {
        this.filterByMaxDate = filterByMaxDate;
    }


    public void filterByTitle(boolean filterByTitle) {
        this.filterByTitle = filterByTitle;
    }

    public void filterByType(boolean filterByType) {
        this.filterByType = filterByType;
    }

    @Override
    public ArrayList<GuestAppNotification> getFilteredList(List<GuestAppNotification> appNotificationList) {
        ArrayList<GuestAppNotification> filteredAppNotificationList = new ArrayList<GuestAppNotification>();
        if (appNotificationList != null) {
            for (GuestAppNotification appNotification: appNotificationList) {
                if (this.filterByMinDate) {
                    Date appNotificationDate =  Timestamp.valueOf(appNotification.getTimeStamp());
                    if (this.minDate != null && appNotificationDate.before(this.minDate)) {
                        continue;
                    }
                }
                if (this.filterByMaxDate) {
                    Date appNotificationDate = Timestamp.valueOf(appNotification.getTimeStamp());
                    if (this.maxDate!= null && appNotificationDate.after(this.maxDate)) {
                        continue;
                    }
                }
                if (this.filterByTitle) {
                    if (this.title != null && !appNotification.getContent().equals(this.title)) {
                        continue;
                    }
                }
                filteredAppNotificationList.add(appNotification);
            }
        }
        return filteredAppNotificationList;
    }


    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isFilterByMinDate() {
        return filterByMinDate;
    }

    public boolean isFilterByMaxDate() {
        return filterByMaxDate;
    }


    public boolean isFilterByTitle() {
        return filterByTitle;
    }

    public boolean isFilterByType() {
        return filterByType;
    }

}
