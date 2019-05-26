package com.bob.bobguestapp.activities.main.fragments.requests.requestslist.requestdetails;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.tools.myFinals;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.database.objects.GuestRequest;
import com.bob.toolsmodule.database.objects.RequestItem;
import com.bob.uimodule.UIModuleManager;
import com.bob.uimodule.menu.DynamicFormView;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.views.MyView;
import com.bob.uimodule.views.loadingcontainer.ManagementLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;

import static android.widget.RelativeLayout.BELOW;

public class RequestDetailsDialogView extends ManagementLayout {

    //main view screen states
    public static final int REQUEST_DETAILS = 10;

    //views
    private RelativeLayout requestDetailsDialogLayout;
    private LinearLayout requestDetailsStatusLayout;
    private TextView requestDetailsTitleTextView;
    private View requestDetailsStatusView;
    private RequestDetailsForm requestDetailsForm;
    private GuestRequest guestRequest;


    //constructors
    public RequestDetailsDialogView(Context context) {
        this(context, null);
    }

    public RequestDetailsDialogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RequestDetailsDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin= MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.REQUEST_LIST_ITEM_REQUEST_DETAILS_DIALOG_SKIN);

    }

    @Override
    protected View onCreateMainView() {

        View view = this.initRequestDetailsDialogView();

        this.setScreenState(REQUEST_DETAILS);

        return view;

    }

    @Override
    protected void setMainViewScreenState(int screenState) {


        this.requestDetailsDialogLayout.setVisibility(INVISIBLE);

        switch (screenState) {
            case REQUEST_DETAILS:
                this.requestDetailsDialogLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //requests list dialog view
    private View initRequestDetailsDialogView() {

        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View view = inflater.inflate(R.layout.dialog_request_details, new RelativeLayout(this.getContext()));
        this.requestDetailsDialogLayout = (RelativeLayout) view.findViewById(R.id.request_details_dialog_layout);

        //init title view
        this.initRequestTitleTextView(view);

        //init status view
        this.initRequestStatusView(view);

        //init form
        this.initRequestDetailsForm();

        //add form
        this.addRequestDetailsForm();

        return view;

    }

    private void initRequestTitleTextView(View view) {

        this.requestDetailsTitleTextView = (TextView) view.findViewById(R.id.request_details_dialog_title_text_view);

        this.updateRequestTitleTextView();

    }

    private void updateRequestTitleTextView() {

        if (this.guestRequest != null
                && this.guestRequest.getTitle() != null) {

            this.requestDetailsTitleTextView.setTextColor(MyAppThemeUtilsManager.get().getColor(
                    MyAppThemeUtilsManager.DEFAULT_BASE_TEXT_COLOR, this.screenSkin
            ));

            this.requestDetailsTitleTextView.setText(this.guestRequest.getTitle());

            this.requestDetailsTitleTextView.setTypeface(this.requestDetailsTitleTextView.getTypeface(), Typeface.BOLD);

        }

    }

    private void initRequestStatusView(View view) {

        this.requestDetailsStatusView = view.findViewById(R.id.request_details_dialog_status_view);

        this.updateRequestStatusView();

    }

    private void updateRequestStatusView() {

        ViewCompat.setElevation(this.requestDetailsStatusView, 20f);

        int statusColor = R.color.waiting_status_color;
        if (this.guestRequest != null
                && this.guestRequest.getStatus() != null
                && myFinals.requestStatusColors.get(this.guestRequest.getStatus().toLowerCase()) != null) {

            statusColor = myFinals.requestStatusColors.get(this.guestRequest.getStatus().toLowerCase());

        }

        GradientDrawable greenGradientDrawable = new GradientDrawable();
        greenGradientDrawable.setShape(GradientDrawable.OVAL);
        greenGradientDrawable.setColor(ContextCompat.getColor(this.getContext(), statusColor));

        this.requestDetailsStatusView.setBackground(greenGradientDrawable);

    }

    private void initRequestDetailsForm() {

        this.requestDetailsForm = new RequestDetailsForm(this.getContext());

    }

    private void addRequestDetailsForm() {

        //layout params
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        layoutParams.addRule(BELOW, R.id.request_details_dialog_title_text_view);

        //add form
        this.requestDetailsDialogLayout.addView(this.requestDetailsForm, layoutParams);

    }

    //set guest request
    public void setGuestRequest(GuestRequest guestRequest) {

        this.guestRequest = guestRequest;

        if (this.guestRequest != null) {

            if (this.guestRequest.getRequestItems() != null) {

                this.requestDetailsForm.setFormItems(this.guestRequest.getRequestItems());

            }

            if (this.guestRequest.getStatus() != null) {

                this.updateRequestStatusView();

            }

            if (this.guestRequest.getTitle() != null) {

                this.updateRequestTitleTextView();

            }

        }

    }

    public class RequestDetailsForm extends DynamicFormView<RequestItem, Map.Entry<String, String> , GuestRequest, RequestItem> {


        public RequestDetailsForm(Context context) {
            super(context);
        }

        @Override
        protected int getTheme() {
            return UIModuleManager.get().getSharedPreferencesManager().getSecureSharedPreferences().getInt("appTheme", ThemeUtilsManager.DEFAULT_THEME);
        }

        @Override
        protected int getSkin() {
            return ThemeUtilsManager.get(this.getContext()).getSkin(this.getTheme(), MyAppThemeUtilsManager.REQUEST_LIST_ITEM_REQUEST_DETAILS_DIALOG_SKIN);
        }

        @Override
        protected int getDialogSkin() {
            return ThemeUtilsManager.get(this.getContext()).getSkin(this.getTheme(), MyAppThemeUtilsManager.REQUEST_LIST_ITEM_REQUEST_DETAILS_DIALOG_SKIN);
        }

        @Override
        protected void onStartLoading() {
            RequestDetailsDialogView.this.setScreenState(LOADING);
        }

        @Override
        protected void onFinishLoading() {
            RequestDetailsDialogView.this.setScreenState(REQUEST_DETAILS);
        }

        @Override
        protected void onError(String error) {
            RequestDetailsDialogView.this.setMessage(error);
        }

        @Override
        protected void initIsSubmittable() {

            this.isSubmittable = this.formItemsAdapter instanceof RequestDetailsFromAdapter
                    && ((RequestDetailsFromAdapter) this.formItemsAdapter).getMode() == RequestDetailsFromAdapter.READ_WRITE_MODE;

        }

        @Override
        protected void initFormItemsAdapter() {

            this.formItemsAdapter = new RequestDetailsFromAdapter(RequestDetailsDialogView.this.getContext());

        }

        @Override
        protected void initFormRequest() {

            this.formRequest = new GuestRequest();

        }

        @Override
        protected void fillFormRequest(GuestRequest formRequest) {

            RealmList<RequestItem> requestItems = new RealmList<RequestItem>();
            requestItems.addAll(this.getRequestItems());
            this.formRequest.setRequestItems(requestItems);

        }

        @Override
        protected ArrayList<RequestItem> generateRequestItem(ArrayList<MyView> formItemsViews) {

            ArrayList<RequestItem> requestItems = new ArrayList<RequestItem>();

            if (formItemsViews != null) {

                for (MyView myView : formItemsViews) {

                    RequestItem requestItem = new RequestItem();

                    requestItem.setKey(myView.getKey());
                    requestItem.setValue(myView.getValue());
                    requestItem.setOrder(myView.getViewOrder());
                    requestItem.setType(myFinals.viewsCodes.get(myView.getViewCodeType()));

                    requestItems.add(requestItem);
                }

            }

            return requestItems;

        }


        @Override
        protected List<Map.Entry<String, String>> getPropertiesOfFormItem(RequestItem formItem) {

            return new ArrayList<Map.Entry<String, String>>();

        }

        @Override
        public void onSubmitButtonClick() {

            //update request

        }
    }


}
