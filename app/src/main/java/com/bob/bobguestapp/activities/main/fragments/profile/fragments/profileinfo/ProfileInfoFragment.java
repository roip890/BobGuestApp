package com.bob.bobguestapp.activities.main.fragments.profile.fragments.profileinfo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.profile.ProfileFragment;
import com.bob.bobguestapp.activities.main.fragments.profile.changeprofilepicture.ChangeProfilePictureBottomSheetDialogFragment;
import com.bob.bobguestapp.activities.main.fragments.profile.connector.ProfileConnector;
import com.bob.bobguestapp.activities.main.fragments.profile.fragments.profileinfo.profileinfoform.ProfileInfo;
import com.bob.bobguestapp.activities.main.fragments.profile.fragments.profileinfo.profileinfoform.ProfileInfoFromAdapter;
import com.bob.bobguestapp.activities.main.fragments.profile.fragments.profileinfo.profileinfoform.ProfileInfoRequest;
import com.bob.bobguestapp.tools.myFinals;
import com.bob.bobguestapp.tools.session.SessionUtilsManager;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.database.objects.RequestItem;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.uimodule.UIModuleManager;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.menu.DynamicFormView;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.views.MyView;
import com.bob.uimodule.views.loadingcontainer.ManagementFragment;
import com.bob.uimodule.views.textviews.MyPhoneEditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.widget.RelativeLayout.ALIGN_PARENT_TOP;
import static android.widget.RelativeLayout.BELOW;
import static com.bob.bobguestapp.activities.main.fragments.profile.fragments.profileinfo.profileinfoform.ProfileInfoFromAdapter.READ_ONLY_MODE;
import static com.bob.bobguestapp.activities.main.fragments.profile.fragments.profileinfo.profileinfoform.ProfileInfoFromAdapter.READ_WRITE_MODE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.FAILURE_MESSAGE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.LOADING;

public class ProfileInfoFragment extends ManagementFragment {


    private static final int PROFILE_INFO_EDIT_BUTTON_INDEX = 0;

    //profile info fields tag
    private final static int PROFILE_INFO_NAME_FIELD_TAG = 0;
    private final static int PROFILE_INFO_EMAIL_FIELD_TAG = 1;
    private final static int PROFILE_INFO_PHONE_FIELD_TAG = 2;

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //get all requests url
    private static final String GET_ALL_REQUESTS_URL = BOB_SERVER_MOBILE_SERVICES_URL + "/wishes/getAllByGuestAndHotel";

    //main view screen states
    public static final int PROFILE_INFO_LIST = 10;

    //intro commands
    private ProfileConnector profileConnector;

    //requests layout
    private RelativeLayout profileInfoLayout;
//    private NestedScrollView profileInfoFormScrollView;
    private RelativeLayout profileInfoFormContainer;
    private ProfileInfoForm profileInfoForm;
    private FloatingActionButton profileInfoEditButton;

    public ProfileInfoFragment() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.PROFILE_INFO_FRAGMENT_SKIN);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        return view;

    }


    @Override
    public View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

        //init management skin
        this.managementViewContainer.setScreenSkin(this.screenSkin);

        //view
        View view = inflater.inflate(R.layout.fragment_profile_info, container, false);

        //init requests list layouts
        this.initProfileInfoLayout(view);

        this.managementViewContainer.setScreenState(PROFILE_INFO_LIST);

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.profileInfoLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case PROFILE_INFO_LIST:
                this.profileInfoLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //main commands
    public void setProfileConnector(ProfileConnector profileConnector) {
        this.profileConnector = profileConnector;
    }

    //profile info list
    private void initProfileInfoLayout(View view) {

        //profile info main layout
        this.initProfileInfoMainLayout(view);

        //profile info edit button
        this.initProfileInfoEditButton(view);

        //profile info scroll view
        this.initProfileInfoFormScrollView(view);

    }

    private void initProfileInfoMainLayout(View view) {

        this.profileInfoLayout = (RelativeLayout) view.findViewById(R.id.profile_info_fragment_profile_info_layout);

    }

    private void initProfileInfoEditButton(View view) {

        //button
        this.profileInfoEditButton = (FloatingActionButton) view.findViewById(R.id.profile_info_fragment_edit_button);


        this.profileInfoEditButton.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this.getContext(), "faw_edit1")).color(
                        MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
                ).sizeDp(24)
        );

        this.profileInfoEditButton.setBackgroundTintList(ColorStateList.valueOf(
                MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN)
        ));

        this.profileInfoEditButton.setSize(FloatingActionButton.SIZE_MINI);

        ViewCompat.setElevation(this.profileInfoEditButton, 20f);

        this.profileInfoEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileInfoFragment.this.onProfileInfoEditClick();

            }
        });

    }

    private void onProfileInfoEditClick() {

        this.profileInfoForm.setEditMode(!this.profileInfoForm.getEditMode());

        this.setProfileInfoEditButtonColorByEditMode(this.profileInfoForm.getEditMode());

    }

    private void setProfileInfoEditButtonColorByEditMode(boolean formEditMode) {

        if (formEditMode) {

            this.profileInfoEditButton.setImageDrawable(
                    ((IconicsDrawable) Icons.get().findDrawable(this.getContext(), "faw_edit")).color(
                            MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN)
                    ).sizeDp(24)
            );

            this.profileInfoEditButton.setBackgroundTintList(ColorStateList.valueOf(
                    MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
            ));

        } else {

            this.profileInfoEditButton.setImageDrawable(
                    ((IconicsDrawable) Icons.get().findDrawable(this.getContext(), "faw_edit")).color(
                            MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
                    ).sizeDp(24)
            );

            this.profileInfoEditButton.setBackgroundTintList(ColorStateList.valueOf(
                    MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN)
            ));

        }

    }
    private void initProfileInfoFormScrollView(View view) {

        //profile info scroll view
//        this.profileInfoFormScrollView = (NestedScrollView) view.findViewById(R.id.profile_info_fragment_profile_info_form_scroll_view);

        //profile info form container
        this.initProfileInfoFormContainer(view);

    }

    private void initProfileInfoFormContainer(View view) {

        //profile info form container
        this.profileInfoFormContainer = (RelativeLayout) view.findViewById(R.id.profile_info_fragment_profile_info_form_container);

        //profile info form
        this.initProfileInfoForm();

        this.addProfileInfoForm();

    }

    private void initProfileInfoForm() {

        this.profileInfoForm = new ProfileInfoForm(this.getContext());

        this.profileInfoForm.setEditMode(false);

        this.initProfileInfoFormFromSession();

        this.profileInfoForm.getRecyclerView().setNestedScrollingEnabled(false);
        this.profileInfoForm.getRecyclerView().setHasFixedSize(true);

    }

    private void initProfileInfoFormFromSession() {

        Guest guest = SessionUtilsManager.get().getGuest();

        ArrayList<ProfileInfo> guestProfileInfoList = new ArrayList<ProfileInfo>();

        guestProfileInfoList.add(new ProfileInfo("Name", guest.getName(), "edit_text", true, PROFILE_INFO_NAME_FIELD_TAG));
        guestProfileInfoList.add(new ProfileInfo("Email", guest.getEmail(), "edit_text", true, PROFILE_INFO_EMAIL_FIELD_TAG));
        guestProfileInfoList.add(new ProfileInfo("Phone", guest.getPhone(), "phone_view", true, PROFILE_INFO_PHONE_FIELD_TAG));

        this.profileInfoForm.setFormItems(guestProfileInfoList);
    }

    private void addProfileInfoForm() {

        //layout params
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        layoutParams.addRule(ALIGN_PARENT_TOP);
        layoutParams.bottomMargin = UIUtilsManager.get().convertDpToPixels(this.getContext(), 100);


        //add form
        this.profileInfoFormContainer.addView(this.profileInfoForm, layoutParams);

    }

    //back pressed handler
    public void onBackPressed() {

    }

    //TEMP FUNCTION
//    private void testingRequests() {
//        //CLEAR DB
//        MyRealmController.with(this.getActivity()).deleteAllGuestRequests();
//        MyRealmController.with(this.getActivity()).deleteAllGuestRequestItems();
//
//        //PAGE
//        long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1L);
//        int requestId = 0;
//        this.createRequest(guestId, requestId, "done", "first_request_title", "first_request_content", "http://icons.iconarchive.com/icons/graphicloads/colorful-long-shadow/256/Man-icon.png", "1/1/2010");
//
//    }
//    void createRequest(long guestId, int id, String status, String title, String content, String iconUrl, String timeStamp) {
//        GuestRequest guestRequest = new GuestRequest();
//        guestRequest.setId(id);
//        guestRequest.setGuestId(guestId);
//        guestRequest.setStatus(status);
//        guestRequest.setIconUrl(iconUrl);
//        guestRequest.setTimeStamp(timeStamp);
//        guestRequest.setTitle(title);
//        MyRealmController.with(this.getActivity()).insertGuestRequest(guestRequest);
//    }
//    void createRequestItem(int id, int requestId, String type, String key, String value) {
//        RequestItem guestRequestItem = new RequestItem();
//        guestRequestItem.setId(id);
//        guestRequestItem.setType(type);
//        guestRequestItem.setKey(key);
//        guestRequestItem.setValue(value);
//        MyRealmController.with(this.getActivity()).insertGuestRequestItem(guestRequestItem);
//    }


    public class ProfileInfoForm extends DynamicFormView<ProfileInfo, Map.Entry<String, String> , ProfileInfoRequest, ProfileInfo> {

        private boolean editMode;

        public ProfileInfoForm(Context context) {
            super(context);
        }

        @Override
        protected int getTheme() {
            return UIModuleManager.get().getSharedPreferencesManager().getSecureSharedPreferences().getInt("appTheme", ThemeUtilsManager.DEFAULT_THEME);
        }

        @Override
        protected int getSkin() {
            return ThemeUtilsManager.get(this.getContext()).getSkin(this.getTheme(), MyAppThemeUtilsManager.PROFILE_INFO_FRAGMENT_SKIN);
        }

        @Override
        protected int getDialogSkin() {
            return ThemeUtilsManager.get(this.getContext()).getSkin(this.getTheme(), MyAppThemeUtilsManager.PROFILE_INFO_FRAGMENT_DIALOG_SKIN);
        }

        @Override
        protected void onStartLoading() {
            ProfileInfoFragment.this.managementViewContainer.setScreenState(LOADING);
        }

        @Override
        protected void onFinishLoading() {
            ProfileInfoFragment.this.managementViewContainer.setScreenState(PROFILE_INFO_LIST);
        }

        @Override
        protected void onError(String error) {
            ProfileInfoFragment.this.managementViewContainer.setMessage(error, FAILURE_MESSAGE);
        }

        @Override
        protected void initIsSubmittable() {

            this.isSubmittable = this.formItemsAdapter != null
                    && this.formItemsAdapter instanceof ProfileInfoFromAdapter
                    && ((ProfileInfoFromAdapter) this.formItemsAdapter).getMode() == READ_WRITE_MODE;

        }

        @Override
        protected void initFormItemsAdapter() {

            this.formItemsAdapter = new ProfileInfoFromAdapter(ProfileInfoFragment.this.getContext());

        }

        @Override
        protected void initFormRequest() {

            this.formRequest = new ProfileInfoRequest();

        }

        @Override
        protected void fillFormRequest(ProfileInfoRequest formRequest) {

            ArrayList<ProfileInfo> requestItems = this.getRequestItems();
            if (requestItems != null) {
                this.formRequest.setRequestItems(requestItems.toArray(new RequestItem[requestItems.size()]));
            } else {
                this.formRequest.setRequestItems(new RequestItem[0]);
            }

        }

        @Override
        protected ArrayList<ProfileInfo> generateRequestItem(ArrayList<MyView> formItemsViews) {
            ArrayList<ProfileInfo> profileInfoList = new ArrayList<ProfileInfo>();

            if (formItemsViews != null) {

                for (MyView myView : formItemsViews) {

                    ProfileInfo profileInfo = new ProfileInfo();

                    profileInfo.setKey(myView.getKey());
                    profileInfo.setValue(myView.getValue());
                    profileInfo.setTag((Integer) myView.getTag());

                    profileInfoList.add(profileInfo);
                }

            }

            return profileInfoList;
        }


        @Override
        protected List<Map.Entry<String, String>> getPropertiesOfFormItem(ProfileInfo formItem) {

            return new ArrayList<Map.Entry<String, String>>();

        }

        @Override
        protected void initSubmitButton() {

            super.initSubmitButton();

            this.submitButton.setText("Update");
            this.submitButton.setWidth((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 260));
            this.submitButton.setButtonBorderRadius((int) UIUtilsManager.get().convertDpToPixels(this.getContext(), 30));

        }


        @Override
        public void onSubmitButtonClick() {

            //update profile info
            Guest guest = SessionUtilsManager.get().getGuest();

            ArrayList<ProfileInfo> profileInfoList = this.getRequestItems();

            for (ProfileInfo profileInfo : profileInfoList) {

                int tag = profileInfo.getTag();

                switch (tag) {

                    case PROFILE_INFO_NAME_FIELD_TAG:
                        guest.setName(profileInfo.getValue());
                        break;
                    case PROFILE_INFO_EMAIL_FIELD_TAG:
                        guest.setEmail(profileInfo.getValue());
                        break;
                    case PROFILE_INFO_PHONE_FIELD_TAG:
                        guest.setPhone(profileInfo.getValue());
                        break;

                }

            }

            SessionUtilsManager.get().saveGuest(guest);
            ProfileInfoFragment.this.initProfileInfoFormFromSession();
        }

        public void setEditMode(boolean editMode) {

            this.editMode = editMode;

            if (this.formItemsAdapter != null
                    && this.formItemsAdapter instanceof ProfileInfoFromAdapter) {

                ((ProfileInfoFromAdapter) this.formItemsAdapter).setMode(editMode ? READ_WRITE_MODE : READ_ONLY_MODE);

            }

            this.setIsSubmittable(editMode);

        }

        public boolean getEditMode() {

            return this.editMode;

        }

        public RecyclerView getRecyclerView() {

            return this.recyclerView;

        }

    }



}
