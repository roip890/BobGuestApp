package com.bob.bobguestapp.activities.codes;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.codes.codeslist.CodesListAdapter;
import com.bob.bobguestapp.tools.database.MyRealmController;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.database.objects.GuestCode;
import com.bob.uimodule.others.BackgroundColorTimer;
import com.bob.uimodule.views.loadingcontainer.ManagementActivity;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;


public class GuestCodesActivity extends ManagementActivity {


    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //app theme
    int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    int screenSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;

    //codes list finals
    private static final int CODES_BATCH = 10;

    //screen states
    private static final int CODES_LIST = 10;

    //background
    private ConstraintLayout backgroundLayout;
    private int backgroundColorPrimary, backgroundColorLight;
    private BackgroundColorTimer backgroundColorTimer;

    //codes layout
    private ConstraintLayout codesLayout;
    private RecyclerView codesRecyclerView;
    private CodesListAdapter codesListAdapter;
    private ArrayList<GuestCode> codesList;




    @Override
    protected View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this).getSkin(appTheme, MyAppThemeUtilsManager.GUEST_CODES_ACTIVITY_SKIN);

        //background
        this.initBackgroundAnimation();

        //view
        View view = inflater.inflate(R.layout.activity_guest_codes, container, true);

        //init codes list
        this.initCodesList();

        //layouts
        this.initCodesLayout(view);

        //status bar color
        this.initStatusBarColor();

        this.managementViewContainer.setScreenState(CODES_LIST);


        //COMMENT THIS!!
        //testing demo codes
        this.testingCodesList();

        //COMMENT THIS!!
        //init guest requests
        this.setGuestCodes();

        return view;

    }

    //management
    private void initBackgroundAnimation() {

        this.backgroundColorPrimary = MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_SCREEN_BACKGROUND_COLOR_PRIMARY, this.screenSkin);
        this.backgroundColorLight = MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_SCREEN_BACKGROUND_COLOR_PRIMARY, MyAppThemeUtilsManager.LIGHT_COLOR_SKIN);

        this.backgroundColorTimer = new BackgroundColorTimer(this.backgroundLayout, this.backgroundColorPrimary, this.backgroundColorPrimary);
        this.backgroundColorTimer.setColorIntervals(1, 0, 0, 1, 0);
        this.backgroundColorTimer.setColorValuesRange(1, 0, 0, 30, 0);

    }

    private void initStatusBarColor() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(this.getResources().getColor(R.color.transparent));
                window.setNavigationBarColor(MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_STATUS_BAR_BACKGROUND_COLOR_PRIMARY, this.screenSkin));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void setMainViewScreenState(int screenState) {

        this.codesLayout.setVisibility(View.INVISIBLE);

        switch (screenState) {

            case CODES_LIST:
                this.codesLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //codes layout
    private void initCodesList() {
        this.codesList = new ArrayList<GuestCode>();
    }

    private void initCodesLayout(View view) {

        this.codesLayout = (ConstraintLayout) view.findViewById(R.id.guest_codes_activity_codes_layout);

        this.initCodesLayoutBackground();

        this.initCodesRecyclerView(view);

    }

    private void initCodesLayoutBackground() {

        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setColors(new int[]{
                MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_ACTIVITY_BACKGROUND_COLOR_PRIMARY, this.screenSkin),
                MyAppThemeUtilsManager.get().getColor(MyAppThemeUtilsManager.DEFAULT_ACTIVITY_BACKGROUND_COLOR_SECONDARY, this.screenSkin)
        });
        shapeDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        shapeDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        shapeDrawable.setCornerRadius(0f);
        this.codesLayout.setBackground(shapeDrawable);

    }

    private void initCodesRecyclerView(View view) {

        this.codesRecyclerView = (RecyclerView) findViewById(R.id.guest_codes_activity_codes_recycler_view);

        //divider line
        this.initCodesRecyclerViewDividerLine();

        //set adapter
        this.setAdapter();

    }

    private void initCodesRecyclerViewDividerLine() {

        DividerItemDecoration horizontalDecorationOpacity = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        Drawable horizontalDividerOpacity = ContextCompat.getDrawable(this, R.drawable.horizontal_divider).mutate();
        horizontalDividerOpacity.setColorFilter(new PorterDuffColorFilter(
                MyAppThemeUtilsManager.get(this).getColor(MyAppThemeUtilsManager.DEFAULT_RECYCLER_VIEW_SEPARATOR_COLOR, this.screenSkin)
                ,PorterDuff.Mode.SRC_ATOP));
        horizontalDecorationOpacity.setDrawable(horizontalDividerOpacity);
        this.codesRecyclerView.addItemDecoration(horizontalDecorationOpacity);

    }

    private void setAdapter() {
        if (this.codesList != null) {
            this.codesListAdapter = new CodesListAdapter(this, this.codesList);
        } else {
            this.codesListAdapter  = new CodesListAdapter(this, new ArrayList<GuestCode>());
        }
        this.codesRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        this.codesRecyclerView.setAdapter(this.codesListAdapter);
    }

    private void setGuestCodes() {
        long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1L);
        if (guestId != -1L) {
            List<GuestCode> guestCodes = MyRealmController.with(BOBGuestApplication.get()).getGuestCodesOfGuest(guestId);
            if ((guestCodes != null) && (guestCodes.size() >0)) {
                this.codesRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                this.codesListAdapter.setCodesList();
                this.codesRecyclerView.setAdapter(this.codesListAdapter);
            } else {

            }

        }
    }

    //TEMP FUNCTION
    private void testingCodesList() {
        //CLEAR DB
        MyRealmController.with(this).deleteAllGuestCodes();

        //PAGE
        long guestId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("guestId", -1L);
        int codeId = 0;
        this.createCode(guestId, codeId++, "Roi", "roi_peretz", "connected");
        this.createCode(guestId, codeId++, "Tomer", "tomer_rahamim", "disconnected");

    }
    void createCode(long guestId, int id, String title, String code, String status) {
        GuestCode guestCode= new GuestCode();
        guestCode.setId(id);
        guestCode.setTitle(title);
        guestCode.setGuestId(guestId);
        guestCode.setStatus(status);
        guestCode.setCode(code);
        MyRealmController.with(this).insertGuestCode(guestCode);
    }

}
