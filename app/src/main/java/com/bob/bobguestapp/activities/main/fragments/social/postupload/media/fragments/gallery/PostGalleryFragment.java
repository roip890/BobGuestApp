package com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.connector.PostMediaConnector;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerybucketspinner.MediaGalleryBucketSpinnerAdapter;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerygrid.MediaGalleryAdapter;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerygrid.MediaGalleryRecyclerView;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerygrid.OverScrollBounceBehavior;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.adapter.MediaGalleryPreviewAdapter;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.container.MediaGalleryPreviewContainer;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.layoutmanager.MediaGalleryPreviewLinearLayoutManager;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.listeners.MediaGalleryItemEventsListener;
import com.bob.toolsmodule.enums.MediaItemType;
import com.bob.toolsmodule.mediagallery.GetBucketsListener;
import com.bob.toolsmodule.mediagallery.GetMediaGalleryBucketsTask;
import com.bob.toolsmodule.mediagallery.GetMediaGalleryItemsOfBucketsTask;
import com.bob.toolsmodule.mediagallery.GetMediaItemsListener;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.toolsmodule.objects.MediaItemBucket;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.drawable.DrawableUtilsManager;
import com.bob.uimodule.image.bitmap.BitmapUtilsManager;
import com.bob.uimodule.image.cropper.CropperView;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import im.ene.toro.widget.PressablePlayerSelector;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class PostGalleryFragment extends ManagementFragment implements MediaGalleryItemEventsListener {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //create wish url
    private static final String CREATE_REQUEST_URL = BOB_SERVER_MOBILE_SERVICES_URL + "/wishes/create";

    //main view screen states
    public static final int GALLERY = 10;

    //post media commands
    private PostMediaConnector postMediaConnector;

    //gallery layout
    private CoordinatorLayout postGalleryLayout;

    //app bar
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    //navigation
    private RelativeLayout appBarNavigationLayout;
    private ImageButton appBarNavigationExitButton;
    private View.OnTouchListener onTouchAppBarNavigationExitButtonListener;
    private ImageButton appBarNavigationNextButton;
    private View.OnTouchListener onTouchAppBarNavigationNextButtonListener;
    private AppCompatSpinner appBarNavigationGalleryBucketsSpinner;
    private MediaGalleryBucketSpinnerAdapter appBarNavigationGalleryBucketsSpinnerAdapter;

    //preview
    private ConstraintLayout appBarPreviewLayout;
    private MediaGalleryPreviewContainer appBarPreviewContainer;
    private SnapHelper appBarPreviewSnapHelper;
    private MediaGalleryPreviewLinearLayoutManager appBarPreviewLinearLayoutManager;
    private PressablePlayerSelector appBarPreviewPlayerSelector;
    private MediaGalleryPreviewAdapter appBarPreviewPlayerAdapter;
    private ImageButton appBarPreviewPrevButton;
    private ImageButton appBarPreviewNextButton;
    private View.OnTouchListener onTouchAppBarPreviewPrevButtonListener;
    private View.OnTouchListener onTouchAppBarPreviewNextButtonListener;

    //media gallery
    private MediaGalleryRecyclerView mediaGalleryImagesRecyclerView;
    private MediaGalleryAdapter mediaGalleryAdapter;

    //media list
    private List<MediaItem> checkedMediaItems;
    private HashMap<String, Bitmap> cachedCroppedBitmap;
    private HashMap<String, String> cachedVideoAspectRatio;

    //animations
    private Animation touchDownAnimation;
    private Animation touchUpAnimation;

    public PostGalleryFragment() {

        //theme
        this.appTheme = ThemeUtilsManager.DEFAULT_THEME;
        this.screenSkin = ThemeUtilsManager.PRIMARY_COLOR_SKIN;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        this.managementViewContainer.setScreenState(GALLERY);

        return view;

    }

    @Override
    public View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //init management skin
        this.managementViewContainer.setScreenSkin(this.screenSkin);

        //view
        View view = inflater.inflate(R.layout.fragment_post_gallery, container, false);

        //init views
        this.initViews(view);

        this.initAnimations();

        return view;

    }

    //screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.postGalleryLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case GALLERY:
                this.postGalleryLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.appBarPreviewPlayerAdapter != null && this.appBarPreviewContainer != null) {
            if (isVisibleToUser) {
                this.appBarPreviewContainer.onWindowVisibilityChanged(View.VISIBLE);

            } else {
                this.appBarPreviewContainer.onWindowVisibilityChanged(View.GONE);
            }
        }
    }

    //main commands
    public void setPostMediaConnector(PostMediaConnector postMediaConnector) {
        this.postMediaConnector = postMediaConnector;
    }

    //post gallery
    private void initViews(View view) {

        this.postGalleryLayout = (CoordinatorLayout) view.findViewById(R.id.post_gallery_fragment_background);

        this.initAppBarLayout(view);

        this.initMediaGalleryImagesRecyclerView(view);

    }

    //app bar
    private void initAppBarLayout(View view) {

        this.appBarLayout = (AppBarLayout) view.findViewById(R.id.post_gallery_fragment_app_bar_layout);

        if (this.appBarLayout.getLayoutParams() != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) this.appBarLayout.getLayoutParams();
            AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
            appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return false;
                }
            });
            layoutParams.setBehavior(appBarLayoutBehaviour);
        }

        this.initCollapsingToolbarLayout(view);

    }

    private void initCollapsingToolbarLayout(View view) {

        this.collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.post_gallery_fragment_app_bar_collapsing_layout);

        this.collapsingToolbarLayout.setBackgroundColor(
                ContextCompat.getColor(this.getContext(), R.color.primary_color)
        );

        ViewCompat.setElevation(
                this.collapsingToolbarLayout,
                UIUtilsManager.get().convertDpToPixels(this.getContext(), 10)
        );

        this.initAppBarNavigationLayout(view);

        this.initAppBarPreviewLayout(view);

    }

    private void initAppBarNavigationLayout(View view) {

        this.appBarNavigationLayout = (RelativeLayout) view.findViewById(R.id.post_gallery_fragment_app_bar_navigation_layout);

        this.appBarNavigationLayout.setBackgroundColor(
                ContextCompat.getColor(this.getContext(), R.color.primary_color)
        );

        this.initAppBarNavigationExitButton(view);

        this.initAppBarNavigationNextButton(view);

        this.initAppBarNavigationGalleryBucketsSpinner(view);
    }

    private void initAppBarNavigationExitButton(View view) {

        this.appBarNavigationExitButton = (ImageButton) view.findViewById(R.id.post_gallery_fragment_app_bar_exit_button);

        this.initOnTouchAppBarNavigationExitButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchAppBarNavigationExitButtonListener() {

        this.onTouchAppBarNavigationExitButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostGalleryFragment.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(PostGalleryFragment.this.touchUpAnimation);

                        PostGalleryFragment.this.onAppBarNavigationExitButtonClicked();

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

        this.appBarNavigationNextButton = (ImageButton) view.findViewById(R.id.post_gallery_fragment_app_bar_next_button);

        this.initOnTouchAppBarNavigationNextButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchAppBarNavigationNextButtonListener() {

        this.onTouchAppBarNavigationNextButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostGalleryFragment.this.touchDownAnimation);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(PostGalleryFragment.this.touchUpAnimation);

                        PostGalleryFragment.this.onAppBarNavigationNextButtonClicked();

                        break;
                    }
                }
                return true;

            }
        };

        this.appBarNavigationNextButton.setOnTouchListener(this.onTouchAppBarNavigationNextButtonListener);

    }

    private void onAppBarNavigationNextButtonClicked() {

        for (int i = 0; i <= PostGalleryFragment.this.appBarPreviewContainer.getChildCount(); i++) {

            View view = PostGalleryFragment.this.appBarPreviewContainer.getChildAt(i);

            if (view != null && view.getTag() instanceof MediaItem && ((MediaItem) view.getTag()).getType() == MediaItemType.IMAGE_TYPE) {

                CropperView cropperView = (CropperView) view.findViewById(R.id.choose_from_gallery_image_preview_view_holder_cropper_view);

                MediaItem mediaItem = (MediaItem) view.getTag();

                Bitmap bitmap = cropperView.getCroppedBitmap().getBitmap();

                if (mediaItem != null && mediaItem.getUrl() != null && bitmap != null) {

                    PostGalleryFragment.this.cachedCroppedBitmap.put(mediaItem.getUrl(), bitmap);

                }

            } else if (view != null && view.getTag() instanceof MediaItem && ((MediaItem) view.getTag()).getType() == MediaItemType.VIDEO_TYPE){

                MediaItem mediaItem = (MediaItem) view.getTag();

                if (mediaItem != null && mediaItem.getProperties() != null && mediaItem.getProperties().containsKey("aspect_ratio")) {

                    PostGalleryFragment.this.cachedVideoAspectRatio.put(mediaItem.getUrl(), mediaItem.getProperties().get("aspect_ratio"));

                }

            }

        }

        ArrayList<MediaItem> mediaItems = new ArrayList<MediaItem>();

        for (MediaItem mediaItem : PostGalleryFragment.this.checkedMediaItems) {

            if (mediaItem.getType() == MediaItemType.IMAGE_TYPE) {

                if (PostGalleryFragment.this.cachedCroppedBitmap.get(mediaItem.getUrl()) != null) {

                    Bitmap croppedBitmap = PostGalleryFragment.this.cachedCroppedBitmap.get(mediaItem.getUrl());

                    try {
                        String croppedImageUrl = Environment.getExternalStorageDirectory()
                                + "/" + Long.toString(System.currentTimeMillis())
                                + ".png";

                        BitmapUtilsManager.writeBitmapToFile(croppedBitmap, new File(croppedImageUrl), 90);

                        mediaItems.add(new MediaItem(croppedImageUrl , mediaItem.getType(), mediaItem.isChecked()));

                    } catch (IOException e) {

                        e.printStackTrace();

                        mediaItems.add(mediaItem);

                    }

                } else {

                    mediaItems.add(mediaItem);

                }

            } else if (mediaItem.getType() == MediaItemType.VIDEO_TYPE) {

                if (PostGalleryFragment.this.cachedVideoAspectRatio.get(mediaItem.getUrl()) != null) {

                    mediaItem.getProperties().put("aspect_ratio", PostGalleryFragment.this.cachedVideoAspectRatio.get(mediaItem.getUrl()));

                }

                mediaItems.add(mediaItem);

            }

        }

        PostGalleryFragment.this.postMediaConnector.continueToContentActivity(
                (ArrayList<MediaItem>) mediaItems
        );

    }

    private void initAppBarNavigationGalleryBucketsSpinner(View view) {

        this.appBarNavigationGalleryBucketsSpinner = (AppCompatSpinner) view.findViewById(R.id.post_gallery_fragment_app_bar_spinner);

        this.initAppBarNavigationGalleryBucketsSpinnerAdapter();

        this.appBarNavigationGalleryBucketsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (PostGalleryFragment.this.appBarNavigationGalleryBucketsSpinner.getAdapter() != null
                        && PostGalleryFragment.this.appBarNavigationGalleryBucketsSpinner.getAdapter() instanceof ArrayAdapter) {

                    ArrayAdapter spinnerBucketsAdapter =
                            ((ArrayAdapter) PostGalleryFragment.this.appBarNavigationGalleryBucketsSpinner.getAdapter());

                    if (position < spinnerBucketsAdapter.getCount()
                            && spinnerBucketsAdapter.getItem(position) != null
                            && spinnerBucketsAdapter.getItem(position) instanceof MediaItemBucket) {

                        if (PostGalleryFragment.this.mediaGalleryImagesRecyclerView.getAdapter() != null
                                && PostGalleryFragment.this.mediaGalleryImagesRecyclerView.getAdapter() instanceof MediaGalleryAdapter) {

                            MediaGalleryAdapter mediaGalleryAdapter =
                                    ((MediaGalleryAdapter) PostGalleryFragment.this.mediaGalleryImagesRecyclerView.getAdapter());

                            GetMediaGalleryItemsOfBucketsTask getMediaGalleryBucketsTask = new GetMediaGalleryItemsOfBucketsTask(PostGalleryFragment.this.getContext(), new GetMediaItemsListener() {
                                @Override
                                public void onGetMediaItems(List<MediaItem> mediaItems) {

                                    if (mediaItems != null) {

                                        mediaGalleryAdapter.setMediaItems(mediaItems);

                                    }

                                    mediaGalleryAdapter.notifyDataSetChanged();

                                }
                            });

                            getMediaGalleryBucketsTask.execute(((MediaItemBucket) spinnerBucketsAdapter.getItem(position)));

                        }

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

//                MainCameraActivity.this.loadNewImageToCropperView("");

            }
        });

        this.appBarNavigationGalleryBucketsSpinner.setBackgroundColor(
                ContextCompat.getColor(this.getContext(), R.color.primary_color)
        );

    }

    private void initAppBarNavigationGalleryBucketsSpinnerAdapter() {

        this.appBarNavigationGalleryBucketsSpinnerAdapter = new MediaGalleryBucketSpinnerAdapter(this.getContext(),
                new ArrayList<MediaItemBucket>());

        this.appBarNavigationGalleryBucketsSpinner.setAdapter(this.appBarNavigationGalleryBucketsSpinnerAdapter);

        GetMediaGalleryBucketsTask getMediaGalleryBucketsTask = new GetMediaGalleryBucketsTask(this.getContext(), new GetBucketsListener() {
            @Override
            public void onGetBuckets(List<MediaItemBucket> mediaItemBuckets) {

                PostGalleryFragment.this.appBarNavigationGalleryBucketsSpinnerAdapter.clear();

                PostGalleryFragment.this.appBarNavigationGalleryBucketsSpinnerAdapter.addAll(mediaItemBuckets);

            }
        });

        getMediaGalleryBucketsTask.execute();

    }

    private void initAppBarPreviewLayout(View view) {

        this.appBarPreviewLayout = (ConstraintLayout) view.findViewById(R.id.post_gallery_fragment_app_bar_preview_layout);

        this.initAppBarPreviewContainer(view);

        this.initAppBarPreviewPrevButton(view);

        this.initAppBarPreviewNextButton(view);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initAppBarPreviewContainer(View view) {

        this.appBarPreviewContainer = (MediaGalleryPreviewContainer) view.findViewById(R.id.post_gallery_fragment_app_bar_preview_container);

        this.appBarPreviewContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN: {

                        PostGalleryFragment.this.postMediaConnector.disableViewPagerSwipe(true);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        PostGalleryFragment.this.postMediaConnector.disableViewPagerSwipe(false);

                        break;
                    }
                }
                return true;
            }
        });

        this.appBarPreviewLinearLayoutManager = new MediaGalleryPreviewLinearLayoutManager(this.getContext());
        this.appBarPreviewContainer.setLayoutManager(this.appBarPreviewLinearLayoutManager);

        this.appBarPreviewPlayerSelector = new PressablePlayerSelector(this.appBarPreviewContainer);
        this.appBarPreviewContainer.setPlayerSelector(this.appBarPreviewPlayerSelector);

        this.appBarPreviewSnapHelper = new PagerSnapHelper();
        this.appBarPreviewSnapHelper.attachToRecyclerView(this.appBarPreviewContainer);

//        this.checkedMediaItems.add(new MediaItem("file:///android_asset/bbb.mp4", MediaItemType.VIDEO_TYPE, false));
//        this.checkedMediaItems.add(new MediaItem("file:///android_asset/tos.mp4", MediaItemType.VIDEO_TYPE, false));
//        this.checkedMediaItems.add(new MediaItem("file:///android_asset/cosmos.mp4", MediaItemType.VIDEO_TYPE, false));
        this.checkedMediaItems = new ArrayList<MediaItem>();

        this.cachedCroppedBitmap = new HashMap<String, Bitmap>();

        this.cachedVideoAspectRatio = new HashMap<String, String>();

        this.appBarPreviewPlayerAdapter = new MediaGalleryPreviewAdapter(this.getContext(), this.appBarPreviewPlayerSelector, this.checkedMediaItems, this);

        this.appBarPreviewPlayerAdapter.setOnCropperGridCallbackListener(new CropperView.GridCallback() {
            @Override
            public boolean onGestureStarted() {

                PostGalleryFragment.this.appBarPreviewContainer.disableSwipe(true);

                PostGalleryFragment.this.postMediaConnector.disableViewPagerSwipe(true);

                return true;
            }

            @Override
            public boolean onGestureCompleted() {

                PostGalleryFragment.this.appBarPreviewContainer.disableSwipe(false);

                PostGalleryFragment.this.postMediaConnector.disableViewPagerSwipe(false);

                return false;
            }
        });

        this.appBarPreviewContainer.setAdapter(this.appBarPreviewPlayerAdapter);

        PostGalleryFragment.this.appBarPreviewContainer.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

                if (view.getTag() instanceof MediaItem && ((MediaItem) view.getTag()).getType() == MediaItemType.IMAGE_TYPE) {

                    CropperView cropperView = (CropperView) view.findViewById(R.id.choose_from_gallery_image_preview_view_holder_cropper_view);

                    MediaItem mediaItem = (MediaItem) view.getTag();

                    Bitmap bitmap = cropperView.getCroppedBitmap().getBitmap();

                    if (mediaItem != null && mediaItem.getUrl() != null && bitmap != null) {

                        PostGalleryFragment.this.cachedCroppedBitmap.put(mediaItem.getUrl(), bitmap);

                    }

                } else if (view.getTag() instanceof MediaItem && ((MediaItem) view.getTag()).getType() == MediaItemType.VIDEO_TYPE) {

                    MediaItem mediaItem = (MediaItem) view.getTag();

                    if (mediaItem != null && mediaItem.getProperties() != null && mediaItem.getProperties().containsKey("aspect_ratio")) {

                        PostGalleryFragment.this.cachedVideoAspectRatio.put(mediaItem.getUrl(), mediaItem.getProperties().get("aspect_ratio"));

                    }

                }

            }

        });



    }

    private void initAppBarPreviewPrevButton(View view) {

        this.appBarPreviewPrevButton = (ImageButton) view.findViewById(R.id.post_gallery_fragment_app_bar_preview_prev_button);

        this.appBarPreviewPrevButton.setBackground(
                DrawableUtilsManager.get().getRoundSelectableDrawable(ContextCompat.getColor(this.getContext(), R.color.primary_color))
        );

        ViewCompat.setElevation(
                this.appBarPreviewPrevButton,
                UIUtilsManager.get().convertDpToPixels(this.getContext(), 10)
        );

        this.appBarPreviewPrevButton.setImageDrawable(
                this.getResources().getDrawable(R.drawable.ic_navigate_before_white_24dp)
        );

        this.initOnTouchPreviewPrevButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPreviewPrevButtonListener() {

        this.onTouchAppBarPreviewPrevButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostGalleryFragment.this.touchDownAnimation);

                        PostGalleryFragment.this.onPreviewPrevButtonClicked();

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(PostGalleryFragment.this.touchUpAnimation);

                        break;
                    }
                }
                return true;

            }
        };

        this.appBarPreviewPrevButton.setOnTouchListener(this.onTouchAppBarPreviewPrevButtonListener);

    }

    public void onPreviewPrevButtonClicked() {

        if (this.appBarPreviewContainer.getLayoutManager() != null
                && this.appBarPreviewContainer.getLayoutManager() instanceof MediaGalleryPreviewLinearLayoutManager) {

            ((MediaGalleryPreviewLinearLayoutManager) this.appBarPreviewContainer.getLayoutManager()).scrollToPositionWithOffset(
                    ((MediaGalleryPreviewLinearLayoutManager)this.appBarPreviewContainer.getLayoutManager()).findLastVisibleItemPosition() + 1,
                    0
            );

        }

    }

    private void initAppBarPreviewNextButton(View view) {

        this.appBarPreviewNextButton = (ImageButton) view.findViewById(R.id.post_gallery_fragment_app_bar_preview_next_button);

        this.appBarPreviewNextButton.setBackground(
                DrawableUtilsManager.get().getRoundSelectableDrawable(ContextCompat.getColor(this.getContext(), R.color.primary_color))
        );

        ViewCompat.setElevation(
                this.appBarPreviewNextButton,
                UIUtilsManager.get().convertDpToPixels(this.getContext(), 10)
        );

        this.appBarPreviewNextButton.setImageDrawable(
                this.getResources().getDrawable(R.drawable.ic_navigate_next_white_24dp)
        );

        this.initOnTouchPreviewNextButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPreviewNextButtonListener() {

        this.onTouchAppBarPreviewNextButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostGalleryFragment.this.touchDownAnimation);

                        PostGalleryFragment.this.onPreviewNextButtonClicked();

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(PostGalleryFragment.this.touchUpAnimation);

                        break;
                    }
                }
                return true;

            }
        };

        this.appBarPreviewNextButton.setOnTouchListener(this.onTouchAppBarPreviewNextButtonListener);

    }

    public void onPreviewNextButtonClicked() {

        if (this.appBarPreviewContainer.getLayoutManager() != null
                && this.appBarPreviewContainer.getLayoutManager() instanceof LinearLayoutManager) {

            ((LinearLayoutManager) this.appBarPreviewContainer.getLayoutManager()).scrollToPositionWithOffset(
                    ((LinearLayoutManager)this.appBarPreviewContainer.getLayoutManager()).findLastVisibleItemPosition() - 1,
                    0
            );

        }

    }

    private void initMediaGalleryImagesRecyclerView(View view) {

        this.mediaGalleryImagesRecyclerView = (MediaGalleryRecyclerView) view.findViewById(R.id.post_gallery_fragment_gallery_images_recycler_view);

        this.initGalleryImagesAdapter();

        this.mediaGalleryImagesRecyclerView.setOnItemClickListener(new MediaGalleryRecyclerView.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (PostGalleryFragment.this.mediaGalleryImagesRecyclerView.getAdapter() != null
                        && PostGalleryFragment.this.mediaGalleryImagesRecyclerView.getAdapter() instanceof MediaGalleryAdapter) {

                    MediaGalleryAdapter mediaGalleryAdapter =
                            ((MediaGalleryAdapter) PostGalleryFragment.this.mediaGalleryImagesRecyclerView.getAdapter());

                    if (position < mediaGalleryAdapter.getMediaItems().size()) {

                        File file = new File(mediaGalleryAdapter.getMediaItems().get(position).getUrl());
                        if (file.exists()) {

//                            MainCameraActivity.this.loadNewImageToCropperView(
//                                    file.getAbsolutePath()
//                            );

                        }

                    }

                }

            }

            @Override
            public void onLongClick(View view, int position) {

                if (PostGalleryFragment.this.mediaGalleryImagesRecyclerView.getAdapter() != null
                        && PostGalleryFragment.this.mediaGalleryImagesRecyclerView.getAdapter() instanceof MediaGalleryAdapter) {

                    MediaGalleryAdapter mediaGalleryAdapter =
                            ((MediaGalleryAdapter) PostGalleryFragment.this.mediaGalleryImagesRecyclerView.getAdapter());

                    if (position < mediaGalleryAdapter.getMediaItems().size()) {

                        int checkedMediaItemIndex = PostGalleryFragment.this.getCheckedMediaItemIndex(mediaGalleryAdapter.getMediaItems().get(position));

                        if (checkedMediaItemIndex != -1) {

                            PostGalleryFragment.this.checkedMediaItems.remove(checkedMediaItemIndex);

                            PostGalleryFragment.this.appBarPreviewPlayerAdapter.notifyItemRemoved(checkedMediaItemIndex);

                            mediaGalleryAdapter.getMediaItems().get(position).setChecked(false);

                        } else {

                            PostGalleryFragment.this.checkedMediaItems.add(mediaGalleryAdapter.getMediaItems().get(position));

                            PostGalleryFragment.this.appBarPreviewPlayerAdapter.notifyItemInserted(PostGalleryFragment.this.appBarPreviewPlayerAdapter.getMediaItems().size());

                            mediaGalleryAdapter.getMediaItems().get(position).setChecked(true);

                        }

                        mediaGalleryAdapter.notifyItemChanged(position);

                    }

                }

            }
        });

        this.mediaGalleryImagesRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));

        this.mediaGalleryImagesRecyclerView.setAdapter(this.mediaGalleryAdapter);

        ViewGroup.LayoutParams params = this.mediaGalleryImagesRecyclerView.getLayoutParams();

        if (params instanceof CoordinatorLayout.LayoutParams) {

            ((CoordinatorLayout.LayoutParams)params).setBehavior(new OverScrollBounceBehavior());

            this.mediaGalleryImagesRecyclerView.requestLayout();

        }

    }

    private void initGalleryImagesAdapter() {

        this.mediaGalleryAdapter = new MediaGalleryAdapter(this.getContext(), new ArrayList<MediaItem>());

//        GetMediaGalleryBucketsTask getMediaGalleryBucketsTask = new GetMediaGalleryBucketsTask(this.getContext(), new GetBucketsListener() {
//            @Override
//            public void onGetBuckets(List<MediaItemBucket> mediaItemBuckets) {
//
//                GetMediaGalleryItemsOfBucketsTask getMediaGalleryItemsOfBucketsTask = new GetMediaGalleryItemsOfBucketsTask(PostGalleryFragment.this.getContext(), new GetMediaItemsListener() {
//                    @Override
//                    public void onGetMediaItems(List<MediaItem> mediaItems) {
//
//                        PostGalleryFragment.this.mediaGalleryAdapter.setMediaItems(mediaItems);
//
//                    }
//                });
//
//                getMediaGalleryItemsOfBucketsTask.execute(mediaItemBuckets.toArray(new MediaItemBucket[mediaItemBuckets.size()]));
//
//            }
//        });
//
//        getMediaGalleryBucketsTask.execute();

    }

    private int getCheckedMediaItemIndex(MediaItem mediaItem) {

        if (mediaItem != null) {

            for(int i = 0; i < this.checkedMediaItems.size(); i++) {

                if (this.checkedMediaItems.get(i) != null
                        && this.checkedMediaItems.get(i).getUrl().equals(mediaItem.getUrl())) {

                    return i;

                }

            }

        }

        return -1;

    }

    private int getGalleryMediaItemIndex(MediaItem mediaItem) {

        if (mediaItem != null
                && this.mediaGalleryAdapter != null
                && this.mediaGalleryAdapter.getMediaItems() != null) {

            List<MediaItem> mediaItems = this.mediaGalleryAdapter.getMediaItems();

            for(int i = 0; i < mediaItems.size(); i++) {

                if (mediaItems.get(i) != null
                        && mediaItems.get(i).getUrl().equals(mediaItem.getUrl())) {

                    return i;

                }

            }

        }

        return -1;

    }

    @Override
    public void unCheckMediaItem(MediaItem mediaItem) {

        int checkedMediaItemIndex = PostGalleryFragment.this.getCheckedMediaItemIndex(mediaItem);

        if (checkedMediaItemIndex != -1) {

            PostGalleryFragment.this.checkedMediaItems.remove(checkedMediaItemIndex);

            PostGalleryFragment.this.appBarPreviewPlayerAdapter.notifyItemRemoved(checkedMediaItemIndex);

            mediaItem.setChecked(false);

        }

        if (PostGalleryFragment.this.mediaGalleryImagesRecyclerView.getAdapter() != null
                && PostGalleryFragment.this.mediaGalleryImagesRecyclerView.getAdapter() instanceof MediaGalleryAdapter
                && ((MediaGalleryAdapter)PostGalleryFragment.this.mediaGalleryImagesRecyclerView.getAdapter()).getMediaItems() != null) {

            MediaGalleryAdapter mediaGalleryAdapter =
                    ((MediaGalleryAdapter) PostGalleryFragment.this.mediaGalleryImagesRecyclerView.getAdapter());

            List<MediaItem> mediaItems = mediaGalleryAdapter.getMediaItems();

            int galleryMediaItemIndex = PostGalleryFragment.this.getGalleryMediaItemIndex(mediaItem);

            if (galleryMediaItemIndex != -1) {

                mediaItems.get(galleryMediaItemIndex).setChecked(false);

                mediaGalleryAdapter.notifyItemChanged(galleryMediaItemIndex);

            }

        }



    }

    //activity result
//    @Override
//    public void onActivityResult(int requestCode, int responseCode, Intent resultIntent) {
//
//        super.onActivityResult(requestCode, responseCode, resultIntent);
//
//        if (responseCode == Activity.RESULT_OK) {
//            String absPath = BitmapUtilsManager.getFilePathFromUri(this.getContext(), resultIntent.getData());
////            loadNewImageToCropperView(absPath);
//        }
//
//    }

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

    //back pressed handler
    public void onBackPressed() {

    }

}
