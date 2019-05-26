package com.bob.bobguestapp.activities.main.fragments.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.ViewCompat;

import com.android.volley.Request;
import com.bob.bobguestapp.activities.main.connector.MainConnector;
import com.bob.toolsmodule.http.HttpUtilsManager;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.requests.MultipartServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.image.ImageDialog;
import com.bob.uimodule.image.LocalImageDialog;
import com.bob.uimodule.image.cropper.BitmapUtils;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementFragment;
import com.bob.uimodule.views.viewpager.DynamicHeightRtlViewPager;
import com.bob.uimodule.views.viewpager.DynamicHeightViewPagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.profile.changeprofilepicture.ChangeProfilePictureBottomSheetDialogFragment;
import com.bob.bobguestapp.activities.main.fragments.profile.changeprofilepicture.OnChangeProfilePictureListener;
import com.bob.bobguestapp.activities.main.fragments.profile.fragments.profileinfo.ProfileInfoFragment;
import com.bob.bobguestapp.activities.main.fragments.profile.fragments.profilestats.ProfileStatsFragment;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.LOADING;

public class ProfileFragment extends ManagementFragment implements OnChangeProfilePictureListener {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";
    private static String BOB_SERVER_FILES_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/FilesServices";
    private static String BOB_SERVER_IMAGES_SERVICES_URL = BOB_SERVER_FILES_SERVICES_URL
            + "/ImageServices";


    //get hotels url
    private static final String CHANGE_USER_STATUS_URL = BOB_SERVER_WEB_SERVICES_URL + "/users/setstatus";

    //upload user profile picture url
    private static final String CHANGE_USER_PROFILE_PICTURE_URL = BOB_SERVER_IMAGES_SERVICES_URL + "/uploadUserProfile";

    //main view screen states
    public static final int PROFILE = 10;

    //intro commands
    private MainConnector mainConnector;

    //profile
    private FrameLayout profileBackgroundLayout;
    private RelativeLayout profileLayout;
    private RelativeLayout profileHeaderLayout;
    private RelativeLayout profilePictureLayout;
    private RelativeLayout profilePictureFrame;
    private ImageView profilePictureView;
    private View profilePictureStatusView;
    private FloatingActionButton profilePictureChangePictureButton;
    private TextView profileTitleView;
    private TabLayout profileTabView;
    private RelativeLayout profileBodyLayout;
    private DynamicHeightRtlViewPager profileViewPager;
    private FloatingActionButton profileChangeStatusButton;

    //profile info
    private ProfileInfoFragment profileInfoFragment;

    //profile stats
    private ProfileStatsFragment profileStatsFragment;

    //change profile picture bottom sheet dialog
    ChangeProfilePictureBottomSheetDialogFragment changeProfilePictureBottomSheetDialogFragment;

    public ProfileFragment() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.PROFILE_FRAGMENT_SKIN);

    }

    public static ProfileFragment newInstance() {

        return new ProfileFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //init management skin
        this.managementViewContainer.setScreenSkin(this.screenSkin);

        //view
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //init views
        this.initProfileLayout(view);

        //init screen state
        this.managementViewContainer.setScreenState(PROFILE);

        return view;

    }


    //main commands
    public void setMainConnector(MainConnector mainConnector) {
        this.mainConnector = mainConnector;
    }

    //profile
    private void initProfileLayout(View view) {

        this.profileBackgroundLayout = (FrameLayout) view.findViewById(R.id.profile_fragment_main_container_frame_layout);

        this.initProfileBackground();

        this.profileLayout = (RelativeLayout) view.findViewById(R.id.profile_fragment_profile_layout);

        this.profileLayout.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.initProfileHeaderLayout(view);

        this.initProfileBodyLayout(view);

    }

    private void initProfileBackground() {

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColors(new int[]{
                MyAppThemeUtilsManager.get().getColor(ThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN),
                MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_SECONDARY, MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN),
                MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_SECONDARY, MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN)
        });
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        this.profileBackgroundLayout.setBackground(gradientDrawable);

    }

    private void initProfileHeaderLayout(View view) {

        this.profileHeaderLayout = (RelativeLayout) view.findViewById(R.id.profile_fragment_profile_header_layout);

        this.profileHeaderLayout.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.initProfileChangeStatusButton(view);

        this.initProfilePictureLayout(view);

        this.initProfileTitleView(view);

        this.initProfileTabsView(view);

    }

    private void initProfileChangeStatusButton(View view) {

        this.profileChangeStatusButton = (FloatingActionButton) view.findViewById(R.id.profile_fragment_profile_change_status_button);

        this.profileChangeStatusButton.setImageDrawable(
                ((IconicsDrawable)Icons.get().findDrawable(this.getContext(), "faw_power_off")).color(
                        MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
                ).sizeDp(24)
        );

        String userStatus = BOBGuestApplication.get().getSecureSharedPreferences().getString("userStatus", "none");
        if (userStatus != null && userStatus.equals("active")) {
            this.profileChangeStatusButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        } else {
            this.profileChangeStatusButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        }
        this.profileChangeStatusButton.setCompatElevation(20f);
        this.profileChangeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileFragment.this.makeChangeUserStatusRequest();

            }
        });
    }

    private void initProfilePictureLayout(View view) {

        this.profilePictureLayout = (RelativeLayout) view.findViewById(R.id.profile_fragment_profile_picture_layout);

        this.initProfilePictureFrame(view);

        this.initProfilePictureView(view);

        this.initProfilePictureStatusView(view);

        this.initProfilePictureChangePictureButton(view);

    }

    private void initProfilePictureFrame(View view) {

        this.profilePictureFrame = (RelativeLayout) view.findViewById(R.id.profile_fragment_profile_picture_frame);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setStroke(
                UIUtilsManager.get().convertDpToPixels(this.getContext(), 5),
                MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN));
        gradientDrawable.setColor(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN));
        this.profilePictureFrame.setBackground(gradientDrawable);
        ViewCompat.setElevation(this.profilePictureFrame, 10f);

    }

    private void initProfilePictureView(View view) {

        this.profilePictureView = (ImageView) view.findViewById(R.id.profile_fragment_profile_picture_view);

        String userIconUrl = BOBGuestApplication.get().getSecureSharedPreferences().getString("userImage", "none");
        String serverExampleProfilePictureUrl = "http://159.65.87.128:8080/FilesServices/ImageServices/getByIdAndName?hotelId=1&img=RickAndMorty_RickHappy1500.png";

        String deadpoolPictureUrl = "https://pmcvariety.files.wordpress.com/2013/06/deadpool-trailer-2.jpg?w=1000";

        this.setProfilePicture(deadpoolPictureUrl);

        this.profilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment.this.onImageClick("https://pmcvariety.files.wordpress.com/2013/06/deadpool-trailer-2.jpg?w=1000");
            }
        });



    }

    private void onImageClick(String imageUri) {
        if (getContext() instanceof Activity) {
                    ImageDialog dialog=new ImageDialog(((Activity)getContext()));
                    dialog.setImageUri(imageUri);
                    dialog.show();

//            if (Build.VERSION.SDK_INT < 21) {
//                Toast.makeText(getContext(), "21+ only, keep out", Toast.LENGTH_SHORT).show();
//            } else {
//                Intent intent = new Intent(getContext(), ImageActivity.class);
//                if (imageUri != null) {
//                    intent.putExtra("IMAGE_URI", imageUri);
//                }
//                ActivityOptionsCompat options = ActivityOptionsCompat.
//                        makeSceneTransitionAnimation(((Activity)getContext()), this.profilePictureView, "image");
//                ((Activity)getContext()).startActivity(intent, options.toBundle());
//            }
        }
    }

    private void setProfilePicture(String profilePictureUrl) {

        RoundedBitmapDrawable placeholderBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), this.drawableToBitmap(
                ((IconicsDrawable)Icons.get().findDrawable(this.getContext(), "faw_user1")).sizeDp(128)
                        .color(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN))
        ));
        placeholderBitmapDrawable.setCircular(true);

        Glide.with(this.getContext())
//                .load("http://159.65.87.128:8080/FilesServices/ImageServices/getByIdAndName?hotelId=1&img=RickAndMorty_RickHappy1500.png")
                .load(profilePictureUrl)
//                .load(userIconUrl)
                .apply(RequestOptions.placeholderOf(placeholderBitmapDrawable))
                .apply(RequestOptions.errorOf(placeholderBitmapDrawable))
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(this.profilePictureView);

    }

    private void setProfilePicture(Bitmap profilePictureBitmap) {

        RoundedBitmapDrawable placeholderBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), this.drawableToBitmap(
                ((IconicsDrawable)Icons.get().findDrawable(this.getContext(), "faw_user2")).sizeDp(128)
                        .color(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN))
        ));
        placeholderBitmapDrawable.setCircular(true);

        Glide.with(this.getContext())
                .load(profilePictureBitmap)
                .apply(RequestOptions.placeholderOf(placeholderBitmapDrawable))
                .apply(RequestOptions.errorOf(placeholderBitmapDrawable))
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(this.profilePictureView);

    }

    private void initProfilePictureStatusView(View view) {

        this.profilePictureStatusView = (View) view.findViewById(R.id.profile_fragment_profile_picture_status_view);
        ViewCompat.setElevation(this.profilePictureStatusView, 20f);

        GradientDrawable greenGradientDrawable = new GradientDrawable();
        greenGradientDrawable.setShape(GradientDrawable.OVAL);
        greenGradientDrawable.setColor(Color.GREEN);
        GradientDrawable redGradientDrawable = new GradientDrawable();
        redGradientDrawable.setShape(GradientDrawable.OVAL);
        redGradientDrawable.setColor(Color.RED);

        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
                greenGradientDrawable,
                redGradientDrawable
        });
        transitionDrawable.setCrossFadeEnabled(true);


        this.profilePictureStatusView.setBackground(transitionDrawable);

        if (ProfileFragment.this.profilePictureStatusView.getBackground() != null &&
                ProfileFragment.this.profilePictureStatusView.getBackground() instanceof TransitionDrawable) {
            String userStatus = BOBGuestApplication.get().getSecureSharedPreferences().getString("userStatus", "none");
            if (userStatus != null && userStatus.equals("active")) {
//                ((TransitionDrawable) ProfileFragment.this.profilePictureStatusView.getBackground()).reverseTransition(300);
            } else {
                ((TransitionDrawable) ProfileFragment.this.profilePictureStatusView.getBackground()).startTransition(300);
            }

        }

    }

    private void initProfilePictureChangePictureButton(View view) {

        //button
        this.profilePictureChangePictureButton = (FloatingActionButton) view.findViewById(R.id.profile_fragment_profile_change_picture_button);


        this.profilePictureChangePictureButton.setImageDrawable(
                ((IconicsDrawable)Icons.get().findDrawable(this.getContext(), "faw_camera")).color(
                        MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN)
                ).sizeDp(24)
        );

        this.profilePictureChangePictureButton.setBackgroundTintList(ColorStateList.valueOf(
                MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
        ));

        this.profilePictureChangePictureButton.setSize(FloatingActionButton.SIZE_MINI);

        ViewCompat.setElevation(this.profilePictureChangePictureButton, 20f);

        this.profilePictureChangePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileFragment.this.changeProfilePictureBottomSheetDialogFragment = new ChangeProfilePictureBottomSheetDialogFragment();
                ProfileFragment.this.changeProfilePictureBottomSheetDialogFragment.setOnChangeProfilePictureListener(ProfileFragment.this);
                ProfileFragment.this.changeProfilePictureBottomSheetDialogFragment.show(
                        ProfileFragment.this.getActivity().getSupportFragmentManager(), "ChangeProfilePictureBottomSheetDialog");
            }
        });

    }

    private void initProfileTitleView(View view) {

        this.profileTitleView = (TextView) view.findViewById(R.id.profile_fragment_profile_title_view);
        MyAppThemeUtilsManager.get().initTitle(this.profileTitleView, "Deadpool", this.getContext(), this.screenSkin);
        this.profileTitleView.setTextSize(this.getContext().getResources().getDimension(R.dimen.text_size_medium));
        this.profileTitleView.setTextColor(
                MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
        );

    }

    private void initProfileTabsView(View view) {

        this.profileTabView = (TabLayout) view.findViewById(R.id.profile_fragment_profile_tabs_view);

        this.profileTabView.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));

        this.profileTabView.setVisibility(View.GONE);

    }

    private void initProfileBodyLayout(View view) {

        this.profileBodyLayout = (RelativeLayout) view.findViewById(R.id.profile_fragment_profile_body_layout);

        this.profileBodyLayout.setBackgroundColor(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN));

        this.initFragments();

        this.initProfileViewPager(view);

    }

    private void initProfileViewPager(View view) {

        this.profileViewPager = (DynamicHeightRtlViewPager) view.findViewById(R.id.profile_fragment_profile_view_pager);

        //adapter
        DynamicHeightViewPagerAdapter adapter = new DynamicHeightViewPagerAdapter(this.getActivity().getSupportFragmentManager());
        adapter.addFragment(this.profileInfoFragment, "Info");

        this.profileViewPager.setAdapter(adapter);

        /* the ViewPager requires a minimum of 1 as OffscreenPageLimit */
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        this.profileViewPager.setOffscreenPageLimit(limit);

        this.profileTabView.post(new Runnable() {
            @Override
            public void run() {
                ProfileFragment.this.profileTabView.setupWithViewPager(ProfileFragment.this.profileViewPager);
//                IntroActivity.this.tabLayout.getTabAt(0).setText("Sign In");
                ProfileFragment.this.profileTabView.setTabTextColors(
                        MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN),
                        MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_BASE_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN)
                );
                ProfileFragment.this.profileTabView.setSelectedTabIndicatorColor(
                        ContextCompat.getColor(ProfileFragment.this.getContext(), R.color.transparent)
                );

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ProfileFragment.this.profileViewPager.setNestedScrollingEnabled(true);
                }
            }
        });

    }

    private void initFragments() {

        this.profileInfoFragment = new ProfileInfoFragment();

        this.profileStatsFragment = new ProfileStatsFragment();

    }

    private void makeChangeUserStatusRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {
                ProfileFragment.this.profileChangeStatusButton.setEnabled(false);
                ProfileFragment.this.profileChangeStatusButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

            }

            @Override
            protected boolean requestCondition() {
                if (BOBGuestApplication.get().getSecureSharedPreferences().contains("userStatus") &&
                        BOBGuestApplication.get().getSecureSharedPreferences().contains("userId")) {
                    String userStatus = BOBGuestApplication.get().getSecureSharedPreferences().getString("userStatus", "none");
                    return userStatus != null && (userStatus.equals("active") || userStatus.equals("inactive"));
                } else {
                    return false;
                }
            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected String getRequestUrl() {

                String requestUrl = CHANGE_USER_STATUS_URL + "?";
                int userId = BOBGuestApplication.get().getSecureSharedPreferences().getInt("userId", -1);
                requestUrl += "user=" + Integer.toString(userId);

                String userStatus = BOBGuestApplication.get().getSecureSharedPreferences().getString("userStatus", "none");
                if (userStatus != null && userStatus.equals("active")) {
                    requestUrl += "&status=" + "inactive";
                } else if (userStatus != null && userStatus.equals("inactive")) {
                    requestUrl += "&status=" + "active";
                }

                return requestUrl;
            }

           @Override
            protected ApplicativeResponse getApplicativeResponse(JSONObject response) {
                try {
                    Gson customGson = MyGsonParser.getParser().create();
                    String statusResponse = response.getJSONObject("response").getJSONObject("statusResponse").toString();

                    return customGson.fromJson(statusResponse, ApplicativeResponse.class);
                } catch (JSONException e) {
                    ApplicativeResponse statusResponse = new ApplicativeResponse();
                    statusResponse.setStatus("Failure");
                    statusResponse.setCode(ApplicativeResponse.FAILURE);
                    statusResponse.setMessage("error in parsing response");
                    return statusResponse;
                }
            }

            @Override
            protected void onSuccess(JSONObject response) {
                try {

                    String userStatus = BOBGuestApplication.get().getSecureSharedPreferences().getString("userStatus", "none");
                    if (userStatus != null && userStatus.equals("active")) {

                        //set to inactive
                        BOBGuestApplication.get().getSecureSharedPreferences().edit().putString("userStatus", "inactive").apply();
                        ProfileFragment.this.profileChangeStatusButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                        if (ProfileFragment.this.profilePictureStatusView.getBackground() != null &&
                                ProfileFragment.this.profilePictureStatusView.getBackground() instanceof TransitionDrawable) {
                            ((TransitionDrawable) ProfileFragment.this.profilePictureStatusView.getBackground()).startTransition(300);
                        }

                    } else if (userStatus != null && userStatus.equals("inactive")) {

                        //set to active
                        BOBGuestApplication.get().getSecureSharedPreferences().edit().putString("userStatus", "active").apply();
                        ProfileFragment.this.profileChangeStatusButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                        if (ProfileFragment.this.profilePictureStatusView.getBackground() != null &&
                                ProfileFragment.this.profilePictureStatusView.getBackground() instanceof TransitionDrawable) {
                            ((TransitionDrawable) ProfileFragment.this.profilePictureStatusView.getBackground()).reverseTransition(300);
                        }

                    }

                    ProfileFragment.this.profileChangeStatusButton.setEnabled(true);

                } catch (Exception e) {
                    e.printStackTrace();
                    this.onDefaultError("error changing user status");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    ProfileFragment.this.profileChangeStatusButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    ProfileFragment.this.profileChangeStatusButton.setEnabled(true);
                } else {
                    ProfileFragment.this.profileChangeStatusButton.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    ProfileFragment.this.profileChangeStatusButton.setEnabled(true);
                }
            }

        };

        jsonServerRequest.makeRequest();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    //back pressed handler
    public void onBackPressed() {

    }


    public Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public void onProfilePictureChange(Bitmap profilePicture, String fileUrl) {

        this.saveProfileAccount(profilePicture, fileUrl);

        this.profilePictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalImageDialog dialog=new LocalImageDialog(((Activity)getContext()));
                dialog.setImageUri(fileUrl);
                dialog.show();

//                ProfileFragment.this.onImageClick(fileUrl);
            }
        });

        this.setProfilePicture(profilePicture);

    }

    private void saveProfileAccount(Bitmap profilePicture, String fileUrl) {

        MultipartServerRequest multipartServerRequest = new MultipartServerRequest() {

            @Override
            protected String getRealFileUrl() {
                return fileUrl;
            }

            @Override
            protected void preRequest() {
                ProfileFragment.this.managementViewContainer.setScreenState(LOADING);
            }

            @Override
            protected boolean requestCondition() {
                if (BOBGuestApplication.get().getSecureSharedPreferences().contains("userStatus") &&
                        BOBGuestApplication.get().getSecureSharedPreferences().contains("userId")) {
                    String userStatus = BOBGuestApplication.get().getSecureSharedPreferences().getString("userStatus", "none");
                    return userStatus != null && (userStatus.equals("active") || userStatus.equals("inactive"));
                } else {
                    return false;
                }
            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "multipart/form-data");
                headers.put("Accept", "application/json");
            }


            @Override
            protected String getRequestUrl() {

                String requestUrl = CHANGE_USER_PROFILE_PICTURE_URL + "?";

                long hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("hotelId", -1);
                requestUrl += "hotelId=" + Long.toString(hotelId);

                int userId = BOBGuestApplication.get().getSecureSharedPreferences().getInt("userId", -1);
                requestUrl += "&userId=" + Integer.toString(userId);

                requestUrl += "&imageName=test.png";

                return requestUrl;

            }

            @Override
            protected ApplicativeResponse getApplicativeResponse(JSONObject response) {
                try {
                    Gson customGson = MyGsonParser.getParser().create();
                    String statusResponse = response.getJSONObject("response").getJSONObject("statusResponse").toString();

                    return customGson.fromJson(statusResponse, ApplicativeResponse.class);
                } catch (JSONException e) {
                    ApplicativeResponse statusResponse = new ApplicativeResponse();
                    statusResponse.setStatus("Failure");
                    statusResponse.setCode(ApplicativeResponse.FAILURE);
                    statusResponse.setMessage("error in parsing response");
                    return statusResponse;
                }
            }

            @Override
            protected void onSuccess(JSONObject response) {
                try {
                    Gson customGson = MyGsonParser.getParser().create();
                    String statusResponseString = response.getJSONObject("response").getJSONObject("statusResponse").toString();

                    ApplicativeResponse statusResponse = customGson.fromJson(statusResponseString, ApplicativeResponse.class);
                    System.out.println(statusResponse.toString());

                } catch (JSONException e) {
                    ApplicativeResponse statusResponse = new ApplicativeResponse();
                    statusResponse.setStatus("Failure");
                    statusResponse.setCode(ApplicativeResponse.FAILURE);
                    statusResponse.setMessage("error in parsing response");

                    System.out.println(statusResponse.toString());
                }
                ProfileFragment.this.managementViewContainer.setScreenState(PROFILE);

            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    System.out.println(message);
                } else {
                    System.out.println("Error - Upload");
                }
                ProfileFragment.this.managementViewContainer.setScreenState(PROFILE);
            }

        };

//        if (this.createFolderIfNotExist("BobWorker") && this.createFolderIfNotExist("BobWorker"   + File.separator + "Images")) {
//            int userId = BOBGuestApplication.get().getSecureSharedPreferences().getInt("userId", -1);
//            if (userId != -1) {
//                String url = Environment.getExternalStorageDirectory()  + File.separator + "BobWorker" + File.separator + "Images" + File.separator + "profile_" + Integer.toString(userId) + "_tmp" + "." + fileUrl.substring(fileUrl.lastIndexOf("."));
//                try (FileOutputStream out = new FileOutputStream(url)) {
//                    String extension = fileUrl.substring(fileUrl.lastIndexOf(".")).toLowerCase();
//                    if (extension.equals("png")) {
//                        profilePicture.compress(Bitmap.CompressFormat.PNG, 100, out);
//                    } else if (extension.equals("jpeg")) {
//                        profilePicture.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                    } else {
//                        profilePicture.compress(Bitmap.CompressFormat.PNG, 100, out);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        multipartServerRequest.makeRequest();

    }

    private boolean createFolderIfNotExist(String path) {
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + path);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

}
