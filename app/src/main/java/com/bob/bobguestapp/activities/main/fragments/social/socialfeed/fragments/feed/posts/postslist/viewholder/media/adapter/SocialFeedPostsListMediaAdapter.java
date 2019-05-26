package com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.container.OnMediaLoadListener;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.viewholders.SocialFeedPostsListMediaBaseViewHolder;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.viewholders.SocialFeedPostsListMediaImageViewHolder;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.viewholders.SocialFeedPostsListMediaTextViewHolder;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.viewholders.SocialFeedPostsListMediaVideoViewHolder;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.viewholders.SocialFeedPostsListMediaYoutubeViewHolder;
import com.bob.toolsmodule.enums.MediaItemType;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.uimodule.video.exoplayer.ExoPlayerFullScreenListener;

import java.util.List;

import im.ene.toro.widget.PressablePlayerSelector;

public class SocialFeedPostsListMediaAdapter extends RecyclerView.Adapter<SocialFeedPostsListMediaBaseViewHolder> {

    private static final int PREVIEW_NO_TYPE = -1;
    private static final int PREVIEW_TYPE_TEXT = 0;
    private static final int PREVIEW_TYPE_VIDEO = 1;
    private static final int PREVIEW_TYPE_IMAGE = 2;
    private static final int PREVIEW_TYPE_YOUTUBE = 3;

    @Nullable
    private final PressablePlayerSelector selector;


    //context
    private Context context;

    //fragment manager
    private FragmentManager fragmentManager;

    //video player full screen listener
    private ExoPlayerFullScreenListener exoPlayerFullScreenListener;

    //on media load listener
    private OnMediaLoadListener onMediaLoadListener;

    //media items
    private List<MediaItem> mediaItems;

    public SocialFeedPostsListMediaAdapter(Context context,
                                           FragmentManager fragmentManager,
                                           ExoPlayerFullScreenListener exoPlayerFullScreenListener,
                                           OnMediaLoadListener onMediaLoadListener,
                                           @Nullable PressablePlayerSelector selector,
                                           List<MediaItem> mediaItems) {

        this.context = context;

        this.fragmentManager = fragmentManager;

        this.exoPlayerFullScreenListener = exoPlayerFullScreenListener;

        this.onMediaLoadListener = onMediaLoadListener;

        this.selector = selector;

        this.mediaItems = mediaItems;

    }

    @NonNull
    @Override
    public SocialFeedPostsListMediaBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case PREVIEW_TYPE_YOUTUBE:
                return new SocialFeedPostsListMediaYoutubeViewHolder(this.context,
                        this.fragmentManager,
                        this.onMediaLoadListener,
                        layoutInflater.inflate(R.layout.view_holder_social_feed_posts_list_media_youtube_preview, parent, false));
            case PREVIEW_TYPE_VIDEO:
                return new SocialFeedPostsListMediaVideoViewHolder(this.context,
                        this.fragmentManager,
                        this.exoPlayerFullScreenListener,
                        this.onMediaLoadListener,
                        this.selector,
                        layoutInflater.inflate(R.layout.view_holder_social_feed_posts_list_media_video_preview, parent, false));
            case PREVIEW_TYPE_IMAGE:
                return new SocialFeedPostsListMediaImageViewHolder(this.context,
                        this.fragmentManager,
                        this.onMediaLoadListener,
                        layoutInflater.inflate(R.layout.view_holder_social_feed_posts_list_media_image_preview, parent, false));
            default:
                return new SocialFeedPostsListMediaTextViewHolder(this.context,
                        layoutInflater.inflate(R.layout.view_holder_social_feed_posts_list_media_text_preview, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull SocialFeedPostsListMediaBaseViewHolder mediaGalleryBasePreviewViewHolder, int position) {

        mediaGalleryBasePreviewViewHolder.setMediaItem(position, this.mediaItems.get(position));

    }

    @Override
    public int getItemViewType(int position) {

        if (position < this.mediaItems.size() && this.mediaItems.get(position) != null) {

            if (this.mediaItems.get(position).getType() == MediaItemType.YOUTUBE_TYPE) {

                return PREVIEW_TYPE_YOUTUBE;

            } else if (this.mediaItems.get(position).getType() == MediaItemType.VIDEO_TYPE) {

                return PREVIEW_TYPE_VIDEO;

            } else if (this.mediaItems.get(position).getType() == MediaItemType.IMAGE_TYPE) {

                return PREVIEW_TYPE_IMAGE;

            } else {

                return PREVIEW_NO_TYPE;

            }

        } else {

            return PREVIEW_NO_TYPE;

        }

    }

    @Override
    public int getItemCount() {

        return this.mediaItems.size();

    }

    public List<MediaItem> getMediaItems() {
        return this.mediaItems;
    }

    public void setMediaItems(List<MediaItem> mediaItems) {
        this.mediaItems = mediaItems;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull SocialFeedPostsListMediaBaseViewHolder socialFeedPostsListMediaBaseViewHolder) {

        socialFeedPostsListMediaBaseViewHolder.onAttachedToRecyclerView();

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull SocialFeedPostsListMediaBaseViewHolder socialFeedPostsListMediaBaseViewHolder) {

        socialFeedPostsListMediaBaseViewHolder.onDetachedToRecyclerView();

    }

}
