package com.bob.bobguestapp.tools.theme;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

import java.util.HashMap;

public class MyAppThemeUtilsManager extends ThemeUtilsManager {

    //theme parts
    public static final int INTRO_ACTIVITY_SKIN = 0;
    public static final int LOGIN_FRAGMENT_SKIN = 1;
    public static final int REGISTER_FRAGMENT_SKIN = 2;
    public static final int AUTHENTICATION_FRAGMENT_SKIN = 3;
    public static final int EMAIL_AUTHENTICATION_FRAGMENT_SKIN = 4;
    public static final int SMS_AUTHENTICATION_FRAGMENT_SKIN = 5;
    public static final int RESET_PASSWORD_FRAGMENT_SKIN = 6;

    public static final int MAIN_ACTIVITY_SKIN = 100;
    public static final int MAIN_ACTIVITY_NAVIGATION_DRAWER_SKIN = 101;

    public static final int MENU_FRAGMENT_SKIN = 102;
    public static final int MENU_FRAGMENT_MENU_SKIN = 1020;
    public static final int MENU_FRAGMENT_MENU_DIALOG_SKIN = 1021;

    public static final int REQUESTS_FRAGMENT_SKIN = 103;
    public static final int REQUESTS_FRAGMENT_SORT_DIALOG_SKIN = 1030;
    public static final int REQUESTS_FRAGMENT_FILTER_DIALOG_SKIN = 1031;
    public static final int REQUEST_LIST_ITEM_SKIN = 1032;
    public static final int REQUEST_LIST_ITEM_MENU_ITEM_SKIN = 1033;
    public static final int REQUEST_LIST_ITEM_CHANGE_STATUS_DIALOG_SKIN = 1034;
    public static final int REQUEST_LIST_ITEM_REQUEST_DETAILS_DIALOG_SKIN = 1035;


    public static final int NOTIFICATIONS_FRAGMENT_SKIN = 104;
    public static final int NOTIFICATION_LIST_ITEM_SKIN = 1040;
    public static final int NOTIFICATION_LIST_ITEM_MENU_ITEM_SKIN = 1041;

    public static final int PROFILE_FRAGMENT_SKIN = 105;
    public static final int PROFILE_FRAGMENT_CHANGE_PICTURE_BOTTOM_SHEET_SKIN = 1050;
    public static final int PROFILE_INFO_FRAGMENT_SKIN = 1051;
    public static final int PROFILE_INFO_FRAGMENT_DIALOG_SKIN = 1052;

    public static final int CHECK_IN_ACTIVITY_SKIN = 106;

    public static final int QR_SCANNER_ACTIVITY_SKIN = 200;

    public static final int GUEST_CODES_ACTIVITY_SKIN = 300;


    //colors keys
    //progress bar
    public static final int DEFAULT_CIRCLE_PROGRESS_TIMER_FILL_CIRCLE_COLOR = 5000;
    public static final int DEFAULT_CIRCLE_PROGRESS_TIMER_RIM_COLOR = 5001;
    public static final int DEFAULT_CIRCLE_PROGRESS_TIMER_TEXT_COLOR = 5002;
    public static final int DEFAULT_CIRCLE_PROGRESS_TIMER_UNIT_COLOR = 5003;
    public static final int DEFAULT_CIRCLE_PROGRESS_TIMER_INNER_COLOR = 5004;
    public static final int DEFAULT_CIRCLE_PROGRESS_TIMER_OUTER_COLOR = 5005;
    public static final int DEFAULT_CIRCLE_PROGRESS_TIMER_SPIN_BAR_COLOR = 5006;
    public static final int DEFAULT_CIRCLE_PROGRESS_TIMER_BAR_COLOR = 5007;

    //menu item
    public static final int DEFAULT_MENU_ITEM_BACKGROUND_COLOR_PRIMARY = 5100;
    public static final int DEFAULT_MENU_ITEM_BACKGROUND_COLOR_SECONDARY = 201;
    public static final int DEFAULT_MENU_ITEM_BACKGROUND_PRESSED_COLOR_PRIMARY = 5102;
    public static final int DEFAULT_MENU_ITEM_BACKGROUND_PRESSED_COLOR_SECONDARY = 5103;
    public static final int DEFAULT_MENU_ITEM_BACKGROUND_DISABLED_COLOR_PRIMARY = 5104;
    public static final int DEFAULT_MENU_ITEM_BACKGROUND_DISABLED_COLOR_SECONDARY = 5105;
    public static final int DEFAULT_MENU_ITEM_TEXT_COLOR = 5106;
    public static final int DEFAULT_MENU_ITEM_PRESSED_TEXT_COLOR = 5107;
    public static final int DEFAULT_MENU_ITEM_DISABLED_TEXT_COLOR = 5108;

    public static final int DEFAULT_QR_BITMAP_PRIMARY_COLOR = 5200;
    public static final int DEFAULT_QR_BITMAP_SECONDARY_COLOR = 5201;

    //progress bar
    public static final int DEFAULT_PROGRESS_BAR_BACKGROUND_PRIMARY_COLOR = 5300;
    public static final int DEFAULT_PROGRESS_BAR_BACKGROUND_SECONDARY_COLOR = 5301;
    public static final int DEFAULT_PROGRESS_BAR_PRIMARY_COLOR = 5302;
    public static final int DEFAULT_PROGRESS_BAR_SECONDARY_COLOR = 5303;
    public static final int DEFAULT_CIRCULAR_PROGRESS_BAR_BACKGROUND_PRIMARY_COLOR = 5304;
    public static final int DEFAULT_CIRCULAR_PROGRESS_BAR_BACKGROUND_SECONDARY_COLOR = 5305;
    public static final int DEFAULT_CIRCULAR_PROGRESS_BAR_PRIMARY_COLOR = 5306;
    public static final int DEFAULT_CIRCULAR_PROGRESS_BAR_SECONDARY_COLOR = 5307;



    //initialization
    protected MyAppThemeUtilsManager(Context context) {
        super(context);
        this.initDefaultTheme();
    }

    public static ThemeUtilsManager get(Context context) {
        if (instance == null) {
            instance = new MyAppThemeUtilsManager(context);
        }
        return instance;
    }

    public static ThemeUtilsManager get() {
        if (instance == null) {
            instance = new MyAppThemeUtilsManager(BOBGuestApplication.get());
        }
        return instance;
    }

    //themes
    @Override
    protected void customDefaultTheme(HashMap<Integer, Integer> defaultTheme ) {

        //intro activity
        defaultTheme.put(INTRO_ACTIVITY_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(LOGIN_FRAGMENT_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(REGISTER_FRAGMENT_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(AUTHENTICATION_FRAGMENT_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(EMAIL_AUTHENTICATION_FRAGMENT_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(SMS_AUTHENTICATION_FRAGMENT_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(RESET_PASSWORD_FRAGMENT_SKIN, PRIMARY_COLOR_SKIN);

        //main activity
        defaultTheme.put(MAIN_ACTIVITY_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(MAIN_ACTIVITY_NAVIGATION_DRAWER_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(MENU_FRAGMENT_SKIN, LIGHT_COLOR_SKIN);
        defaultTheme.put(MENU_FRAGMENT_MENU_SKIN, LIGHT_COLOR_SKIN);
        defaultTheme.put(MENU_FRAGMENT_MENU_DIALOG_SKIN, LIGHT_COLOR_SKIN);
        defaultTheme.put(REQUESTS_FRAGMENT_SKIN, LIGHT_COLOR_SKIN);
        defaultTheme.put(REQUESTS_FRAGMENT_SORT_DIALOG_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(REQUESTS_FRAGMENT_FILTER_DIALOG_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(REQUEST_LIST_ITEM_SKIN, LIGHT_COLOR_SKIN);
        defaultTheme.put(REQUEST_LIST_ITEM_MENU_ITEM_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(REQUEST_LIST_ITEM_CHANGE_STATUS_DIALOG_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(REQUEST_LIST_ITEM_REQUEST_DETAILS_DIALOG_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(NOTIFICATIONS_FRAGMENT_SKIN, LIGHT_COLOR_SKIN);
        defaultTheme.put(NOTIFICATION_LIST_ITEM_SKIN, LIGHT_COLOR_SKIN);
        defaultTheme.put(NOTIFICATION_LIST_ITEM_MENU_ITEM_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(PROFILE_FRAGMENT_SKIN, LIGHT_COLOR_SKIN);
        defaultTheme.put(PROFILE_FRAGMENT_CHANGE_PICTURE_BOTTOM_SHEET_SKIN, PRIMARY_COLOR_SKIN);
        defaultTheme.put(PROFILE_INFO_FRAGMENT_SKIN, LIGHT_COLOR_SKIN);
        defaultTheme.put(PROFILE_INFO_FRAGMENT_DIALOG_SKIN, PRIMARY_COLOR_SKIN);

        //check in activity
        defaultTheme.put(CHECK_IN_ACTIVITY_SKIN, PRIMARY_COLOR_SKIN);

        //qr scanner activity
        defaultTheme.put(QR_SCANNER_ACTIVITY_SKIN, PRIMARY_COLOR_SKIN);

        //guest code activity
        defaultTheme.put(GUEST_CODES_ACTIVITY_SKIN, PRIMARY_COLOR_SKIN);

        //default menu
        defaultTheme.put(DEFAULT_MENU_SKIN, LIGHT_COLOR_SKIN);
        defaultTheme.put(DEFAULT_MENU_DIALOG_SKIN, LIGHT_COLOR_SKIN);


    }
    
    //skins
    @Override
    protected void customPrimaryColorSkin(Context context, HashMap<Integer, Integer> primaryColorSkin) {

        //progress bar
        primaryColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_FILL_CIRCLE_COLOR , ContextCompat.getColor(context, R.color.light_primary_color));
        primaryColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_RIM_COLOR , ContextCompat.getColor(context, R.color.light_primary_color));
        primaryColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_TEXT_COLOR , ContextCompat.getColor(context, R.color.primary_color));
        primaryColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_UNIT_COLOR , ContextCompat.getColor(context, R.color.primary_color));
        primaryColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_INNER_COLOR , ContextCompat.getColor(context, R.color.light_primary_color));
        primaryColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_OUTER_COLOR , ContextCompat.getColor(context, R.color.light_primary_color));
        primaryColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_SPIN_BAR_COLOR , ContextCompat.getColor(context, R.color.primary_color));
        primaryColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_BAR_COLOR , ContextCompat.getColor(context, R.color.primary_color));

        //menu item
        primaryColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_COLOR_PRIMARY, ContextCompat.getColor(context, R.color.primary_color));
        primaryColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_COLOR_SECONDARY, ContextCompat.getColor(context, R.color.secondary_color));
        primaryColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_PRESSED_COLOR_PRIMARY, ContextCompat.getColor(context, R.color.light_primary_color));
        primaryColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_PRESSED_COLOR_SECONDARY, ContextCompat.getColor(context, R.color.light_secondary_color));
        primaryColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_DISABLED_COLOR_PRIMARY, ContextCompat.getColor(context, R.color.primary_color_opacity));
        primaryColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_DISABLED_COLOR_SECONDARY, ContextCompat.getColor(context, R.color.secondary_color_opacity));
        primaryColorSkin.put(DEFAULT_MENU_ITEM_TEXT_COLOR, ContextCompat.getColor(context, R.color.light_primary_color));
        primaryColorSkin.put(DEFAULT_MENU_ITEM_PRESSED_TEXT_COLOR, ContextCompat.getColor(context, R.color.primary_color));
        primaryColorSkin.put(DEFAULT_MENU_ITEM_DISABLED_TEXT_COLOR, ContextCompat.getColor(context, R.color.light_primary_color_opacity));

        //qr bitmap
        primaryColorSkin.put(DEFAULT_QR_BITMAP_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.light_primary_color));
        primaryColorSkin.put(DEFAULT_QR_BITMAP_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.primary_color));

        //progress bar
        primaryColorSkin.put(DEFAULT_PROGRESS_BAR_BACKGROUND_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.primary_color));
        primaryColorSkin.put(DEFAULT_PROGRESS_BAR_BACKGROUND_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.secondary_color));
        primaryColorSkin.put(DEFAULT_PROGRESS_BAR_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.light_primary_color));
        primaryColorSkin.put(DEFAULT_PROGRESS_BAR_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.light_secondary_color));

        //circular progress bar
        primaryColorSkin.put(DEFAULT_CIRCULAR_PROGRESS_BAR_BACKGROUND_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.primary_color));
        primaryColorSkin.put(DEFAULT_CIRCULAR_PROGRESS_BAR_BACKGROUND_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.secondary_color));
        primaryColorSkin.put(DEFAULT_CIRCULAR_PROGRESS_BAR_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.light_primary_color));
        primaryColorSkin.put(DEFAULT_CIRCULAR_PROGRESS_BAR_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.light_secondary_color));

    }

    @Override
    protected void customLightColorSkin(Context context, HashMap<Integer, Integer> lightColorSkin) {

        //progress bar
        lightColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_FILL_CIRCLE_COLOR , ContextCompat.getColor(context, R.color.primary_color));
        lightColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_RIM_COLOR , ContextCompat.getColor(context, R.color.primary_color));
        lightColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_TEXT_COLOR , ContextCompat.getColor(context, R.color.light_primary_color));
        lightColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_UNIT_COLOR , ContextCompat.getColor(context, R.color.light_primary_color));
        lightColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_INNER_COLOR , ContextCompat.getColor(context, R.color.primary_color));
        lightColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_OUTER_COLOR , ContextCompat.getColor(context, R.color.primary_color));
        lightColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_SPIN_BAR_COLOR , ContextCompat.getColor(context, R.color.light_primary_color));
        lightColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_BAR_COLOR , ContextCompat.getColor(context, R.color.light_primary_color));

        //menu item
        lightColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_COLOR_PRIMARY, ContextCompat.getColor(context, R.color.light_primary_color));
        lightColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_COLOR_SECONDARY, ContextCompat.getColor(context, R.color.light_secondary_color));
        lightColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_PRESSED_COLOR_PRIMARY, ContextCompat.getColor(context, R.color.primary_color));
        lightColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_PRESSED_COLOR_SECONDARY, ContextCompat.getColor(context, R.color.secondary_color));
        lightColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_DISABLED_COLOR_PRIMARY, ContextCompat.getColor(context, R.color.light_primary_color_opacity));
        lightColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_DISABLED_COLOR_SECONDARY, ContextCompat.getColor(context, R.color.light_secondary_color_opacity));
        lightColorSkin.put(DEFAULT_MENU_ITEM_TEXT_COLOR, ContextCompat.getColor(context, R.color.primary_color));
        lightColorSkin.put(DEFAULT_MENU_ITEM_PRESSED_TEXT_COLOR, ContextCompat.getColor(context, R.color.light_primary_color));
        lightColorSkin.put(DEFAULT_MENU_ITEM_DISABLED_TEXT_COLOR, ContextCompat.getColor(context, R.color.primary_color_opacity));

        //qr bitmap
        lightColorSkin.put(DEFAULT_QR_BITMAP_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.primary_color));
        lightColorSkin.put(DEFAULT_QR_BITMAP_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.light_primary_color));

        //progress bar
        lightColorSkin.put(DEFAULT_PROGRESS_BAR_BACKGROUND_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.light_primary_color));
        lightColorSkin.put(DEFAULT_PROGRESS_BAR_BACKGROUND_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.light_secondary_color));
        lightColorSkin.put(DEFAULT_PROGRESS_BAR_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.primary_color));
        lightColorSkin.put(DEFAULT_PROGRESS_BAR_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.secondary_color));

        //circular progress bar
        lightColorSkin.put(DEFAULT_CIRCULAR_PROGRESS_BAR_BACKGROUND_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.light_primary_color));
        lightColorSkin.put(DEFAULT_CIRCULAR_PROGRESS_BAR_BACKGROUND_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.light_secondary_color));
        lightColorSkin.put(DEFAULT_CIRCULAR_PROGRESS_BAR_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.primary_color));
        lightColorSkin.put(DEFAULT_CIRCULAR_PROGRESS_BAR_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.secondary_color));

    }

    @Override
    protected void customDarkColorSkin(Context context, HashMap<Integer, Integer> darkColorSkin) {

        //progress bar
        darkColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_FILL_CIRCLE_COLOR , ContextCompat.getColor(context, R.color.light_primary_color));
        darkColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_RIM_COLOR , ContextCompat.getColor(context, R.color.light_primary_color));
        darkColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_TEXT_COLOR , ContextCompat.getColor(context, R.color.primary_color));
        darkColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_UNIT_COLOR , ContextCompat.getColor(context, R.color.primary_color));
        darkColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_INNER_COLOR , ContextCompat.getColor(context, R.color.light_primary_color));
        darkColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_OUTER_COLOR , ContextCompat.getColor(context, R.color.light_primary_color));
        darkColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_SPIN_BAR_COLOR , ContextCompat.getColor(context, R.color.primary_color));
        darkColorSkin.put(DEFAULT_CIRCLE_PROGRESS_TIMER_BAR_COLOR , ContextCompat.getColor(context, R.color.primary_color));

        //menu item
        darkColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_COLOR_PRIMARY, ContextCompat.getColor(context, R.color.dark_primary_color));
        darkColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_COLOR_SECONDARY, ContextCompat.getColor(context, R.color.dark_secondary_color));
        darkColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_PRESSED_COLOR_PRIMARY, ContextCompat.getColor(context, R.color.primary_color));
        darkColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_PRESSED_COLOR_SECONDARY, ContextCompat.getColor(context, R.color.secondary_color));
        darkColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_DISABLED_COLOR_PRIMARY, ContextCompat.getColor(context, R.color.dark_primary_color_opacity));
        darkColorSkin.put(DEFAULT_MENU_ITEM_BACKGROUND_DISABLED_COLOR_SECONDARY, ContextCompat.getColor(context, R.color.dark_secondary_color_opacity));
        darkColorSkin.put(DEFAULT_MENU_ITEM_TEXT_COLOR, ContextCompat.getColor(context, R.color.primary_color));
        darkColorSkin.put(DEFAULT_MENU_ITEM_PRESSED_TEXT_COLOR, ContextCompat.getColor(context, R.color.dark_primary_color));
        darkColorSkin.put(DEFAULT_MENU_ITEM_DISABLED_TEXT_COLOR, ContextCompat.getColor(context, R.color.primary_color_opacity));

        //qr bitmap
        darkColorSkin.put(DEFAULT_QR_BITMAP_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.light_primary_color));
        darkColorSkin.put(DEFAULT_QR_BITMAP_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.dark_primary_color));

        //progress bar
        darkColorSkin.put(DEFAULT_PROGRESS_BAR_BACKGROUND_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.dark_primary_color));
        darkColorSkin.put(DEFAULT_PROGRESS_BAR_BACKGROUND_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.dark_secondary_color));
        darkColorSkin.put(DEFAULT_PROGRESS_BAR_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.primary_color));
        darkColorSkin.put(DEFAULT_PROGRESS_BAR_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.secondary_color));

        //circular progress bar
        darkColorSkin.put(DEFAULT_CIRCULAR_PROGRESS_BAR_BACKGROUND_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.dark_primary_color));
        darkColorSkin.put(DEFAULT_CIRCULAR_PROGRESS_BAR_BACKGROUND_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.dark_secondary_color));
        darkColorSkin.put(DEFAULT_CIRCULAR_PROGRESS_BAR_PRIMARY_COLOR, ContextCompat.getColor(context, R.color.primary_color));
        darkColorSkin.put(DEFAULT_CIRCULAR_PROGRESS_BAR_SECONDARY_COLOR, ContextCompat.getColor(context, R.color.secondary_color));

    }

    //navigation drawer
    public void initProfileDrawerItem(ProfileDrawerItem profileDrawerItem, long identifier, String name, String email, String icon, int skinKey) {

        profileDrawerItem
                .withIdentifier(identifier)
                .withEmail(email)
                .withName(name)
                .withIcon(icon)
                .withTextColor(this.getColor(DEFAULT_NAVIGATION_DRAWER_PRIMARY_ITEM_TEXT_COLOR, skinKey))
                .withSelectedColor(this.getColor(DEFAULT_NAVIGATION_DRAWER_PRIMARY_ITEM_SELECTED_BACKGROUND_COLOR_PRIMARY, skinKey))
                .withSelectedTextColor(this.getColor(DEFAULT_NAVIGATION_DRAWER_PRIMARY_ITEM_SELECTED_TEXT_COLOR, skinKey))
                .withDisabledTextColor(this.getColor(DEFAULT_NAVIGATION_DRAWER_PRIMARY_ITEM_DISABLED_TEXT_COLOR, skinKey))
                .withNameShown(true);

    }


}