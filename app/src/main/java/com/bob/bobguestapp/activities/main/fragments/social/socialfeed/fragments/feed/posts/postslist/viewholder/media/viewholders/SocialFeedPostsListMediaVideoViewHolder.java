package com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.viewholders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.container.OnMediaLoadListener;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.videocache.SocialFeedPostsListVideoCache;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.drawable.DrawableUtilsManager;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.video.exoplayer.ExoPlayerFullScreenDialogFragment;
import com.bob.uimodule.video.exoplayer.ExoPlayerFullScreenListener;
import com.bob.uimodule.video.exoplayer.ExoPlayerVolumeInfoListener;
import com.bob.uimodule.video.exoplayer.MyExoPlayerDesigner;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.offline.FilteringManifestParser;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.manifest.DashManifestParser;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.hls.playlist.DefaultHlsPlaylistParserFactory;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.TrackSelectionView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.Util;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.Config;
import im.ene.toro.exoplayer.ExoPlayerDispatcher;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.exoplayer.MediaSourceBuilder;
import im.ene.toro.exoplayer.ToroExoPlayer;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.media.VolumeInfo;
import im.ene.toro.widget.Container;
import im.ene.toro.widget.PressablePlayerSelector;

public class SocialFeedPostsListMediaVideoViewHolder extends SocialFeedPostsListMediaBaseViewHolder implements ToroPlayer, ExoPlayerVolumeInfoListener {

    protected static final String TAG = "SocialFeedPostsListMediaVideoViewHolder";

    //context
    protected Context context;

    //fragment manager
    protected FragmentManager fragmentManager;

    //on media load listener
    private OnMediaLoadListener onMediaLoadListener;

    //video player full screen listener
    private ExoPlayerFullScreenListener exoPlayerFullScreenListener;

    //views
    protected ConstraintLayout playerLayout;
    protected PlayerView playerView;
    protected Bitmap videoThumbnail = null;

    //player
    private TrackGroupArray lastSeenTrackGroupArray;
    private DataSource.Factory dataSourceFactory;
    protected SimpleExoPlayer player;
    protected ExoPlayerViewHelper exoPlayerViewHelper;
    protected PressablePlayerSelector selector;
    protected ImageButton playerStartButton;
    protected DefaultTrackSelector playerTrackSelector;
    protected DefaultTrackSelector.Parameters playerTrackSelectorParameters;
    protected int aspectRatio = AspectRatioFrameLayout.RESIZE_MODE_FIT;

    private LinearLayout debugView;

    //media
    protected MediaItem mediaItem;
    protected Uri mediaUri;

    public SocialFeedPostsListMediaVideoViewHolder(Context context,
                                                   FragmentManager fragmentManager,
                                                   ExoPlayerFullScreenListener exoPlayerFullScreenListener,
                                                   OnMediaLoadListener onMediaLoadListener,
                                                   PressablePlayerSelector selector,
                                                   View view) {

        super(view);

        this.context = context;

        this.fragmentManager = fragmentManager;

        this.exoPlayerFullScreenListener = exoPlayerFullScreenListener;

        this.onMediaLoadListener = onMediaLoadListener;

        this.selector = selector;

        this.debugView = (LinearLayout) view.findViewById(R.id.social_feed_posts_list_media_video_view_holder_player_debug_view);

        this.dataSourceFactory = buildDataSourceFactory();

        this.playerTrackSelectorParameters = new DefaultTrackSelector.ParametersBuilder().build();

        this.initViews(view);

    }

    private void showSelection(View view) {

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = this.playerTrackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            CharSequence title = ((Button) view).getText();
            int rendererIndex = (int) view.getTag();
            int rendererType = mappedTrackInfo.getRendererType(rendererIndex);
            boolean allowAdaptiveSelections =
                    rendererType == C.TRACK_TYPE_VIDEO
                            || (rendererType == C.TRACK_TYPE_AUDIO
                            && mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_NO_TRACKS);
            Pair<AlertDialog, TrackSelectionView> dialogPair =
                    TrackSelectionView.getDialog((Activity) this.context, title, this.playerTrackSelector, rendererIndex);
            dialogPair.second.setShowDisableOption(true);
            dialogPair.second.setAllowAdaptiveSelections(allowAdaptiveSelections);
            dialogPair.first.show();
        }

    }

    private void updateButtonVisibilities() {
        debugView.removeAllViews();
        if (this.playerView.getPlayer() == null) {
            return;
        }

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = this.playerTrackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }

        for (int i = 0; i < mappedTrackInfo.getRendererCount(); i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                Button button = new Button(this.context);
                int label;
                switch (this.playerView.getPlayer().getRendererType(i)) {
                    case C.TRACK_TYPE_AUDIO:
                        label = R.string.exo_track_selection_title_audio;
                        break;
                    case C.TRACK_TYPE_VIDEO:
                        label = R.string.exo_track_selection_title_video;
                        break;
                    case C.TRACK_TYPE_TEXT:
                        label = R.string.exo_track_selection_title_text;
                        break;
                    default:
                        continue;
                }
                button.setText(label);
                button.setTag(i);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SocialFeedPostsListMediaVideoViewHolder.this.showSelection(view);

                    }
                });
                debugView.addView(button);
            }
        }
    }

    private void updateTrackSelectorParameters() {
        if (this.playerTrackSelector != null) {
            this.playerTrackSelectorParameters = this.playerTrackSelector.getParameters();
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return buildMediaSource(uri, null);
    }

    @SuppressWarnings("unchecked")
    private MediaSource buildMediaSource(Uri uri, @Nullable String overrideExtension) {
        @C.ContentType int type = Util.inferContentType(uri, overrideExtension);
        switch (type) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(dataSourceFactory)
                        .setManifestParser(
                                new FilteringManifestParser<>(new DashManifestParser(), getOfflineStreamKeys(uri)))
                        .createMediaSource(uri);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(dataSourceFactory)
                        .setManifestParser(
                                new FilteringManifestParser<>(new SsManifestParser(), getOfflineStreamKeys(uri)))
                        .createMediaSource(uri);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(dataSourceFactory)
                        .setPlaylistParserFactory(
                                new DefaultHlsPlaylistParserFactory(getOfflineStreamKeys(uri)))
                        .createMediaSource(uri);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private DataSource.Factory buildDataSourceFactory() {
        return BOBGuestApplication.get().buildDataSourceFactory();
    }

    private List<StreamKey> getOfflineStreamKeys(Uri uri) {
        return BOBGuestApplication.get().getDownloadTracker().getOfflineStreamKeys(uri);
    }


    protected void initViews(View view) {

        if (this.selector != null) view.setOnLongClickListener(this.selector);

        this.initPlayerLayout(view);

    }

    protected void initPlayerLayout(View view) {

        this.playerLayout = (ConstraintLayout) view.findViewById(R.id.social_feed_posts_list_media_video_view_holder_player_layout);

        this.initPlayerView(view);

        this.initPlayerStartButton(view);

//        this.initPlayerPlayButton(view);

//        this.initPlayerPauseButton(view);

    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initPlayerView(View view) {

        this.playerView = (PlayerView) view.findViewById(R.id.social_feed_posts_list_media_video_view_holder_player_view);

        new MyExoPlayerDesigner().initExoPlayerView(this.context, this.playerView, this, false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SocialFeedPostsListMediaVideoViewHolder.this.exoPlayerFullScreenListener != null
                        && SocialFeedPostsListMediaVideoViewHolder.this.playerView.getPlayer() instanceof ToroExoPlayer
                        && ((ToroExoPlayer) SocialFeedPostsListMediaVideoViewHolder.this.playerView.getPlayer()).getVideoFormat() != null
                        && SocialFeedPostsListMediaVideoViewHolder.this.getCurrentPlaybackInfo() != null) {

                    int height = ((ToroExoPlayer) SocialFeedPostsListMediaVideoViewHolder.this.playerView.getPlayer()).getVideoFormat().height;
                    int width = ((ToroExoPlayer) SocialFeedPostsListMediaVideoViewHolder.this.playerView.getPlayer()).getVideoFormat().width;
                    PlaybackInfo playbackInfo = SocialFeedPostsListMediaVideoViewHolder.this.getCurrentPlaybackInfo();

                    ExoPlayerFullScreenDialogFragment fullScreenDialogFragment = ExoPlayerFullScreenDialogFragment.newInstance(
                            SocialFeedPostsListMediaVideoViewHolder.this.getPlayerOrder(),
                            SocialFeedPostsListMediaVideoViewHolder.this.mediaUri,
                            playbackInfo,
                            new Point(width, height)
                    );

                    fullScreenDialogFragment.setFullScreenListener(
                            SocialFeedPostsListMediaVideoViewHolder.this.exoPlayerFullScreenListener
                    );

                    fullScreenDialogFragment.show(
                            SocialFeedPostsListMediaVideoViewHolder.this.fragmentManager,
                            ExoPlayerFullScreenDialogFragment.TAG
                    );

                }

            }
        });

        this.playerView.setResizeMode(this.aspectRatio);

        if (this.selector != null) this.playerView.setControlDispatcher(new ExoPlayerDispatcher(this.selector, this));

    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initPlayerStartButton(View view) {

        this.playerStartButton = (ImageButton) view.findViewById(R.id.social_feed_posts_list_media_video_view_holder_player_start_button);

        this.playerStartButton.setBackground(
                DrawableUtilsManager.get().getRoundSelectableDrawable(ContextCompat.getColor(this.context, R.color.primary_color))
        );

        ViewCompat.setElevation(
                this.playerStartButton,
                UIUtilsManager.get().convertDpToPixels(this.context, 10)
        );

        this.playerStartButton.setImageDrawable(
                this.context.getResources().getDrawable(R.drawable.exo_controls_play)
        );

        this.playerStartButton.setVisibility(View.VISIBLE);

        this.playerStartButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    SocialFeedPostsListMediaVideoViewHolder.this.play();

                }

                return false;
            }
        });

        this.playerStartButton.setVisibility(View.VISIBLE);

    }

    //bind view holder
    @SuppressLint("CheckResult")
    @Override
    public void setMediaItem(int position, MediaItem mediaItem) {

        if (mediaItem != null) {

            this.mediaItem = mediaItem;

            if (this.mediaItem.getUrl() != null) {

                this.mediaUri = Uri.parse(this.mediaItem.getUrl());
//                this.mediaUri = Uri.parse("https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd");

                this.setPlayerViewDimensionsAndThumbnailAsync(this.mediaItem.getUrl());
//                this.setPlayerViewDimensionsAndThumbnailAsync("https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd");

            }

            if (this.mediaItem.getProperties() != null
                    && this.mediaItem.getProperties().containsKey("aspect_ratio")
                    && this.mediaItem.getProperties().get("aspect_ratio") != null) {

                this.aspectRatio = Integer.valueOf(this.mediaItem.getProperties().get("aspect_ratio"));

            }


            if (this.playerView != null) {

                this.playerView.setResizeMode(this.aspectRatio);

            }

        }

    }

    protected void setPlayerViewDimensionsAndThumbnailAsync(String videoUrl) {

        if (videoUrl != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

//                        Log.i("VideoVars", "Starts");
//
//                        FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
//
//                        if (Build.VERSION.SDK_INT >= 14) {
//                            retriever.setDataSource(videoUrl, new HashMap<String, String>());
//                        } else {
//                            retriever.setDataSource(videoUrl);
//                        }
//
//                        long width = Long.parseLong(retriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
//                        long height = Long.parseLong(retriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
//
//                        Log.i("VideoVars", "Dimensions: " + "H," + width + ":" + height);
//
//                        if (SocialFeedPostsListMediaVideoViewHolder.this.youtubePlayerView != null
//                                && SocialFeedPostsListMediaVideoViewHolder.this.youtubePlayerView.getLayoutParams() instanceof ConstraintLayout.LayoutParams
//                                && SocialFeedPostsListMediaVideoViewHolder.this.playerViewThumbnailCover != null
//                                && SocialFeedPostsListMediaVideoViewHolder.this.playerViewThumbnailCover.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {
//
//                            ConstraintLayout.LayoutParams playerViewLayoutParams =
//                                    ((ConstraintLayout.LayoutParams)SocialFeedPostsListMediaVideoViewHolder.this.youtubePlayerView.getLayoutParams());
//
//                            ConstraintLayout.LayoutParams playerViewThumbnailCoverLayoutParams =
//                                    ((ConstraintLayout.LayoutParams)SocialFeedPostsListMediaVideoViewHolder.this.playerViewThumbnailCover.getLayoutParams());
//
//                            playerViewLayoutParams.dimensionRatio = "H," + width + ":" + height;
//
//                            playerViewThumbnailCoverLayoutParams.dimensionRatio = "H," + width + ":" + height;
//
//                        }
//
//                        retriever.release();
//


//                        Log.i("VideoVars", "Starts");
//
//                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                        retriever.setDataSource(videoUrl, new HashMap<String, String>());
//
//                        int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
//                        int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
//
//                        Log.i("VideoVars", "Dimensions: " + "H," + width + ":" + height);
//
//                        if (SocialFeedPostsListMediaVideoViewHolder.this.youtubePlayerView != null
//                                && SocialFeedPostsListMediaVideoViewHolder.this.youtubePlayerView.getLayoutParams() instanceof ConstraintLayout.LayoutParams
//                                && SocialFeedPostsListMediaVideoViewHolder.this.playerViewThumbnailCover != null
//                                && SocialFeedPostsListMediaVideoViewHolder.this.playerViewThumbnailCover.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {
//
//                            ConstraintLayout.LayoutParams playerViewLayoutParams =
//                                    ((ConstraintLayout.LayoutParams)SocialFeedPostsListMediaVideoViewHolder.this.youtubePlayerView.getLayoutParams());
//
//                            ConstraintLayout.LayoutParams playerViewThumbnailCoverLayoutParams =
//                                    ((ConstraintLayout.LayoutParams)SocialFeedPostsListMediaVideoViewHolder.this.playerViewThumbnailCover.getLayoutParams());
//
//                            playerViewLayoutParams.dimensionRatio = "H," + width + ":" + height;
//
//                            playerViewThumbnailCoverLayoutParams.dimensionRatio = "H," + width + ":" + height;
//
//                        }
//
//                        if (SocialFeedPostsListMediaVideoViewHolder.this.videoThumbnail == null) {
//                            SocialFeedPostsListMediaVideoViewHolder.this.videoThumbnail = retriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST);
//
//                            if (SocialFeedPostsListMediaVideoViewHolder.this.videoThumbnail == null) {
//                                Log.i("VideoVars", "Thumbnail: Success");
//                            } else {
//                                Log.i("VideoVars", "Thumbnail: Failure");
//                            }
//                        }
//
//                        retriever.release();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("VideoVars", "Exceptions: " + e.toString());
                        Log.i("VideoVars", "videoUrl: " + videoUrl);
                    }
                }
            }).start();
        }


    }

    //toro player override functions
    @NonNull
    @Override public View getPlayerView() {
        return playerView;
    }

    public ExoPlayerViewHelper getExoPlayerViewHelper() {
        return this.exoPlayerViewHelper;
    }

    private void updateDimensions() {

        if (SocialFeedPostsListMediaVideoViewHolder.this.playerView.getPlayer() instanceof ToroExoPlayer) {

            int height = ((ToroExoPlayer)SocialFeedPostsListMediaVideoViewHolder.this.playerView.getPlayer()).getVideoFormat().height;
            int width = ((ToroExoPlayer)SocialFeedPostsListMediaVideoViewHolder.this.playerView.getPlayer()).getVideoFormat().width;

            if (SocialFeedPostsListMediaVideoViewHolder.this.playerView != null
                    && SocialFeedPostsListMediaVideoViewHolder.this.playerView.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {


                SocialFeedPostsListMediaVideoViewHolder.this.playerView.setTag("H," + width + ":" + height);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(SocialFeedPostsListMediaVideoViewHolder.this.playerLayout);
                constraintSet.setDimensionRatio(SocialFeedPostsListMediaVideoViewHolder.this.playerView.getId(), "H," + width + ":" + height);
                constraintSet.applyTo(SocialFeedPostsListMediaVideoViewHolder.this.playerLayout);

                if (SocialFeedPostsListMediaVideoViewHolder.this.onMediaLoadListener != null) {

                    SocialFeedPostsListMediaVideoViewHolder.this.onMediaLoadListener.onGetDimensionsRatio("H," + width + ":" + height);

                }

//                ConstraintLayout.LayoutParams playerViewLayoutParams =
//                        ((ConstraintLayout.LayoutParams)SocialFeedPostsListMediaVideoViewHolder.this.youtubePlayerView.getLayoutParams());
//
//                ConstraintLayout.LayoutParams playerViewThumbnailCoverLayoutParams =
//                        ((ConstraintLayout.LayoutParams)SocialFeedPostsListMediaVideoViewHolder.this.playerViewThumbnailCover.getLayoutParams());

//                playerViewLayoutParams.dimensionRatio = "H," + width + ":" + height;

//                playerViewThumbnailCoverLayoutParams.dimensionRatio = "H," + width + ":" + height;

//                ConstraintSet constraintSet = new ConstraintSet();
//                constraintSet.clone(SocialFeedPostsListMediaVideoViewHolder.this.playerLayout);
//                constraintSet.setDimensionRatio(SocialFeedPostsListMediaVideoViewHolder.this.youtubePlayerView.getId(), "H," + width + ":" + height);
//                constraintSet.setDimensionRatio(SocialFeedPostsListMediaVideoViewHolder.this.playerViewThumbnailCover.getId(), "H," + width + ":" + height);
//                constraintSet.applyTo(SocialFeedPostsListMediaVideoViewHolder.this.playerLayout);

//                SocialFeedPostsListMediaVideoViewHolder.this.youtubePlayerView.setLayoutParams(playerViewLayoutParams);

//                SocialFeedPostsListMediaVideoViewHolder.this.playerViewThumbnailCover.setLayoutParams(playerViewThumbnailCoverLayoutParams);

                Log.i("Player Metadata", "H," + width + ":" + height);
            }

        }

    }

    @NonNull
    @Override public PlaybackInfo getCurrentPlaybackInfo() {
        return this.exoPlayerViewHelper != null ? this.exoPlayerViewHelper.getLatestPlaybackInfo() : new PlaybackInfo();
    }

    @Override
    public void initialize(@NonNull Container container, @NonNull PlaybackInfo playbackInfo) {

        if (this.playerView != null && this.videoThumbnail != null) {

            this.playerView.setDefaultArtwork(new BitmapDrawable(this.context.getResources(), this.videoThumbnail));

        }

        if (this.exoPlayerViewHelper == null) {

            Config config = new Config.Builder()
                    .setMediaSourceBuilder(MediaSourceBuilder.LOOPING)
                    .setCache(SocialFeedPostsListVideoCache.getInstance())
                    .build();

            this.exoPlayerViewHelper = new ExoPlayerViewHelper(this, this.mediaUri, null, config);

            this.exoPlayerViewHelper.addPlayerEventListener(new EventListener() {
                @Override
                public void onFirstFrameRendered() {

                    SocialFeedPostsListMediaVideoViewHolder.this.pause();

                    SocialFeedPostsListMediaVideoViewHolder.this.updateVolumeButton();

                }

                @Override
                public void onBuffering() {

                    SocialFeedPostsListMediaVideoViewHolder.this.updateVolumeButton();

                }

                @Override
                public void onPlaying() {

                    SocialFeedPostsListMediaVideoViewHolder.this.playerStartButton.setVisibility(View.GONE);

                    SocialFeedPostsListMediaVideoViewHolder.this.updateDimensions();

                    SocialFeedPostsListMediaVideoViewHolder.this.updateVolumeButton();

                }

                @Override
                public void onPaused() {

                    SocialFeedPostsListMediaVideoViewHolder.this.updateDimensions();

                    SocialFeedPostsListMediaVideoViewHolder.this.updateVolumeButton();

                }

                @Override
                public void onCompleted() {
                    SocialFeedPostsListMediaVideoViewHolder.this.playerView.getPlayer().seekTo(0);

                    SocialFeedPostsListMediaVideoViewHolder.this.pause();

                    SocialFeedPostsListMediaVideoViewHolder.this.updateVolumeButton();

                }
            });

//            //track selector
//            TrackSelection.Factory trackSelectionFactory = new AdaptiveTrackSelection.Factory();
//
//            @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode =
//                    BOBGuestApplication.get().useExtensionRenderers()
//                            ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON
//                            : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;
//
//            DefaultRenderersFactory renderersFactory =
//                    new DefaultRenderersFactory(this.context, extensionRendererMode);
//
//            this.playerTrackSelector = new DefaultTrackSelector(trackSelectionFactory);
//            this.playerTrackSelector.setParameters(this.playerTrackSelectorParameters);
//
//            //load control
//            LoadControl loadControl = new DefaultLoadControl();
//
//            this.player = ExoPlayerFactory.newSimpleInstance(
//                    this.context,
//                    renderersFactory,
//                    this.playerTrackSelector,
//                    loadControl);
//
//            this.player.addListener(new PlayerEventListener());
//            this.player.addAnalyticsListener(new EventLogger(this.playerTrackSelector));
//            this.playerView.setPlayer(this.player);
//
//            MediaSource mediaSource = buildMediaSource(this.mediaUri, null);
//
//            this.player.prepare(mediaSource);
//
//            this.playerView.requestFocus();

        }

        this.exoPlayerViewHelper.initialize(container, playbackInfo);

    }

    @Override public void play() {
        if (this.mediaUri != null && this.exoPlayerViewHelper != null) this.exoPlayerViewHelper.play();
    }

    @Override public void pause() {

//        if (youtubePlayerView != null) {
//
//            this.videoThumbnail = this.getBitmap(this.youtubePlayerView);
//
//        }

        if (this.mediaUri != null && this.exoPlayerViewHelper != null) this.exoPlayerViewHelper.pause();
    }

//    protected Bitmap getBitmap(View layout) {
//
//        layout.setDrawingCacheEnabled(true);
//
//        layout.buildDrawingCache();
//
//        Bitmap bitmap = layout.getDrawingCache();
//
//        layout.setDrawingCacheEnabled(false);
//
//        return bitmap;
//
//    }

    @Override public boolean isPlaying() {
        return this.mediaUri != null && this.exoPlayerViewHelper != null && this.exoPlayerViewHelper.isPlaying();
    }

    @Override public void release() {
        if (this.exoPlayerViewHelper != null) {

            this.exoPlayerViewHelper.release();
            this.exoPlayerViewHelper = null;

            updateTrackSelectorParameters();
            this.playerTrackSelector = null;

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

    @Override
    public void setVolumeInfo(VolumeInfo volumeInfo) {

        if (this.exoPlayerViewHelper != null && volumeInfo != null) {

            this.exoPlayerViewHelper.setVolumeInfo(volumeInfo);

        }

    }

    @Override
    public VolumeInfo getVolumeInfo() {
        if (this.exoPlayerViewHelper != null) {

            return this.exoPlayerViewHelper.getVolumeInfo();

        }
        return null;
    }

    private void updateVolumeButton() {

        ImageButton volumeButton = (ImageButton) this.playerView.findViewById(R.id.exo_volume_off);

        if (volumeButton != null && this.getVolumeInfo() != null) {

            if (this.getVolumeInfo().isMute()) {

                volumeButton.setImageDrawable(
                        ((IconicsDrawable) Icons.get().findDrawable(
                                this.context,
                                "gmd_volume_off"))
                                .sizeDp(16).colorRes(R.color.light_primary_color));

            } else if (!this.getVolumeInfo().isMute()) {

                volumeButton.setImageDrawable(
                        ((IconicsDrawable) Icons.get().findDrawable(
                                this.context,
                                "gmd_volume_up"))
                                .sizeDp(16).colorRes(R.color.light_primary_color));

            }

        }

    }

    @Override
    public void onAttachedToRecyclerView() {

        this.pause();

        if (this.playerStartButton != null) {

            this.playerStartButton.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onDetachedToRecyclerView() {

        this.pause();

        if (this.playerStartButton != null) {

            this.playerStartButton.setVisibility(View.VISIBLE);

        }

    }

    private class PlayerEventListener implements Player.EventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            updateButtonVisibilities();
        }

        @Override
        public void onPositionDiscontinuity(@Player.DiscontinuityReason int reason) {
        }

        @Override
        public void onPlayerError(ExoPlaybackException e) {
        }

        @Override
        @SuppressWarnings("ReferenceEquality")
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            updateButtonVisibilities();
            if (trackGroups != SocialFeedPostsListMediaVideoViewHolder.this.lastSeenTrackGroupArray) {
                MappingTrackSelector.MappedTrackInfo mappedTrackInfo = SocialFeedPostsListMediaVideoViewHolder.this.playerTrackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        Toast.makeText(SocialFeedPostsListMediaVideoViewHolder.this.context, R.string.error_unsupported_video, Toast.LENGTH_SHORT)
                        .show();
                    }
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
                            == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        Toast.makeText(SocialFeedPostsListMediaVideoViewHolder.this.context, R.string.error_unsupported_audio, Toast.LENGTH_SHORT)
                                .show();
                    }
                }
                SocialFeedPostsListMediaVideoViewHolder.this.lastSeenTrackGroupArray = trackGroups;
            }
        }
    }

}
