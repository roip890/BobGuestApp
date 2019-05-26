package com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.cachemanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bob.toolsmodule.objects.MediaItem;

import java.util.ArrayList;

import im.ene.toro.CacheManager;

public class SocialFeedPostsListCacheManager implements CacheManager {

    public ArrayList<MediaItem> mediaItems;

    public SocialFeedPostsListCacheManager(ArrayList<MediaItem> mediaItems) {
        this.mediaItems = mediaItems;
    }

    @NonNull
    @Override public Object getKeyForOrder(int order) {
        return this.mediaItems.get(order);
    }

    @Nullable
    @Override public Integer getOrderForKey(@NonNull Object key) {
        return key instanceof MediaItem ? this.mediaItems.indexOf(key) : null;
    }

}
