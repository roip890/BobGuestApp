package com.bob.bobguestapp.activities.main.fragments.social.postupload.media;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.content.PostContentActivity;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.connector.PostMediaConnector;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.camera.PostCameraFragment;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.PostGalleryFragment;
import com.bob.bobguestapp.tools.database.objects.Post;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.toolsmodule.parsing.GsonParser;
import com.bob.uimodule.drawable.DrawableHelper;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementActivity;
import com.bob.uimodule.views.viewpager.RtlViewPager;
import com.bob.uimodule.views.viewpager.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static android.view.View.VISIBLE;
import static com.bob.bobguestapp.activities.main.fragments.social.postupload.content.PostContentActivity.ARG_EXTRA_POST_CONTENT_POST_JSON;
import static com.bob.bobguestapp.activities.main.fragments.social.postupload.content.PostContentActivity.RESPONSE_UPLOADED_POST;
import static com.google.android.material.tabs.TabLayout.INDICATOR_GRAVITY_TOP;


public class PostMediaActivity extends ManagementActivity implements PostMediaConnector {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //screen states
    private static final int MAIN_VIEW = 3;
    private static final int GALLERY = 4;
    private static final int CAMERA = 5;

    //tab indices
    private static final int GALLERY_TAB_INDEX = 0;
    private static final int CAMERA_TAB_INDEX = 1;
    private static final int TAB_COUNTS = 2;

    //main view
    private TabLayout tabLayout;
    private RtlViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ArrayList<Integer> tabIcons;
    private ArrayList<Fragment> tabFragments;
    private RelativeLayout mainViewLayout;

    //gallery fragment
    private PostGalleryFragment galleryFragment;

    //camera fragment
    private PostCameraFragment cameraFragment;

    public PostMediaActivity() {

        //theme
        this.appTheme = ThemeUtilsManager.DEFAULT_THEME;
        this.screenSkin = ThemeUtilsManager.PRIMARY_COLOR_SKIN;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.managementViewContainer.setScreenState(GALLERY);

    }
    

    @Override
    protected View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {

        //background

        //view
        View view = inflater.inflate(R.layout.activity_post_media, container, false);

        //layouts
        this.initMainViewLayout(view);

        //portrait only
        this.initPortraitOnly();

        //status bar color
        this.initStatusBarColor();

        //navigation bar color
        this.initNavigationBarColor();

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("onActivityResultYo", "PostMediaActivity - ReqCode: " + Integer.toString(requestCode)
                + "ResCode: " + Integer.toString(resultCode));

         if (requestCode == RESPONSE_UPLOADED_POST) {

             if(resultCode == Activity.RESULT_OK){

                 if (data.hasExtra(ARG_EXTRA_POST_CONTENT_POST_JSON)) {

                     String postJson = data.getStringExtra(ARG_EXTRA_POST_CONTENT_POST_JSON);

                     try {

                         Post post = GsonParser.getParser().create().fromJson(postJson, Post.class);

                         if (post != null) {

                             postJson = GsonParser.getParser().create().toJson(post, Post.class);

                             Intent returnIntent = new Intent();

                             returnIntent.putExtra(PostContentActivity.ARG_EXTRA_POST_CONTENT_POST_JSON, postJson);

                             PostMediaActivity.this.setResult(RESULT_OK,returnIntent);

                             PostMediaActivity.this.finishActivity(RESPONSE_UPLOADED_POST);

                             PostMediaActivity.this.finish();

                         }

                     } catch (Exception e) {

                         e.printStackTrace();

                     }

                 }

             } else if (resultCode == Activity.RESULT_CANCELED) {

                 //Write your code if there's no result

             }

        }

    }


    private void initPortraitOnly() {

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    private void initStatusBarColor() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_color));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initNavigationBarColor() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.primary_color));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //management
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.mainViewLayout.setVisibility(View.INVISIBLE);

        switch (screenState) {

            case MAIN_VIEW:
                this.mainViewLayout.setVisibility(VISIBLE);
                break;
            case GALLERY:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setGalleryState();
                break;
            case CAMERA:
                this.mainViewLayout.setVisibility(VISIBLE);
                this.setCameraState();
                break;
            default:
                break;

        }

    }

    protected void setGalleryState() {

        this.setViewPagerIndex(GALLERY_TAB_INDEX);

    }

    protected void setCameraState() {

        this.setViewPagerIndex(CAMERA_TAB_INDEX);

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

    //main view
    private void initMainViewLayout(View view) {

        //main view
        this.initMainViewBackground(view);

        //fragments
        this.initFragments();

        //tab layout
        this.initTabLayout(view);

        //view pager
        this.initViewPager(view);

    }

    private void initMainViewBackground(View view) {

        //main view background
        this.mainViewLayout = (RelativeLayout) view.findViewById(R.id.post_media_activity_background);

        //main view background animation
        this.initMainViewBackgroundDrawable();

    }

    private void initMainViewBackgroundDrawable() {

        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setColors(new int[]{
                ThemeUtilsManager.get(this).getColor(ThemeUtilsManager.DEFAULT_ACTIVITY_BACKGROUND_COLOR_PRIMARY, this.screenSkin),
                ThemeUtilsManager.get(this).getColor(ThemeUtilsManager.DEFAULT_ACTIVITY_BACKGROUND_COLOR_SECONDARY, this.screenSkin)
        });
        shapeDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        shapeDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        shapeDrawable.setCornerRadius(0f);
        this.managementViewContainer.getBackgroundLayout().setBackground(shapeDrawable);

    }

    private void initFragments() {

        this.galleryFragment = new PostGalleryFragment();
        this.galleryFragment.setPostMediaConnector(this);

        this.cameraFragment = new PostCameraFragment();
        this.cameraFragment.setPostMediaConnector(this);

    }

    private void initTabLayout(View view) {

        this.tabLayout = (TabLayout) view.findViewById(R.id.post_media_activity_tabs);

        //tab layout color
        this.tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_color));

        this.tabLayout.setSelectedTabIndicatorGravity(INDICATOR_GRAVITY_TOP);

        //tabs fragments list
        this.initFragmentsList();

    }

    private void initFragmentsList() {

        this.tabFragments = new ArrayList<Fragment>();
        this.tabFragments.add(GALLERY_TAB_INDEX, this.galleryFragment);
        this.tabFragments.add(CAMERA_TAB_INDEX, this.cameraFragment);

    }

    private void initViewPager(View view) {

        this.viewPager = (RtlViewPager) view.findViewById(R.id.post_media_activity_view_pager);

        //adapter
        this.viewPagerAdapter = new ViewPagerAdapter(this.getSupportFragmentManager());
        this.viewPagerAdapter.addFragment(this.galleryFragment, "");
        this.viewPagerAdapter.addFragment(this.cameraFragment, "");


        this.viewPager.setAdapter(this.viewPagerAdapter);

        /* the ViewPager requires a minimum of 1 as OffscreenPageLimit */
        int limit = (this.viewPagerAdapter.getCount() > 1 ? this.viewPagerAdapter.getCount() - 1 : 1);
        this.viewPager.setOffscreenPageLimit(limit);

        this.tabLayout.post(new Runnable() {
            @Override
            public void run() {

                PostMediaActivity.this.tabLayout.setupWithViewPager(PostMediaActivity.this.viewPager);

                //tabs icons list
                PostMediaActivity.this.initTabIconsList();

                //tabs colors
                PostMediaActivity.this.initTabsColors();

                //tabs on selected listener
                PostMediaActivity.this.initTabsOnSelectedListener();


            }
        });

    }

    private void initTabIconsList() {

        this.tabIcons = new ArrayList<Integer>();
        this.tabIcons.add(GALLERY_TAB_INDEX, R.drawable.ic_photo_library_white_24dp);
        this.tabIcons.add(CAMERA_TAB_INDEX, R.drawable.ic_camera_white_24dp);

    }

    private void initTabsColors() {

        this.tabLayout.post(new Runnable() {
            @Override
            public void run() {

                //tabs text colors
//                PostMediaActivity.this.tabLayout.setTabTextColors(
//                        ContextCompat.getColor(PostMediaActivity.this, R.color.light_primary_color),
//                        ContextCompat.getColor(PostMediaActivity.this, R.color.light_primary_color_half_opacity)
//                );

                //tabs indicator color
                PostMediaActivity.this.tabLayout.setSelectedTabIndicatorColor(
                        ContextCompat.getColor(PostMediaActivity.this, R.color.transparent)
                );

                //tabs icons color
                for (int i = 0; i < TAB_COUNTS; i++) {

                    Drawable tabIconDrawable = DrawableHelper.withContext(PostMediaActivity.this)
                            .withDrawable(tabIcons.get(i))
                            .withColor(
                                    ContextCompat.getColor(PostMediaActivity.this, R.color.light_primary_color_half_opacity)
                            )
                            .tint()
                            .get();
                    PostMediaActivity.this.tabLayout.getTabAt(i).setIcon(tabIconDrawable);

                    PostMediaActivity.this.tabLayout.getTabAt(i).getIcon().setColorFilter(
                            ContextCompat.getColor(PostMediaActivity.this, R.color.light_primary_color_half_opacity),
                            PorterDuff.Mode.SRC_IN
                    );

                }

                if (TAB_COUNTS > 0) {

                    PostMediaActivity.this.tabLayout.getTabAt(0).getIcon().setColorFilter(
                            ContextCompat.getColor(PostMediaActivity.this, R.color.light_primary_color),
                            PorterDuff.Mode.SRC_IN
                    );

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

                        tab.getIcon().setColorFilter(
                                ContextCompat.getColor(PostMediaActivity.this, R.color.light_primary_color),
                                PorterDuff.Mode.SRC_IN
                        );

//                        PostMediaActivity.this.viewPager.setCurrentItem(tab.getPosition());
//                        Drawable tabIconDrawable = DrawableHelper.withContext(PostMediaActivity.this)
//                                .withDrawable(tabIcons.get(tab.getPosition()))
//                                .withColor(
//                                        ContextCompat.getColor(PostMediaActivity.this, R.color.light_primary_color_half_opacity)
//                                )
//                                .tint()
//                                .get();
//                        PostMediaActivity.this.tabLayout.getTabAt(tab.getPosition()).setIcon(tabIconDrawable);

                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);

                        tab.getIcon().setColorFilter(
                                ContextCompat.getColor(PostMediaActivity.this, R.color.light_primary_color_half_opacity),
                                PorterDuff.Mode.SRC_IN
                        );

//                        PostMediaActivity.this.viewPager.setCurrentItem(tab.getPosition());
//                        Drawable tabIconDrawable = DrawableHelper.withContext(PostMediaActivity.this)
//                                .withDrawable(tabIcons.get(tab.getPosition()))
//                                .withColor(
//                                        ContextCompat.getColor(PostMediaActivity.this, R.color.light_primary_color)
//                                )
//                                .tint()
//                                .get();
//                        PostMediaActivity.this.tabLayout.getTabAt(tab.getPosition()).setIcon(tabIconDrawable);

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

    }

    @Override
    public void disableViewPagerSwipe(boolean disable) {

        this.viewPager.disableSwipe(disable);

    }

    @Override
    public void continueToContentActivity(ArrayList<MediaItem> mediaItems) {

        Intent intent = new Intent(this, PostContentActivity.class);

        intent.putParcelableArrayListExtra("media_items", mediaItems);

        this.startActivityForResult(intent, RESPONSE_UPLOADED_POST);

    }

    @Override
    public void onBackPressed() {

        if (this.managementViewContainer.getScreenState() != MAIN_VIEW
                && this.managementViewContainer.getScreenState() != GALLERY
                && this.managementViewContainer.getScreenState() != CAMERA) {

            this.managementViewContainer.setScreenState(MAIN_VIEW);

        } else {

            switch (this.viewPager.getCurrentItem()) {
                case GALLERY_TAB_INDEX:
                    this.galleryFragment.onBackPressed();
                    super.onBackPressed();
                    break;
                case CAMERA_TAB_INDEX:
                    this.cameraFragment.onBackPressed();
                    super.onBackPressed();
                    break;
                default:
                    super.onBackPressed();
                    break;
            }

        }

    }
}
