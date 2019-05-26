package com.bob.bobguestapp.activities.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.checkin.CheckInActivity;
import com.bob.bobguestapp.activities.codes.GuestCodesActivity;
import com.bob.bobguestapp.activities.intro.IntroActivity;
import com.bob.bobguestapp.activities.intro.fragments.login.LoginFragment;
import com.bob.bobguestapp.activities.main.connector.MainConnector;
import com.bob.bobguestapp.activities.main.fragments.menu.MenuFragment;
import com.bob.bobguestapp.activities.main.fragments.notifications.NotificationsFragment;
import com.bob.bobguestapp.activities.main.fragments.profile.ProfileFragment;
import com.bob.bobguestapp.activities.main.fragments.requests.RequestsFragment;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.SocialFeedFragment;
import com.bob.bobguestapp.tools.comparators.guestbooking.GuestBookingCheckInComparator;
import com.bob.bobguestapp.tools.database.MyDBUtilsManager;
import com.bob.bobguestapp.tools.database.MyRealmController;
import com.bob.bobguestapp.tools.database.objects.GuestBooking;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.bobguestapp.tools.recyclerview.StateViewPager;
import com.bob.bobguestapp.tools.recyclerview.StateViewPagerAdapter;
import com.bob.bobguestapp.tools.session.SessionUtilsManager;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.http.ServerRequest;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.uimodule.drawable.DrawableHelper;
import com.bob.uimodule.navdrawer.items.secondary.CustomCenteredSecondaryDrawerItem;
import com.bob.uimodule.others.BackgroundColorTimer;
import com.bob.uimodule.views.loadingcontainer.ManagementActivity;
import com.bob.uimodule.views.viewpager.RtlViewPager;
import com.bob.uimodule.views.viewpager.ViewPagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialdrawer.view.BezelImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.javascript.tools.jsc.Main;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.view.View.VISIBLE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.FAILURE_MESSAGE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.LOADING;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.MESSAGE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.NONE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.PROGRESS;


public class MainActivity extends ManagementActivity implements MainConnector {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //guest bookings
    private static final String GET_ALL_GUEST_BOOKING_URL = BOB_SERVER_WEB_SERVICES_URL +"/bookings/findForGuest";

    //screen states
    private static final int MAIN_VIEW = 3;
    private static final int MENU = 4;
    private static final int PROFILE = 5;
    private static final int SOCIAL = 6;
    private static final int REQUESTS = 7;
    private static final int NOTIFICATIONS = 8;

    //tab indices
    private static final int MENU_TAB_INDEX = 0;
    private static final int PROFILE_TAB_INDEX = 1;
    private static final int SOCIAL_TAB_INDEX = 2;
    private static final int REQUESTS_TAB_INDEX = 3;
    private static final int NOTIFICATIONS_TAB_INDEX = 4;
    private static final int TAB_COUNTS = 5;

    //drawer identifiers
    private static final int HOME_DRAWER_ITEM_IDENTIFIER = 0;
    private static final int REQUESTS_DRAWER_ITEM_IDENTIFIER = 1;
    private static final int CHECK_IN_DRAWER_ITEM_IDENTIFIER = 2;
    private static final int ROOMMATES_DRAWER_ITEM_IDENTIFIER = 3;
    private static final int ACCOUNT_DRAWER_ITEM_IDENTIFIER = 4;
    private static final int SECTION_DIVIDER_DRAWER_ITEM_IDENTIFIER = 5;
    private static final int SETTINGS_DRAWER_ITEM_IDENTIFIER = 6;
    private static final int PRIVACY_DRAWER_ITEM_IDENTIFIER = 7;
    private static final int HELP_DRAWER_ITEM_IDENTIFIER = 8;
    private static final int ABOUT_US_DRAWER_ITEM_IDENTIFIER = 9;
    private static final int LOGOUT_DRAWER_ITEM_IDENTIFIER = 10;

    //app theme
    int navigationDrawerSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;

    //toolbar
    private Toolbar toolbar;

    //background
    private int backgroundColorPrimary, backgroundColorLight;
    private BackgroundColorTimer backgroundColorTimer;

    //main view
    private TabLayout tabLayout;
    private RtlViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ArrayList<Integer> tabIcons;
    private ArrayList<Fragment> tabFragments;
    private ConstraintLayout mainViewLayout;

    //menu
    private MenuFragment menuFragment;

    //notifications
    private NotificationsFragment notificationsFragment;

    //profile
    private ProfileFragment profileFragment;

    //social
    private SocialFeedFragment socialFeedFragment;

    //requests
    private RequestsFragment requestsFragment;

    //nav drawer
    private AccountHeader headerResult = null;
    private Drawer drawer = null;
    private AppCompatImageView headerBackgroundImage;
    private BezelImageView headerGuestProfileImage;
    private PrimaryDrawerItem homeDrawerItem, requestsDrawerItem,checkInDrawerItem,
            roommatesDrawerItem, accountDrawerItem;
    private SectionDrawerItem sectionDrawerItem;
    private SecondaryDrawerItem settingsDrawerItem, privacyDrawerItem, helpDrawerItem,
            aboutUsDrawerItem, logoutDrawerItem;
    private List<IProfile> guestBookingsProfiles;



    public MainActivity() {

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this).getSkin(this.appTheme, MyAppThemeUtilsManager.MAIN_ACTIVITY_SKIN);
        this.navigationDrawerSkin = MyAppThemeUtilsManager.get(this).getSkin(this.appTheme, MyAppThemeUtilsManager.MAIN_ACTIVITY_NAVIGATION_DRAWER_SKIN);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get all bookings
        this.makeGetAllGuestBookings();


        this.managementViewContainer.setScreenState(MENU);
        this.setBackground(MENU);


    }


    @Override
    protected View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {

        //background
        this.initBackgroundAnimation();

        //view
        View view = inflater.inflate(R.layout.activity_main, container, false);

        //layouts
        this.initMainViewLayout(view);

        //status bar color
        this.initStatusBarColor();

        //navigation bar color
        this.initNavigationBarColor();

        //nav drawer
        this.initDrawerItems();
        this.buildHeader(false, savedInstanceState);
        this.buildDrawer(savedInstanceState);

        return view;

    }

    @Override
    protected void onResume() {
        super.onResume();
//        this.backgroundColorTimer.startTimer();
//        this.getLastRequestedFocus();
    }

    @Override
    protected void onPause() {
        this.backgroundColorTimer.stopTimer();
        super.onPause();
    }

    //management
    private void initBackgroundAnimation() {

        this.backgroundColorPrimary = MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_SCREEN_BACKGROUND_COLOR_PRIMARY, this.screenSkin);
        this.backgroundColorLight = MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_SCREEN_BACKGROUND_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN);

        this.backgroundColorTimer = new BackgroundColorTimer(this.managementViewContainer.getBackgroundLayout(), this.backgroundColorPrimary, this.backgroundColorPrimary);
        this.backgroundColorTimer.setColorIntervals(1, 0, 0, 1, 0);
        this.backgroundColorTimer.setColorValuesRange(1, 0, 0, 30, 0);

    }

    private void initStatusBarColor() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.transparent));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
            this.drawer.getDrawerLayout().setStatusBarBackgroundColor(MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_STATUS_BAR_BACKGROUND_COLOR_PRIMARY, this.screenSkin));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initNavigationBarColor() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.getWindow().setNavigationBarColor(MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_NAVIGATION_BAR_BACKGROUND_COLOR_PRIMARY, this.screenSkin));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void setMainViewScreenState(int screenState) {

        this.mainViewLayout.setVisibility(View.INVISIBLE);

        switch (screenState) {

            case MAIN_VIEW:
                this.mainViewLayout.setVisibility(VISIBLE);
                break;
            case MENU:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setMenuState();
                break;
            case PROFILE:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setProfileState();
                break;
            case SOCIAL:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setSocialState();
                break;
            case REQUESTS:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setRequestsState();
                break;
            case NOTIFICATIONS:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setNotificationsState();
                break;
            default:
                break;

        }

    }

    protected void setBackground(int backgroundType) {

        switch (backgroundType) {

            case LOADING: case MESSAGE: case PROGRESS: case NONE:
                if (this.backgroundColorTimer != null) {
//                    this.backgroundColorTimer.startTimer();
                }
                break;
            case MAIN_VIEW: case MENU: case PROFILE: case REQUESTS: case NOTIFICATIONS:
                if (this.backgroundColorTimer != null) {
                    this.backgroundColorTimer.stopTimer();
                }
                this.initMainViewBackgroundDrawable();
                break;
            default:
                if (this.backgroundColorTimer != null) {
                    this.backgroundColorTimer.stopTimer();
                }
                this.initMainViewBackgroundDrawable();
                break;
        }

    }

    protected void setMenuState() {

        this.setViewPagerIndex(MENU_TAB_INDEX);

    }

    protected void setProfileState() {

        this.setViewPagerIndex(PROFILE_TAB_INDEX);

    }

    protected void setSocialState() {

        this.setViewPagerIndex(SOCIAL_TAB_INDEX);

    }

    protected void setRequestsState() {

        this.setViewPagerIndex(REQUESTS_TAB_INDEX);

    }

    protected void setNotificationsState() {

        this.setViewPagerIndex(NOTIFICATIONS_TAB_INDEX);

    }

    protected void setViewPagerIndex(int index) {

        if (this.viewPagerAdapter == null) {

            this.viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        }

        if (!this.viewPager.getAdapter().equals(this.viewPagerAdapter)) {

            this.viewPager.setAdapter(this.viewPagerAdapter);

        }

        if (this.viewPager.getChildCount() <= index) {

            this.viewPager.setCurrentItem(index);

        }

    }

    public int getNavigationBarHeight() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && getResources().getIdentifier("navigation_bar_height", "dimen", "android") > 0) {

            return getResources().getDimensionPixelSize(
                    getResources().getIdentifier("navigation_bar_height", "dimen", "android")
            );

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            this.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }

        return 0;

    }

    private int getActionBarHeight() {

        TypedValue tv = new TypedValue();

        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        return 0;

    }

    //main view
    private void initMainViewLayout(View view) {

        //main view
        this.initMainViewBackground(view);

        //toolbar
        this.initToolbar(view);

        //fragments
        this.initFragments();

        //tab layout
        this.initTabLayout(view);

        //view pager
        this.initViewPager(view);

    }

    private void initMainViewBackground(View view) {

        //main view background
        this.mainViewLayout = (ConstraintLayout) view.findViewById(R.id.main_activity_layout);

        //main view background animation
        this.initMainViewBackgroundDrawable();

    }

    private void initMainViewBackgroundDrawable() {

        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setColors(new int[]{
                MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_ACTIVITY_BACKGROUND_COLOR_PRIMARY, this.screenSkin),
                MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_ACTIVITY_BACKGROUND_COLOR_SECONDARY, this.screenSkin)
        });
        shapeDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        shapeDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        shapeDrawable.setCornerRadius(0f);
        this.managementViewContainer.getBackgroundLayout().setBackground(shapeDrawable);

    }

    private void initFragments() {

        this.menuFragment = new MenuFragment();

        this.profileFragment = new ProfileFragment();

        this.socialFeedFragment = new SocialFeedFragment();

        this.requestsFragment = new RequestsFragment();

        this.notificationsFragment = new NotificationsFragment();

    }

    private void initTabLayout(View view) {

        this.tabLayout = (TabLayout) view.findViewById(R.id.main_activity_tabs);

        //tab layout color
        this.tabLayout.setBackgroundColor(MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_TAB_LAYOUT_BACKGROUND_COLOR_PRIMARY, this.screenSkin));

        //tabs fragments list
        this.initFragmentsList();

    }

    private void initFragmentsList() {

        this.tabFragments = new ArrayList<Fragment>();
        this.tabFragments.add(MENU_TAB_INDEX, this.menuFragment);
        this.tabFragments.add(PROFILE_TAB_INDEX, this.profileFragment);
        this.tabFragments.add(SOCIAL_TAB_INDEX, this.socialFeedFragment);
        this.tabFragments.add(REQUESTS_TAB_INDEX, this.requestsFragment);
        this.tabFragments.add(NOTIFICATIONS_TAB_INDEX, this.notificationsFragment);

    }

    private void initViewPager(View view) {

        this.viewPager = (RtlViewPager) view.findViewById(R.id.main_activity_view_pager);

        this.viewPager.setPadding(
                0,
                0,
                0,
                this.getActionBarHeight()

        );

        //adapter
        this.viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        this.viewPagerAdapter.addFragment(this.menuFragment, "");
        this.viewPagerAdapter.addFragment(this.profileFragment, "");
        this.viewPagerAdapter.addFragment(this.socialFeedFragment, "");
        this.viewPagerAdapter.addFragment(this.requestsFragment, "");
        this.viewPagerAdapter.addFragment(this.notificationsFragment, "");

        this.viewPager.setAdapter(this.viewPagerAdapter);

        /* the ViewPager requires a minimum of 1 as OffscreenPageLimit */
        int limit = (this.viewPagerAdapter.getCount() > 1 ? this.viewPagerAdapter.getCount() - 1 : 1);
        this.viewPager.setOffscreenPageLimit(limit);

        this.tabLayout.post(new Runnable() {
            @Override
            public void run() {

                MainActivity.this.tabLayout.setupWithViewPager(MainActivity.this.viewPager);

                //tabs icons list
                MainActivity.this.initTabIconsList();

                //tabs colors
                MainActivity.this.initTabsColors();

                //tabs on selected listener
                MainActivity.this.initTabsOnSelectedListener();

                MainActivity.this.viewPager.setPadding(
                        0,
                        0,
                        0,
                        MainActivity.this.getActionBarHeight() + MainActivity.this.tabLayout.getMeasuredHeight()

                );


            }
        });



    }

    private void initTabIconsList() {

        this.tabIcons = new ArrayList<Integer>();
        this.tabIcons.add(MENU_TAB_INDEX, R.drawable.ic_home_white_24dp);
        this.tabIcons.add(PROFILE_TAB_INDEX, R.drawable.ic_person_white_24dp);
        this.tabIcons.add(SOCIAL_TAB_INDEX, R.drawable.ic_whatshot_white_24dp);
        this.tabIcons.add(REQUESTS_TAB_INDEX, R.drawable.ic_room_service_white_24dp);
        this.tabIcons.add(NOTIFICATIONS_TAB_INDEX, R.drawable.ic_notifications_white_24dp);

    }

    private void initTabsColors() {

        this.tabLayout.post(new Runnable() {
            @Override
            public void run() {

                //tabs text colors
                MainActivity.this.tabLayout.setTabTextColors(
                        MyAppThemeUtilsManager.get(MainActivity.this).getColor(MyAppThemeUtilsManager.DEFAULT_TAB_TEXT_COLOR, MainActivity.this.screenSkin),
                        MyAppThemeUtilsManager.get(MainActivity.this).getColor(MyAppThemeUtilsManager.DEFAULT_TAB_TEXT_PRESSED_COLOR, MainActivity.this.screenSkin)
                );

                //tabs indicator color
                MainActivity.this.tabLayout.setSelectedTabIndicatorColor(
                        ContextCompat.getColor(MainActivity.this, R.color.light_primary_color)
                );

                //tabs icons color
                for (int i = 0; i < TAB_COUNTS; i++) {
                    Drawable tabIconDrawable = DrawableHelper.withContext(MainActivity.this)
                            .withDrawable(tabIcons.get(i))
                            .withColor(
                                    MyAppThemeUtilsManager.get(MainActivity.this).getColor(MyAppThemeUtilsManager.DEFAULT_TAB_ICON_COLOR, MainActivity.this.screenSkin)
                            )
                            .tint()
                            .get();
                    MainActivity.this.tabLayout.getTabAt(i).setIcon(tabIconDrawable);
                }

            }
        });

    }

    private void initTabsOnSelectedListener() {

        this.tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(this.viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);

                        MainActivity.this.viewPager.setCurrentItem(tab.getPosition());
                        Drawable tabIconDrawable = DrawableHelper.withContext(MainActivity.this)
                                .withDrawable(tabIcons.get(tab.getPosition()))
                                .withColor(
                                        MyAppThemeUtilsManager.get(MainActivity.this).getColor(MyAppThemeUtilsManager.DEFAULT_TAB_ICON_PRESSED_COLOR, MainActivity.this.screenSkin)
                                )
                                .tint()
                                .get();
                        MainActivity.this.tabLayout.getTabAt(tab.getPosition()).setIcon(tabIconDrawable);

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);

//                        MainActivity.this.viewPager.setCurrentItem(tab.getPosition());
                        Drawable tabIconDrawable = DrawableHelper.withContext(MainActivity.this)
                                .withDrawable(tabIcons.get(tab.getPosition()))
                                .withColor(
                                        MyAppThemeUtilsManager.get(MainActivity.this).getColor(MyAppThemeUtilsManager.DEFAULT_TAB_ICON_COLOR, MainActivity.this.screenSkin)
                                )
                                .tint()
                                .get();
                        MainActivity.this.tabLayout.getTabAt(tab.getPosition()).setIcon(tabIconDrawable);

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

    }

    //toolbar
    private void initToolbar(View view) {

        this.toolbar = (Toolbar) view.findViewById(R.id.main_activity_toolbar);

        this.toolbar.setBackgroundColor(MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_TOOLBAR_BACKGROUND_COLOR_PRIMARY, this.screenSkin));

        setSupportActionBar(this.toolbar);

        getSupportActionBar().setTitle(R.string.app_name);

    }

    //nav drawer
    private void buildHeader(boolean compact, Bundle savedInstanceState) {
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                Glide.with(imageView.getContext()).load(uri)
                        .apply(new RequestOptions().placeholder(R.drawable.placeholder).error(R.drawable.missing_image))
                        .into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.with(MainActivity.this).clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });

        this.headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withAccountHeader(this.initHeaderView())
                .withHeaderBackground(new ImageHolder("https://static1.squarespace.com/static/5537f229e4b05d21beb7b738/t/5b33cbab1ae6cf493d643e7d/1530121170861/wallpaper-widescreen-perfect-hotel-wallpapers-background-paper.jpg?format=2500w"))
//                .withHeaderBackground(R.drawable.header)
//                .addProfiles(
//                        new ProfileDrawerItem()
//                                .withName(BOBGuestApplication.get().getSecureSharedPreferences().getString("guestName", "Roi Peretz"))
//                                .withEmail(BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelName", "Meridian")
//                                        + ", Room No. " + BOBGuestApplication.get().getSecureSharedPreferences().getString("roomNumber", "2507"))
//                                .withIcon("https://static1.squarespace.com/static/5537f229e4b05d21beb7b738/t/5b33cbab1ae6cf493d643e7d/1530121170861/wallpaper-widescreen-perfect-hotel-wallpapers-background-paper.jpg?format=2500w"),
//                        new ProfileDrawerItem()
//                                .withName(BOBGuestApplication.get().getSecureSharedPreferences().getString("guestName", "Nisim Peretz"))
//                                .withEmail(BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelName", "Herods")
//                                        + ", Room No. " + BOBGuestApplication.get().getSecureSharedPreferences().getString("roomNumber", "2511"))
//                                .withIcon("http://files.all-free-download.com//downloadfiles/wallpapers/1920_1200/burj_al_arab_hotel_wallpaper_united_arab_emirates_world_1801.jpg")
//
//                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {

                        if (!currentProfile) {

                            long bookingId = profile.getIdentifier();
                            GuestBooking currentGuestBooking = MyRealmController.get().getGuestBookingById(bookingId);
                            MainActivity.this.changeCurrentBooking(currentGuestBooking);

                            profile.getIcon().applyTo(MainActivity.this.headerBackgroundImage);

                        }

                        MainActivity.this.drawer.closeDrawer();

                        return false;
                    }
                })
                .withTranslucentStatusBar(true)
                .withSavedInstance(savedInstanceState)
                .build();


        // Create the AccountHeader
    }

    private void setGuestBookings() {

        List<GuestBooking> guestBookings = MyRealmController.get().getGuestBookings();

        if (this.headerResult.getProfiles() != null) {

            for(IProfile bookingProfile : this.headerResult.getProfiles()) {

                this.headerResult.removeProfile(bookingProfile);

            }

        }

        String guestName = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestName", "");

        List<IProfile> bookingsProfiles = new ArrayList<IProfile>();

        for (GuestBooking guestBooking : guestBookings) {

            ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem();

            ((MyAppThemeUtilsManager)MyAppThemeUtilsManager.get()).initProfileDrawerItem(
                    profileDrawerItem,
                    guestBooking.getId(),
                    guestBooking.getHotel().getName() + " -  "
                            + guestBooking.getRoom(),
                    this.prettyTimestamp(guestBooking.getCheckIn()) + " - "
                            + this.prettyTimestamp(guestBooking.getCheckOut()),
                    "http://files.all-free-download.com//downloadfiles/wallpapers/1920_1200/burj_al_arab_hotel_wallpaper_united_arab_emirates_world_1801.jpg"
                    , MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN

            );

            bookingsProfiles.add(
                    profileDrawerItem
            );

        }

        this.headerResult.addProfiles(bookingsProfiles.toArray(new IProfile[guestBookings.size()]));

    }

    private String prettyTimestamp(String timestamp) {

        try {

            Date date = Timestamp.valueOf(timestamp);
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.getTime());

        } catch (Exception e) {

            e.printStackTrace();
            return timestamp;
        }

    }

    private void changeCurrentBooking(GuestBooking guestBooking) {

        long currentBookingId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("bookingId", -1L);

        if (guestBooking.getId() != -1L && guestBooking.getId() != currentBookingId) {

            SessionUtilsManager.get().saveBooking(guestBooking);

            this.menuFragment.refreshMenu();

            this.requestsFragment.refreshRequests();

            this.notificationsFragment.refreshNotifications();

        }

    }

    private void buildDrawer(Bundle savedInstanceState) {

        this.drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(this.toolbar)
                .withAccountHeader(this.headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        this.homeDrawerItem,
                        this.requestsDrawerItem,
                        this.checkInDrawerItem,
                        this.roommatesDrawerItem,
                        this.accountDrawerItem,
                        this.sectionDrawerItem,
                        this.settingsDrawerItem,
                        this.privacyDrawerItem,
                        this.helpDrawerItem,
                        this.aboutUsDrawerItem,
                        this.logoutDrawerItem
                ) // add the items we want to use with our Drawer
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {

                        //this method is only called if the Arrow icon is shown. The hamburger is automatically managed by the MaterialDrawer
                        //if the back arrow is shown. close the activity
                        MainActivity.this.finish();
                        //return true if we have consumed the event
                        return true;

                    }
                })
                .withSavedInstance(savedInstanceState)
                .withSliderBackgroundColor(MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_NAVIGATION_DRAWER_BACKGROUND_COLOR_PRIMARY, this.navigationDrawerSkin))
                .build();

    }

    private void initDrawerItems() {

        //primary drawer items
        //home
        this.initHomeNavDrawerItem();

        //requests
        this.initRequestsNavDrawerItem();

        //check in
        this.initCheckInNavDrawerItem();

        //roommates
        this.initRoommatesNavDrawerItem();

        //account
        this.initAccountNavDrawerItem();

        //section divider drawer item
        this.initSectionDividerNavDrawerItem();

        //secondary drawer items
        //settings
        this.initSettingsNavDrawerItem();

        //privacy
        this.initPrivacyNavDrawerItem();

        //help
        this.initHelpNavDrawerItem();

        //about us
        this.initAboutUsNavDrawerItem();

        //centered section divider drawer item
        //logout
        this.initLogoutNavDrawerItem();

    }

    private void initHomeNavDrawerItem() {

        this.homeDrawerItem = new PrimaryDrawerItem();
        MyAppThemeUtilsManager.get(this).initPrimaryDrawerItem(this.homeDrawerItem, "Home",
                FontAwesome.Icon.faw_home, HOME_DRAWER_ITEM_IDENTIFIER, this.navigationDrawerSkin);

    }

    private void initRequestsNavDrawerItem() {

        this.requestsDrawerItem = new PrimaryDrawerItem();
        MyAppThemeUtilsManager.get(this).initPrimaryDrawerItem(this.requestsDrawerItem, "Requests",
                FontAwesome.Icon.faw_bell, REQUESTS_DRAWER_ITEM_IDENTIFIER, this.navigationDrawerSkin);

    }

    private void initCheckInNavDrawerItem() {

        this.checkInDrawerItem = new PrimaryDrawerItem();
        MyAppThemeUtilsManager.get(this).initPrimaryDrawerItem(this.checkInDrawerItem, "Check In",
                FontAwesome.Icon.faw_h_square, CHECK_IN_DRAWER_ITEM_IDENTIFIER, this.navigationDrawerSkin);
        this.checkInDrawerItem.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Intent intent = new Intent(MainActivity.this, CheckInActivity.class);
                MainActivity.this.startActivity(intent);
                return false;
            }
        });

    }

    private void initRoommatesNavDrawerItem() {

        this.roommatesDrawerItem = new PrimaryDrawerItem();
        MyAppThemeUtilsManager.get(this).initPrimaryDrawerItem(this.roommatesDrawerItem, "Roommates Codes",
                FontAwesome.Icon.faw_key, ROOMMATES_DRAWER_ITEM_IDENTIFIER, this.navigationDrawerSkin);
        this.roommatesDrawerItem.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Intent intent = new Intent(MainActivity.this, GuestCodesActivity.class);
                MainActivity.this.startActivity(intent);
                return false;
            }
        });

    }

    private void initAccountNavDrawerItem() {

        this.accountDrawerItem = new PrimaryDrawerItem();
        MyAppThemeUtilsManager.get(this).initPrimaryDrawerItem(this.accountDrawerItem, "Account",
                MaterialDesignIconic.Icon.gmi_account, ACCOUNT_DRAWER_ITEM_IDENTIFIER, this.navigationDrawerSkin);

    }

    private void initSectionDividerNavDrawerItem() {

        this.sectionDrawerItem = new SectionDrawerItem();
        MyAppThemeUtilsManager.get(this).initSectionDrawerItem(this.sectionDrawerItem, SECTION_DIVIDER_DRAWER_ITEM_IDENTIFIER, this.navigationDrawerSkin);

    }

    private void initSettingsNavDrawerItem() {

        this.settingsDrawerItem = new SecondaryDrawerItem();
        MyAppThemeUtilsManager.get(this).initSecondaryDrawerItem(this.settingsDrawerItem, "Settings",
                MaterialDesignIconic.Icon.gmi_settings, SETTINGS_DRAWER_ITEM_IDENTIFIER, this.navigationDrawerSkin);

    }

    private void initPrivacyNavDrawerItem() {

        this.privacyDrawerItem = new SecondaryDrawerItem();
        MyAppThemeUtilsManager.get(this).initSecondaryDrawerItem(this.privacyDrawerItem, "Privacy Settings",
                FontAwesome.Icon.faw_lock, PRIVACY_DRAWER_ITEM_IDENTIFIER, this.navigationDrawerSkin);

    }

    private void initHelpNavDrawerItem() {

        this.helpDrawerItem = new SecondaryDrawerItem();
        MyAppThemeUtilsManager.get(this).initSecondaryDrawerItem(this.helpDrawerItem, "Help",
                MaterialDesignIconic.Icon.gmi_help, HELP_DRAWER_ITEM_IDENTIFIER, this.navigationDrawerSkin);

    }

    private void initAboutUsNavDrawerItem() {

        this.aboutUsDrawerItem = new SecondaryDrawerItem();
        MyAppThemeUtilsManager.get(this).initSecondaryDrawerItem(this.aboutUsDrawerItem, "About Us",
                MaterialDesignIconic.Icon.gmi_info, ABOUT_US_DRAWER_ITEM_IDENTIFIER, this.navigationDrawerSkin);

    }

    private void initLogoutNavDrawerItem() {

        this.logoutDrawerItem = new CustomCenteredSecondaryDrawerItem();
        MyAppThemeUtilsManager.get(this).initSecondaryDrawerItem(this.logoutDrawerItem, "Logout", null, LOGOUT_DRAWER_ITEM_IDENTIFIER, this.navigationDrawerSkin);
        this.logoutDrawerItem
                .withTextColor(MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_NAVIGATION_DRAWER_LOGOUT_ITEM_TEXT_COLOR, this.navigationDrawerSkin))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        SessionUtilsManager.get().clearSession();

//                        MainActivity.super.onBackPressed();
                        Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                        MainActivity.this.startActivity(intent);

                        return false;
                    }
                });

    }

    protected View initHeaderView() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.material_drawer_account_header_custom, null, false);

        this.headerBackgroundImage = (AppCompatImageView) view.findViewById(R.id.material_drawer_account_header_background);
        this.headerGuestProfileImage = (BezelImageView) view.findViewById(R.id.material_drawer_guest_profile);
        Glide.with(this)
                .asBitmap()
                .load("https://i.pinimg.com/originals/63/a5/e8/63a5e8ee8cdcfab2f952bcd46a73e5c4.jpg")
                .into(this.headerGuestProfileImage);

        return view;
    }

    private Drawable getProfileImage() {
        return null;
    }

    private DrawerImageLoader getHotelBackgroundImage() {
        return  DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.placeholder);
                requestOptions.error(R.drawable.missing_image);
                Glide.with(imageView.getContext()).load(uri).apply(requestOptions).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.with(MainActivity.this).clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });
    }

    //http requests

    //guest bookings
    private void makeGetAllGuestBookings() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {
                MainActivity.this.managementViewContainer.setScreenState(LOADING);
            }

            @Override
            protected boolean requestCondition() {

                Guest guest = SessionUtilsManager.get().getGuest();
                if ((guest != null) && (guest.getEmail() != null)) {
                    return true;
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
                return GET_ALL_GUEST_BOOKING_URL;
            }

            @Override
            protected JSONObject getJsonObject() {
                try {

                    Guest guest = SessionUtilsManager.get().getGuest();
                    JsonElement jsonGuest = MyGsonParser.getParser().create().toJsonTree(guest, Guest.class);

                    JsonObject jsonRequest = new JsonObject();
                    jsonRequest.add("guest", jsonGuest);

                    JsonObject jsonLoginRequest = new JsonObject();
                    jsonLoginRequest.add("request", jsonRequest);

                    return new JSONObject( new Gson().toJson(jsonLoginRequest));

                } catch (JSONException e) {

                    e.printStackTrace();
                    return null;
                }

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

                    if (response.getJSONObject("response").has("bookings")) {

                        GuestBooking[] guestBookings = new GuestBooking[0];
                        if (response.getJSONObject("response").optJSONArray("bookings") != null) {
                            String guestBookingString = response.getJSONObject("response").getJSONArray("bookings").toString();
                            guestBookings = customGson.fromJson(guestBookingString, GuestBooking[].class);
                        } else if (response.getJSONObject("response").optJSONObject("bookings") != null) {
                            String guestBookingString = response.getJSONObject("response").getJSONObject("bookings").toString();
                            guestBookings = new GuestBooking[] {
                                    customGson.fromJson(guestBookingString, GuestBooking.class)
                            };
                        }

                        if (guestBookings != null && guestBookings.length > 0) {

                            List<GuestBooking> guestBookingList = Arrays.asList(guestBookings);

                            Collections.sort(guestBookingList, new GuestBookingCheckInComparator());

                            GuestBooking[] guestBookingArray = guestBookingList.toArray(new GuestBooking[guestBookingList.size()]);

//                            MyDBUtilsManager.get().clearGuestBookingsFromDB();

                            MyDBUtilsManager.get().insertGuestBookingsListToDB(guestBookingArray);

                            MainActivity.this.setGuestBookings();

                            if (MainActivity.this.headerResult.getProfiles().size() > 0) {

                                MainActivity.this.headerResult.setActiveProfile(
                                        MainActivity.this.headerResult.getProfiles().get(0),
                                        true
                                );

                                long bookingId = MainActivity.this.headerResult.getProfiles().get(0).getIdentifier();
                                GuestBooking currentGuestBooking = MyRealmController.get().getGuestBookingById(bookingId);
                                MainActivity.this.changeCurrentBooking(currentGuestBooking);

                            }

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    MainActivity.this.managementViewContainer.setMessage(message, FAILURE_MESSAGE);
                } else {
                    MainActivity.this.managementViewContainer.setMessage("Getting Menu Error", FAILURE_MESSAGE);
                }
            }

            @Override
            protected void onException(Exception e) {
                e.printStackTrace();
                this.onDefaultError(e.toString());
            }

        };

        jsonServerRequest.makeRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_1:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (drawer != null && drawer.isDrawerOpen()) {

            drawer.closeDrawer();
        } else {

            switch (this.viewPager.getCurrentItem()) {
                case MENU_TAB_INDEX:
                    this.menuFragment.onBackPressed();
                    break;
                case PROFILE_TAB_INDEX:
                    this.profileFragment.onBackPressed();
                    break;
                case SOCIAL_TAB_INDEX:
                    this.socialFeedFragment.onBackPressed();
                    break;
                case REQUESTS_TAB_INDEX:
                    this.requestsFragment.onBackPressed();
                    break;
                case NOTIFICATIONS_TAB_INDEX:
                    this.notificationsFragment.onBackPressed();
                    break;
                default:
                    break;

            }
        }
//        } else {
//            //super.onBackPressed();
//        }
    }

}
