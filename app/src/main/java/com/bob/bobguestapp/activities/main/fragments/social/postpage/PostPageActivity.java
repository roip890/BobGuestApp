package com.bob.bobguestapp.activities.main.fragments.social.postpage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.text.TextUtilsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.likespage.LikesListPageActivity;
import com.bob.bobguestapp.activities.main.fragments.social.postpage.comments.commentslist.adapter.PostPageCommentsListAdapter;
import com.bob.bobguestapp.activities.main.fragments.social.postpage.comments.commentslist.listener.CommentsListEventsListener;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.content.PostContentActivity;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.listener.PostsListEventsListener;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.adapter.SocialFeedPostsListMediaAdapter;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.cachemanager.SocialFeedPostsListCacheManager;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.container.OnMediaLoadListener;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.container.SocialFeedPostsListMediaContainer;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.layoutmanager.SocialFeedPostsListMediaLinearLayoutManager;
import com.bob.bobguestapp.tools.database.objects.Post;
import com.bob.bobguestapp.tools.database.objects.PostComment;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.toolsmodule.parsing.GsonParser;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.drawable.DrawableUtilsManager;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.popup.MyPowerMenu;
import com.bob.uimodule.popup.PowerMenuListener;
import com.bob.uimodule.recyclerview.MyRecyclerView;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.video.exoplayer.ExoPlayerFullScreenListener;
import com.bob.uimodule.views.loadingcontainer.ManagementActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mikepenz.iconics.IconicsDrawable;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnDismissedListener;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import im.ene.toro.PlayerSelector;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.PressablePlayerSelector;

import static android.view.View.VISIBLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.bob.bobguestapp.activities.main.fragments.social.postupload.content.PostContentActivity.ARG_EXTRA_POST_CONTENT_POST_JSON;
import static com.bob.bobguestapp.activities.main.fragments.social.postupload.content.PostContentActivity.RESPONSE_UPLOADED_POST;
import static com.bob.uimodule.recyclerview.MyRecyclerView.RECYCLER_VIEW;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.FAILURE_MESSAGE;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.LOADING;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.NONE_MESSAGE;

public class PostPageActivity extends ManagementActivity
        implements OnMediaLoadListener, PowerMenuListener, CommentsListEventsListener, PostsListEventsListener {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //urls
    private static final String GET_ALL_COMMENTS_OF_POST_URL = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/comment/page";
    private static final String LIKE_A_POST = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/like";
    private static final String UNLIKE_A_POST = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/unlike";
    private static final String GET_LIKES_COUNT = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/like/count";
    private static final String GET_COMMENTS_COUNT = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/comment/count";
    private static final String COMMENT_A_POST = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/comment";
    private static final String DELETE_A_COMMENT = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/comment";
    private static final String EDIT_A_COMMENT = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/comment";
    private static final String DELETE_A_POST = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/post";

    private static final int MAX_COMMENT_LENGTH = 100;
    private static final int MAX_COMMENT_LINES = 5;

    //post power menu items
    private static final int POST_ITEM_DROPDOWN_MENU_COPY_ID = 0;
    private static final int POST_ITEM_DROPDOWN_MENU_EDIT_ID = 1;
    private static final int POST_ITEM_DROPDOWN_MENU_DELETE_ID = 2;
    private static final int POST_ITEM_DROPDOWN_MENU_CANCEL_ID = 3;

    //post page arguments
    public static final String ARG_EXTRA_POST_PAGE_POST_JSON = "bob:mobile:postpage:postjson";  // Int

    //main view screen states
    public static final int POST = 10;

    //views
    private ConstraintLayout postPageLayout;

    //post layout
    private RelativeLayout postLayout;

    //post expandable layout
    private ExpandableLayout postExpandableLayout;

    //post header layout
    private RelativeLayout postHeaderLayout;
    private ImageView editorProfilePictureImageView;
    private TextView editorNameTextView;
    private TextView postTimestampTextView;
    protected ImageButton postMenuButton;
    protected View.OnTouchListener onTouchPostMenuButtonListener;

    //post text layout
    private RelativeLayout postTextLayout;
    private TextView postTextTextView;

    //post media layout
    private ConstraintLayout postMediaLayout;
    private SocialFeedPostsListMediaContainer postMediaContainer;
    private SocialFeedPostsListMediaAdapter postMediaContainerPlayerAdapter;
    private SocialFeedPostsListMediaLinearLayoutManager postMediaContainerLinearLayoutManager;
    private PressablePlayerSelector postMediaContainerPlayerSelector;
    private SnapHelper postMediaContainerSnapHelper;

    //post footer layout
    private RelativeLayout postFooterLayout;
    private RelativeLayout postFooterIconsLayout;
    private ImageView postLikesButtonImageView;
    private View.OnTouchListener onTouchPostLikesButtonListener;
    private ImageView postCommentsButtonImageView;
    private View.OnTouchListener onTouchPostCommentsButtonListener;
    private ImageView postExpandButtonImageView;
    private View.OnTouchListener onTouchPostExpandButtonListener;
    private ImageView postShareButtonImageView;
    private View.OnTouchListener onTouchPostShareButtonListener;
    private RelativeLayout postFooterTextsLayout;
    private TextView postLikesTextView;
    protected View.OnTouchListener onTouchPostLikesTextListener;
    private TextView postIntermediateTextView;
    private TextView postCommentsTextView;
    protected View.OnTouchListener onTouchPostCommentsTextListener;

    //popup
    private View popupAnchor;
    private MyPowerMenu longClickPowerMenu;
    private MyPowerMenu moreButtonPowerMenu;
    protected PowerMenuItem copyPostMenuItem;
    protected PowerMenuItem editPostMenuItem;
    protected PowerMenuItem deletePostMenuItem;
    protected PowerMenuItem cancelPostMenuItem;

    //comments
    private SwipeRefreshLayout postCommentsSwipeRefreshLayout;
    private MyRecyclerView postCommentsRecyclerView;
    private PostPageCommentsListAdapter postPageCommentsListAdapter;
    private ArrayList<PostComment> commentsList;
    private Post post;
    private PostComment postCommentToEdit;

    //comment input upper message
    private ExpandableLayout postCommentInputUpperMessageExpandableLayout;
    private RelativeLayout postCommentInputUpperMessageLayout;
    private TextView postCommentInputUpperMessageTextView;
    private ImageButton postCommentInputUpperMessageCloseButton;
    private View.OnTouchListener onTouchPostCommentInputUpperMessageCloseButtonListener;

    //comment input
    private RelativeLayout postCommentInputLayout;
    private RelativeLayout postCommentInputCommentTextLayout;
    private TextInputEditText postCommentInputCommentTextView;
    private ImageButton postCommentInputCommentButton;
    private View.OnTouchListener onTouchPostCommentInputCommentButtonListener;

    //current power menu
    private PowerMenu currentPowerMenu;

    //lock like
    protected boolean likeLocked = false;
    protected boolean commentLocked = false;

    //animations
    private Animation touchDownAnimation;
    private Animation touchUpAnimation;

    //exo full screen listener
    private ExoPlayerFullScreenListener exoPlayerFullScreenListener;

    public PostPageActivity() {

        //theme
        this.appTheme = ThemeUtilsManager.DEFAULT_THEME;
        this.screenSkin = ThemeUtilsManager.LIGHT_COLOR_SKIN;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.makeRefreshPostCommentsRequest();

        this.managementViewContainer.setScreenState(POST);

    }

    @Override
    protected View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {

        //background

        //view
        View view = inflater.inflate(R.layout.activity_post_page, container, false);

        //layouts
        this.initPostCommentsSwipeRefreshLayout(view);

        this.initMainViewLayout(view);

        //animation
        this.initAnimations();
        
        //status bar color
        this.initStatusBarColor();

        //navigation bar color
        this.initNavigationBarColor();

        //update post
        this.updatePost();

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

        Log.i("onActivityResultYo", "PostPageActivity - ReqCode: " + Integer.toString(requestCode)
                + "ResCode: " + Integer.toString(resultCode));

        if (requestCode == RESPONSE_UPLOADED_POST) {

            if(resultCode == Activity.RESULT_OK){

                if (data.hasExtra(ARG_EXTRA_POST_CONTENT_POST_JSON)) {

                    String postJson = data.getStringExtra(ARG_EXTRA_POST_CONTENT_POST_JSON);

                    try {

                        Post post = GsonParser.getParser().create().fromJson(postJson, Post.class);

                        if (post != null) {

                            this.post = post;

                            this.updatePost();

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

    @Override
    public void onBackPressed() {

        if (this.currentPowerMenu != null && this.currentPowerMenu.isShowing()) {

            this.currentPowerMenu.dismiss();

        } else if (this.postCommentToEdit != null
                && this.postCommentToEdit.getCommentId() != -1L) {

            this.onPostCommentInputUpperMessageCloseButtonClicked();

            this.postCommentToEdit = null;

        } else if (this.managementViewContainer.getScreenState() != POST) {

            this.managementViewContainer.setScreenState(POST);

        } else {

            if (this.post != null) {

                String postJson = GsonParser.getParser().create().toJson(this.post, Post.class);

                Intent returnIntent = new Intent();

                returnIntent.putExtra(PostContentActivity.ARG_EXTRA_POST_CONTENT_POST_JSON, postJson);

                PostPageActivity.this.setResult(RESULT_OK,returnIntent);

            }

            super.onBackPressed();

        }

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

    public int getNavigationBarSize() {

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

    //arguments
    protected void fillPostPageParameters(Bundle savedInstanceState) {

        Bundle bundle = savedInstanceState;

        if (bundle == null) bundle = getIntent().getExtras();

        if (bundle != null) {

            String postJson = bundle.getString(ARG_EXTRA_POST_PAGE_POST_JSON);

            try {

                this.post = GsonParser.getParser().create().fromJson(postJson, Post.class);

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

    //management
    @Override
    protected void preOnCreate() {


    }

    @Override
    protected void postOnCreate() {


    }

    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {

        this.fillPostPageParameters(savedInstanceState);

    }

    @Override
    protected void setMainViewScreenState(int screenState) {

        this.postPageLayout.setVisibility(View.INVISIBLE);

        switch (screenState) {

            case POST:
                this.postPageLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //init views
    protected void initMainViewLayout(View view) {

        this.postPageLayout = (ConstraintLayout) view.findViewById(R.id.post_page_activity_background_layout);

        this.initPostLayout(view);

        this.initPostCommentsRecyclerView(view);

        this.initPostCommentInputUpperMessageExpandableLayout(view);

        this.initPostCommentInputLayout(view);

    }

    private void initPostCommentsSwipeRefreshLayout(View view) {

        this.postCommentsSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.post_page_activity_comments_swipe_refresh_layout);

        this.postCommentsSwipeRefreshLayout.setColorSchemeColors(MyAppThemeUtilsManager.get(this).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_PRIMARY_COLOR,
                this.screenSkin
        ), MyAppThemeUtilsManager.get(this).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_SECONDARY_COLOR,
                this.screenSkin
        ));

        this.postCommentsSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(MyAppThemeUtilsManager.get(this).getColor(
                MyAppThemeUtilsManager.DEFAULT_CIRCULAR_PROGRESS_BAR_BACKGROUND_PRIMARY_COLOR,
                this.screenSkin
        ));

        this.postCommentsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                PostPageActivity.this.postCommentsRecyclerView.setScreenState(LOADING);
                PostPageActivity.this.makeRefreshPostCommentsRequest();

                PostPageActivity.this.postCommentsSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    //post layout
    private void initPostLayout(View view) {

        this.postLayout = (RelativeLayout) view.findViewById(R.id.post_page_activity_post_layout);

        this.initPostExpandableLayout(view);
        
        this.initPostFooterLayout(view);

    }

    private void initPostExpandableLayout(View view) {
        
        this.postExpandableLayout = (ExpandableLayout) view.findViewById(R.id.post_page_activity_post_expandable_layout);

        this.postExpandableLayout.setExpanded(false);

        this.postExpandableLayout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {

                switch (state) {

                    case ExpandableLayout.State.EXPANDED:
                        PostPageActivity.this.refreshPostCommentsRecyclerViewPadding();
                        break;
                    case ExpandableLayout.State.COLLAPSED:
                        PostPageActivity.this.refreshPostCommentsRecyclerViewPadding();
                        break;
                }

            }
        });

        this.initPostHeaderLayout(view);

        this.initPostTextLayout(view);

        this.initPostMediaLayout(view);
        
    }

    //post header
    private void initPostHeaderLayout(View view) {

        this.postHeaderLayout = (RelativeLayout) view.findViewById(R.id.post_page_activity_post_header_layout);

        this.initPostMenuButton(view);

        this.initEditorProfilePictureImageView(view);

        this.initEditorNameTextView(view);

        this.initPostTimestampTextView(view);

        this.initPopupAnchor(view);

        this.initLongClickMenuItems();

    }

    private void initPostMenuButton(View view) {

        this.postMenuButton = (ImageButton) view.findViewById(R.id.post_page_activity_post_menu_icon);

        this.postMenuButton.setBackgroundColor(Color.TRANSPARENT);

        this.initOnTouchPostMenuButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPostMenuButtonListener() {

        this.onTouchPostMenuButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostPageActivity.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(PostPageActivity.this.touchUpAnimation);

                        PostPageActivity.this.onPostMenuButtonClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.postMenuButton.setOnTouchListener(this.onTouchPostMenuButtonListener);

    }

    private void onPostMenuButtonClicked() {

        this.moreButtonPowerMenu = ((MyPowerMenu) new MyPowerMenu.Builder(this)
                .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                .setAutoDismiss(true)
                .addItem(this.editPostMenuItem)
                .addItem(this.deletePostMenuItem)
                .addItem(this.cancelPostMenuItem)
                .setMenuRadius(UIUtilsManager.get().convertDpToPixels(this, 10))
                .setMenuShadow(UIUtilsManager.get().convertDpToPixels(this, 2))
                .setTextColor(ContextCompat.getColor(this, R.color.light_primary_color))
                .setSelectedTextColor(ContextCompat.getColor(this, R.color.light_primary_color_half_opacity))
                .setMenuColor(ContextCompat.getColor(this, R.color.primary_color))
                .setSelectedMenuColor(ContextCompat.getColor(this, R.color.primary_color_half_opacity))
                .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {

                        PostPageActivity.this.onPostPowerMenuItemClick(position, item);

                    }
                }).setOnDismissListener(new OnDismissedListener() {
                    @Override
                    public void onDismissed() {

                        PostPageActivity.this.notifyPowerMenuShow(
                                PostPageActivity.this.moreButtonPowerMenu
                        );

                    }
                })
                .build());

        this.notifyPowerMenuShow(
                this.moreButtonPowerMenu
        );

        //screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)this).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        //popup location
        int[] popupLocation = new int[2];
        this.postHeaderLayout.getLocationOnScreen(popupLocation);
        int popupLocationY = popupLocation[1];

        //popup height
        int popupHeight = this.moreButtonPowerMenu.getContentViewHeight();

        if (popupLocationY + UIUtilsManager.get().convertDpToPixels(this, 12)
                > screenHeight - popupHeight) {

            if (this.isRtl()) {

                this.moreButtonPowerMenu.setAnimation(MenuAnimation.SHOWUP_BOTTOM_LEFT);

            } else {

                this.moreButtonPowerMenu.setAnimation(MenuAnimation.SHOWUP_BOTTOM_RIGHT);

            }

            this.moreButtonPowerMenu.showAsDropDown(this.popupAnchor, 0 ,-popupHeight);

        } else {

            if (this.isRtl()) {

                this.moreButtonPowerMenu.setAnimation(MenuAnimation.SHOWUP_TOP_LEFT);

            } else {

                this.moreButtonPowerMenu.setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT);

            }

            this.moreButtonPowerMenu.showAsDropDown(this.popupAnchor, 0 ,0);

        }

    }

    private void initEditorProfilePictureImageView(View view) {

        this.editorProfilePictureImageView = (ImageView) view.findViewById(R.id.post_page_activity_editor_profile_picture_view);

    }

    private void initEditorNameTextView(View view) {

        this.editorNameTextView = (TextView) view.findViewById(R.id.post_page_activity_editor_name_view);

    }

    private void initPostTimestampTextView(View view) {

        this.postTimestampTextView = (TextView) view.findViewById(R.id.post_page_activity_post_timestamp_view);
    }

    private void initPopupAnchor(View view) {

        this.popupAnchor = view.findViewById(R.id.post_page_activity_popup_anchor);

    }

    private void initLongClickMenuItems() {

        this.initCopyPostMenuItem();

        this.initEditPostMenuItem();

        this.initDeletePostMenuItem();

        this.initCancelPostMenuItem();

    }

    private void initCopyPostMenuItem() {

        this.copyPostMenuItem = new PowerMenuItem("Copy", false);
        this.copyPostMenuItem.setTag(POST_ITEM_DROPDOWN_MENU_COPY_ID);

    }

    private void onCopyPostMenuItemClick() {

        if (this.post != null
                && this.post.getText() != null
                && !this.post.getText().equals("")) {

            ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Post: ",this.post.getText());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, "Text copied", Toast.LENGTH_SHORT).show();

        }

    }

    private void initEditPostMenuItem() {

        this.editPostMenuItem = new PowerMenuItem("Edit", false);
        this.editPostMenuItem.setTag(POST_ITEM_DROPDOWN_MENU_EDIT_ID);

    }

    private void onEditPostMenuItemClick() {

        this.onPostEdit(this.post);

    }

    private void initDeletePostMenuItem() {

        this.deletePostMenuItem = new PowerMenuItem("Delete", false);
        this.deletePostMenuItem.setTag(POST_ITEM_DROPDOWN_MENU_DELETE_ID);

    }

    private void onDeletePostMenuItemClick() {

        this.onPostDelete(this.post);

    }

    private void initCancelPostMenuItem() {

        this.cancelPostMenuItem = new PowerMenuItem("Cancel", false);
        this.cancelPostMenuItem.setTag(POST_ITEM_DROPDOWN_MENU_CANCEL_ID);

    }

    private void onCancelPostMenuItemClick() {

    }

    private void onPostHeaderLongClick() {

        PostPageActivity.this.postTextLayout.setBackgroundColor(
                ContextCompat.getColor(this, R.color.dark_primary_color_extra_opacity)
        );

        this.longClickPowerMenu = ((MyPowerMenu) new MyPowerMenu.Builder(this)
                .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                .setAutoDismiss(true)
                .addItem(this.copyPostMenuItem)
                .setMenuRadius(UIUtilsManager.get().convertDpToPixels(this, 10))
                .setMenuShadow(UIUtilsManager.get().convertDpToPixels(this, 2))
                .setTextColor(ContextCompat.getColor(this, R.color.light_primary_color))
                .setSelectedTextColor(ContextCompat.getColor(this, R.color.light_primary_color_half_opacity))
                .setMenuColor(ContextCompat.getColor(this, R.color.primary_color))
                .setSelectedMenuColor(ContextCompat.getColor(this, R.color.primary_color_half_opacity))
                .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {

                        PostPageActivity.this.onPostPowerMenuItemClick(position, item);

                    }
                }).setOnDismissListener(new OnDismissedListener() {
                    @Override
                    public void onDismissed() {

                        PostPageActivity.this.notifyPowerMenuShow(
                                PostPageActivity.this.longClickPowerMenu
                        );

                        PostPageActivity.this.postTextLayout.setBackgroundColor(
                                ContextCompat.getColor(PostPageActivity.this, R.color.light_primary_color)
                        );

                    }
                })
                .build());

        this.notifyPowerMenuShow(
                this.longClickPowerMenu
        );

        //screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)this).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        //popup location
        int[] popupLocation = new int[2];
        this.postHeaderLayout.getLocationOnScreen(popupLocation);
        int popupLocationY = popupLocation[1];

        //popup height
        int popupHeight = this.longClickPowerMenu.getContentViewHeight();

        if (popupLocationY + UIUtilsManager.get().convertDpToPixels(this, 12)
                > screenHeight - popupHeight) {

            if (this.isRtl()) {

                this.longClickPowerMenu.setAnimation(MenuAnimation.SHOWUP_BOTTOM_LEFT);

            } else {

                this.longClickPowerMenu.setAnimation(MenuAnimation.SHOWUP_BOTTOM_RIGHT);

            }

            this.longClickPowerMenu.showAsDropDown(this.popupAnchor, 0 ,-popupHeight);

        } else {

            if (this.isRtl()) {

                this.longClickPowerMenu.setAnimation(MenuAnimation.SHOWUP_TOP_LEFT);

            } else {

                this.longClickPowerMenu.setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT);

            }

            this.longClickPowerMenu.showAsDropDown(this.popupAnchor, 0 ,0);

        }

    }

    private void onPostPowerMenuItemClick(int position, PowerMenuItem item) {

        if (item.getTag().equals(POST_ITEM_DROPDOWN_MENU_COPY_ID)) {

            this.onCopyPostMenuItemClick();

        } else if (item.getTag().equals(POST_ITEM_DROPDOWN_MENU_EDIT_ID)) {

            this.onEditPostMenuItemClick();

        } else if (item.getTag().equals(POST_ITEM_DROPDOWN_MENU_DELETE_ID)) {

            this.onDeletePostMenuItemClick();

        } else if (item.getTag().equals(POST_ITEM_DROPDOWN_MENU_CANCEL_ID)) {

            this.onCancelPostMenuItemClick();

        }

    }

    @Override
    public void onPostDelete(Post post) {

        this.makeDeletePostRequest(post);

    }

    @Override
    public void onPostEdit(Post post) {

        if (post != null) {

            Intent intent = new Intent(this, PostContentActivity.class);

            String postJson = GsonParser.getParser().create().toJson(post, Post.class);

            intent.putExtra(PostContentActivity.ARG_EXTRA_POST_CONTENT_POST_JSON, postJson);

            this.startActivityForResult(intent, RESPONSE_UPLOADED_POST);

        }

    }

    @Override
    public void showPostPage(Post post) {


    }

    //post text
    private void initPostTextLayout(View view) {

        this.postTextLayout = (RelativeLayout) view.findViewById(R.id.post_page_activity_text_layout);

        this.postTextLayout.setLongClickable(true);

        this.postTextLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PostPageActivity.this.onPostHeaderLongClick();

                return true;
            }
        });

        this.postTextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PostPageActivity.this.postTextTextView.getText().toString().contains(
                        PostPageActivity.this.post.getText())) {

                    if (PostPageActivity.this.post.getText().length() > MAX_COMMENT_LENGTH) {

                        String readMoreText = "Load More";
                        String shortcutContent = PostPageActivity.this.post.getText().substring(
                                0, MAX_COMMENT_LENGTH
                        );
                        String text = shortcutContent + " ... " + readMoreText;
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK)
                                , 0, text.length() - readMoreText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(PostPageActivity.this, R.color.primary_color))
                                , text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        PostPageActivity.this.postTextTextView.setText(
                                spannableStringBuilder
                        );

                    } else if (PostPageActivity.this.post.getText().split("\r\n|\r|\n").length > MAX_COMMENT_LINES) {

                        String readMoreText = "Load More";

                        String[] lines = PostPageActivity.this.post.getText().split("\r\n|\r|\n");
                        String shortcutContent = TextUtils.join("\n", Arrays.copyOfRange(lines, 0, MAX_COMMENT_LINES));

                        String text = shortcutContent + " ... \n" + readMoreText;
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(PostPageActivity.this, R.color.primary_color))
                                , text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        PostPageActivity.this.postTextTextView.setText(
                                spannableStringBuilder
                        );

                    }

                } else {

                    String readLessText = "";
                    String text = PostPageActivity.this.post.getText() + readLessText;
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                    spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readLessText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK)
                            , 0, text.length() - readLessText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(PostPageActivity.this, R.color.primary_color))
                            , text.length() - readLessText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    PostPageActivity.this.postTextTextView.setText(
                            spannableStringBuilder
                    );

                }

            }
        });

        this.initPostTextTextView(view);

    }

    private void initPostTextTextView(View view) {

        this.postTextTextView = (TextView) view.findViewById(R.id.post_page_activity_post_text_view);

    }

    //post media
    private void initPostMediaLayout(View view) {

        this.postMediaLayout = (ConstraintLayout) view.findViewById(R.id.post_page_activity_media_layout);

        this.initPostMediaContainer(view);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initPostMediaContainer(View view) {

        this.postMediaContainer = (SocialFeedPostsListMediaContainer) view.findViewById(R.id.post_page_activity_media_container);

        this.postMediaContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private String dimensionRatio = "H,1:1";

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == SCROLL_STATE_IDLE) {

                    int curPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    View view = ((LinearLayoutManager)recyclerView.getLayoutManager()).findViewByPosition(curPosition);

                    if (view != null && view.findViewById(R.id.social_feed_posts_list_media_image_view_holder_cropper_view) instanceof ImageView) {

                        ImageView imageView = (ImageView) view.findViewById(R.id.social_feed_posts_list_media_image_view_holder_cropper_view);

                        if (imageView != null
                                && imageView.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {

                            ConstraintLayout.LayoutParams imageViewLayoutParams =
                                    ((ConstraintLayout.LayoutParams) imageView.getLayoutParams());

                            imageView.setLayoutParams(imageViewLayoutParams);

                            PostPageActivity.this.onGetDimensionsRatio(imageViewLayoutParams.dimensionRatio);

                        }

                    }else if (view != null && view.findViewById(R.id.social_feed_posts_list_media_video_view_holder_player_view) instanceof PlayerView) {

                        PlayerView playerView = (PlayerView) view.findViewById(R.id.social_feed_posts_list_media_video_view_holder_player_view);

                        if (playerView != null
                                && playerView.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {

//                            ConstraintLayout.LayoutParams playerViewLayoutParams =
//                                    ((ConstraintLayout.LayoutParams) youtubePlayerView.getLayoutParams());
//
//                            ConstraintLayout.LayoutParams playerViewThumbnailCoverLayoutParams =
//                                    ((ConstraintLayout.LayoutParams) youtubePlayerView.getLayoutParams());


//                            youtubePlayerView.setLayoutParams(playerViewLayoutParams);

//                            playerViewThumbnailCover.setLayoutParams(playerViewThumbnailCoverLayoutParams);

                            if (playerView.getTag() instanceof String) {
                                PostPageActivity.this.onGetDimensionsRatio((String) playerView.getTag());
                            }

                        }

                    } else {

                        PostPageActivity.this.onGetDimensionsRatio("H,1:1");

                    }


                }


            }

        });

        //layout manager
        this.initPostMediaContainerLinearLayoutManager();

        //player selector
        this.initPostMediaContainerPlayerSelector();

        //snap helper
        this.initPostMediaContainerSnapHelper();

        //exo full screen listener
        this.initExoPlayerFullScreenListener();

        //adapter
        this.initPostMediaContainerAdapter();

    }

    private void initPostMediaContainerLinearLayoutManager() {

        this.postMediaContainerLinearLayoutManager = new SocialFeedPostsListMediaLinearLayoutManager(this);
        this.postMediaContainer.setLayoutManager(this.postMediaContainerLinearLayoutManager);

    }

    private void initPostMediaContainerPlayerSelector() {

        this.postMediaContainerPlayerSelector = new PressablePlayerSelector(this.postMediaContainer);
        this.postMediaContainer.setPlayerSelector(this.postMediaContainerPlayerSelector);

    }

    private void initPostMediaContainerSnapHelper() {

        this.postMediaContainerSnapHelper = new PagerSnapHelper();
        this.postMediaContainerSnapHelper.attachToRecyclerView(this.postMediaContainer);

    }

    private void initExoPlayerFullScreenListener() {

        this.exoPlayerFullScreenListener = new ExoPlayerFullScreenListener() {

            int orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

            @Override
            public void startFullScreen() {

                PostPageActivity.this.postMediaContainer.setPlayerSelector(PlayerSelector.NONE);

                this.orientation = PostPageActivity.this.getRequestedOrientation();

                PostPageActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);

            }

            @Override
            public void exitFullScreen(Uri mediaUri, PlaybackInfo playbackInfo, int order, Point videoSize) {

                PostPageActivity.this.postMediaContainer.savePlaybackInfo(order, playbackInfo);

                PostPageActivity.this.postMediaContainer.setPlayerSelector(
                        PostPageActivity.this.postMediaContainerPlayerSelector
                );

                PostPageActivity.this.setRequestedOrientation(this.orientation);

            }
        };

    }

    private void initPostMediaContainerAdapter() {

        this.postMediaContainerPlayerAdapter = new SocialFeedPostsListMediaAdapter(this,
                this.getSupportFragmentManager(),
                this.exoPlayerFullScreenListener,
                this,
                this.postMediaContainerPlayerSelector,
                new ArrayList<MediaItem>());

        this.postMediaContainer.setAdapter(this.postMediaContainerPlayerAdapter);

    }

    //post footer
    private void initPostFooterLayout(View view) {

        this.postFooterLayout = (RelativeLayout) view.findViewById(R.id.post_page_activity_footer_layout);

        this.initPostFooterIconsLayout(view);

        this.initPostFooterTextsLayout(view);
    }

    private void initPostFooterIconsLayout(View view) {

        this.postFooterIconsLayout = (RelativeLayout) view.findViewById(R.id.post_page_activity_footer_icons_layout);

        this.initPostLikesButtonImageView(view);

        this.initPostCommentsButtonImageView(view);

        this.initPostExpandButtonImageView(view);

        this.initPostShareButtonImageView(view);

    }

    private void initPostLikesButtonImageView(View view) {

        this.postLikesButtonImageView = (ImageView) view.findViewById(R.id.post_page_activity_like_button);

        this.initOnTouchPostLikesButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPostLikesButtonListener() {

        this.onTouchPostLikesButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostPageActivity.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(PostPageActivity.this.touchUpAnimation);

                        PostPageActivity.this.onPostLikesButtonClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.postLikesButtonImageView.setOnTouchListener(this.onTouchPostLikesButtonListener);

    }

    private void onPostLikesButtonClicked() {

        if (!this.likeLocked) {

            if (this.post != null) {

                if (this.post.isLike()) {

                    this.makeUnlikePostRequest();

                } else {

                    this.makeLikePostRequest();

                }

            }

        }

    }

    private void initPostCommentsButtonImageView(View view) {

        this.postCommentsButtonImageView = (ImageView) view.findViewById(R.id.post_page_activity_comments_button);

        this.initOnTouchPostCommentsButtonListener();

    }

    private void onPostCommentsButtonClicked() {

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPostCommentsButtonListener() {

        this.onTouchPostCommentsButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostPageActivity.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(PostPageActivity.this.touchUpAnimation);

                        PostPageActivity.this.onPostCommentsButtonClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.postCommentsButtonImageView.setOnTouchListener(this.onTouchPostCommentsButtonListener);

    }

    private void initPostExpandButtonImageView(View view) {

        this.postExpandButtonImageView = (ImageView) view.findViewById(R.id.post_page_activity_expand_button);

        this.initOnTouchPostExpandButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPostExpandButtonListener() {

        this.onTouchPostExpandButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostPageActivity.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(PostPageActivity.this.touchUpAnimation);

                        PostPageActivity.this.onPostExpandButtonClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.postExpandButtonImageView.setOnTouchListener(this.onTouchPostExpandButtonListener);

    }

    private void onPostExpandButtonClicked() {

        if (this.postExpandableLayout.isExpanded()) {

            this.postExpandableLayout.setExpanded(false, true);

            this.postExpandButtonImageView.setImageDrawable(
                    ((IconicsDrawable) Icons.get().findDrawable(this, "faw_caret_down"))
                            .sizeDp(24).colorRes(R.color.light_primary_color));

        } else {

            this.postExpandableLayout.setExpanded(true, true);

            this.postExpandButtonImageView.setImageDrawable(
                    ((IconicsDrawable) Icons.get().findDrawable(this, "faw_caret_up"))
                            .sizeDp(24).colorRes(R.color.light_primary_color));

        }

    }

    private void initPostShareButtonImageView(View view) {

        this.postShareButtonImageView = (ImageView) view.findViewById(R.id.post_page_activity_share_button);

        this.initOnTouchPostShareButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPostShareButtonListener() {

        this.onTouchPostShareButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostPageActivity.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(PostPageActivity.this.touchUpAnimation);

                        PostPageActivity.this.onPostShareButtonClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.postShareButtonImageView.setOnTouchListener(this.onTouchPostShareButtonListener);

    }

    private void onPostShareButtonClicked() {


    }
    
    private void initPostFooterTextsLayout(View view) {

        this.postFooterTextsLayout = (RelativeLayout) view.findViewById(R.id.post_page_activity_footer_texts_layout);

        this.initPostLikesTextView(view);

        this.initPostIntermediateTextView(view);

        this.initPostCommentsTextView(view);

    }

    private void initPostLikesTextView(View view) {

        this.postLikesTextView = (TextView) view.findViewById(R.id.post_page_activity_likes_text);

        this.initOnTouchPostLikesTextListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPostLikesTextListener() {

        this.onTouchPostLikesTextListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        PostPageActivity.this.postLikesTextView.setTextColor(
                                ContextCompat.getColor(PostPageActivity.this, R.color.primary_color_extra_opacity)
                        );

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        PostPageActivity.this.postLikesTextView.setTextColor(
                                ContextCompat.getColor(PostPageActivity.this, R.color.primary_color_half_opacity)
                        );

                        PostPageActivity.this.onPostLikesTextClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.postLikesTextView.setOnTouchListener(this.onTouchPostLikesTextListener);

    }

    private void onPostLikesTextClicked() {

        if (this.post != null) {

            Intent intent = new Intent(this, LikesListPageActivity.class);

            String postJson = GsonParser.getParser().create().toJson(this.post, Post.class);

            intent.putExtra(PostPageActivity.ARG_EXTRA_POST_PAGE_POST_JSON, postJson);

            this.startActivity(intent);

        }

    }

    private void initPostIntermediateTextView(View view) {

        this.postIntermediateTextView = (TextView) view.findViewById(R.id.post_page_activity_intermediate_text);

    }

    private void initPostCommentsTextView(View view) {

        this.postCommentsTextView = (TextView) view.findViewById(R.id.post_page_activity_comments_text);

        this.initOnTouchPostCommentsTextListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPostCommentsTextListener() {

        this.onTouchPostCommentsTextListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        PostPageActivity.this.postCommentsTextView.setTextColor(
                                ContextCompat.getColor(PostPageActivity.this, R.color.primary_color_extra_opacity)
                        );

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        PostPageActivity.this.postCommentsTextView.setTextColor(
                                ContextCompat.getColor(PostPageActivity.this, R.color.primary_color_half_opacity)
                        );

                        PostPageActivity.this.onPostLikesCommentsClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.postCommentsTextView.setOnTouchListener(this.onTouchPostCommentsTextListener);

    }

    private void onPostLikesCommentsClicked() {

    }

    //comments list
    private void initPostCommentsRecyclerView(View view) {

        this.postCommentsRecyclerView = (MyRecyclerView) view.findViewById(R.id.post_page_activity_comments_recycler_view);

        this.postCommentsRecyclerView.setRecyclerViewOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                PostPageActivity.this.makeGetPostCommentsRequest();

            }
        });

        this.postCommentsRecyclerView.setScreenSkin(ThemeUtilsManager.LIGHT_COLOR_SKIN);

        this.postCommentsRecyclerView.getRecyclerView().setBackgroundColor(
                ContextCompat.getColor(this, R.color.light_primary_color)
        );

        //horizontal line divider between items
//        this.setHorizontalDivider();

        this.setAdapter();

        this.refreshPostCommentsRecyclerViewPadding();

    }

    private void setAdapter() {

        if (this.commentsList != null) {
            this.postPageCommentsListAdapter = new PostPageCommentsListAdapter(this, this.commentsList, this);
        } else {
            this.postPageCommentsListAdapter = new PostPageCommentsListAdapter(this, new ArrayList<PostComment>(), this);
        }

        this.postPageCommentsListAdapter.setScreenSkin(this.screenSkin);
        
        this.postCommentsRecyclerView.getRecyclerView().setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false) {

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

        });
        
        this.postCommentsRecyclerView.getRecyclerView().setAdapter(this.postPageCommentsListAdapter);

    }

    private void refreshPostCommentsRecyclerViewPadding() {

        if (this.postCommentsRecyclerView != null) {

            this.postCommentsRecyclerView.post(new Runnable() {
                @Override
                public void run() {

                    PostPageActivity.this.postCommentsRecyclerView.setPadding(
                            0,
                            0,
                            0,
                            PostPageActivity.this.getPaddingForCommentsList()
                    );

                }
            });

        }


    }

    private int getPaddingForCommentsList() {

        int navigationBarHeight = this.getNavigationBarSize();

        int commentInputHeight = 0;
        if (PostPageActivity.this.postCommentInputLayout != null) {

            commentInputHeight = PostPageActivity.this.postCommentInputLayout.getHeight();

        }

        int postHeight = 0;
        if (PostPageActivity.this.postLayout != null) {

            postHeight = PostPageActivity.this.postLayout.getHeight();

        }

        return commentInputHeight + postHeight;
    }

    @Override
    public void onCommentDelete(PostComment comment) {

        this.makeDeleteCommentRequest(comment);

    }

    @Override
    public void onCommentEdit(PostComment comment){

        if (comment != null && comment.getCommentId() != -1L) {

            this.postCommentToEdit = comment;

            this.postCommentInputUpperMessageTextView.setText("ערוך תגובה");

            this.postCommentInputCommentTextView.setText(comment.getContent());

            this.postCommentInputUpperMessageExpandableLayout.setExpanded(true, true);

        }

    }

    @Override
    public void notifyPowerMenuShow(PowerMenu powerMenu) {

        this.currentPowerMenu = powerMenu;

    }

    //comment input upper message
    private void initPostCommentInputUpperMessageExpandableLayout(View view) {

        this.postCommentInputUpperMessageExpandableLayout = (ExpandableLayout) view.findViewById(R.id.post_page_activity_comment_input_upper_message_expandable_layout);

        this.postCommentInputUpperMessageExpandableLayout.setExpanded(false);

        this.initPostCommentInputUpperMessageLayout(view);

    }

    private void initPostCommentInputUpperMessageLayout(View view) {

        this.postCommentInputUpperMessageLayout = (RelativeLayout) view.findViewById(R.id.post_page_activity_comment_input_upper_message_layout);

        this.postCommentInputUpperMessageLayout.setBackgroundColor(
                ContextCompat.getColor(this, R.color.primary_color)
        );

        this.initPostCommentInputUpperMessageTextView(view);

        this.initPostCommentInputUpperMessageCloseButton(view);

    }

    private void initPostCommentInputUpperMessageTextView(View view) {

        this.postCommentInputUpperMessageTextView = (TextView) view.findViewById(R.id.post_page_activity_comment_input_upper_message_text_view);

        this.postCommentInputUpperMessageTextView.setTextColor(
                ContextCompat.getColor(this, R.color.light_primary_color)
        );

    }

    private void initPostCommentInputUpperMessageCloseButton(View view) {

        this.postCommentInputUpperMessageCloseButton = (ImageButton) view.findViewById(R.id.post_page_activity_comment_input_upper_message_close_button);

        this.postCommentInputUpperMessageCloseButton.setBackground(null);

        this.postCommentInputUpperMessageCloseButton.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this, "gmd_close"))
                        .sizeDp(16).colorRes(R.color.light_primary_color)
        );

        this.initOnTouchPostCommentInputUpperMessageCloseButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPostCommentInputUpperMessageCloseButtonListener() {

        this.onTouchPostCommentInputUpperMessageCloseButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostPageActivity.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(PostPageActivity.this.touchUpAnimation);

                        PostPageActivity.this.onPostCommentInputUpperMessageCloseButtonClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.postCommentInputUpperMessageCloseButton.setOnTouchListener(this.onTouchPostCommentInputUpperMessageCloseButtonListener);

    }

    public void onPostCommentInputUpperMessageCloseButtonClicked() {

        if (this.postCommentInputUpperMessageExpandableLayout != null
                && this.postCommentInputUpperMessageExpandableLayout.isExpanded()) {

            this.postCommentInputUpperMessageExpandableLayout.setExpanded(false, true);

        }

        this.postCommentInputCommentTextView.setText("");

        this.postCommentToEdit = null;

    }

    //comment input
    private void initPostCommentInputLayout(View view) {

        this.postCommentInputLayout = (RelativeLayout) view.findViewById(R.id.post_page_activity_comment_input_layout);

        this.postCommentInputLayout.setBackgroundColor(
                ContextCompat.getColor(this, R.color.primary_color)
        );

        this.initPostCommentInputCommentTextLayout(view);

        this.initPostCommentInputCommentButton(view);

        this.checkComment(this.postCommentInputCommentTextView.getText());

    }

    private void initPostCommentInputCommentTextLayout(View view) {

        this.postCommentInputCommentTextLayout = (RelativeLayout) view.findViewById(R.id.post_page_activity_comment_input_text_layout);

        float[] outerRadii = new float[] {
                100,
                100,
                100,
                100,
                100,
                100,
                100,
                100
        };
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(ContextCompat.getColor(this, R.color.light_primary_color));

        this.postCommentInputCommentTextLayout.setBackground(null);

        this.postCommentInputCommentTextLayout.setBackground(shapeDrawable);

        this.initPostCommentInputCommentTextView(view);

    }

    private void initPostCommentInputCommentTextView(View view) {

        this.postCommentInputCommentTextView = (TextInputEditText) view.findViewById(R.id.post_page_activity_comment_input_text);

        this.postCommentInputCommentTextView.setBackground(null);

        this.postCommentInputCommentTextView.setTextColor(Color.BLACK);

        this.postCommentInputCommentTextView.setMaxLines(3);

        this.postCommentInputCommentTextView.setHint("הגב...");

        this.postCommentInputCommentTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                PostPageActivity.this.checkComment(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

                PostPageActivity.this.refreshPostCommentsRecyclerViewPadding();

            }
        });
    }

    private void checkComment(CharSequence comment) {

        if (isCommentValid(comment)) {

            this.onValidComment();

        } else {

            this.onInvalidComment();

        }

    }

    private boolean isCommentValid(CharSequence comment) {

        if (comment != null && !comment.toString().trim().equals("") && comment.toString().trim().length() > 0) {

            return true;
        }

        return false;

    }

    @SuppressLint("ClickableViewAccessibility")
    private void onValidComment() {

        this.postCommentInputCommentButton.setBackground(
                DrawableUtilsManager.get().getRoundSelectableDrawable(ContextCompat.getColor(this, R.color.light_primary_color))
        );

//        ViewCompat.setElevation(
//                this.postCommentInputCommentButton,
//                UIUtilsManager.get().convertDpToPixels(this, 10)
//        );

        this.postCommentInputCommentButton.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this, "gmd_send"))
                        .sizeDp(24).colorRes(R.color.primary_color)
        );

        if (this.isRtl()) {

            this.postCommentInputCommentButton.setRotationY(180);

        } else {

            Log.i("Send Button", "Not Rotate");
            this.postCommentInputCommentButton.setRotationX(0);

        }

        this.postCommentInputCommentButton.setEnabled(true);

        this.postCommentInputCommentButton.setOnTouchListener(this.onTouchPostCommentInputCommentButtonListener);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void onInvalidComment() {

        this.postCommentInputCommentButton.setBackground(
                DrawableUtilsManager.get().getRoundSelectableDrawable(ContextCompat.getColor(this, R.color.light_primary_color_half_opacity))
        );

//        ViewCompat.setElevation(
//                this.postCommentInputCommentButton,
//                UIUtilsManager.get().convertDpToPixels(this, 10)
//        );

        this.postCommentInputCommentButton.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this, "gmd_send"))
                        .sizeDp(24).colorRes(R.color.primary_color).alpha(136)
        );

        if (this.isRtl()) {

            Log.i("Send Button", "Rotate");
            this.postCommentInputCommentButton.setRotationY(180);

        } else {

            Log.i("Send Button", "Not Rotate");
            this.postCommentInputCommentButton.setRotationX(0);

        }

        this.postCommentInputCommentButton.setEnabled(false);

        this.postCommentInputCommentButton.setOnTouchListener(null);

    }

    private void initPostCommentInputCommentButton(View view) {

        this.postCommentInputCommentButton = (ImageButton) view.findViewById(R.id.post_page_activity_comment_input_button);

        this.postCommentInputCommentButton.setBackground(
                DrawableUtilsManager.get().getRoundSelectableDrawable(ContextCompat.getColor(this, R.color.light_primary_color))
        );

//        ViewCompat.setElevation(
//                this.postCommentInputCommentButton,
//                UIUtilsManager.get().convertDpToPixels(this, 10)
//        );

        this.postCommentInputCommentButton.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this, "gmd_send"))
                        .sizeDp(24).colorRes(R.color.primary_color)
        );

        if (this.isRtl()) {

            Log.i("Send Button", "Rotate");
            this.postCommentInputCommentButton.setRotationY(180);

        } else {

            this.postCommentInputCommentButton.setRotationX(0);

        }

        this.initOnTouchPostCommentInputCommentButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPostCommentInputCommentButtonListener() {

        this.onTouchPostCommentInputCommentButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostPageActivity.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(PostPageActivity.this.touchUpAnimation);

                        PostPageActivity.this.onPostCommentInputCommentButtonClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.postCommentInputCommentButton.setOnTouchListener(this.onTouchPostCommentInputCommentButtonListener);

    }

    public void onPostCommentInputCommentButtonClicked() {

        if (!this.commentLocked && this.isCommentValid(this.postCommentInputCommentTextView.getText())) {

            if (this.postCommentToEdit != null
                    && this.postCommentToEdit.getCommentId() != -1L) {

                this.postCommentToEdit.setContent(
                        this.postCommentInputCommentTextView.getText().toString()
                );

                this.makeEditCommentRequest(this.postCommentToEdit);

            } else {

                this.makeCommentPostRequest(this.postCommentInputCommentTextView.getText().toString());

            }

        }

    }

    //update views
    public void updatePost() {

        if (this.post != null) {

            //post menu button
            this.updatePostMenuButtonImageView();

            //post editor
            this.updateEditor();

            //post timestamp
            this.updatePostTimestampTextView();

            //post text
            this.updatePostTextTextView();

            //post media
            this.updatePostMediaContainer();

            //post like button
            this.updatePostLikesButtonImageView();

            //post comments button
            this.updatePostCommentsButtonImageView();

            //post expand button
            this.updatePostExpandButtonImageView();

            //post share button
            this.updatePostShareButtonImageView();

            //post likes text
            this.updatePostLikesTextView();

            //post intermediate text
            this.updatePostIntermediateTextView();

            //post comments text
            this.updatePostCommentsTextView();

        }

    }
    
    //post menu button
    private void updatePostMenuButtonImageView() {

        this.postMenuButton.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this, "gmd_more_horiz"))
                        .sizeDp(16).color(Color.DKGRAY));

    }


    //post editor
    private void updateEditor() {

        if (this.post.getGuest() != null
                || this.post.getHotel() != null) {

            //post editor profile picture
            this.updateEditorProfilePictureImageView();

            //post editor name
            this.updateEditorNameTextView();

        }

    }

    //post editor profile picture
    private void updateEditorProfilePictureImageView() {

        if (this.post != null && this.post.getGuest() != null && this.post.getGuest().getImageUrl() != null) {

            Glide.with(this)
                    .asBitmap()
                    .load(this.post.getGuest().getImageUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.circleCropTransform())
                    .into(this.editorProfilePictureImageView);

        } else if (this.post != null && this.post.getHotel() != null && this.post.getHotel().getImageUrl() != null) {

            Glide.with(this)
                    .asBitmap()
                    .load(this.post.getHotel().getImageUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.circleCropTransform())
                    .into(this.editorProfilePictureImageView);

        } else {

            //put an placeholder default image
//            Glide.with(this)
//                    .asBitmap()
//                    .load(placeholderimage)
//                    .into(this.editorProfilePictureImageView);

        }

    }

    //post editor name
    private void updateEditorNameTextView() {

        if (this.post != null && this.post.getGuest() != null && this.post.getGuest().getName() != null) {

            this.editorNameTextView.setText(this.post.getGuest().getName());

        } else if (this.post != null && this.post.getHotel() != null && this.post.getHotel().getName() != null) {

            this.editorNameTextView.setText(this.post.getHotel().getName());

        } else {

            this.editorNameTextView.setText("No User!");

        }

        this.editorNameTextView.setTypeface(this.editorNameTextView.getTypeface(), Typeface.BOLD);

        this.editorNameTextView.setTextColor(
                Color.BLACK
        );

    }

    //post timestamp
    private void updatePostTimestampTextView() {

        if (this.post != null && this.post.getTimeStamp() != null) {

            this.postTimestampTextView.setText(prettyTimestamp(this.post.getTimeStamp()));

        } else {

            this.postTimestampTextView.setText("--/--/----");

        }

        this.postTimestampTextView.setTextColor(
                Color.DKGRAY
        );

    }

    //post text
    private void updatePostTextTextView() {

        if (this.post != null && this.post.getText() != null) {

            if (PostPageActivity.this.post.getText().length() > MAX_COMMENT_LENGTH) {

                String readMoreText = "Load More";
                String shortcutContent = PostPageActivity.this.post.getText().substring(
                        0, MAX_COMMENT_LENGTH
                );
                String text = shortcutContent + " .. " + readMoreText;
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK)
                        , 0, text.length() - readMoreText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(PostPageActivity.this, R.color.primary_color))
                        , text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                PostPageActivity.this.postTextTextView.setText(
                        spannableStringBuilder
                );

            } else if (PostPageActivity.this.post.getText().split("\r\n|\r|\n").length > MAX_COMMENT_LINES) {


                String readMoreText = "Load More";

                String[] lines = PostPageActivity.this.post.getText().split("\r\n|\r|\n");
                String shortcutContent = TextUtils.join("\n", Arrays.copyOfRange(lines, 0, MAX_COMMENT_LINES));

                String text = shortcutContent + " ... \n" + readMoreText;
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(PostPageActivity.this, R.color.primary_color))
                        , text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                PostPageActivity.this.postTextTextView.setText(
                        spannableStringBuilder
                );

            } else {

                String text = PostPageActivity.this.post.getText();
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK)
                        , 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                PostPageActivity.this.postTextTextView.setText(
                        spannableStringBuilder
                );

            }

        } else {

            this.postTextTextView.setText("");

        }

    }

    //post media
    private void updatePostMediaContainer() {

        if (this.post != null && this.post.getMediaItems() != null) {

            if (this.postMediaContainer.getAdapter() instanceof SocialFeedPostsListMediaAdapter) {

                SocialFeedPostsListMediaAdapter socialFeedPostsListMediaAdapter =
                        ((SocialFeedPostsListMediaAdapter)this.postMediaContainer.getAdapter());

                socialFeedPostsListMediaAdapter.setMediaItems(
                        new ArrayList<MediaItem>(Arrays.asList(PostPageActivity.this.post.getMediaItems())));

                if (PostPageActivity.this.postMediaContainer.getCacheManager() == null) {

                    PostPageActivity.this.postMediaContainer.setCacheManager(new SocialFeedPostsListCacheManager(
                            new ArrayList<MediaItem>(Arrays.asList(PostPageActivity.this.post.getMediaItems())
                            )));

                }

                socialFeedPostsListMediaAdapter.notifyDataSetChanged();

            }

        }

    }

    @Override
    public void onGetDimensionsRatio(String dimensionsRatio) {

        if (PostPageActivity.this.postMediaContainer != null
                && PostPageActivity.this.postMediaContainer.getLayoutParams() instanceof ConstraintLayout.LayoutParams
                && PostPageActivity.this.postMediaLayout != null
                && PostPageActivity.this.postMediaLayout.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {

            ConstraintLayout.LayoutParams containerLayoutParams =
                    ((ConstraintLayout.LayoutParams) PostPageActivity.this.postMediaContainer.getLayoutParams());

            ConstraintLayout.LayoutParams layoutLayoutParams =
                    ((ConstraintLayout.LayoutParams) PostPageActivity.this.postMediaLayout.getLayoutParams());


            containerLayoutParams.dimensionRatio = dimensionsRatio;
            PostPageActivity.this.postMediaContainer.setLayoutParams(containerLayoutParams);

            layoutLayoutParams.dimensionRatio = dimensionsRatio;
            PostPageActivity.this.postMediaLayout.setLayoutParams(layoutLayoutParams);

        }

    }

    //post like button
    private void updatePostLikesButtonImageView() {

        this.postLikesButtonImageView.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this, "faw_heart1"))
                        .sizeDp(24).colorRes(R.color.light_primary_color));

        if (this.post != null && this.post.isLike()) {

            this.postLikesButtonImageView.setImageDrawable(
                    ((IconicsDrawable) Icons.get().findDrawable(this, "faw_heart1"))
                            .sizeDp(24).colorRes(R.color.lava_red));

        }

    }

    //post comments button
    private void updatePostCommentsButtonImageView() {

        this.postCommentsButtonImageView.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this, "faw_comment1"))
                        .sizeDp(24).colorRes(R.color.light_primary_color));

        if (this.post != null) {

        }

    }

    //post expand button
    private void updatePostExpandButtonImageView() {

        if (this.postExpandableLayout != null && this.postExpandableLayout.isExpanded()) {

            this.postExpandButtonImageView.setImageDrawable(
                    ((IconicsDrawable) Icons.get().findDrawable(this, "faw_caret_up"))
                            .sizeDp(24).colorRes(R.color.light_primary_color));

        } else {

            this.postExpandButtonImageView.setImageDrawable(
                    ((IconicsDrawable) Icons.get().findDrawable(this, "faw_caret_down"))
                            .sizeDp(24).colorRes(R.color.light_primary_color));

        }

        if (this.post != null) {

        }

    }

    //post share button
    private void updatePostShareButtonImageView() {

        this.postShareButtonImageView.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this, "faw_share"))
                        .sizeDp(24).colorRes(R.color.light_primary_color));

        if (this.post != null) {

        }

    }

    //post likes text
    private void updatePostLikesTextView() {

        if (this.post != null) {

            this.postLikesTextView.setText(Math.max(0, this.post.getLikesCount()) + " Likes");

        } else {

            this.postLikesTextView.setText("0 Likes");

        }

        this.postLikesTextView.setTextColor(ContextCompat.getColor(this, R.color.primary_color_half_opacity));
        this.postLikesTextView.setTypeface(this.postLikesTextView.getTypeface(), Typeface.BOLD);

    }

    //post intermediate text
    private void updatePostIntermediateTextView() {

        this.postIntermediateTextView.setText(" • ");

        this.postIntermediateTextView.setTextColor(ContextCompat.getColor(this, R.color.primary_color_half_opacity));
        this.postIntermediateTextView.setTypeface(this.postIntermediateTextView.getTypeface(), Typeface.BOLD);

    }

    //post comments text
    private void updatePostCommentsTextView() {

        if (this.post != null) {

            this.postCommentsTextView.setText(Math.max(0, this.post.getCommentsCount()) + " Comments");

        } else {

            this.postCommentsTextView.setText("0 Comments");

        }

        this.postCommentsTextView.setTextColor(ContextCompat.getColor(this, R.color.primary_color_half_opacity));
        this.postCommentsTextView.setTypeface(this.postLikesTextView.getTypeface(), Typeface.BOLD);

    }

    //http requests
    private void makeRefreshPostCommentsRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                PostPageActivity.this.postCommentsRecyclerView.setMessage("Loading...", NONE_MESSAGE);
                PostPageActivity.this.postCommentsRecyclerView.setLoadMore(true, "Loading...");
                PostPageActivity.this.postCommentsRecyclerView.setLoadingItems(true);

                PostPageActivity.this.postCommentsSwipeRefreshLayout.setEnabled(false);
                PostPageActivity.this.postCommentsSwipeRefreshLayout.setRefreshing(true);

            }

            @Override
            protected boolean requestCondition() {

                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);

                return guestId != null && hotelId != null && PostPageActivity.this.post != null
                        && PostPageActivity.this.post.getPostId() != -1L;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected void setRequestParams(HashMap<String, String> params) {
                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);
//                params.put("guest", guestId);
//                params.put("hotel", hotelId);
                params.put("guest", "1");
                params.put("hotel", "1");
            }

            @Override
            protected String getRequestUrl() {

                String url = GET_ALL_COMMENTS_OF_POST_URL;

                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);
                url += "?hotel=" + hotelId;

                long postId = PostPageActivity.this.post.getPostId();
                url += "&post=" + Long.toString(postId);

                if (PostPageActivity.this.postCommentsRecyclerView != null
                        && PostPageActivity.this.postCommentsRecyclerView.getRecyclerView() != null
                        && PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter() != null
                        && PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter() instanceof PostPageCommentsListAdapter
                        && ((PostPageCommentsListAdapter) PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter()).getFilter() != null) {

                    PostPageCommentsListAdapter adapter = ((PostPageCommentsListAdapter) PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter());

                    //offset & amount
                    url += "&offset=0";

                    url += "&amount=20";

                    //sorting type
                    String sortingType = adapter.getSortingType();
                    if (sortingType != null && !sortingType.equals("")) {

                        url += "&sort=" + adapter.getSortingType();

                    }

                }


                return url;
//                return BOB_SERVER_WEB_SERVICES_URL + "/wishes/getAllByUser?user=15";
            }

            @Override
            protected int getMethod() {
                return Request.Method.GET;
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

                    if (response.getJSONObject("response").has("comments")) {

                        PostComment[] postComments = new PostComment[0];
                        if (response.getJSONObject("response").optJSONArray("comments") != null) {
                            String postCommentsString = response.getJSONObject("response").getJSONArray("comments").toString();
                            postComments = customGson.fromJson(postCommentsString, PostComment[].class);
                        } else if (response.getJSONObject("response").optJSONObject("comments") != null) {
                            String postCommentsString = response.getJSONObject("response").getJSONObject("comments").toString();
                            postComments = new PostComment[] {
                                    customGson.fromJson(postCommentsString, PostComment.class)
                            };
                        }

                        if (postComments != null && postComments.length > 0) {

                            for (PostComment postComment : postComments) {

                                try {

                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).parse(postComment.getTimeStamp());

                                } catch (Exception e) {

                                    postComment.setTimeStamp(
                                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(System.currentTimeMillis())
                                    );

                                }

                            }

                            if (PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter() != null
                                    && PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter() instanceof PostPageCommentsListAdapter) {

                                ArrayList<PostComment> postsCommentsFromServer = new ArrayList<PostComment>(Arrays.asList(postComments));
                                ((PostPageCommentsListAdapter) PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter()).setObjects(postsCommentsFromServer);
                                PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter().notifyDataSetChanged();

                            }

                            PostPageActivity.this.postCommentsRecyclerView.setHasMoreItems(true);

                        } else {

                            PostPageActivity.this.postCommentsRecyclerView.setFinishLoading(true, "-End-");
                            PostPageActivity.this.postCommentsRecyclerView.setHasMoreItems(false);

                        }


                    } else {

                        PostPageActivity.this.postCommentsRecyclerView.setFinishLoading(true, "-End-");
                        PostPageActivity.this.postCommentsRecyclerView.setHasMoreItems(false);

                    }

                    PostPageActivity.this.makeGetLikesCountRequest();

                    PostPageActivity.this.makeGetCommentsCountRequest();

                    PostPageActivity.this.postCommentsRecyclerView.setScreenState(RECYCLER_VIEW);

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    PostPageActivity.this.postCommentsRecyclerView.setMessage(message, FAILURE_MESSAGE);
                } else {
                    PostPageActivity.this.postCommentsRecyclerView.setMessage("Getting Posts List Error!", FAILURE_MESSAGE);
                }
            }

            @Override
            protected void postRequest() {

                PostPageActivity.this.postCommentsSwipeRefreshLayout.setRefreshing(false);
                PostPageActivity.this.postCommentsSwipeRefreshLayout.setEnabled(true);

                PostPageActivity.this.postCommentsRecyclerView.setLoadMore(false);
                PostPageActivity.this.postCommentsRecyclerView.setLoadingItems(false);

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void makeGetPostCommentsRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                PostPageActivity.this.postCommentsRecyclerView.setLoadingItems(true);
                PostPageActivity.this.postCommentsRecyclerView.setLoadMore(true, "Loading...");

            }

            @Override
            protected boolean requestCondition() {

                return PostPageActivity.this.post != null
                        && PostPageActivity.this.post.getPostId() != -1L;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected void setRequestParams(HashMap<String, String> params) {
                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);
                params.put("guestId", guestId);
                params.put("hotelId", hotelId);
            }

            @Override
            protected int getMethod() {
                return Request.Method.GET;
            }

            @Override
            protected String getRequestUrl() {

                String url = GET_ALL_COMMENTS_OF_POST_URL;

                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);
                url += "?hotel=" + hotelId;

                long postId = PostPageActivity.this.post.getPostId();
                url += "&post=" + Long.toString(postId);

                if (PostPageActivity.this.postCommentsRecyclerView != null
                        && PostPageActivity.this.postCommentsRecyclerView.getRecyclerView() != null
                        && PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter() != null
                        && PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter() instanceof PostPageCommentsListAdapter
                        && ((PostPageCommentsListAdapter) PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter()).getFilter() != null) {

                    PostPageCommentsListAdapter adapter = ((PostPageCommentsListAdapter) PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter());

                    //offset & amount
                    if (adapter.getAllObjects() != null && adapter.getAllObjects().size() > 0) {
                        url += "&offset=" + adapter.getAllObjects().get(adapter.getAllObjects().size() - 1).getCommentId();
                    } else {
                        url += "&offset=0";
                    }

                    url += "&amount=20";

                    //sorting type
                    String sortingType = adapter.getSortingType();
                    if (sortingType != null && !sortingType.equals("")) {

                        url += "&sort=" + adapter.getSortingType();

                    }

                }

                return url;

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

                    if (response.getJSONObject("response").has("comments")) {

                        PostComment[] postComments = new PostComment[0];
                        if (response.getJSONObject("response").optJSONArray("comments") != null) {
                            String postCommentsString = response.getJSONObject("response").getJSONArray("comments").toString();
                            postComments = customGson.fromJson(postCommentsString, PostComment[].class);
                        } else if (response.getJSONObject("response").optJSONObject("comments") != null) {
                            String postCommentsString = response.getJSONObject("response").getJSONObject("comments").toString();
                            postComments = new PostComment[] {
                                    customGson.fromJson(postCommentsString, PostComment.class)
                            };
                        }

                        if (postComments != null && postComments.length > 0) {

                            if (PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter() != null
                                    && PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter() instanceof PostPageCommentsListAdapter) {

                                ArrayList<PostComment> postCommentsToInsert = new ArrayList<PostComment>();
                                ArrayList<PostComment> postCommentsFromServer = new ArrayList<PostComment>(Arrays.asList(postComments));

                                List<PostComment> allPostsComments = ((PostPageCommentsListAdapter) PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter()).getAllObjects();

                                ArrayList<Long> allPostsCommentsIds = new ArrayList<Long>();
                                for (PostComment postComment : allPostsComments) {
                                    if (postComment != null) {
                                        allPostsCommentsIds.add(postComment.getCommentId());
                                    }
                                }

                                for (PostComment postCommentFromServer : postCommentsFromServer) {
                                    if (postCommentFromServer != null && !allPostsCommentsIds.contains(postCommentFromServer.getCommentId())) {

                                        try {

                                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).parse(postCommentFromServer.getTimeStamp());

                                        } catch (Exception e) {

                                            postCommentFromServer.setTimeStamp(
                                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(System.currentTimeMillis())
                                            );

                                        }

                                        postCommentsToInsert.add(postCommentFromServer);
                                    }
                                }

                                ((PostPageCommentsListAdapter) PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter()).addObjects(postCommentsToInsert);
                                PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter().notifyDataSetChanged();

                            }

                            PostPageActivity.this.postCommentsRecyclerView.setHasMoreItems(true);

                        } else {

                            PostPageActivity.this.postCommentsRecyclerView.setFinishLoading(true, "-End-");
                            PostPageActivity.this.postCommentsRecyclerView.setHasMoreItems(false);

                        }


                    } else {

                        PostPageActivity.this.postCommentsRecyclerView.setFinishLoading(true, "-End-");
                        PostPageActivity.this.postCommentsRecyclerView.setHasMoreItems(false);

                    }

                    PostPageActivity.this.postCommentsRecyclerView.setScreenState(RECYCLER_VIEW);

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }
            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    PostPageActivity.this.postCommentsRecyclerView.setMessage(message, FAILURE_MESSAGE);
                } else {
                    PostPageActivity.this.postCommentsRecyclerView.setMessage("Getting Posts List Error!", FAILURE_MESSAGE);
                }
            }

            @Override
            protected void postRequest() {

                PostPageActivity.this.postCommentsRecyclerView.setLoadMore(false);
                PostPageActivity.this.postCommentsRecyclerView.setLoadingItems(false);

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void makeLikePostRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                PostPageActivity.this.likeLocked = true;

            }

            @Override
            protected boolean requestCondition() {

                return PostPageActivity.this.post != null
                        && PostPageActivity.this.post.getPostId() != -1L;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {

                try {

                    if (PostPageActivity.this.post.getPostId() != -1L) {

                        Guest guest = new Guest();
                        guest.setId(1);
                        JsonElement jsonGuest = MyGsonParser.getParser().create().toJsonTree(guest, Guest.class);

                        Post post = new Post();
                        post.setPostId(
                                PostPageActivity.this.post.getPostId()
                        );
                        JsonElement jsonPost = MyGsonParser.getParser().create().toJsonTree(post, Post.class);

                        JsonObject jsonRequest = new JsonObject();
                        jsonRequest.add("guest", jsonGuest);
                        jsonRequest.add("post", jsonPost);

                        JsonObject jsonLoginRequest = new JsonObject();
                        jsonLoginRequest.add("request", jsonRequest);

                        return new JSONObject( new Gson().toJson(jsonLoginRequest));

                    } else {

                        return null;

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected String getRequestUrl() {

                return LIKE_A_POST;

            }

            @Override
            protected int getMethod() {
                return Request.Method.POST;
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

                if (PostPageActivity.this.post != null) {

                    PostPageActivity.this.post.setLike(true);

                    PostPageActivity.this.updatePostLikesButtonImageView();

                    PostPageActivity.this.makeGetLikesCountRequest();

                    PostPageActivity.this.makeGetCommentsCountRequest();

                }

            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    //some error message
                } else {
                    //some default error message
                }
            }

            @Override
            protected void postRequest() {

                PostPageActivity.this.likeLocked = false;

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void makeUnlikePostRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                PostPageActivity.this.likeLocked = true;

            }

            @Override
            protected boolean requestCondition() {

                return PostPageActivity.this.post != null
                        && PostPageActivity.this.post.getPostId() != -1L;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {

                try {

                    if (PostPageActivity.this.post.getPostId() != -1L) {

                        Guest guest = new Guest();
                        guest.setId(1);
                        JsonElement jsonGuest = MyGsonParser.getParser().create().toJsonTree(guest, Guest.class);

                        Post post = new Post();
                        post.setPostId(
                                PostPageActivity.this.post.getPostId()
                        );
                        JsonElement jsonPost = MyGsonParser.getParser().create().toJsonTree(post, Post.class);

                        JsonObject jsonRequest = new JsonObject();
                        jsonRequest.add("guest", jsonGuest);
                        jsonRequest.add("post", jsonPost);

                        JsonObject jsonLoginRequest = new JsonObject();
                        jsonLoginRequest.add("request", jsonRequest);

                        return new JSONObject( new Gson().toJson(jsonLoginRequest));

                    } else {

                        return null;

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected String getRequestUrl() {

                return UNLIKE_A_POST;

            }

            @Override
            protected int getMethod() {
                return Request.Method.POST;
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

                if (PostPageActivity.this.post != null) {

                    PostPageActivity.this.post.setLike(false);

                    PostPageActivity.this.updatePostLikesButtonImageView();

                    PostPageActivity.this.makeGetLikesCountRequest();

                    PostPageActivity.this.makeGetCommentsCountRequest();

                }

            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    //some error message
                } else {
                    //some default error message
                }
            }

            @Override
            protected void postRequest() {

                PostPageActivity.this.likeLocked = false;

            }

        };

        jsonServerRequest.makeRequest();


    }

    private void makeGetLikesCountRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                PostPageActivity.this.likeLocked = true;

            }

            @Override
            protected boolean requestCondition() {

                return PostPageActivity.this.post != null
                        && PostPageActivity.this.post.getPostId() != -1L;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {

                return super.getJsonObject();

            }

            @Override
            protected String getRequestUrl() {

                String url = GET_LIKES_COUNT;

                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);
                url += "?hotel=" + hotelId;

                long postId = PostPageActivity.this.post.getPostId();
                url += "&post=" + Long.toString(postId);

                return url;

            }

            @Override
            protected int getMethod() {
                return Request.Method.GET;
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

                    if (response.getJSONObject("response").has("likesCount")) {

                        long likesCount = response.getJSONObject("response").getLong("likesCount");

                        if (PostPageActivity.this.post != null) {

                            PostPageActivity.this.post.setLikesCount((int) likesCount);

                            PostPageActivity.this.updatePostLikesTextView();

                        }

                    } else {

                        this.onDefaultError("error in parsing response");

                    }

                    PostPageActivity.this.postCommentsRecyclerView.setScreenState(RECYCLER_VIEW);

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }

            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    //some error message
                } else {
                    //some default error message
                }
            }

            @Override
            protected void postRequest() {

                PostPageActivity.this.likeLocked = false;

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void makeGetCommentsCountRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                PostPageActivity.this.commentLocked = true;

            }

            @Override
            protected boolean requestCondition() {

                return PostPageActivity.this.post != null
                        && PostPageActivity.this.post.getPostId() != -1L;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {

                return super.getJsonObject();

            }

            @Override
            protected String getRequestUrl() {

                String url = GET_COMMENTS_COUNT;

                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);
                url += "?hotel=" + hotelId;

                long postId = PostPageActivity.this.post.getPostId();
                url += "&post=" + Long.toString(postId);

                return url;

            }

            @Override
            protected int getMethod() {
                return Request.Method.GET;
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

                    if (response.getJSONObject("response").has("commentsCount")) {

                        long commentsCount = response.getJSONObject("response").getLong("commentsCount");

                        if (PostPageActivity.this.post != null) {

                            PostPageActivity.this.post.setCommentsCount((int) commentsCount);

                            PostPageActivity.this.updatePostCommentsTextView();

                        }

                    } else {

                        this.onDefaultError("error in parsing response");

                    }

                    PostPageActivity.this.postCommentsRecyclerView.setScreenState(RECYCLER_VIEW);

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }

            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {
                    //some error message
                } else {
                    //some default error message
                }
            }

            @Override
            protected void postRequest() {

                PostPageActivity.this.commentLocked = false;

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void makeCommentPostRequest(String comment) {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                PostPageActivity.this.commentLocked = true;

                PostPageActivity.this.likeLocked = true;

            }

            @Override
            protected boolean requestCondition() {

                return PostPageActivity.this.post != null
                        && PostPageActivity.this.post.getPostId() != -1L
                        && PostPageActivity.this.isCommentValid(comment);

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {

                try {

                    if (PostPageActivity.this.post.getPostId() != -1L) {

                        PostComment postComment = new PostComment();
                        postComment.setContent(comment);

                        Guest guest = new Guest();
                        guest.setId(1);
                        postComment.setGuest(guest);

                        Post post = new Post();
                        post.setPostId(
                                PostPageActivity.this.post.getPostId()
                        );
                        postComment.setPost(post);

                        JsonElement jsonComment = MyGsonParser.getParser().create().toJsonTree(postComment, PostComment.class);


                        JsonObject jsonRequest = new JsonObject();
                        jsonRequest.add("comment", jsonComment);

                        JsonObject jsonLoginRequest = new JsonObject();
                        jsonLoginRequest.add("request", jsonRequest);

                        return new JSONObject( new Gson().toJson(jsonLoginRequest));

                    } else {

                        return null;

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected String getRequestUrl() {

                return COMMENT_A_POST;

            }

            @Override
            protected int getMethod() {
                return Request.Method.POST;
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

                    if (response.getJSONObject("response").has("comments")) {

                        PostComment[] postComments = new PostComment[0];
                        if (response.getJSONObject("response").optJSONArray("comments") != null) {
                            String postCommentsString = response.getJSONObject("response").getJSONArray("comments").toString();
                            postComments = customGson.fromJson(postCommentsString, PostComment[].class);
                        } else if (response.getJSONObject("response").optJSONObject("comments") != null) {
                            String postCommentsString = response.getJSONObject("response").getJSONObject("comments").toString();
                            postComments = new PostComment[] {
                                    customGson.fromJson(postCommentsString, PostComment.class)
                            };
                        }

                        if (postComments != null && postComments.length > 0) {

                            if (PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter() != null
                                    && PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter() instanceof PostPageCommentsListAdapter) {

                                ArrayList<PostComment> postCommentsToInsert = new ArrayList<PostComment>();
                                ArrayList<PostComment> postCommentsFromServer = new ArrayList<PostComment>(Arrays.asList(postComments));

                                List<PostComment> allPostsComments = ((PostPageCommentsListAdapter) PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter()).getAllObjects();

                                ArrayList<Long> allPostsCommentsIds = new ArrayList<Long>();
                                for (PostComment postComment : allPostsComments) {
                                    if (postComment != null) {
                                        allPostsCommentsIds.add(postComment.getCommentId());
                                    }
                                }


                                for (PostComment postCommentFromServer : postCommentsFromServer) {
                                    if (postCommentFromServer != null && !allPostsCommentsIds.contains(postCommentFromServer.getCommentId())) {

                                        try {

                                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).parse(postCommentFromServer.getTimeStamp());

                                        } catch (Exception e) {

                                            postCommentFromServer.setTimeStamp(
                                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(System.currentTimeMillis())
                                            );

                                        }

                                        postCommentsToInsert.add(postCommentFromServer);
                                    }
                                }

                                ((PostPageCommentsListAdapter) PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter()).addObjects(postCommentsToInsert);
                                PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter().notifyDataSetChanged();

                            }

                        }

                    }

                    PostPageActivity.this.postCommentsRecyclerView.setScreenState(RECYCLER_VIEW);

                    PostPageActivity.this.postCommentInputCommentTextView.setText("");

                    PostPageActivity.this.makeGetLikesCountRequest();

                    PostPageActivity.this.makeGetCommentsCountRequest();

                } catch (JSONException e) {
                    e.printStackTrace();
                    this.onDefaultError("error in parsing response");
                }

            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {

                    Toast toast=Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    //some error message

                } else {

                    Toast toast=Toast.makeText(getApplicationContext(),"Error publishing comment", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    //some default error message

                }
            }

            @Override
            protected void postRequest() {

                PostPageActivity.this.commentLocked = false;

                PostPageActivity.this.likeLocked = false;

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void makeDeleteCommentRequest(PostComment postComment) {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

            }

            @Override
            protected boolean requestCondition() {

                return postComment != null
                        && postComment.getCommentId() != -1L;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {

                return super.getJsonObject();
//
//                try {
//
//                    if (PostPageActivity.this.postPageComment.getCommentId() != -1L) {
//
//
//                        JsonElement jsonComment = MyGsonParser.getParser().create().toJsonTree(PostPageActivity.this.postPageComment, PostComment.class);
//
//
//                        JsonObject jsonRequest = new JsonObject();
//                        jsonRequest.add("comment", jsonComment);
//
//                        JsonObject jsonLoginRequest = new JsonObject();
//                        jsonLoginRequest.add("request", jsonRequest);
//
//                        return new JSONObject( new Gson().toJson(jsonLoginRequest));
//
//                    } else {
//
//                        return null;
//
//                    }
//
//                } catch (JSONException e) {
//
//                    e.printStackTrace();
//                    return null;
//                }

            }

            @Override
            protected String getRequestUrl() {

                String url = DELETE_A_COMMENT;

                url += "?comment=" + Long.toString(postComment.getCommentId());

                return url;

            }

            @Override
            protected int getMethod() {
                return Request.Method.DELETE;
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

                if (PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter() != null
                        && PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter() instanceof PostPageCommentsListAdapter) {

                    PostPageCommentsListAdapter postPageCommentsListAdapter =
                            ((PostPageCommentsListAdapter)PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter());

                    long postCommentId = postComment.getCommentId();

                    List<PostComment> postComments = postPageCommentsListAdapter.getAllObjects();

                    List<PostComment> postCommentsToRemove = new ArrayList<PostComment>();

                    for (PostComment postComment : postComments) {

                        if (postComment != null && postComment.getCommentId() == postCommentId) {

                            postCommentsToRemove.add(postComment);

                        }

                    }

                    postPageCommentsListAdapter.getAllObjects().removeAll(postCommentsToRemove);

                    postPageCommentsListAdapter.updateObjects();

                    postPageCommentsListAdapter.notifyDataSetChanged();

                    PostPageActivity.this.makeGetLikesCountRequest();

                    PostPageActivity.this.makeGetCommentsCountRequest();

                }


            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {

                    Toast toast=Toast.makeText(PostPageActivity.this, message, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    //some error message

                } else {

                    Toast toast=Toast.makeText(PostPageActivity.this,"Error deleting comment", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    //some default error message

                }
            }

            @Override
            protected void postRequest() {

            }

        };

        jsonServerRequest.makeRequest();


    }

    private void makeEditCommentRequest(PostComment postComment) {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

            }

            @Override
            protected boolean requestCondition() {

                return postComment != null
                        && postComment.getCommentId() != -1L;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {

                try {

                    if (postComment.getCommentId() != -1L) {


                        JsonElement jsonComment = MyGsonParser.getParser().create().toJsonTree(postComment, PostComment.class);

                        JsonObject jsonRequest = new JsonObject();
                        jsonRequest.add("comment", jsonComment);

                        JsonObject jsonLoginRequest = new JsonObject();
                        jsonLoginRequest.add("request", jsonRequest);

                        return new JSONObject( new Gson().toJson(jsonLoginRequest));

                    } else {

                        return null;

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected String getRequestUrl() {

                String url = DELETE_A_COMMENT;

                url += "?comment=" + Long.toString(postComment.getCommentId());

                return url;

            }

            @Override
            protected int getMethod() {
                return Request.Method.PUT;
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

                if (PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter() != null
                        && PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter() instanceof PostPageCommentsListAdapter) {

                    PostPageCommentsListAdapter postPageCommentsListAdapter =
                            ((PostPageCommentsListAdapter)PostPageActivity.this.postCommentsRecyclerView.getRecyclerView().getAdapter());

                    long postCommentId = postComment.getCommentId();
                    String postCommentContent = postComment.getContent();

                    List<PostComment> postComments = postPageCommentsListAdapter.getAllObjects();

                    for (PostComment postComment : postComments) {

                        if (postComment != null && postComment.getCommentId() == postCommentId) {

                            postComment.setContent(postCommentContent);

                        }

                    }

                    postPageCommentsListAdapter.updateObjects();
                    postPageCommentsListAdapter.notifyDataSetChanged();

                    PostPageActivity.this.onPostCommentInputUpperMessageCloseButtonClicked();

                    PostPageActivity.this.postCommentToEdit = null;

                    PostPageActivity.this.makeGetLikesCountRequest();

                    PostPageActivity.this.makeGetCommentsCountRequest();

                }


            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {

                    Toast toast=Toast.makeText(PostPageActivity.this, message, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    //some error message

                } else {

                    Toast toast=Toast.makeText(PostPageActivity.this,"Error editing comment", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    //some default error message

                }
            }

            @Override
            protected void postRequest() {

            }

        };

        jsonServerRequest.makeRequest();


    }

    private void makeDeletePostRequest(Post post) {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

            }

            @Override
            protected boolean requestCondition() {

                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);

                return PostPageActivity.this.post != null && guestId != null && hotelId != null && post != null
                        && post.getPostId() != -1L;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {

//                try {
//
//                    Guest guest = new Guest();
//                    guest.setId(1);
//                    post.setGuest(guest);
//
//                    Hotel hotel = new Hotel();
//                    hotel.setId(1);
//                    post.setHotel(hotel);
//
//                    JsonElement jsonPost = MyGsonParser.getParser().create().toJsonTree(post, Post.class);
//
//                    JsonObject jsonRequest = new JsonObject();
//                    jsonRequest.add("post", jsonPost);
//
//                    JsonObject jsonLoginRequest = new JsonObject();
//                    jsonLoginRequest.add("request", jsonRequest);
//
//                    return new JSONObject( new Gson().toJson(jsonLoginRequest));
//
//                } catch (JSONException e) {
//
//                    e.printStackTrace();
//                    return null;
//                }


                return null;

            }

            @Override
            protected String getRequestUrl() {

                String url = DELETE_A_POST;

                String guestId = BOBGuestApplication.get().getSecureSharedPreferences().getString("guestId", null);
                url += "?guest=" + guestId;

                String hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getString("hotelId", null);
                url += "&hotel=" + hotelId;

                url += "&post=" + Long.toString(post.getPostId());

                return url;

            }

            @Override
            protected int getMethod() {
                return Request.Method.DELETE;
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

                String postJson = GsonParser.getParser().create().toJson(post, Post.class);

                Intent returnIntent = new Intent();

                returnIntent.putExtra(PostContentActivity.ARG_EXTRA_POST_CONTENT_POST_JSON, postJson);

                returnIntent.putExtra("DELETE", true);

                PostPageActivity.this.setResult(RESULT_OK, returnIntent);

                PostPageActivity.this.finishActivity(RESPONSE_UPLOADED_POST);

                PostPageActivity.this.finish();

            }

            @Override
            protected void onDefaultError(String message) {
                if (message != null) {

                    Toast toast=Toast.makeText(PostPageActivity.this, message, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    //some error message

                } else {

                    Toast toast=Toast.makeText(PostPageActivity.this, "Error deleting comment", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    //some default error message

                }
            }

            @Override
            protected void postRequest() {

            }

        };

        jsonServerRequest.makeRequest();


    }

    //animations
    private void initAnimations() {

        this.initTouchDownAnimation();

        this.initTouchUpAnimation();

    }

    private void initTouchDownAnimation() {

        this.touchDownAnimation = new ScaleAnimation(1f, 0.80f, 1f, 0.80f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        this.touchDownAnimation.setDuration(300);
        this.touchDownAnimation.setInterpolator(new OvershootInterpolator());

    }

    private void initTouchUpAnimation() {

        this.touchUpAnimation = new ScaleAnimation(0.80f, 1f, 0.80f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        this.touchUpAnimation.setDuration(300);
        this.touchUpAnimation.setInterpolator(new OvershootInterpolator());

    }

    private String prettyTimestamp(String timestamp) {

        PrettyTime prettyTime = new PrettyTime(Locale.getDefault());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

        try {

            Calendar timestampCalendar = Calendar.getInstance();
            Date timestampDate = dateFormat.parse(timestamp);
            timestampCalendar.setTimeInMillis(timestampDate.getTime());

            Calendar currentTime = Calendar.getInstance();

            if (TimeUnit.MILLISECONDS.toHours(currentTime.getTimeInMillis()) - TimeUnit.MILLISECONDS.toHours(timestampCalendar.getTimeInMillis()) <= 24) {

                return prettyTime.format(dateFormat.parse(timestamp));

            } else if (currentTime.get(Calendar.YEAR) == timestampCalendar.get(Calendar.YEAR)) {

                SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.getDefault());

                return newDateFormat.format(timestampDate);

            } else {

                SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());

                return newDateFormat.format(timestampDate);

            }

        } catch (ParseException e) {

            e.printStackTrace();

            return timestamp;

        }

    }

    protected boolean isRtl() {
        return TextUtilsCompat.getLayoutDirectionFromLocale(
                this.getResources().getConfiguration().locale) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

}
