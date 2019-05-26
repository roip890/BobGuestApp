package com.bob.bobguestapp.activities.main.fragments.requests.requestsfilter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.requests.RequestsFragment;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.RequestsListAdapter;
import com.bob.bobguestapp.activities.main.fragments.requests.requestslist.filter.GuestRequestsFilter;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.database.objects.GuestRequest;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementLayout;
import com.bob.uimodule.views.textviews.MyButton;
import com.bob.uimodule.views.textviews.MyEditText;
import com.bob.uimodule.views.textviews.dialogviews.MyDateTextView;
import com.bob.uimodule.views.textviews.dialogviews.MyTextViewMultiChoiceDialog;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RequestsFilterDialogView extends ManagementLayout {

    //main view screen states
    public static final int REQUESTS_LIST_FILTER = 10;

    //views
    private RelativeLayout requestsFilterDialogLayout;
    private MyDateTextView requestsFilterDialogMinDateFilterView, requestsFilterDialogMaxDateFilterView;
    private MyTextViewMultiChoiceDialog requestsFilterDialogStatusFilterView;
    private MyEditText requestsFilterDialogTitleFilterView;
    private AppCompatCheckBox requestsFilterDialogMinDateFilterCheckBox, requestsFilterDialogMaxDateFilterCheckBox,
            requestsFilterDialogStatusFilterCheckBox, requestsFilterDialogTitleFilterCheckBox;
    private MyButton requestsFilterDialogSubmitButton;

    //guest requests filter
    private GuestRequestsFilter guestRequestsFilter;

    //request filter button listener
    private OnRequestFilterButtonClickListener onRequestFilterButtonClickListener;


    //constructors
    public RequestsFilterDialogView(Context context) {
        this(context, null);
    }

    public RequestsFilterDialogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RequestsFilterDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.setScreenState(REQUESTS_LIST_FILTER);
    }


    @Override
    protected View onCreateMainView() {

        return this.initFilterDialogView();

    }

    @Override
    protected void setMainViewScreenState(int screenState) {


        this.requestsFilterDialogLayout.setVisibility(INVISIBLE);

        switch (screenState) {
            case REQUESTS_LIST_FILTER:
                this.requestsFilterDialogLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //request filter button listener
    public void setOnRequestFilterButtonClickListener(OnRequestFilterButtonClickListener onRequestFilterButtonClickListener) {

        this.onRequestFilterButtonClickListener = onRequestFilterButtonClickListener;

    }

    //guest requests filter
    public void setGuestRequestsFilter(GuestRequestsFilter guestRequestsFilter) {

        this.guestRequestsFilter = guestRequestsFilter;

        this.updateGuestRequestsFilter();

    }

    private void updateGuestRequestsFilter() {

        //min date filter
        this.updateFilterDialogViewMinDateFilter();

        //max date filter
        this.updateFilterDialogViewMaxDateFilter();

        //status filter
        this.updateFilterDialogViewStatusFilter();

        //title filter
        this.updateFilterDialogViewTitleFilter();

    }

    private void updateFilterDialogViewMinDateFilter() {

        Date minDate = null;
        if (this.guestRequestsFilter != null) {
            if (this.guestRequestsFilter.isFilterByMinDate()) {
                this.requestsFilterDialogMinDateFilterCheckBox.setChecked(true);
            } else {
                this.requestsFilterDialogMinDateFilterCheckBox.setChecked(false);
            }
            minDate = this.guestRequestsFilter.getMinDate();
        }

        Calendar calendar = Calendar.getInstance();
        if (minDate != null) {
            calendar.setTime(minDate);
        }
        this.requestsFilterDialogMinDateFilterView.setDays(calendar.get(Calendar.DAY_OF_MONTH));
        this.requestsFilterDialogMinDateFilterView.setMonths(calendar.get(Calendar.MONTH) + 1);
        this.requestsFilterDialogMinDateFilterView.setYears(calendar.get(Calendar.YEAR));

    }

    private void updateFilterDialogViewMaxDateFilter() {

        Date maxDate = null;
        if (this.guestRequestsFilter != null) {
            if (this.guestRequestsFilter.isFilterByMaxDate()) {
                this.requestsFilterDialogMaxDateFilterCheckBox.setChecked(true);
            } else {
                this.requestsFilterDialogMaxDateFilterCheckBox.setChecked(false);
            }
            maxDate = this.guestRequestsFilter.getMaxDate();
        }

        Calendar calendar = Calendar.getInstance();
        if (maxDate != null) {
            calendar.setTime(maxDate);
        }
        this.requestsFilterDialogMaxDateFilterView.setDays(calendar.get(Calendar.DAY_OF_MONTH));
        this.requestsFilterDialogMaxDateFilterView.setMonths(calendar.get(Calendar.MONTH) + 1);
        this.requestsFilterDialogMaxDateFilterView.setYears(calendar.get(Calendar.YEAR));

    }

    private void updateFilterDialogViewStatusFilter() {

        ArrayList<String> statuses = new ArrayList<String>();
        statuses.add("Waiting");
        statuses.add("In Progress");
        statuses.add("Done");
        ArrayList<Integer> selectedStatuses = new ArrayList<Integer>();
        if (this.guestRequestsFilter != null) {
            if (this.guestRequestsFilter .isFilterByStatus()) {
                this.requestsFilterDialogStatusFilterCheckBox.setChecked(true);
            } else {
                this.requestsFilterDialogStatusFilterCheckBox.setChecked(false);
            }
            if (this.guestRequestsFilter .getStatuses() != null) {
                ArrayList<String> filterStatuses = guestRequestsFilter.getStatuses();
                for (String status: filterStatuses) {
                    selectedStatuses.add(statuses.indexOf(status));
                }
            }
        }

        this.requestsFilterDialogStatusFilterView.setItems(statuses);
        this.requestsFilterDialogStatusFilterView.setSelectedItems(selectedStatuses);


    }

    private void updateFilterDialogViewTitleFilter() {


        String title = "";
        if (this.guestRequestsFilter != null) {
            if (this.guestRequestsFilter.isFilterByTitle()) {
                this.requestsFilterDialogTitleFilterCheckBox.setChecked(true);
            } else {
                this.requestsFilterDialogTitleFilterCheckBox.setChecked(false);
            }
            title = this.guestRequestsFilter.getTitle();
        }

        this.requestsFilterDialogTitleFilterView.setText(title);

    }



    //requests list dialog view
    private View initFilterDialogView() {

        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View view = inflater.inflate(R.layout.dialog_filter_requests, new RelativeLayout(this.getContext()));
        this.requestsFilterDialogLayout = (RelativeLayout) view.findViewById(R.id.requests_filter_dialog_layout);

        //min date field
        this.initFilterDialogViewMinDateField(view);

        //max date field
        this.initFilterDialogViewMaxDateField(view);

        //status field
        this.initFilterDialogViewStatusField(view);

        //title field
        this.initFilterDialogViewTitleField(view);

        //submit button
        this.initFilterDialogViewSubmitButton(view);

        return view;
    }

    private void initFilterDialogViewMinDateField(View view) {

        //check box
        this.initFilterDialogViewMinDateFieldCheckBox(view);

        //date text view
        this.initFilterDialogViewMinDateFieldDateTextView(view);

    }

    private void initFilterDialogViewMinDateFieldCheckBox(View view) {

        this.requestsFilterDialogMinDateFilterCheckBox = (AppCompatCheckBox) view.findViewById(R.id.requests_filter_dialog_min_date_filter_check_box);
        if (Build.VERSION.SDK_INT < 21) {
            CompoundButtonCompat.setButtonTintList(this.requestsFilterDialogMinDateFilterCheckBox, ColorStateList.valueOf(
                    MyAppThemeUtilsManager.get().getColor(ThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
            ));
        } else {
            this.requestsFilterDialogMinDateFilterCheckBox.setButtonTintList(ColorStateList.valueOf(
                    MyAppThemeUtilsManager.get().getColor(ThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
            ));
        }

    }

    private void initFilterDialogViewMinDateFieldDateTextView(View view) {

        this.requestsFilterDialogMinDateFilterView = (MyDateTextView) view.findViewById(R.id.requests_filter_dialog_min_date_filter_date_text_view);

        MyAppThemeUtilsManager.get(this.getContext()).initMyDateTextView(this.requestsFilterDialogMinDateFilterView, "Min Date",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"date_range"), null, this.getContext(), this.screenSkin);

        this.requestsFilterDialogMinDateFilterView.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.requestsFilterDialogMinDateFilterView.setWidth(UIUtilsManager.get().convertDpToPixels(this.getContext(), 220));

        this.requestsFilterDialogMinDateFilterView.setDialogSkin(MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN);

    }

    private void initFilterDialogViewMaxDateField(View view) {

        //check box
        this.initFilterDialogViewMaxDateFieldCheckBox(view);

        //date text view
        this.initFilterDialogViewMaxDateFieldDateTextView(view);

    }

    private void initFilterDialogViewMaxDateFieldCheckBox(View view) {

        this.requestsFilterDialogMaxDateFilterCheckBox = (AppCompatCheckBox) view.findViewById(R.id.requests_filter_dialog_max_date_filter_check_box);

        if (Build.VERSION.SDK_INT < 21) {
            CompoundButtonCompat.setButtonTintList(this.requestsFilterDialogMaxDateFilterCheckBox, ColorStateList.valueOf(
                    MyAppThemeUtilsManager.get().getColor(ThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
            ));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
        } else {
            this.requestsFilterDialogMaxDateFilterCheckBox.setButtonTintList(ColorStateList.valueOf(
                    MyAppThemeUtilsManager.get().getColor(ThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
            ));//setButtonTintList is accessible directly on API>19
        }

    }

    private void initFilterDialogViewMaxDateFieldDateTextView(View view) {

        this.requestsFilterDialogMaxDateFilterView = (MyDateTextView) view.findViewById(R.id.requests_filter_dialog_max_date_filter_date_text_view);

        MyAppThemeUtilsManager.get(this.getContext()).initMyDateTextView(this.requestsFilterDialogMaxDateFilterView, "Max Date",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"date_range"), null, this.getContext(), this.screenSkin);

        this.requestsFilterDialogMaxDateFilterView.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.requestsFilterDialogMaxDateFilterView.setWidth(UIUtilsManager.get().convertDpToPixels(this.getContext(), 220));

        this.requestsFilterDialogMaxDateFilterView.setDialogSkin(MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN);

    }

    private void initFilterDialogViewStatusField(View view) {

        //check box
        this.initFilterDialogViewStatusFieldCheckBox(view);

        //date text view
        this.initFilterDialogViewStatusFieldMultiChoiceDialogView(view);

    }

    private void initFilterDialogViewStatusFieldCheckBox(View view) {

        this.requestsFilterDialogStatusFilterCheckBox= (AppCompatCheckBox) view.findViewById(R.id.requests_filter_dialog_status_picker_filter_check_box);

        if (Build.VERSION.SDK_INT < 21) {
            CompoundButtonCompat.setButtonTintList(this.requestsFilterDialogStatusFilterCheckBox, ColorStateList.valueOf(
                    MyAppThemeUtilsManager.get().getColor(ThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
            ));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
        } else {
            this.requestsFilterDialogStatusFilterCheckBox.setButtonTintList(ColorStateList.valueOf(
                    MyAppThemeUtilsManager.get().getColor(ThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
            ));//setButtonTintList is accessible directly on API>19
        }

    }

    private void initFilterDialogViewStatusFieldMultiChoiceDialogView(View view) {

        this.requestsFilterDialogStatusFilterView = (MyTextViewMultiChoiceDialog) view.findViewById(R.id.requests_filter_dialog_status_picker_filter_multi_choice_dialog_view);

        MyAppThemeUtilsManager.get(this.getContext()).initMyMultiChoiceTextView(this.requestsFilterDialogStatusFilterView, "Status",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"assignment_turned_in"), new ArrayList<String>(), new ArrayList<Integer>(), this.getContext(), this.screenSkin);

        this.requestsFilterDialogStatusFilterView.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.requestsFilterDialogStatusFilterView.setWidth(UIUtilsManager.get().convertDpToPixels(this.getContext(), 220));

        this.requestsFilterDialogStatusFilterView.setDialogSkin(MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN);

        this.requestsFilterDialogStatusFilterView.setItemsWidgetColor(MyAppThemeUtilsManager.get().getColor(ThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN));

        this.requestsFilterDialogStatusFilterView.setItemsGravity(GravityEnum.START);

    }

    private void initFilterDialogViewTitleField(View view) {

        //check box
        this.initFilterDialogViewTitleFieldCheckBox(view);

        //date text view
        this.initFilterDialogViewTitleFieldMultiChoiceDialogView(view);

    }

    private void initFilterDialogViewTitleFieldCheckBox(View view) {

        this.requestsFilterDialogTitleFilterCheckBox = (AppCompatCheckBox) view.findViewById(R.id.requests_filter_dialog_title_filter_check_box);

        if (Build.VERSION.SDK_INT < 21) {
            CompoundButtonCompat.setButtonTintList(this.requestsFilterDialogTitleFilterCheckBox, ColorStateList.valueOf(
                    MyAppThemeUtilsManager.get().getColor(ThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
            ));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
        } else {
            this.requestsFilterDialogTitleFilterCheckBox.setButtonTintList(ColorStateList.valueOf(
                    MyAppThemeUtilsManager.get().getColor(ThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
            ));//setButtonTintList is accessible directly on API>19
        }

    }

    private void initFilterDialogViewTitleFieldMultiChoiceDialogView(View view) {

        this.requestsFilterDialogTitleFilterView = (MyEditText) view.findViewById(R.id.requests_filter_dialog_title_filter_edit_text);


        MyAppThemeUtilsManager.get(this.getContext()).initMyTextView(this.requestsFilterDialogTitleFilterView, "Title",
                (IconicsDrawable) Icons.get().findDrawable(this.getContext(),"title"), this.getContext(), this.screenSkin);

        this.requestsFilterDialogTitleFilterView.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.requestsFilterDialogTitleFilterView.setWidth(UIUtilsManager.get().convertDpToPixels(this.getContext(), 220));

        this.requestsFilterDialogTitleFilterView.setBottomLineColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

    }

    private void initFilterDialogViewSubmitButton(View view) {

        this.requestsFilterDialogSubmitButton = (MyButton) view.findViewById(R.id.requests_filter_submit_button);

        MyAppThemeUtilsManager.get(this.getContext()).initMyButton(this.requestsFilterDialogSubmitButton, "Filter", this.getContext(), this.screenSkin);

        this.requestsFilterDialogSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (RequestsFilterDialogView.this.guestRequestsFilter == null) {

                    RequestsFilterDialogView.this.guestRequestsFilter = new GuestRequestsFilter();

                }

                String minDate = RequestsFilterDialogView.this.requestsFilterDialogMinDateFilterView.getText().toString();
                if (minDate != null && !minDate.equals("") && RequestsFilterDialogView.this.requestsFilterDialogMinDateFilterCheckBox.isChecked()) {
                    RequestsFilterDialogView.this.guestRequestsFilter.setMinDate(RequestsFilterDialogView.this.requestsFilterDialogMinDateFilterView.getDate());
                    RequestsFilterDialogView.this.guestRequestsFilter.filterByMinDate(true);
                } else {
                    RequestsFilterDialogView.this.guestRequestsFilter.filterByMinDate(false);
                }

                String maxDate = RequestsFilterDialogView.this.requestsFilterDialogMaxDateFilterView.getText().toString();
                if (maxDate != null && !maxDate.equals("") && RequestsFilterDialogView.this.requestsFilterDialogMaxDateFilterCheckBox.isChecked()) {
                    RequestsFilterDialogView.this.guestRequestsFilter.setMaxDate(RequestsFilterDialogView.this.requestsFilterDialogMaxDateFilterView.getDate());
                    RequestsFilterDialogView.this.guestRequestsFilter.filterByMaxDate(true);
                } else {
                    RequestsFilterDialogView.this.guestRequestsFilter.filterByMaxDate(false);
                }

                String status = RequestsFilterDialogView.this.requestsFilterDialogStatusFilterView.getText().toString();
                if (status != null && !status.equals("") && RequestsFilterDialogView.this.requestsFilterDialogStatusFilterCheckBox.isChecked()) {
                    RequestsFilterDialogView.this.guestRequestsFilter.setStatuses(RequestsFilterDialogView.this.requestsFilterDialogStatusFilterView.getSelectedItems());
                    RequestsFilterDialogView.this.guestRequestsFilter.filterByStatus(true);
                } else {
                    RequestsFilterDialogView.this.guestRequestsFilter.filterByStatus(false);
                }

                String title = RequestsFilterDialogView.this.requestsFilterDialogTitleFilterView.getText().toString();
                if (title != null && !title.equals("") && RequestsFilterDialogView.this.requestsFilterDialogTitleFilterCheckBox.isChecked()) {
                    RequestsFilterDialogView.this.guestRequestsFilter.setTitle(title);
                    RequestsFilterDialogView.this.guestRequestsFilter.filterByTitle(true);
                } else {
                    RequestsFilterDialogView.this.guestRequestsFilter.filterByTitle(false);
                }

                RequestsFilterDialogView.this.onRequestFilterButtonClickListener.onRequestFilterButtonClick(RequestsFilterDialogView.this.guestRequestsFilter);

            }
        });

    }

}
