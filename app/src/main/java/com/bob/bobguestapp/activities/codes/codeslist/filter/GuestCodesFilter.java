package com.bob.bobguestapp.activities.codes.codeslist.filter;

import com.bob.toolsmodule.database.objects.GuestCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 14/09/2018.
 */

public class GuestCodesFilter {

    private ArrayList<String> statuses;
    private String title;
    private String code;
    private boolean filterByStatus;
    private boolean filterByTitle;
    private boolean filterByCode;

    public GuestCodesFilter() {
        this.statuses = null;
        this.title = null;
        this.code = null;
        this.filterByStatus = false;
        this.filterByTitle = false;
        this.filterByCode = false;
    }

    public void filterByCode(boolean filterByCode) {
        this.filterByCode = filterByCode;
    }

    public void filterByStatus(boolean filterByStatus) {
        this.filterByStatus = filterByStatus;
    }

    public void filterByTitle(boolean filterByTitle) {
        this.filterByTitle = filterByTitle;
    }

    public ArrayList<GuestCode> getFilteredGuestCodesList(List<GuestCode> guestCodesList) {
        ArrayList<GuestCode> filteredGuestCodeArrayList = new ArrayList<GuestCode>();
        if (guestCodesList != null) {
            for (GuestCode guestCode: guestCodesList) {
                if (this.filterByStatus) {
                    if (this.statuses != null && !this.statuses.contains(guestCode.getStatus().toLowerCase())) {
                        continue;
                    }
                }
                if (this.filterByTitle) {
                    if (this.title != null && !guestCode.getTitle().equals(this.title)) {
                        continue;
                    }
                }
                if (this.filterByCode) {
                    if (this.code != null && !guestCode.getCode().equals(this.code)) {
                        continue;
                    }
                }
                filteredGuestCodeArrayList.add(guestCode);
            }
        }
        return filteredGuestCodeArrayList;
    }


    public ArrayList<String> getStatuses() {
        return this.statuses;
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
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isFilterByStatus() {
        return filterByStatus;
    }

    public boolean isFilterByTitle() {
        return filterByTitle;
    }

    public boolean isFilterByCode() {
        return filterByCode;
    }
}
