package com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.viewholders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.container.OnMediaLoadListener;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.video.exoplayer.ExoPlayerFullScreenDialogFragment;
import com.bob.uimodule.youtube.MyYoutubePlayerUiController;
import com.bob.uimodule.youtube.YoutubePlayerFullScreenDialogFragment;
import com.bob.uimodule.youtube.YoutubePlayerFullScreenListener;
import com.mikepenz.iconics.IconicsDrawable;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

public class SocialFeedPostsListMediaYoutubeViewHolder extends SocialFeedPostsListMediaBaseViewHolder {

    protected static final String TAG = "SocialFeedPostsListMediaVideoViewHolder";

    //context
    protected Context context;

    //fragment manager
    protected FragmentManager fragmentManager;

    //on media load listener
    private OnMediaLoadListener onMediaLoadListener;

    //video player full screen listener
    private YoutubePlayerFullScreenListener youtubePlayerFullScreenListener;

    //views
    protected ConstraintLayout playerLayout;
    protected YouTubePlayerView youtubePlayerView;

    //player
    private YouTubePlayerTracker youTubePlayerTracker;
    private YouTubePlayer youTubePlayer;
    private float position = -1f;
    private float volume = 100f;

    //media
    protected MediaItem mediaItem;
    protected String videoId;

    public SocialFeedPostsListMediaYoutubeViewHolder(Context context, FragmentManager fragmentManager, OnMediaLoadListener onMediaLoadListener, View view) {

        super(view);

        this.context = context;

        this.fragmentManager = fragmentManager;

        this.onMediaLoadListener = onMediaLoadListener;

        this.initPlayerFullScreenListener();

        this.initViews(view);

    }

    protected void initViews(View view) {

        this.initPlayerLayout(view);

    }

    protected void initPlayerLayout(View view) {

        this.playerLayout = (ConstraintLayout) view.findViewById(R.id.social_feed_posts_list_media_youtube_view_holder_player_layout);

        this.initPlayerView(view);

    }

    @SuppressLint("ClickableViewAccessibility")
    protected void initPlayerView(View view) {

        this.youtubePlayerView = (YouTubePlayerView) view.findViewById(R.id.social_feed_posts_list_media_youtube_view_holder_player_view);
        
        if (this.context instanceof AppCompatActivity) {

            ((AppCompatActivity) this.context).getLifecycle().addObserver(this.youtubePlayerView);

        }

        this.youtubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
            @Override
            public void onYouTubePlayer(@NotNull YouTubePlayer youTubePlayer) {

                Log.i("YoutubePlayerHelper", "onYouTubePlayer, On Video: " + videoId);

                SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayerTracker = new YouTubePlayerTracker();

                SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer = youTubePlayer;

                SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer.addListener(
                        SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayerTracker
                );

            }
        });

        this.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);

                Log.i("YoutubePlayerHelper", "onReady, On Video: " + videoId);

                if (SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer == null) {

                    SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer = youTubePlayer;

                }

                View customYoutubeControlView = SocialFeedPostsListMediaYoutubeViewHolder.this.youtubePlayerView.inflateCustomPlayerUi(R.layout.youtube_player_custom_controls_layout);

                MyYoutubePlayerUiController myYoutubePlayerUiController = new MyYoutubePlayerUiController(
                        SocialFeedPostsListMediaYoutubeViewHolder.this.context,
                        customYoutubeControlView,
                        SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer,
                        SocialFeedPostsListMediaYoutubeViewHolder.this.youtubePlayerView,
                        SocialFeedPostsListMediaYoutubeViewHolder.this.videoId,
                        SocialFeedPostsListMediaYoutubeViewHolder.this.volume,
                        false,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (view instanceof ImageButton) {

                                    ImageButton volumeScreenButton = (ImageButton) view;

                                    if (SocialFeedPostsListMediaYoutubeViewHolder.this.volume <= 0f) {

                                        SocialFeedPostsListMediaYoutubeViewHolder.this.volume = 100f;

                                        if (SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer != null) {
                                            SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer.setVolume(
                                                    (int) Math.max(0f, SocialFeedPostsListMediaYoutubeViewHolder.this.volume)
                                            );
                                        }

                                        volumeScreenButton.setImageDrawable(
                                                ((IconicsDrawable) Icons.get().findDrawable(
                                                        SocialFeedPostsListMediaYoutubeViewHolder.this.context,
                                                        "gmd_volume_up"))
                                                        .sizeDp(16).colorRes(R.color.light_primary_color));


                                    } else if (SocialFeedPostsListMediaYoutubeViewHolder.this.volume > 0f) {

                                        SocialFeedPostsListMediaYoutubeViewHolder.this.volume = 0f;

                                        if (SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer != null) {
                                            SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer.setVolume(
                                                    (int) Math.max(0f, SocialFeedPostsListMediaYoutubeViewHolder.this.volume)
                                            );
                                        }

                                        volumeScreenButton.setImageDrawable(
                                                ((IconicsDrawable) Icons.get().findDrawable(
                                                        SocialFeedPostsListMediaYoutubeViewHolder.this.context,
                                                        "gmd_volume_off"))
                                                        .sizeDp(16).colorRes(R.color.light_primary_color));

                                    }

                                }

                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer != null
                                        && SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayerTracker != null) {

                                    YoutubePlayerFullScreenDialogFragment fullScreenDialogFragment = YoutubePlayerFullScreenDialogFragment.newInstance(
                                            SocialFeedPostsListMediaYoutubeViewHolder.this.videoId,
                                            SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayerTracker.getCurrentSecond(),
                                            SocialFeedPostsListMediaYoutubeViewHolder.this.volume
                                    );

                                    fullScreenDialogFragment.setFullScreenListener(
                                            SocialFeedPostsListMediaYoutubeViewHolder.this.youtubePlayerFullScreenListener
                                    );

                                    fullScreenDialogFragment.show(
                                            SocialFeedPostsListMediaYoutubeViewHolder.this.fragmentManager,
                                            ExoPlayerFullScreenDialogFragment.TAG
                                    );

                                }

                            }
                        }
                );

                SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer.addListener(
                        myYoutubePlayerUiController
                );

                SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer.cueVideo(SocialFeedPostsListMediaYoutubeViewHolder.this.videoId,
                        (Math.max(0f, SocialFeedPostsListMediaYoutubeViewHolder.this.position)));

                SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer.setVolume(
                        (int) Math.max(0f, SocialFeedPostsListMediaYoutubeViewHolder.this.volume)
                );

            }

            @Override
            public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);

                switch (state) {

                    case BUFFERING:
                        Log.i("YoutubeViewHolder", "BUFFERING, Time: " + SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayerTracker.getCurrentSecond());
                        break;
                    case PLAYING:
                        Log.i("YoutubeViewHolder", "PLAYING, Time: " + SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayerTracker.getCurrentSecond());
                        SocialFeedPostsListMediaYoutubeViewHolder.this.updateDimensions();
                        break;
                    case PAUSED:
                        Log.i("YoutubeViewHolder", "PAUSED, Time: " + SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayerTracker.getCurrentSecond());
                        SocialFeedPostsListMediaYoutubeViewHolder.this.updateDimensions();
                        break;
                    case UNKNOWN:
                        Log.i("YoutubeViewHolder", "UNKNOWN, Time: " + SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayerTracker.getCurrentSecond());
                        break;
                    case UNSTARTED:
                        Log.i("YoutubeViewHolder", "UNSTARTED, Time: " + SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayerTracker.getCurrentSecond());
                        break;
                    case VIDEO_CUED:
                        Log.i("YoutubeViewHolder", "VIDEO_CUED, Time: " + SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayerTracker.getCurrentSecond());
                        break;
                    case ENDED:
                        Log.i("YoutubeViewHolder", "ENDED, Time: " + SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayerTracker.getCurrentSecond());
                        break;


                }
            }

        });

    }

    protected void initPlayerFullScreenListener() {

        this.youtubePlayerFullScreenListener = new YoutubePlayerFullScreenListener() {

            int orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

            @Override
            public void startFullScreen() {

                if (SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer != null) {

                    SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer.pause();

                }

                if (SocialFeedPostsListMediaYoutubeViewHolder.this.context instanceof Activity) {

                    this.orientation = ((Activity)SocialFeedPostsListMediaYoutubeViewHolder.this.context).getRequestedOrientation();

                    ((Activity)SocialFeedPostsListMediaYoutubeViewHolder.this.context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);

                }

            }

            @Override
            public void exitFullScreen(String videoId, float position, float volume) {

                SocialFeedPostsListMediaYoutubeViewHolder.this.position = position;

                SocialFeedPostsListMediaYoutubeViewHolder.this.volume = volume;

                if (SocialFeedPostsListMediaYoutubeViewHolder.this.youtubePlayerView != null
                        && SocialFeedPostsListMediaYoutubeViewHolder.this.youtubePlayerView.findViewById(R.id.youtube_volume) != null) {

                    ImageButton volumeButton = (ImageButton) SocialFeedPostsListMediaYoutubeViewHolder.this.youtubePlayerView.findViewById(R.id.youtube_volume);

                    if (SocialFeedPostsListMediaYoutubeViewHolder.this.volume <= 0f) {

                        volumeButton.setImageDrawable(
                                ((IconicsDrawable) Icons.get().findDrawable(
                                        SocialFeedPostsListMediaYoutubeViewHolder.this.context,
                                        "gmd_volume_off"))
                                        .sizeDp(16).colorRes(R.color.light_primary_color));

                    } else if (SocialFeedPostsListMediaYoutubeViewHolder.this.volume > 0f) {

                        volumeButton.setImageDrawable(
                                ((IconicsDrawable) Icons.get().findDrawable(
                                        SocialFeedPostsListMediaYoutubeViewHolder.this.context,
                                        "gmd_volume_up"))
                                        .sizeDp(16).colorRes(R.color.light_primary_color));

                    }

                }

                if (SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer != null) {

                    SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer.loadVideo(SocialFeedPostsListMediaYoutubeViewHolder.this.videoId,
                            (Math.max(0f, SocialFeedPostsListMediaYoutubeViewHolder.this.position)));

                    SocialFeedPostsListMediaYoutubeViewHolder.this.youTubePlayer.setVolume(
                            (int) Math.max(0f, SocialFeedPostsListMediaYoutubeViewHolder.this.volume)
                    );

                }

                if (SocialFeedPostsListMediaYoutubeViewHolder.this.context instanceof Activity) {

                    ((Activity)SocialFeedPostsListMediaYoutubeViewHolder.this.context).setRequestedOrientation(this.orientation);

                }

            }
        };

    }

    //bind view holder
    @SuppressLint("CheckResult")
    @Override
    public void setMediaItem(int position, MediaItem mediaItem) {

        if (mediaItem != null) {

            this.mediaItem = mediaItem;

            if (this.mediaItem.getUrl() != null) {

                this.videoId = this.mediaItem.getUrl();

            }


        }

    }

    private void saveCurrentPosition() {

        if (this.youTubePlayerTracker != null) {

            this.position = this.youTubePlayerTracker.getCurrentSecond();

        }

    }

    private void seekCurrentPosition() {

        if (this.youTubePlayer!= null) {

            this.youTubePlayer.seekTo(Math.max(0, this.position));

        }

    }

    private void seekToStart() {

        if (this.youTubePlayer!= null) {

            this.position = 0f;

            this.youTubePlayer.seekTo(0f);

        }

    }

    private void updateDimensions() {


        if (SocialFeedPostsListMediaYoutubeViewHolder.this.onMediaLoadListener != null) {

            SocialFeedPostsListMediaYoutubeViewHolder.this.onMediaLoadListener.onGetDimensionsRatio("H," + 16 + ":" + 9);

        }

    }

    @Override
    public void onAttachedToRecyclerView() {

    }

    @Override
    public void onDetachedToRecyclerView() {

    }

}
