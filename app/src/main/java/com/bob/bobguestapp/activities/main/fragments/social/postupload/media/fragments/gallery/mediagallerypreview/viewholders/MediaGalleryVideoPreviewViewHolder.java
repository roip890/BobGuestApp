package com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.viewholders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.listeners.MediaGalleryItemEventsListener;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.drawable.DrawableUtilsManager;
import com.bob.uimodule.icons.Icons;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.mikepenz.iconics.IconicsDrawable;

import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerDispatcher;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;
import im.ene.toro.widget.PressablePlayerSelector;

public class MediaGalleryVideoPreviewViewHolder extends MediaGalleryBasePreviewViewHolder implements ToroPlayer {

    private static final String TAG = "SocialFeedPostsListMediaVideoViewHolder";

    //context
    private Context context;

    private MediaGalleryItemEventsListener mediaGalleryItemEventsListener;

    //views
    private ConstraintLayout playerLayout;
    private PlayerView playerView;

    //player
    private ExoPlayerViewHelper exoPlayerViewHelper;
    private PressablePlayerSelector selector;
    private ImageButton playerPlayButton;
    private ImageButton playerSnapButton;
    private View.OnTouchListener onTouchPlayerSnapButtonListener;
    private ImageButton playerDeleteButton;
    private View.OnTouchListener onTouchPlayerDeleteButtonListener;
    private int aspectRatio = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT;

    //media
    private MediaItem mediaItem;
    private Uri mediaUri;

    //animations
    private Animation touchDownAnimation;
    private Animation touchUpAnimation;

    public MediaGalleryVideoPreviewViewHolder(Context context, PressablePlayerSelector selector, View view, MediaGalleryItemEventsListener mediaGalleryItemEventsListener) {

        super(view);

        this.context = context;

        this.mediaGalleryItemEventsListener = mediaGalleryItemEventsListener;

        this.selector = selector;

        this.initViews(view);

        this.initAnimations();

    }

    private void initViews(View view) {

        if (this.selector != null) view.setOnLongClickListener(this.selector);

        this.initPlayerLayout(view);

    }

    private void initPlayerLayout(View view) {

        this.playerLayout = (ConstraintLayout) view.findViewById(R.id.choose_from_gallery_video_preview_view_holder_player_layout);

        this.initPlayerView(view);

        this.initPlayerPlayButton(view);

        this.initPlayerSnapButton(view);

        this.initPlayerDeleteButton(view);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initPlayerView(View view) {

        this.playerView = (PlayerView) view.findViewById(R.id.choose_from_gallery_video_preview_view_holder_player_view);

        this.playerView.setResizeMode(this.aspectRatio);

        if (this.selector != null) this.playerView.setControlDispatcher(new ExoPlayerDispatcher(this.selector, this));

        this.playerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                boolean onTouchResult = MediaGalleryVideoPreviewViewHolder.this.playerView.onTouchEvent(event);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        return true;

                    }

                    case MotionEvent.ACTION_UP: {

                        if (MediaGalleryVideoPreviewViewHolder.this.isPlaying()) {

                            MediaGalleryVideoPreviewViewHolder.this.pause();

                        } else {

                            MediaGalleryVideoPreviewViewHolder.this.play();

                        }

                        break;

                    }
                }

                return onTouchResult;
            }

        });

        this.playerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                MediaGalleryVideoPreviewViewHolder.this.pause();

            }
        });


    }

    @SuppressLint("ClickableViewAccessibility")
    private void initPlayerPlayButton(View view) {

        this.playerPlayButton = (ImageButton) view.findViewById(R.id.choose_from_gallery_video_preview_view_holder_player_play_button);

        this.playerPlayButton.setBackground(
                DrawableUtilsManager.get().getRoundSelectableDrawable(ContextCompat.getColor(this.context, R.color.primary_color))
        );

        ViewCompat.setElevation(
                this.playerPlayButton,
                UIUtilsManager.get().convertDpToPixels(this.context, 10)
        );

        this.playerPlayButton.setImageDrawable(
                this.context.getResources().getDrawable(R.drawable.exo_controls_play)
        );

        this.playerPlayButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        return true;

                    }

                    case MotionEvent.ACTION_UP: {

                        if (MediaGalleryVideoPreviewViewHolder.this.isPlaying()) {

                            MediaGalleryVideoPreviewViewHolder.this.pause();

                        } else {

                            MediaGalleryVideoPreviewViewHolder.this.play();

                        }

                        break;

                    }
                }

                return true;
            }

        });


        MediaGalleryVideoPreviewViewHolder.this.playerPlayButton.setVisibility(View.INVISIBLE);

    }

    private void initPlayerSnapButton(View view) {

        this.playerSnapButton = (ImageButton) view.findViewById(R.id.choose_from_gallery_video_preview_view_holder_player_snap_button);

        this.playerSnapButton.setBackground(
                DrawableUtilsManager.get().getRoundSelectableDrawable(ContextCompat.getColor(this.context, R.color.primary_color))
        );

        ViewCompat.setElevation(
                this.playerSnapButton,
                UIUtilsManager.get().convertDpToPixels(this.context, 10)
        );

        this.playerSnapButton.setImageDrawable(
                this.context.getResources().getDrawable(R.drawable.ic_crop_free_white_24dp)
        );

        this.initOnTouchPlayerSnapButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPlayerSnapButtonListener() {

        this.onTouchPlayerSnapButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(MediaGalleryVideoPreviewViewHolder.this.touchDownAnimation);

                        MediaGalleryVideoPreviewViewHolder.this.onImageSnapClicked();

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(MediaGalleryVideoPreviewViewHolder.this.touchUpAnimation);

                        break;
                    }
                }
                return true;

            }
        };

        this.playerSnapButton.setOnTouchListener(this.onTouchPlayerSnapButtonListener);

    }

    public void onImageSnapClicked() {

        this.snapPlayer();

    }

    private void snapPlayer() {

        switch (this.playerView.getResizeMode()) {
            case AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT:
                this.aspectRatio = AspectRatioFrameLayout.RESIZE_MODE_FIT;
                break;
            case AspectRatioFrameLayout.RESIZE_MODE_FIT:
                this.aspectRatio = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT;
                break;
            default:
                break;
        }

        this.playerView.setResizeMode(this.aspectRatio);
        this.mediaItem.getProperties().put("aspect_ratio", Integer.toString(this.aspectRatio));
        if (this.itemView.getTag() instanceof MediaItem) {
            ((MediaItem)this.itemView.getTag()).getProperties().put("aspect_ratio", Integer.toString(this.aspectRatio));
        }

    }

    private void initPlayerDeleteButton(View view) {

        this.playerDeleteButton = (ImageButton) view.findViewById(R.id.choose_from_gallery_video_preview_view_holder_player_delete_button);

        this.playerDeleteButton.setBackground(
                DrawableUtilsManager.get().getRoundSelectableDrawable(ContextCompat.getColor(this.context, R.color.primary_color))
        );

        ViewCompat.setElevation(
                this.playerDeleteButton,
                UIUtilsManager.get().convertDpToPixels(this.context, 10)
        );

        this.playerDeleteButton.setImageDrawable(

                ((IconicsDrawable) Icons.get().findDrawable(
                        MediaGalleryVideoPreviewViewHolder.this.context,
                        "gmd_close"))
                        .sizeDp(12).colorRes(R.color.light_primary_color)
        );

        this.initOnTouchPlayerDeleteButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchPlayerDeleteButtonListener() {

        this.onTouchPlayerDeleteButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(MediaGalleryVideoPreviewViewHolder.this.touchDownAnimation);

                        MediaGalleryVideoPreviewViewHolder.this.onVideoDeleteClicked();

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(MediaGalleryVideoPreviewViewHolder.this.touchUpAnimation);

                        break;
                    }
                }
                return true;

            }
        };

        this.playerDeleteButton.setOnTouchListener(this.onTouchPlayerDeleteButtonListener);

    }

    public void onVideoDeleteClicked() {

        if (this.mediaGalleryItemEventsListener != null
                && this.mediaItem != null) {
            this.mediaGalleryItemEventsListener.unCheckMediaItem(this.mediaItem);
        }

    }

    //bind view holder
    @Override
    public void setMediaItem(int position, MediaItem mediaItem) {

        if (mediaItem != null) {

            this.mediaItem = mediaItem;

            this.mediaItem.getProperties().put("aspect_ratio", Integer.toString(this.aspectRatio));

            this.itemView.setTag(this.mediaItem);

            if (this.mediaItem.getUrl() != null) {

                this.mediaUri = Uri.parse(this.mediaItem.getUrl());

            }


        }

    }

    //toro player override functions
    @NonNull
    @Override public View getPlayerView() {
        return playerView;
    }

    @NonNull
    @Override public PlaybackInfo getCurrentPlaybackInfo() {
        return this.exoPlayerViewHelper != null ? this.exoPlayerViewHelper.getLatestPlaybackInfo() : new PlaybackInfo();
    }

    @Override
    public void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo) {
        if (this.exoPlayerViewHelper == null) {
            this.exoPlayerViewHelper = new ExoPlayerViewHelper(this, this.mediaUri);

            this.exoPlayerViewHelper.addPlayerEventListener(new EventListener() {
                @Override
                public void onFirstFrameRendered() {
                    MediaGalleryVideoPreviewViewHolder.this.pause();

                }

                @Override
                public void onBuffering() {

                }

                @Override
                public void onPlaying() {

                    MediaGalleryVideoPreviewViewHolder.this.playerPlayButton.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onPaused() {

                    MediaGalleryVideoPreviewViewHolder.this.playerPlayButton.setVisibility(View.VISIBLE);

                }

                @Override
                public void onCompleted() {
                    MediaGalleryVideoPreviewViewHolder.this.playerView.getPlayer().seekTo(0);
//                    SocialFeedPostsListMediaVideoViewHolder.this.playerPlayButton.setVisibility(View.VISIBLE);
                    MediaGalleryVideoPreviewViewHolder.this.pause();
                }
            });
        }
        this.exoPlayerViewHelper.initialize(container, playbackInfo);
    }

    @Override public void play() {
        if (this.exoPlayerViewHelper != null) this.exoPlayerViewHelper.play();
    }

    @Override public void pause() {
        if (this.exoPlayerViewHelper != null) this.exoPlayerViewHelper.pause();
    }

    @Override public boolean isPlaying() {
        return this.exoPlayerViewHelper != null && this.exoPlayerViewHelper.isPlaying();
    }

    @Override public void release() {
        if (this.exoPlayerViewHelper != null) {
            this.exoPlayerViewHelper.release();
            this.exoPlayerViewHelper = null;
        }
    }

    @Override public boolean wantsToPlay() {
        return ToroUtil.visibleAreaOffset(this, this.itemView.getParent()) >= 0.85;
    }

    @Override public int getPlayerOrder() {
        return getAdapterPosition();
    }

    @Override public String toString() {
        return "ExoPlayer{" + hashCode() + " " + getAdapterPosition() + "}";
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


}
