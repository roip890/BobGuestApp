package com.bob.bobguestapp.activities.main.fragments.requests.requestslist.filter;


import com.bob.bobguestapp.tools.recyclerview.ListFilter;
import com.bob.toolsmodule.database.objects.GuestRequest;
import com.bob.bobguestapp.tools.myFinals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 14/09/2018.
 */

public class GuestRequestsFilter extends ListFilter<GuestRequest> {

    private Date minDate;
    private Date maxDate;
    private ArrayList<String> statuses;
    private String title;
    private String type;
    private boolean filterByMinDate;
    private boolean filterByMaxDate;
    private boolean filterByStatus;
    private boolean filterByTitle;
    private boolean filterByType;

    public GuestRequestsFilter() {
        this.minDate = null;
        this.maxDate = null;
        this.statuses = null;
        this.title = null;
        this.type = null;
        this.filterByMinDate = false;
        this.filterByMaxDate = false;
        this.filterByStatus = false;
        this.filterByTitle = false;
        this.filterByType = false;
    }

    public void filterByMinDate(boolean filterByMinDate) {
        this.filterByMinDate = filterByMinDate;
    }

    public void filterByMaxDate(boolean filterByMaxDate) {
        this.filterByMaxDate = filterByMaxDate;
    }

    public void filterByStatus(boolean filterByStatus) {
        this.filterByStatus = filterByStatus;
    }

    public void filterByTitle(boolean filterByTitle) {
        this.filterByTitle = filterByTitle;
    }

    public void filterByType(boolean filterByType) {
        this.filterByType = filterByType;
    }

    public ArrayList<GuestRequest> getFilteredList(List<GuestRequest> guestRequestList) {
        ArrayList<GuestRequest> filteredGuestRequestsList = new ArrayList<GuestRequest>();
        if (guestRequestList != null) {
            for (GuestRequest guestRequest: guestRequestList) {
                if (this.filterByMinDate) {
                    Date guestDate =  Timestamp.valueOf(guestRequest.getTimeStamp());
                    if (this.minDate != null && guestDate.before(this.minDate)) {
                        continue;
                    }
                }
                if (this.filterByMaxDate) {
                    Date guestDate = Timestamp.valueOf(guestRequest.getTimeStamp());
                    if (this.maxDate!= null && guestDate.after(this.maxDate)) {
                        continue;
                    }
                }
                if (this.filterByStatus) {
                    if (this.statuses != null && !this.statuses.contains(myFinals.requestStatusStrings.get(guestRequest.getStatus().toLowerCase()))) {
                        continue;
                    }
                }
                if (this.filterByTitle) {
                    if (this.title != null && !guestRequest.getTitle().toLowerCase().contains(this.title.toLowerCase())) {
                        continue;
                    }
                }
                filteredGuestRequestsList.add(guestRequest);
            }
        }
        return filteredGuestRequestsList;
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

    public ArrayList<String> getStatuses() {
        return statuses;
    }

    public void setStatus(String status) {
        ArrayList<String> statuses = new ArrayList<String>();
        statuses.add(status);
        this.statuses.add(status);
    }

    public void setStatuses(ArrayList<String> statuses) {
        this.statuses = statuses;
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

    public boolean isFilterByStatus() {
        return filterByStatus;
    }

    public boolean isFilterByTitle() {
        return filterByTitle;
    }

    public boolean isFilterByType() {
        return filterByType;
    }
}
