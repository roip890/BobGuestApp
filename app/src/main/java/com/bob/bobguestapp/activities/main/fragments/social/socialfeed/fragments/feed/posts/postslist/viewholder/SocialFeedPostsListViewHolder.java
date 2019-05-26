package com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder;

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
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.text.TextUtilsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.Request;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.likespage.LikesListPageActivity;
import com.bob.bobguestapp.activities.main.fragments.social.postpage.PostPageActivity;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.listener.PostsListEventsListener;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.adapter.SocialFeedPostsListMediaAdapter;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.cachemanager.SocialFeedPostsListCacheManager;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.container.OnMediaLoadListener;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.container.SocialFeedPostsListMediaContainer;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.layoutmanager.SocialFeedPostsListMediaLinearLayoutManager;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.playbackinfo.SocialFeedPostsListPlaybackInfo;
import com.bob.bobguestapp.tools.database.objects.Post;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.toolsmodule.parsing.GsonParser;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.popup.MyPowerMenu;
import com.bob.uimodule.popup.PowerMenuListener;
import com.bob.uimodule.video.exoplayer.ExoPlayerFullScreenListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mikepenz.iconics.IconicsDrawable;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnDismissedListener;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenuItem;

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
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;
import im.ene.toro.widget.PressablePlayerSelector;

import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by User on 07/09/2018.
 */

public class SocialFeedPostsListViewHolder extends RecyclerView.ViewHolder implements ToroPlayer, OnMediaLoadListener {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    private static final int POST_ITEM_DROPDOWN_MENU_COPY_ID = 0;
    private static final int POST_ITEM_DROPDOWN_MENU_EDIT_ID = 1;
    private static final int POST_ITEM_DROPDOWN_MENU_DELETE_ID = 2;
    private static final int POST_ITEM_DROPDOWN_MENU_CANCEL_ID = 3;

    private static final int MAX_COMMENT_LENGTH = 150;
    private static final int MAX_COMMENT_LINES = 5;

    //get hotels url
    private static final String LIKE_A_POST = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/like";
    private static final String UNLIKE_A_POST = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/unlike";
    private static final String GET_LIKES_COUNT = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/like/count";
    private static final String GET_COMMENTS_COUNT = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/comment/count";

    //app theme
    private int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    private int screenSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;

    //context
    protected Context context;

    //fragment manager
    protected FragmentManager fragmentManager;

    //posts list events listener
    protected PostsListEventsListener postsListEventsListener;

    //views
    protected View view;
    protected ConstraintLayout backgroundLayout;

    //post header layout
    protected RelativeLayout postHeaderLayout;
    protected ImageView editorProfilePictureImageView;
    protected TextView editorNameTextView;
    protected TextView postTimestampTextView;
    protected ImageButton postMenuButton;
    protected View.OnTouchListener onTouchPostMenuButtonListener;

    //post text layout
    protected RelativeLayout postTextLayout;
    protected TextView postTextTextView;

    //post media layout
    protected ConstraintLayout postMediaLayout;
    protected SocialFeedPostsListMediaContainer postMediaContainer;
    private SocialFeedPostsListMediaAdapter postMediaContainerPlayerAdapter;
    private SocialFeedPostsListMediaLinearLayoutManager postMediaContainerLinearLayoutManager;
    private PressablePlayerSelector postMediaContainerPlayerSelector;
    private SnapHelper postMediaContainerSnapHelper;
    private ExoPlayerFullScreenListener exoPlayerFullScreenListener;

    //post footer layout
    protected RelativeLayout postFooterLayout;
    protected RelativeLayout postFooterIconsLayout;
    protected ImageView postLikesButtonImageView;
    protected View.OnTouchListener onTouchPostLikesButtonListener;
    protected ImageView postCommentsButtonImageView;
    protected View.OnTouchListener onTouchPostCommentsButtonListener;
    protected ImageView postShareButtonImageView;
    protected View.OnTouchListener onTouchPostShareButtonListener;
    protected RelativeLayout postFooterTextsLayout;
    protected TextView postLikesTextView;
    protected View.OnTouchListener onTouchPostLikesTextListener;
    protected TextView postIntermediateTextView;
    protected TextView postCommentsTextView;
    protected View.OnTouchListener onTouchPostCommentsTextListener;

    //popup
    private View popupAnchor;
    private MyPowerMenu longClickPowerMenu;
    private MyPowerMenu moreButtonPowerMenu;
    protected PowerMenuItem copyPostMenuItem;
    protected PowerMenuItem editPostMenuItem;
    protected PowerMenuItem deletePostMenuItem;
    protected PowerMenuItem cancelPostMenuItem;
    protected PowerMenuListener powerMenuListener;

    //post
    protected Post socialFeedPost;

    //lock like
    protected boolean likeLocked = false;

    //animations
    private Animation touchDownAnimation;
    private Animation touchUpAnimation;

    //toro
    private int initPosition = -1;

    public SocialFeedPostsListViewHolder(Context context, FragmentManager fragmentManager, View view, PowerMenuListener powerMenuListener, PostsListEventsListener postsListEventsListener) {
        super(view);

        this.context = context;

        this.fragmentManager = fragmentManager;

        this.powerMenuListener = powerMenuListener;

        this.postsListEventsListener = postsListEventsListener;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_SKIN);

        this.initView(view);

        this.initAnimations();

    }

    public void onResume() {


    }

    public void onPause() {


    }

    //general
    protected void initView(View view) {

        this.view = view;

        this.initPostBackgroundLayout(view);

    }

    private void initPostBackgroundLayout(View view) {

        this.backgroundLayout = (ConstraintLayout) view.findViewById(R.id.social_feed_post_view_holder_background_layout);

        this.initPostHeaderLayout(view);

        this.initPostTextLayout(view);

        this.initPostMediaLayout(view);

        this.initPostFooterLayout(view);

    }

    //post header
    private void initPostHeaderLayout(View view) {

        this.postHeaderLayout = (RelativeLayout) view.findViewById(R.id.social_feed_post_view_holder_header_layout);

        this.initPostMenuButton(view);

        this.initEditorProfilePictureImageView(view);

        this.initEditorNameTextView(view);

        this.initPostTimestampTextView(view);

        this.initPopupAnchor(view);

        this.initLongClickMenuItems();

    }

    private void initPostMenuButton(View view) {

        this.postMenuButton = (ImageButton) view.findViewById(R.id.social_feed_post_view_holder_post_menu_icon);

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

                        view.startAnimation(SocialFeedPostsListViewHolder.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(SocialFeedPostsListViewHolder.this.touchUpAnimation);

                        SocialFeedPostsListViewHolder.this.onPostMenuButtonClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.postMenuButton.setOnTouchListener(this.onTouchPostMenuButtonListener);

    }

    private void onPostMenuButtonClicked() {

        this.moreButtonPowerMenu = ((MyPowerMenu) new MyPowerMenu.Builder(this.context)
                .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                .setAutoDismiss(true)
                .addItem(this.editPostMenuItem)
                .addItem(this.deletePostMenuItem)
                .addItem(this.cancelPostMenuItem)
                .setMenuRadius(UIUtilsManager.get().convertDpToPixels(this.context, 10))
                .setMenuShadow(UIUtilsManager.get().convertDpToPixels(this.context, 2))
                .setTextColor(ContextCompat.getColor(this.context, R.color.light_primary_color))
                .setSelectedTextColor(ContextCompat.getColor(this.context, R.color.light_primary_color_half_opacity))
                .setMenuColor(ContextCompat.getColor(this.context, R.color.primary_color))
                .setSelectedMenuColor(ContextCompat.getColor(this.context, R.color.primary_color_half_opacity))
                .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {

                        SocialFeedPostsListViewHolder.this.onPostPowerMenuItemClick(position, item);

                    }
                }).setOnDismissListener(new OnDismissedListener() {
                    @Override
                    public void onDismissed() {

                        SocialFeedPostsListViewHolder.this.powerMenuListener.notifyPowerMenuShow(
                                SocialFeedPostsListViewHolder.this.moreButtonPowerMenu
                        );

                    }
                })
                .build());

        this.powerMenuListener.notifyPowerMenuShow(
                this.moreButtonPowerMenu
        );

        //screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)this.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        //popup location
        int[] popupLocation = new int[2];
        this.postHeaderLayout.getLocationOnScreen(popupLocation);
        int popupLocationY = popupLocation[1];

        //popup height
        int popupHeight = this.moreButtonPowerMenu.getContentViewHeight();

        if (popupLocationY + UIUtilsManager.get().convertDpToPixels(this.context, 12)
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

        this.editorProfilePictureImageView = (ImageView) view.findViewById(R.id.social_feed_post_view_holder_editor_profile_picture_view);

    }

    private void initEditorNameTextView(View view) {

        this.editorNameTextView = (TextView) view.findViewById(R.id.social_feed_post_view_holder_editor_name_view);

    }

    private void initPostTimestampTextView(View view) {

        this.postTimestampTextView = (TextView) view.findViewById(R.id.social_feed_post_view_holder_post_timestamp_view);

    }

    private void initPopupAnchor(View view) {

        this.popupAnchor = view.findViewById(R.id.social_feed_post_view_holder_popup_anchor);

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

        if (this.socialFeedPost != null
                && this.socialFeedPost.getText() != null
                && !this.socialFeedPost.getText().equals("")) {

            ClipboardManager clipboard = (ClipboardManager) this.context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Post: ",this.socialFeedPost.getText());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this.context, "Text copied", Toast.LENGTH_SHORT).show();

        }

    }

    private void initEditPostMenuItem() {

        this.editPostMenuItem = new PowerMenuItem("Edit", false);
        this.editPostMenuItem.setTag(POST_ITEM_DROPDOWN_MENU_EDIT_ID);

    }

    private void onEditPostMenuItemClick() {

        if (this.postsListEventsListener != null) {

            this.postsListEventsListener.onPostEdit(this.socialFeedPost);

        }

    }

    private void initDeletePostMenuItem() {

        this.deletePostMenuItem = new PowerMenuItem("Delete", false);
        this.deletePostMenuItem.setTag(POST_ITEM_DROPDOWN_MENU_DELETE_ID);

    }

    private void onDeletePostMenuItemClick() {

        if (this.postsListEventsListener != null) {

            this.postsListEventsListener.onPostDelete(this.socialFeedPost);

        }

    }

    private void initCancelPostMenuItem() {

        this.cancelPostMenuItem = new PowerMenuItem("Cancel", false);
        this.cancelPostMenuItem.setTag(POST_ITEM_DROPDOWN_MENU_CANCEL_ID);

    }

    private void onCancelPostMenuItemClick() {

    }

    private void onPostHeaderLongClick() {

        SocialFeedPostsListViewHolder.this.postTextLayout.setBackgroundColor(
                ContextCompat.getColor(this.context, R.color.dark_primary_color_extra_opacity)
        );

        this.longClickPowerMenu = ((MyPowerMenu) new MyPowerMenu.Builder(this.context)
                .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                .setAutoDismiss(true)
                .addItem(this.copyPostMenuItem)
                .setMenuRadius(UIUtilsManager.get().convertDpToPixels(this.context, 10))
                .setMenuShadow(UIUtilsManager.get().convertDpToPixels(this.context, 2))
                .setTextColor(ContextCompat.getColor(this.context, R.color.light_primary_color))
                .setSelectedTextColor(ContextCompat.getColor(this.context, R.color.light_primary_color_half_opacity))
                .setMenuColor(ContextCompat.getColor(this.context, R.color.primary_color))
                .setSelectedMenuColor(ContextCompat.getColor(this.context, R.color.primary_color_half_opacity))
                .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {

                        SocialFeedPostsListViewHolder.this.onPostPowerMenuItemClick(position, item);

                    }
                }).setOnDismissListener(new OnDismissedListener() {
                    @Override
                    public void onDismissed() {

                        SocialFeedPostsListViewHolder.this.powerMenuListener.notifyPowerMenuShow(
                                SocialFeedPostsListViewHolder.this.longClickPowerMenu
                        );

                        SocialFeedPostsListViewHolder.this.postTextLayout.setBackgroundColor(
                                ContextCompat.getColor(SocialFeedPostsListViewHolder.this.context, R.color.light_primary_color)
                        );

                    }
                })
                .build());

        this.powerMenuListener.notifyPowerMenuShow(
                this.longClickPowerMenu
        );

        //screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)this.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        //popup location
        int[] popupLocation = new int[2];
        this.postHeaderLayout.getLocationOnScreen(popupLocation);
        int popupLocationY = popupLocation[1];

        //popup height
        int popupHeight = this.longClickPowerMenu.getContentViewHeight();

        if (popupLocationY + UIUtilsManager.get().convertDpToPixels(this.context, 12)
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

    //post text
    private void initPostTextLayout(View view) {

        this.postTextLayout = (RelativeLayout) view.findViewById(R.id.social_feed_post_view_holder_text_layout);

        this.postTextLayout.setLongClickable(true);

        this.postTextLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                SocialFeedPostsListViewHolder.this.onPostHeaderLongClick();

                return true;
            }
        });

        this.postTextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (SocialFeedPostsListViewHolder.this.postTextTextView.getText().toString().contains(
                        SocialFeedPostsListViewHolder.this.socialFeedPost.getText())) {

                    if (SocialFeedPostsListViewHolder.this.socialFeedPost.getText().length() > MAX_COMMENT_LENGTH) {

                        String readMoreText = "Load More";
                        String shortcutContent = SocialFeedPostsListViewHolder.this.socialFeedPost.getText().substring(
                                0, MAX_COMMENT_LENGTH
                        );
                        String text = shortcutContent + " ... " + readMoreText;
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK)
                                , 0, text.length() - readMoreText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(SocialFeedPostsListViewHolder.this.context, R.color.primary_color))
                                , text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        SocialFeedPostsListViewHolder.this.postTextTextView.setText(
                                spannableStringBuilder
                        );

                    } else if (SocialFeedPostsListViewHolder.this.socialFeedPost.getText().split("\r\n|\r|\n").length > MAX_COMMENT_LINES) {

                        String readMoreText = "Load More";

                        String[] lines = SocialFeedPostsListViewHolder.this.socialFeedPost.getText().split("\r\n|\r|\n");
                        String shortcutContent = TextUtils.join("\n", Arrays.copyOfRange(lines, 0, MAX_COMMENT_LINES));

                        String text = shortcutContent + " ... \n" + readMoreText;
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                        spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(SocialFeedPostsListViewHolder.this.context, R.color.primary_color))
                                , text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        SocialFeedPostsListViewHolder.this.postTextTextView.setText(
                                spannableStringBuilder
                        );

                    }

                } else {

                    String readLessText = "";
                    String text = SocialFeedPostsListViewHolder.this.socialFeedPost.getText() + readLessText;
                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                    spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readLessText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK)
                            , 0, text.length() - readLessText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(SocialFeedPostsListViewHolder.this.context, R.color.primary_color))
                            , text.length() - readLessText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    SocialFeedPostsListViewHolder.this.postTextTextView.setText(
                            spannableStringBuilder
                    );

                }

            }
        });

        this.initPostTextTextView(view);

    }

    private void initPostTextTextView(View view) {

        this.postTextTextView = (TextView) view.findViewById(R.id.social_feed_post_view_holder_post_text_view);

    }

    //post media
    private void initPostMediaLayout(View view) {

        this.postMediaLayout = (ConstraintLayout) view.findViewById(R.id.social_feed_post_view_holder_media_layout);

        this.initPostMediaContainer(view);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initPostMediaContainer(View view) {

        this.postMediaContainer = (SocialFeedPostsListMediaContainer) view.findViewById(R.id.social_feed_post_view_holder_media_container);

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

                            SocialFeedPostsListViewHolder.this.onGetDimensionsRatio(imageViewLayoutParams.dimensionRatio);

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
                                SocialFeedPostsListViewHolder.this.onGetDimensionsRatio((String) playerView.getTag());
                            }

                        }

                    } else {

                        SocialFeedPostsListViewHolder.this.onGetDimensionsRatio("H,1:1");

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

        //full screen listener
        this.initFullScreenListener();

        //adapter
        this.initPostMediaContainerAdapter();

    }

    private void initPostMediaContainerLinearLayoutManager() {

        this.postMediaContainerLinearLayoutManager = new SocialFeedPostsListMediaLinearLayoutManager(this.context);
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

    private void initFullScreenListener() {

        this.exoPlayerFullScreenListener = new ExoPlayerFullScreenListener() {

            int orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

            @Override
            public void startFullScreen() {

                SocialFeedPostsListViewHolder.this.postMediaContainer.setPlayerSelector(PlayerSelector.NONE);

                if (SocialFeedPostsListViewHolder.this.context instanceof Activity) {

                    this.orientation = ((Activity)SocialFeedPostsListViewHolder.this.context).getRequestedOrientation();

                    ((Activity)SocialFeedPostsListViewHolder.this.context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);

                }

            }

            @Override
            public void exitFullScreen(Uri mediaUri, PlaybackInfo playbackInfo, int order, Point videoSize) {

                SocialFeedPostsListViewHolder.this.postMediaContainer.savePlaybackInfo(order, playbackInfo);

                SocialFeedPostsListViewHolder.this.postMediaContainer.setPlayerSelector(
                        SocialFeedPostsListViewHolder.this.postMediaContainerPlayerSelector
                );

                if (SocialFeedPostsListViewHolder.this.context instanceof Activity) {

                    ((Activity)SocialFeedPostsListViewHolder.this.context).setRequestedOrientation(this.orientation);

                }

            }
        };

    }

    private void initPostMediaContainerAdapter() {

        this.postMediaContainerPlayerAdapter = new SocialFeedPostsListMediaAdapter(
                this.context,
                this.fragmentManager,
                this.exoPlayerFullScreenListener,
                this,
                this.postMediaContainerPlayerSelector,
                new ArrayList<MediaItem>()
        );

        this.postMediaContainer.setAdapter(this.postMediaContainerPlayerAdapter);

    }

    //post footer
    private void initPostFooterLayout(View view) {

        this.postFooterLayout = (RelativeLayout) view.findViewById(R.id.social_feed_post_view_holder_footer_layout);

        this.initPostFooterIconsLayout(view);

        this.initPostFooterTextsLayout(view);
    }

    private void initPostFooterIconsLayout(View view) {

        this.postFooterIconsLayout = (RelativeLayout) view.findViewById(R.id.social_feed_post_view_holder_footer_icons_layout);

        this.initPostLikesButtonImageView(view);

        this.initPostCommentsButtonImageView(view);

        this.initPostShareButtonImageView(view);

    }

    private void initPostLikesButtonImageView(View view) {

        this.postLikesButtonImageView = (ImageView) view.findViewById(R.id.social_feed_post_view_holder_like_button);

        this.initOnTouchPostLikesButtonListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPostLikesButtonListener() {

        this.onTouchPostLikesButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(SocialFeedPostsListViewHolder.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(SocialFeedPostsListViewHolder.this.touchUpAnimation);

                        SocialFeedPostsListViewHolder.this.onPostLikesButtonClicked();

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

            if (this.socialFeedPost != null) {

                if (this.socialFeedPost.isLike()) {

                    this.makeUnlikePostRequest();

                } else {

                    this.makeLikePostRequest();

                }

            }

        }

    }

    private void initPostCommentsButtonImageView(View view) {

        this.postCommentsButtonImageView = (ImageView) view.findViewById(R.id.social_feed_post_view_holder_comments_button);

        this.initOnTouchPostCommentsButtonListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPostCommentsButtonListener() {

        this.onTouchPostCommentsButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(SocialFeedPostsListViewHolder.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(SocialFeedPostsListViewHolder.this.touchUpAnimation);

                        SocialFeedPostsListViewHolder.this.onPostCommentsButtonClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.postCommentsButtonImageView.setOnTouchListener(this.onTouchPostCommentsButtonListener);

    }

    private void onPostCommentsButtonClicked() {

        if (this.socialFeedPost != null && this.postsListEventsListener != null) {

            this.postsListEventsListener.showPostPage(this.socialFeedPost);

        }

    }

    private void initPostShareButtonImageView(View view) {

        this.postShareButtonImageView = (ImageView) view.findViewById(R.id.social_feed_post_view_holder_share_button);

        this.initOnTouchPostShareButtonListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPostShareButtonListener() {

        this.onTouchPostShareButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(SocialFeedPostsListViewHolder.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(SocialFeedPostsListViewHolder.this.touchUpAnimation);

                        SocialFeedPostsListViewHolder.this.onPostShareButtonClicked();

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

        this.postFooterTextsLayout = (RelativeLayout) view.findViewById(R.id.social_feed_post_view_holder_footer_texts_layout);

        this.initPostLikesTextView(view);

        this.initPostIntermediateTextView(view);

        this.initPostCommentsTextView(view);

    }

    private void initPostLikesTextView(View view) {

        this.postLikesTextView = (TextView) view.findViewById(R.id.social_feed_post_view_holder_likes_text);

        this.initOnTouchPostLikesTextListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPostLikesTextListener() {

        this.onTouchPostLikesTextListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        SocialFeedPostsListViewHolder.this.postLikesTextView.setTextColor(
                                ContextCompat.getColor(SocialFeedPostsListViewHolder.this.context, R.color.primary_color_extra_opacity)
                        );

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        SocialFeedPostsListViewHolder.this.postLikesTextView.setTextColor(
                                ContextCompat.getColor(SocialFeedPostsListViewHolder.this.context, R.color.primary_color_half_opacity)
                        );

                        SocialFeedPostsListViewHolder.this.onPostLikesTextClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.postLikesTextView.setOnTouchListener(this.onTouchPostLikesTextListener);

    }

    private void onPostLikesTextClicked() {

        if (this.socialFeedPost != null && this.context != null) {

            Intent intent = new Intent(this.context, LikesListPageActivity.class);

            String postJson = GsonParser.getParser().create().toJson(this.socialFeedPost, Post.class);

            intent.putExtra(PostPageActivity.ARG_EXTRA_POST_PAGE_POST_JSON, postJson);

            this.context.startActivity(intent);

        }

    }

    private void initPostIntermediateTextView(View view) {

        this.postIntermediateTextView = (TextView) view.findViewById(R.id.social_feed_post_view_holder_intermediate_text);

    }

    private void initPostCommentsTextView(View view) {

        this.postCommentsTextView = (TextView) view.findViewById(R.id.social_feed_post_view_holder_comments_text);

        this.initOnTouchPostCommentsTextListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPostCommentsTextListener() {

        this.onTouchPostCommentsTextListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        SocialFeedPostsListViewHolder.this.postCommentsTextView.setTextColor(
                                ContextCompat.getColor(SocialFeedPostsListViewHolder.this.context, R.color.primary_color_extra_opacity)
                        );

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        SocialFeedPostsListViewHolder.this.postCommentsTextView.setTextColor(
                                ContextCompat.getColor(SocialFeedPostsListViewHolder.this.context, R.color.primary_color_half_opacity)
                        );

                        SocialFeedPostsListViewHolder.this.onPostLikesCommentsClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.postCommentsTextView.setOnTouchListener(this.onTouchPostCommentsTextListener);

    }

    private void onPostLikesCommentsClicked() {

        this.onPostCommentsButtonClicked();

    }

    //social feed post
    public void setSocialFeedPost(Post socialFeedPost) {

        if (socialFeedPost != null) {

            this.socialFeedPost = socialFeedPost;

        }

        this.updatePost();

    }

    //http requests
    private void makeLikePostRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                SocialFeedPostsListViewHolder.this.likeLocked = true;

            }

            @Override
            protected boolean requestCondition() {

                return SocialFeedPostsListViewHolder.this.socialFeedPost != null
                        && SocialFeedPostsListViewHolder.this.socialFeedPost.getPostId() != -1L;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {

                try {

                    if (SocialFeedPostsListViewHolder.this.socialFeedPost.getPostId() != -1L) {

                        Guest guest = new Guest();
                        guest.setId(1);

                        JsonElement jsonGuest = MyGsonParser.getParser().create().toJsonTree(guest, Guest.class);

                        Post post = new Post();
                        post.setPostId(
                                SocialFeedPostsListViewHolder.this.socialFeedPost.getPostId()
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

                if (SocialFeedPostsListViewHolder.this.socialFeedPost != null) {

                    SocialFeedPostsListViewHolder.this.socialFeedPost.setLike(true);

                    SocialFeedPostsListViewHolder.this.updatePostLikesButtonImageView();

                    SocialFeedPostsListViewHolder.this.makeGetLikesCountRequest();

                    SocialFeedPostsListViewHolder.this.makeGetCommentsCountRequest();

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

                SocialFeedPostsListViewHolder.this.likeLocked = false;

            }

        };

        jsonServerRequest.makeRequest();


    }

    private void makeUnlikePostRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                SocialFeedPostsListViewHolder.this.likeLocked = true;

            }

            @Override
            protected boolean requestCondition() {

                return SocialFeedPostsListViewHolder.this.socialFeedPost != null
                        && SocialFeedPostsListViewHolder.this.socialFeedPost.getPostId() != -1L;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {

                try {

                    if (SocialFeedPostsListViewHolder.this.socialFeedPost.getPostId() != -1L) {

                        Guest guest = new Guest();
                        guest.setId(1);
                        JsonElement jsonGuest = MyGsonParser.getParser().create().toJsonTree(guest, Guest.class);

                        Post post = new Post();
                        post.setPostId(
                                SocialFeedPostsListViewHolder.this.socialFeedPost.getPostId()
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

                if (SocialFeedPostsListViewHolder.this.socialFeedPost != null) {

                    SocialFeedPostsListViewHolder.this.socialFeedPost.setLike(false);

                    SocialFeedPostsListViewHolder.this.updatePostLikesButtonImageView();

                    SocialFeedPostsListViewHolder.this.makeGetLikesCountRequest();

                    SocialFeedPostsListViewHolder.this.makeGetCommentsCountRequest();

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

                SocialFeedPostsListViewHolder.this.likeLocked = false;

            }

        };

        jsonServerRequest.makeRequest();


    }

    private void makeGetLikesCountRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                SocialFeedPostsListViewHolder.this.likeLocked = true;

            }

            @Override
            protected boolean requestCondition() {

                return SocialFeedPostsListViewHolder.this.socialFeedPost != null
                        && SocialFeedPostsListViewHolder.this.socialFeedPost.getPostId() != -1L;

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

                long postId = SocialFeedPostsListViewHolder.this.socialFeedPost.getPostId();
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

                        if (SocialFeedPostsListViewHolder.this.socialFeedPost != null) {

                            SocialFeedPostsListViewHolder.this.socialFeedPost.setLikesCount((int) likesCount);

                            SocialFeedPostsListViewHolder.this.updatePostLikesTextView();

                        }

                    } else {

                        this.onDefaultError("error in parsing response");

                    }

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

                SocialFeedPostsListViewHolder.this.likeLocked = false;

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void makeGetCommentsCountRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

                SocialFeedPostsListViewHolder.this.likeLocked = true;

            }

            @Override
            protected boolean requestCondition() {

                return SocialFeedPostsListViewHolder.this.socialFeedPost != null
                        && SocialFeedPostsListViewHolder.this.socialFeedPost.getPostId() != -1L;

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

                long postId = SocialFeedPostsListViewHolder.this.socialFeedPost.getPostId();
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

                        if (SocialFeedPostsListViewHolder.this.socialFeedPost != null) {

                            SocialFeedPostsListViewHolder.this.socialFeedPost.setCommentsCount((int) commentsCount);

                            SocialFeedPostsListViewHolder.this.updatePostCommentsTextView();

                        }

                    } else {

                        this.onDefaultError("error in parsing response");

                    }

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

                SocialFeedPostsListViewHolder.this.likeLocked = false;

            }

        };

        jsonServerRequest.makeRequest();

    }

    //update
    public void updatePost() {

        if (this.socialFeedPost != null) {

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
                ((IconicsDrawable) Icons.get().findDrawable(this.context, "gmd_more_horiz"))
                        .sizeDp(16).color(Color.DKGRAY));

    }

    //post editor
    private void updateEditor() {

        if (this.socialFeedPost.getGuest() != null
                || this.socialFeedPost.getHotel() != null) {

            //post editor profile picture
            this.updateEditorProfilePictureImageView();

            //post editor name
            this.updateEditorNameTextView();

        }

    }

    //post editor profile picture
    private void updateEditorProfilePictureImageView() {

        if (this.socialFeedPost != null && this.socialFeedPost.getGuest() != null && this.socialFeedPost.getGuest().getImageUrl() != null) {

            Glide.with(this.context)
                    .asBitmap()
                    .load(this.socialFeedPost.getGuest().getImageUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.circleCropTransform())
                    .into(this.editorProfilePictureImageView);

        } else if (this.socialFeedPost != null && this.socialFeedPost.getHotel() != null && this.socialFeedPost.getHotel().getImageUrl() != null) {

            Glide.with(this.context)
                    .asBitmap()
                    .load(this.socialFeedPost.getHotel().getImageUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.circleCropTransform())
                    .into(this.editorProfilePictureImageView);

        } else {

            //put an placeholder default image
//            Glide.with(this.context)
//                    .asBitmap()
//                    .load(placeholderimage)
//                    .into(this.editorProfilePictureImageView);

        }

    }

    //post editor name
    private void updateEditorNameTextView() {

        if (this.socialFeedPost != null && this.socialFeedPost.getGuest() != null && this.socialFeedPost.getGuest().getName() != null) {

            this.editorNameTextView.setText(this.socialFeedPost.getGuest().getName());

        } else if (this.socialFeedPost != null && this.socialFeedPost.getHotel() != null && this.socialFeedPost.getHotel().getName() != null) {

            this.editorNameTextView.setText(this.socialFeedPost.getHotel().getName());

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

        if (this.socialFeedPost != null && this.socialFeedPost.getTimeStamp() != null) {

            this.postTimestampTextView.setText(prettyTimestamp(this.socialFeedPost.getTimeStamp()));

        } else {

            this.postTimestampTextView.setText("--/--/----");

        }

        this.postTimestampTextView.setTextColor(
                Color.DKGRAY
        );

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

    //post text
    private void updatePostTextTextView() {

        if (this.socialFeedPost != null && this.socialFeedPost.getText() != null) {

            if (SocialFeedPostsListViewHolder.this.socialFeedPost.getText().length() > MAX_COMMENT_LENGTH) {

                String readMoreText = "Load More";
                String shortcutContent = SocialFeedPostsListViewHolder.this.socialFeedPost.getText().substring(
                        0, MAX_COMMENT_LENGTH
                );
                String text = shortcutContent + " .. " + readMoreText;
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK)
                        , 0, text.length() - readMoreText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(SocialFeedPostsListViewHolder.this.context, R.color.primary_color))
                        , text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                SocialFeedPostsListViewHolder.this.postTextTextView.setText(
                        spannableStringBuilder
                );
            } else if (SocialFeedPostsListViewHolder.this.socialFeedPost.getText().split("\r\n|\r|\n").length > MAX_COMMENT_LINES) {

                String readMoreText = "Load More";

                String[] lines = SocialFeedPostsListViewHolder.this.socialFeedPost.getText().split("\r\n|\r|\n");
                String shortcutContent = TextUtils.join("\n", Arrays.copyOfRange(lines, 0, MAX_COMMENT_LINES));

                String text = shortcutContent + " ... \n" + readMoreText;
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(SocialFeedPostsListViewHolder.this.context, R.color.primary_color))
                        , text.length() - readMoreText.length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                SocialFeedPostsListViewHolder.this.postTextTextView.setText(
                        spannableStringBuilder
                );

            } else {

                String text = SocialFeedPostsListViewHolder.this.socialFeedPost.getText();
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK)
                        , 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                SocialFeedPostsListViewHolder.this.postTextTextView.setText(
                        spannableStringBuilder
                );

            }

        } else {

            this.postTextTextView.setText("");

        }

    }

    //post media
    private void updatePostMediaContainer() {

        if (this.socialFeedPost != null && this.socialFeedPost.getMediaItems() != null) {

            if (this.postMediaContainer.getAdapter() instanceof SocialFeedPostsListMediaAdapter) {

                SocialFeedPostsListMediaAdapter socialFeedPostsListMediaAdapter =
                        ((SocialFeedPostsListMediaAdapter)this.postMediaContainer.getAdapter());

                socialFeedPostsListMediaAdapter.setMediaItems(
                        new ArrayList<MediaItem>(Arrays.asList(SocialFeedPostsListViewHolder.this.socialFeedPost.getMediaItems())));

                if (SocialFeedPostsListViewHolder.this.postMediaContainer.getCacheManager() == null) {

                    SocialFeedPostsListViewHolder.this.postMediaContainer.setCacheManager(new SocialFeedPostsListCacheManager(
                            new ArrayList<MediaItem>(Arrays.asList(SocialFeedPostsListViewHolder.this.socialFeedPost.getMediaItems())
                            )));

                }

                socialFeedPostsListMediaAdapter.notifyDataSetChanged();

            }

        }

    }

    @Override
    public void onGetDimensionsRatio(String dimensionsRatio) {

        if (SocialFeedPostsListViewHolder.this.postMediaContainer != null
                && SocialFeedPostsListViewHolder.this.postMediaContainer.getLayoutParams() instanceof ConstraintLayout.LayoutParams
                && SocialFeedPostsListViewHolder.this.postMediaLayout != null
                && SocialFeedPostsListViewHolder.this.postMediaLayout.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {

            ConstraintLayout.LayoutParams containerLayoutParams =
                    ((ConstraintLayout.LayoutParams) SocialFeedPostsListViewHolder.this.postMediaContainer.getLayoutParams());

            ConstraintLayout.LayoutParams layoutLayoutParams =
                    ((ConstraintLayout.LayoutParams) SocialFeedPostsListViewHolder.this.postMediaLayout.getLayoutParams());


            containerLayoutParams.dimensionRatio = dimensionsRatio;
            SocialFeedPostsListViewHolder.this.postMediaContainer.setLayoutParams(containerLayoutParams);

            layoutLayoutParams.dimensionRatio = dimensionsRatio;
            SocialFeedPostsListViewHolder.this.postMediaLayout.setLayoutParams(layoutLayoutParams);

        }

    }

    //post like button
    private void updatePostLikesButtonImageView() {

        this.postLikesButtonImageView.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this.context, "faw_heart1"))
                .sizeDp(24).colorRes(R.color.light_primary_color));

        if (this.socialFeedPost != null && this.socialFeedPost.isLike()) {

            this.postLikesButtonImageView.setImageDrawable(
                    ((IconicsDrawable) Icons.get().findDrawable(this.context, "faw_heart1"))
                            .sizeDp(24).colorRes(R.color.lava_red));

        }

    }

    //post comments button
    private void updatePostCommentsButtonImageView() {

        this.postCommentsButtonImageView.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this.context, "faw_comment1"))
                        .sizeDp(24).colorRes(R.color.light_primary_color));

        if (this.socialFeedPost != null) {

        }

    }

    //post share button
    private void updatePostShareButtonImageView() {

        this.postShareButtonImageView.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this.context, "faw_share"))
                        .sizeDp(24).colorRes(R.color.light_primary_color));

        if (this.socialFeedPost != null) {

        }

    }

    //post likes text
    private void updatePostLikesTextView() {

        if (this.socialFeedPost != null) {

            this.postLikesTextView.setText(Math.max(0, this.socialFeedPost.getLikesCount()) + " Likes");

        } else {

            this.postLikesTextView.setText("0 Likes");

        }

        this.postLikesTextView.setTextColor(ContextCompat.getColor(this.context, R.color.primary_color_half_opacity));
        this.postLikesTextView.setTypeface(this.postLikesTextView.getTypeface(), Typeface.BOLD);

    }

    //post likes text
    private void updatePostIntermediateTextView() {

        this.postIntermediateTextView.setText(" • ");

        this.postIntermediateTextView.setTextColor(ContextCompat.getColor(this.context, R.color.primary_color_half_opacity));
        this.postIntermediateTextView.setTypeface(this.postIntermediateTextView.getTypeface(), Typeface.BOLD);

    }

    //post comments text
    private void updatePostCommentsTextView() {

        if (this.socialFeedPost != null) {

            this.postCommentsTextView.setText(Math.max(0, this.socialFeedPost.getCommentsCount()) + " Comments");

        } else {

            this.postCommentsTextView.setText("0 Comments");

        }

        this.postCommentsTextView.setTextColor(ContextCompat.getColor(this.context, R.color.primary_color_half_opacity));
        this.postCommentsTextView.setTypeface(this.postLikesTextView.getTypeface(), Typeface.BOLD);

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


    //skins
    public void setScreenSkin(int screenSkin) {
        this.screenSkin = screenSkin;

        this.updatePost();

    }

    @NonNull
    @Override
    public View getPlayerView() {
        return this.postMediaContainer;
    }

    @NonNull
    @Override
    public PlaybackInfo getCurrentPlaybackInfo() {
        SparseArray<PlaybackInfo> actualInfos = this.postMediaContainer.getLatestPlaybackInfos();
        SocialFeedPostsListPlaybackInfo resultInfo = new SocialFeedPostsListPlaybackInfo(actualInfos);

        List<ToroPlayer> activePlayers = this.postMediaContainer.filterBy(Container.Filter.PLAYING);
        if (activePlayers.size() >= 1) {
            resultInfo.setResumeWindow(activePlayers.get(0).getPlayerOrder());
        }

        return resultInfo;
    }

    protected boolean isRtl() {
        return TextUtilsCompat.getLayoutDirectionFromLocale(
                this.context.getResources().getConfiguration().locale) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

    @Override
    public void initialize(@NonNull Container container, @Nullable PlaybackInfo playbackInfo) {
        this.initPosition = -1;
        if (playbackInfo instanceof SocialFeedPostsListPlaybackInfo) {
            //noinspection unchecked
            SparseArray<PlaybackInfo> cache = ((SocialFeedPostsListPlaybackInfo) playbackInfo).getActualInfo();
            if (cache != null && cache.size() > 0) {
                for (int i = 0; i < cache.size(); i++) {
                    int key = cache.keyAt(i);
                    this.postMediaContainer.savePlaybackInfo(key, cache.get(key));
                }
            }
            this.initPosition = playbackInfo.getResumeWindow();
        }
        this.postMediaContainer.setPlayerSelector(PlayerSelector.NONE);
    }

    @Override public void play() {
        if (this.initPosition >= 0) this.postMediaContainer.scrollToPosition(this.initPosition);
        this.initPosition = -1;
        this.postMediaContainer.setPlayerSelector(PlayerSelector.DEFAULT);
    }

    @Override public void pause() {
        this.postMediaContainer.setPlayerSelector(PlayerSelector.NONE);
    }

    @Override public boolean isPlaying() {
        return this.postMediaContainer.filterBy(Container.Filter.PLAYING).size() > 0;
    }

    @Override public void release() {
        // release here
        List<ToroPlayer> managed = this.postMediaContainer.filterBy(Container.Filter.MANAGING);
        for (ToroPlayer player : managed) {
            if (player.isPlaying()) {
                this.postMediaContainer.savePlaybackInfo(player.getPlayerOrder(), player.getCurrentPlaybackInfo());
                player.pause();
            }
            player.release();
        }
        this.postMediaContainer.setPlayerSelector(PlayerSelector.NONE);
    }

    @Override public boolean wantsToPlay() {
        return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.85;
    }

    @Override public int getPlayerOrder() {
        return getAdapterPosition();
    }

}