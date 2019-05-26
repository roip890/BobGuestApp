package com.bob.bobguestapp.activities.codes.codeslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.codes.codeslist.comperators.GuestCodeCodeComparator;
import com.bob.bobguestapp.activities.codes.codeslist.comperators.GuestCodeStatusComparator;
import com.bob.bobguestapp.activities.codes.codeslist.comperators.GuestCodeTitleComparator;
import com.bob.bobguestapp.activities.codes.codeslist.filter.GuestCodesFilter;
import com.bob.bobguestapp.tools.database.MyRealmController;
import com.bob.toolsmodule.database.objects.GuestCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class CodesListAdapter extends RecyclerView.Adapter<CodesListViewHolder> {

    private static final int CODE_VIEW_TYPE = 0;

    private Context context;
    private List<GuestCode> allCodes;
    private List<GuestCode> codesToShow;
    private GuestCodesFilter guestCodesFilter;
	private String sortingType;
	private boolean ascending;
    private static final HashMap<String, Comparator<GuestCode>> guestCodesComparators;
    static {
        guestCodesComparators = new HashMap<String, Comparator<GuestCode>>();
        guestCodesComparators.put("status", new GuestCodeStatusComparator());
        guestCodesComparators.put("title", new GuestCodeCodeComparator());
        guestCodesComparators.put("code", new GuestCodeTitleComparator());
    }


    public CodesListAdapter(Context context, List<GuestCode> items) {
		this.context = context;
		if (items != null) {
            this.allCodes = items;
        } else {
		    this.allCodes = new ArrayList<GuestCode>();
        }
        this.codesToShow = new ArrayList<GuestCode>();
		this.codesToShow.addAll(this.allCodes);
        this.guestCodesFilter = null;
		this.sortingType = "date";
		this.ascending = true;
	}

    public CodesListAdapter(Context context) {
        this.context = context;
        this.allCodes = new ArrayList<GuestCode>();
        this.codesToShow = new ArrayList<GuestCode>();
    }

    @Override
    public int getItemViewType(int position) {
        return CODE_VIEW_TYPE;
    }

    @Override
    public CodesListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new CodesListViewHolder(context, inflater.inflate(R.layout.list_item_guest_code, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(CodesListViewHolder viewHolder, int position) {

        GuestCode code = this.codesToShow.get(position);
        viewHolder.configureGuestCode(code);
    }
    
    @Override
    public int getItemCount() {
        return codesToShow.size();
	}

    public void setCodes(List<GuestCode> codes) {
        if (this.allCodes != null) {
            this.allCodes.clear();
        } else {
            this.allCodes = new ArrayList<GuestCode>();
        }
        this.allCodes.addAll(codes);
        this.setCodesToShow(this.allCodes);
        this.filterCodes(this.guestCodesFilter);
        this.sortCodes(this.sortingType, this.ascending);
    }

    public void setCodesToShow(List<GuestCode> codes) {
        if (this.codesToShow != null) {
            this.codesToShow.clear();
        } else {
            this.codesToShow = new ArrayList<GuestCode>();
        }
        this.codesToShow.addAll(codes);
        this.notifyDataSetChanged();
    }

    public void setCodesList() {
        long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1L);
        this.setCodes(MyRealmController.get().with(BOBGuestApplication.get()).getGuestCodesOfGuest(guestId));
    }

    public void filterCodes(GuestCodesFilter guestCodesFilter) {
        this.guestCodesFilter = guestCodesFilter;
        if (this.guestCodesFilter != null) {
            this.setCodesToShow(this.guestCodesFilter.getFilteredGuestCodesList(this.allCodes));
        }
        this.notifyDataSetChanged();
    }

    public void sortCodes(String sortingType, boolean ascending) {
        if (CodesListAdapter.guestCodesComparators.containsKey(sortingType)) {
            this.sortingType = sortingType;
        } else {
            this.sortingType = "date";
        }
        this.ascending = ascending;
        Comparator<GuestCode> guestCodeComparator = CodesListAdapter.guestCodesComparators.get(sortingType);
        if (guestCodeComparator != null) {
            Collections.sort(this.codesToShow, guestCodeComparator);
            if (!this.ascending) {
                Collections.reverse(this.codesToShow);
            }
        }
        this.notifyDataSetChanged();
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
        this.sortCodes(this.sortingType, this.ascending);
    }

    public GuestCodesFilter getGuestCodesFilter() {
        return this.guestCodesFilter;
    }


}
