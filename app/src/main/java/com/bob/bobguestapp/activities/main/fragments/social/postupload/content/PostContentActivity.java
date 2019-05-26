package com.bob.bobguestapp.activities.main.fragments.social.postupload.content;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.android.volley.Request;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.adapter.SocialFeedPostsListMediaAdapter;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.cachemanager.SocialFeedPostsListCacheManager;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.container.OnMediaLoadListener;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.container.SocialFeedPostsListMediaContainer;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.layoutmanager.SocialFeedPostsListMediaLinearLayoutManager;
import com.bob.bobguestapp.tools.database.objects.Post;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.toolsmodule.http.HttpUtilsManager;
import com.bob.toolsmodule.http.requests.JsonServerRequest;
import com.bob.toolsmodule.http.requests.MultipartServerRequest;
import com.bob.toolsmodule.http.serverbeans.ApplicativeResponse;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.http.serverbeans.Hotel;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.toolsmodule.parsing.GsonParser;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.video.exoplayer.ExoPlayerFullScreenListener;
import com.bob.uimodule.views.loadingcontainer.ManagementActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import im.ene.toro.PlayerSelector;
import im.ene.toro.ToroPlayer;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.PressablePlayerSelector;

import static android.view.View.VISIBLE;
import static androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE;
import static com.android.volley.Request.Method.POST;
import static com.bob.uimodule.views.loadingcontainer.ManagementViewContainer.LOADING;

public class PostContentActivity extends ManagementActivity implements OnMediaLoadListener {

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

    //urls
    private static final String UPLOAD_A_POST = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/post";
    private static final String EDIT_A_POST = BOB_SERVER_MOBILE_SERVICES_URL + "/feed/guest/post";
    private static final String UPLOAD_FILE_FOR_GUEST = BOB_SERVER_FILES_SERVICES_URL + "/guest/post/upload";

    //post content page arguments
    public static final String ARG_EXTRA_POST_CONTENT_POST_JSON = "bob:mobile:postcontentpage:postjson";  // String

    //uploded post
    public static final int RESPONSE_UPLOADED_POST = 100; // Integer

    //screen states
    private static final int MAIN_VIEW = 3;

    //main view
    private ConstraintLayout mainViewLayout;

    //app bar
    private AppBarLayout appBarLayout;

    //navigation
    private RelativeLayout appBarNavigationLayout;
    private ImageButton appBarNavigationExitButton;
    private View.OnTouchListener onTouchAppBarNavigationExitButtonListener;
    private ImageButton appBarNavigationNextButton;
    private View.OnTouchListener onTouchAppBarNavigationNextButtonListener;

    //preview post layout
    private ConstraintLayout previewPostLayout;

    //post header layout
    private RelativeLayout previewPostHeaderLayout;
    private ImageView previewPostEditorProfilePictureImageView;
    private TextView previewPostEditorNameTextView;

    //post text layout
    private RelativeLayout previewPostTextLayout;
    private TextInputEditText previewPostostTextTextView;

    //preview
    //media container
    private ConstraintLayout previewPostMediaLayout;
    private SocialFeedPostsListMediaContainer previewPostMediaContainer;
    private SocialFeedPostsListMediaAdapter previewPostMediaContainerPlayerAdapter;
    private SocialFeedPostsListMediaLinearLayoutManager previewPostMediaContainerLinearLayoutManager;
    private PressablePlayerSelector previewPostMediaContainerPlayerSelector;
    private SnapHelper previewPostMediaContainerSnapHelper;

    //exo full screen listener
    private ExoPlayerFullScreenListener exoPlayerFullScreenListener;

    //media list
    private List<MediaItem> checkedMediaItems;
    private HashMap<String, MediaItem> uploadedMediaItems;

    //post
    private Post post;

    //guest
    private Guest guest;

    //animations
    private Animation touchDownAnimation;
    private Animation touchUpAnimation;

    public PostContentActivity() {

        //theme
        this.appTheme = ThemeUtilsManager.DEFAULT_THEME;
        this.screenSkin = ThemeUtilsManager.PRIMARY_COLOR_SKIN;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.managementViewContainer.setScreenState(MAIN_VIEW);

    }

    @Override
    protected View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {

        //background

        //view
        View view = inflater.inflate(R.layout.activity_post_content, container, false);

        //layouts
        this.initMainViewLayout(view);

        this.initUploadedMediaItems();

        //status bar color
        this.initStatusBarColor();

        //navigation bar color
        this.initNavigationBarColor();

        //animation
        this.initAnimations();

        //update preview post data
        this.updatePreviewPost();

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

    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {

        this.fillPostPageParameters(savedInstanceState);

    }

    //arguments
    protected void fillPostPageParameters(Bundle savedInstanceState) {

        Bundle bundle = savedInstanceState;

        if (bundle == null) bundle = getIntent().getExtras();

        if (bundle != null) {

            if (getIntent().hasExtra(ARG_EXTRA_POST_CONTENT_POST_JSON)) {

                String postJson = bundle.getString(ARG_EXTRA_POST_CONTENT_POST_JSON);

                try {

                    this.post = GsonParser.getParser().create().fromJson(postJson, Post.class);

                    if (this.post != null && this.post.getMediaItems() != null) {

                        this.checkedMediaItems = Arrays.asList(this.post.getMediaItems());

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }

            } else if (getIntent().hasExtra("media_items")) {

                this.checkedMediaItems = getIntent().getParcelableArrayListExtra("media_items");

            } else {

                this.checkedMediaItems = new ArrayList<MediaItem>();

            }

        }

        this.guest = new Guest();

        this.guest.setName("Deadpool");

        this.guest.setImageUrl("https://avatarfiles.alphacoders.com/129/thumb-129487.jpg");

    }

    //main view
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.mainViewLayout.setVisibility(View.INVISIBLE);

        switch (screenState) {

            case MAIN_VIEW:
                this.mainViewLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //main view
    private void initMainViewLayout(View view) {

        //main view
        this.initMainViewBackground(view);

        this.initAppBarLayout(view);

        this.initPostPreviewLayout(view);

        this.checkPostText(this.previewPostostTextTextView.getText());

    }

    private void initMainViewBackground(View view) {

        //main view background
        this.mainViewLayout = (ConstraintLayout) view.findViewById(R.id.post_content_activity_background);

        //main view background animation
        this.initMainViewBackgroundDrawable();

        this.initAppBarLayout(view);


    }

    private void initAppBarLayout(View view) {

        this.appBarLayout = (AppBarLayout) view.findViewById(R.id.post_content_activity_app_bar_layout);

        this.appBarLayout.setBackgroundColor(
                ContextCompat.getColor(this, R.color.primary_color)
        );

        this.initAppBarNavigationLayout(view);

    }

    private void initAppBarNavigationLayout(View view) {

        this.appBarNavigationLayout = (RelativeLayout) view.findViewById(R.id.post_content_activity_app_bar_navigation_layout);

        this.appBarNavigationLayout.setBackgroundColor(
                ContextCompat.getColor(this, R.color.primary_color)
        );

        this.initAppBarNavigationExitButton(view);

        this.initAppBarNavigationNextButton(view);
    }

    private void initAppBarNavigationExitButton(View view) {

        this.appBarNavigationExitButton = (ImageButton) view.findViewById(R.id.post_content_activity_app_bar_exit_button);

        this.initOnTouchAppBarNavigationExitButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchAppBarNavigationExitButtonListener() {

        this.onTouchAppBarNavigationExitButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostContentActivity.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(PostContentActivity.this.touchUpAnimation);

                        PostContentActivity.this.onAppBarNavigationExitButtonClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.appBarNavigationExitButton.setOnTouchListener(this.onTouchAppBarNavigationExitButtonListener);

    }

    private void onAppBarNavigationExitButtonClicked() {

    }

    private void initAppBarNavigationNextButton(View view) {

        this.appBarNavigationNextButton = (ImageButton) view.findViewById(R.id.post_content_activity_app_bar_next_button);

        this.appBarNavigationNextButton.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this, "gmd_send"))
                        .sizeDp(24).colorRes(R.color.light_primary_color).alpha(136)
        );

        if (this.isRtl()) {

            this.appBarNavigationNextButton.setRotationY(180);

        } else {

            this.appBarNavigationNextButton.setRotationX(0);

        }

        this.initOnTouchAppBarNavigationNextButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchAppBarNavigationNextButtonListener() {

        this.onTouchAppBarNavigationNextButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostContentActivity.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(PostContentActivity.this.touchUpAnimation);

                        PostContentActivity.this.onAppBarNavigationNextButtonClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.appBarNavigationNextButton.setOnTouchListener(this.onTouchAppBarNavigationNextButtonListener);

    }

    private void onAppBarNavigationNextButtonClicked() {

        if (this.previewPostMediaContainer != null
                && this.previewPostMediaContainer.getChildCount() > 0) {

            for (int x = this.previewPostMediaContainer.getChildCount(), i = 0; i < x; ++i) {

                RecyclerView.ViewHolder holder = this.previewPostMediaContainer.getChildViewHolder(
                        this.previewPostMediaContainer.getChildAt(i));

                if (holder instanceof ToroPlayer) {

                    ((ToroPlayer) holder).pause();

                }
            }

        }


        if (this.post != null) {

            this.makeEditPostRequest();

        } else {

            if (this.checkedMediaItems != null && this.checkedMediaItems.size() > 0) {

                this.uploadedMediaItems.clear();

                this.makeUploadMediaItemRequest(this.getNextMediaItemToUpload());

            } else {

                this.makeUploadPostRequest();

            }

        }

    }

    private MediaItem getNextMediaItemToUpload() {

        for (MediaItem mediaItem : this.checkedMediaItems) {

            if (mediaItem.getUrl() != null && !this.uploadedMediaItems.containsKey(mediaItem.getUrl())) {

                return mediaItem;

            }

        }

        return null;

    }

    private void initMainViewBackgroundDrawable() {

//        GradientDrawable shapeDrawable = new GradientDrawable();
//        shapeDrawable.setColors(new int[]{
//                ThemeUtilsManager.get(this).getColor(ThemeUtilsManager.DEFAULT_ACTIVITY_BACKGROUND_COLOR_PRIMARY, this.screenSkin),
//                ThemeUtilsManager.get(this).getColor(ThemeUtilsManager.DEFAULT_ACTIVITY_BACKGROUND_COLOR_SECONDARY, this.screenSkin)
//        });
//        shapeDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
//        shapeDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
//        shapeDrawable.setCornerRadius(0f);
//        this.managementViewContainer.getBackgroundLayout().setBackground(shapeDrawable);

        this.managementViewContainer.getBackgroundLayout().setBackgroundColor(
                ContextCompat.getColor(this, R.color.light_primary_color)
        );

    }

    private void initPostPreviewLayout(View view) {

        this.previewPostLayout = (ConstraintLayout) findViewById(R.id.post_content_activity_post_preview_layout);

        this.initPreviewPostHeaderLayout(view);

        this.initPreviewPostTextLayout(view);

        this.initPreviewPostMediaLayout(view);

    }

    //post header
    private void initPreviewPostHeaderLayout(View view) {

        this.previewPostHeaderLayout = (RelativeLayout) view.findViewById(R.id.post_content_activity_post_header_layout);

        this.initPreviewPostEditorProfilePictureImageView(view);

        this.initPreviewPostEditorNameTextView(view);

    }

    private void initPreviewPostEditorProfilePictureImageView(View view) {

        this.previewPostEditorProfilePictureImageView = (ImageView) view.findViewById(R.id.post_content_activity_editor_profile_picture_view);

    }

    private void initPreviewPostEditorNameTextView(View view) {

        this.previewPostEditorNameTextView = (TextView) view.findViewById(R.id.post_content_activity_editor_name_view);

    }

    //post text
    private void initPreviewPostTextLayout(View view) {

        this.previewPostTextLayout = (RelativeLayout) view.findViewById(R.id.post_content_activity_text_layout);

        this.initPreviewPostTextTextView(view);

    }

    private void initPreviewPostTextTextView(View view) {

        this.previewPostostTextTextView = (TextInputEditText) view.findViewById(R.id.post_content_activity_post_text_view);

        this.previewPostostTextTextView.setBackground(null);

        this.previewPostostTextTextView.setTextColor(Color.BLACK);

        this.previewPostostTextTextView.setHint("על מה אתה חושב?");

        this.previewPostostTextTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                PostContentActivity.this.checkPostText(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void checkPostText(CharSequence postText) {

        if (isPostTextValid(postText) || (this.checkedMediaItems != null && this.checkedMediaItems.size() > 0)) {

            this.onValidPostText();

        } else {

            this.onInvalidPostText();

        }

    }

    private boolean isPostTextValid(CharSequence postText) {

        if (postText != null && !postText.toString().trim().equals("") && postText.toString().trim().length() > 0) {

            return true;
        }

        return false;

    }

    @SuppressLint("ClickableViewAccessibility")
    private void onValidPostText() {

        this.appBarNavigationNextButton.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this, "gmd_send"))
                        .sizeDp(24).colorRes(R.color.light_primary_color)
        );

        if (this.isRtl()) {

            this.appBarNavigationNextButton.setRotationY(180);

        } else {

            this.appBarNavigationNextButton.setRotationX(0);

        }

        this.appBarNavigationNextButton.setEnabled(true);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void onInvalidPostText() {

        this.appBarNavigationNextButton.setImageDrawable(
                ((IconicsDrawable) Icons.get().findDrawable(this, "gmd_send"))
                        .sizeDp(24).colorRes(R.color.light_primary_color).alpha(136)
        );

        if (this.isRtl()) {

            this.appBarNavigationNextButton.setRotationY(180);

        } else {

            this.appBarNavigationNextButton.setRotationX(0);

        }

        this.appBarNavigationNextButton.setEnabled(false);

    }

    //post media
    private void initPreviewPostMediaLayout(View view) {

        this.previewPostMediaLayout = (ConstraintLayout) view.findViewById(R.id.post_content_activity_post_preview_media_layout);

        this.initPreviewPostMediaContainer(view);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initPreviewPostMediaContainer(View view) {

        this.previewPostMediaContainer = (SocialFeedPostsListMediaContainer) view.findViewById(R.id.post_content_activity_post_preview_media_container);

        this.previewPostMediaContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {

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

                            PostContentActivity.this.onGetDimensionsRatio(imageViewLayoutParams.dimensionRatio);

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
                                PostContentActivity.this.onGetDimensionsRatio((String) playerView.getTag());
                            }

                        }

                    } else {

                        PostContentActivity.this.onGetDimensionsRatio("H,1:1");

                    }


                }


            }

        });

        //layout manager
        this.initPreviewPostMediaContainerLinearLayoutManager();

        //player selector
        this.initPreviewPostMediaContainerPlayerSelector();

        //snap helper
        this.initPreviewPostMediaContainerSnapHelper();

        //exo full screen listener
        this.initExoPlayerFullScreenListener();

        //adapter
        this.initPreviewPostMediaContainerAdapter();

    }

    private void initPreviewPostMediaContainerLinearLayoutManager() {

        this.previewPostMediaContainerLinearLayoutManager = new SocialFeedPostsListMediaLinearLayoutManager(this);
        this.previewPostMediaContainer.setLayoutManager(this.previewPostMediaContainerLinearLayoutManager);

    }

    private void initPreviewPostMediaContainerPlayerSelector() {

        this.previewPostMediaContainerPlayerSelector = new PressablePlayerSelector(this.previewPostMediaContainer);
        this.previewPostMediaContainer.setPlayerSelector(this.previewPostMediaContainerPlayerSelector);

    }

    private void initPreviewPostMediaContainerSnapHelper() {

        this.previewPostMediaContainerSnapHelper = new PagerSnapHelper();
        this.previewPostMediaContainerSnapHelper.attachToRecyclerView(this.previewPostMediaContainer);

    }

    private void initExoPlayerFullScreenListener() {

        this.exoPlayerFullScreenListener = new ExoPlayerFullScreenListener() {

            int orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

            @Override
            public void startFullScreen() {

                PostContentActivity.this.previewPostMediaContainer.setPlayerSelector(PlayerSelector.NONE);

                this.orientation = PostContentActivity.this.getRequestedOrientation();

                PostContentActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);

            }

            @Override
            public void exitFullScreen(Uri mediaUri, PlaybackInfo playbackInfo, int order, Point videoSize) {

                PostContentActivity.this.previewPostMediaContainer.savePlaybackInfo(order, playbackInfo);

                PostContentActivity.this.previewPostMediaContainer.setPlayerSelector(
                        PostContentActivity.this.previewPostMediaContainerPlayerSelector
                );

                PostContentActivity.this.setRequestedOrientation(this.orientation);

            }
        };

    }

    private void initPreviewPostMediaContainerAdapter() {

        this.previewPostMediaContainerPlayerAdapter = new SocialFeedPostsListMediaAdapter(this,
                this.getSupportFragmentManager(),
                this.exoPlayerFullScreenListener,
                this,
                this.previewPostMediaContainerPlayerSelector,
                new ArrayList<MediaItem>());

        this.previewPostMediaContainer.setAdapter(this.previewPostMediaContainerPlayerAdapter);

    }

    @Override
    public void onGetDimensionsRatio(String dimensionsRatio) {

        if (PostContentActivity.this.previewPostMediaContainer != null
                && PostContentActivity.this.previewPostMediaContainer.getLayoutParams() instanceof ConstraintLayout.LayoutParams
                && PostContentActivity.this.previewPostMediaLayout != null
                && PostContentActivity.this.previewPostMediaLayout.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {

            ConstraintLayout.LayoutParams containerLayoutParams =
                    ((ConstraintLayout.LayoutParams) PostContentActivity.this.previewPostMediaContainer.getLayoutParams());

            ConstraintLayout.LayoutParams layoutLayoutParams =
                    ((ConstraintLayout.LayoutParams) PostContentActivity.this.previewPostMediaLayout.getLayoutParams());


            containerLayoutParams.dimensionRatio = dimensionsRatio;
            PostContentActivity.this.previewPostMediaContainer.setLayoutParams(containerLayoutParams);

            layoutLayoutParams.dimensionRatio = dimensionsRatio;
            PostContentActivity.this.previewPostMediaLayout.setLayoutParams(layoutLayoutParams);

        }

    }

    //uploaded media items
    private void initUploadedMediaItems() {

        this.uploadedMediaItems = new HashMap<String, MediaItem>();

    }

    //update preview
    private void updatePreviewPost() {

        //update media items
        this.updatePreviewPostMedia();

        //update post text
        this.updatePreviewPostText();

        //update guest info
        this.updateEditorInfo();

    }
    
    //update media items
    private void updatePreviewPostMedia() {

        if (this.checkedMediaItems != null
                && this.checkedMediaItems.size() > 0) {

            if (this.previewPostMediaContainer.getAdapter() instanceof SocialFeedPostsListMediaAdapter) {

                SocialFeedPostsListMediaAdapter socialFeedPostsListMediaAdapter =
                        ((SocialFeedPostsListMediaAdapter)this.previewPostMediaContainer.getAdapter());

                socialFeedPostsListMediaAdapter.setMediaItems(
                        new ArrayList<MediaItem>(this.checkedMediaItems));

                if (this.previewPostMediaContainer.getCacheManager() == null) {

                    this.previewPostMediaContainer.setCacheManager(new SocialFeedPostsListCacheManager(
                            new ArrayList<MediaItem>(this.checkedMediaItems)
                            ));

                }

                socialFeedPostsListMediaAdapter.notifyDataSetChanged();

            }

        }

        if (this.checkedMediaItems == null || this.checkedMediaItems.size() == 0) {

            this.previewPostMediaContainer.setVisibility(View.GONE);

        }


    }

    //update media items
    private void updatePreviewPostText() {

        if (this.post != null
                && this.post.getText() != null
                && this.post.getText().trim().length() > 0) {

            this.previewPostostTextTextView.setText(this.post.getText());

        }

    }

    //update guest info
    private void updateEditorInfo() {

        this.updateEditorProfilePictureImageView();

        this.updateEditorNameTextView();

    }

    //post editor profile picture
    private void updateEditorProfilePictureImageView() {

        if (this.guest != null && this.guest.getName() != null) {

            Glide.with(this)
                    .asBitmap()
                    .load(this.guest.getImageUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.circleCropTransform())
                    .into(this.previewPostEditorProfilePictureImageView);

        } else {

            //put an placeholder default image
//            Glide.with(this)
//                    .asBitmap()
//                    .load(placeholderimage)
//                    .into(this.previewPostEditorProfilePictureImageView);

        }


    }

    //post editor name
    private void updateEditorNameTextView() {

        if (this.guest != null && this.guest.getName() != null) {

            this.previewPostEditorNameTextView.setText(this.guest.getName());

        } else {

            this.previewPostEditorNameTextView.setText("No User!");

        }

        this.previewPostEditorNameTextView.setTypeface(this.previewPostEditorNameTextView.getTypeface(), Typeface.BOLD);

        this.previewPostEditorNameTextView.setTextColor(
                Color.BLACK
        );

    }

    //http requests
    private void makeUploadPostRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

            }

            @Override
            protected boolean requestCondition() {

                if (PostContentActivity.this.post == null
                        && (PostContentActivity.this.checkedMediaItems != null
                        || (PostContentActivity.this.previewPostostTextTextView != null
                            && PostContentActivity.this.isPostTextValid(PostContentActivity.this.previewPostostTextTextView.getText())))) {

                    return true;

                }

                return false;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {

                try {

                    Post post = new Post();

                    if (PostContentActivity.this.previewPostostTextTextView.getText() != null) {

                        post.setText(
                                PostContentActivity.this.previewPostostTextTextView.getText().toString()
                        );

                    }

                    if (PostContentActivity.this.checkedMediaItems != null
                        && PostContentActivity.this.checkedMediaItems.size() > 0) {

                        post.setMediaItems(
                                PostContentActivity.this.uploadedMediaItems.values().toArray(new MediaItem[PostContentActivity.this.uploadedMediaItems.values().size()])
                        );

                    } else {

                        post.setMediaItems(
                                new MediaItem[0]
                        );

                    }

                    Guest guest = new Guest();
                    guest.setId(1);

                    post.setGuest(guest);

                    Hotel hotel = new Hotel();
                    hotel.setId(1);

                    post.setHotel(hotel);

                    JsonElement jsonPost = MyGsonParser.getParser().create().toJsonTree(post, Post.class);


                    JsonObject jsonRequest = new JsonObject();
                    jsonRequest.add("post", jsonPost);

                    JsonObject jsonLoginRequest = new JsonObject();
                    jsonLoginRequest.add("request", jsonRequest);

                    return new JSONObject( new Gson().toJson(jsonLoginRequest));

                } catch (JSONException e) {

                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected String getRequestUrl() {

                return UPLOAD_A_POST;

            }

            @Override
            protected int getMethod() {
                return POST;
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

                    if (response.getJSONObject("response").has("posts")) {

                        Post[] postPosts = new Post[0];
                        if (response.getJSONObject("response").optJSONArray("posts") != null) {
                            String postPostsString = response.getJSONObject("response").getJSONArray("posts").toString();
                            postPosts = customGson.fromJson(postPostsString, Post[].class);
                        } else if (response.getJSONObject("response").optJSONObject("posts") != null) {
                            String postPostsString = response.getJSONObject("response").getJSONObject("posts").toString();
                            postPosts = new Post[] {
                                    customGson.fromJson(postPostsString, Post.class)
                            };
                        }

                        if (postPosts != null && postPosts.length > 0) {

                            postPosts[0].setTimeStamp(
                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(System.currentTimeMillis())

                            );

                            String postJson = GsonParser.getParser().create().toJson(postPosts[0], Post.class);

                            Intent returnIntent = new Intent();

                            returnIntent.putExtra(PostContentActivity.ARG_EXTRA_POST_CONTENT_POST_JSON, postJson);

                            PostContentActivity.this.setResult(RESULT_OK,returnIntent);

                            PostContentActivity.this.finishActivity(RESPONSE_UPLOADED_POST);

                            PostContentActivity.this.finish();

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

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void makeEditPostRequest() {

        JsonServerRequest jsonServerRequest = new JsonServerRequest() {

            @Override
            protected void preRequest() {

            }

            @Override
            protected boolean requestCondition() {

                if (PostContentActivity.this.post != null
                        && (PostContentActivity.this.checkedMediaItems != null
                        || (PostContentActivity.this.previewPostostTextTextView != null
                            && PostContentActivity.this.isPostTextValid(PostContentActivity.this.previewPostostTextTextView.getText())))) {

                    return true;

                }

                return false;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
            }

            @Override
            protected JSONObject getJsonObject() {

                try {

                    PostContentActivity.this.post.setText(
                            PostContentActivity.this.previewPostostTextTextView.getText().toString()
                    );

                    Guest guest = new Guest();
                    guest.setId(1);
                    PostContentActivity.this.post.setGuest(guest);

                    Hotel hotel = new Hotel();
                    hotel.setId(1);
                    PostContentActivity.this.post.setHotel(hotel);


                    JsonElement jsonPost = MyGsonParser.getParser().create().toJsonTree(PostContentActivity.this.post, Post.class);


                    JsonObject jsonRequest = new JsonObject();
                    jsonRequest.add("post", jsonPost);

                    JsonObject jsonLoginRequest = new JsonObject();
                    jsonLoginRequest.add("request", jsonRequest);

                    return new JSONObject( new Gson().toJson(jsonLoginRequest));

                } catch (JSONException e) {

                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            protected String getRequestUrl() {

                return EDIT_A_POST;

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

                try {
                    Gson customGson = MyGsonParser.getParser().create();

                    if (response.getJSONObject("response").has("posts")) {

                        Post[] postPosts = new Post[0];
                        if (response.getJSONObject("response").optJSONArray("posts") != null) {
                            String postPostsString = response.getJSONObject("response").getJSONArray("posts").toString();
                            postPosts = customGson.fromJson(postPostsString, Post[].class);
                        } else if (response.getJSONObject("response").optJSONObject("posts") != null) {
                            String postPostsString = response.getJSONObject("response").getJSONObject("posts").toString();
                            postPosts = new Post[] {
                                    customGson.fromJson(postPostsString, Post.class)
                            };
                        }

                        if (postPosts != null && postPosts.length > 0) {

                            String postJson = GsonParser.getParser().create().toJson(postPosts[0], Post.class);

                            Intent returnIntent = new Intent();

                            returnIntent.putExtra(PostContentActivity.ARG_EXTRA_POST_CONTENT_POST_JSON, postJson);

                            PostContentActivity.this.setResult(RESULT_OK,returnIntent);

                            PostContentActivity.this.finishActivity(RESPONSE_UPLOADED_POST);

                            PostContentActivity.this.finish();

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

            }

        };

        jsonServerRequest.makeRequest();

    }

    private void makeUploadMediaItemRequest(MediaItem mediaItem) {

        MultipartServerRequest multipartServerRequest = new MultipartServerRequest() {

            @Override
            protected String getRealFileUrl() {
                return mediaItem.getUrl();
            }

            @Override
            protected int getMethod() {
                return POST;
            }

            @Override
            protected void preRequest() {
                PostContentActivity.this.managementViewContainer.setScreenState(LOADING);
            }

            @Override
            protected boolean requestCondition() {

                long hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("hotelId", -1);
                int guestId = BOBGuestApplication.get().getSecureSharedPreferences().getInt("guestId", -1);

                if (hotelId == -1 || guestId == -1) {
                    return false;
                }

                try {

                    File file = new File(mediaItem.getUrl());
                    if (mediaItem.getUrl() != null && file.exists()) {
                        return true;
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                    return false;

                }

                return false;

            }

            @Override
            protected void setRequestHeaders(HashMap<String, String> headers) {
                headers.put("Content-Type", "multipart/form-data");
                headers.put("Accept", "application/json");
            }


            @Override
            protected String getRequestUrl() {

                String requestUrl = UPLOAD_FILE_FOR_GUEST + "?";

                long hotelId = BOBGuestApplication.get().getSecureSharedPreferences().getLong("hotelId", -1);
                requestUrl += "hotel=" + hotelId;

                int guestId = BOBGuestApplication.get().getSecureSharedPreferences().getInt("guestId", -1);
                requestUrl += "&guest=" + guestId;

                requestUrl += "&file_name=" + HttpUtilsManager.get().getFileNameFromPath(mediaItem.getUrl());

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

                    String mediaItemUrl = response.getJSONObject("response").getString("url");

                    MediaItem newMediaItem = new MediaItem(mediaItemUrl, mediaItem.getType(), mediaItem.isChecked());
                    newMediaItem.setProperties(mediaItem.getProperties());

                    PostContentActivity.this.uploadedMediaItems.put(mediaItem.getUrl(), newMediaItem);

                    MediaItem nextMediaItem = PostContentActivity.this.getNextMediaItemToUpload();

                    if (nextMediaItem != null) {

                        PostContentActivity.this.makeUploadMediaItemRequest(nextMediaItem);

                    } else {

                        PostContentActivity.this.makeUploadPostRequest();

                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                    this.onDefaultError("error in parsing response");

                }

            }

            @Override
            protected void onDefaultError(String message) {

                if (message != null) {

                    Toast toast=Toast.makeText(getApplicationContext(),message + "\n" + HttpUtilsManager.get().getFileNameFromPath(mediaItem.getUrl()), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    //some error message

                } else {

                    Toast toast=Toast.makeText(getApplicationContext(),"Error Uploading\n" + HttpUtilsManager.get().getFileNameFromPath(mediaItem.getUrl()), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    //some default error message

                }

                PostContentActivity.this.managementViewContainer.setScreenState(MAIN_VIEW);

            }

        };


        multipartServerRequest.makeRequest();

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

    @Override
    public void onBackPressed() {

        if (this.managementViewContainer.getScreenState() != MAIN_VIEW) {

            this.managementViewContainer.setScreenState(MAIN_VIEW);

        } else {

            super.onBackPressed();

        }

    }

    protected boolean isRtl() {
        return TextUtilsCompat.getLayoutDirectionFromLocale(
                this.getResources().getConfiguration().locale) == ViewCompat.LAYOUT_DIRECTION_RTL;
    }

}