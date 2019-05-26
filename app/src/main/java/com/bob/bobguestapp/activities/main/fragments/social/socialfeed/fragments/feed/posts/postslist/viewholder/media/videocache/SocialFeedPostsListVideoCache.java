package com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.videocache;

import com.bob.bobguestapp.BOBGuestApplication;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

public class SocialFeedPostsListVideoCache {

    private static SimpleCache simpleDownloadCache;

    public static SimpleCache getInstance() {

        if (simpleDownloadCache == null) simpleDownloadCache = new SimpleCache(
                new File(BOBGuestApplication.get().getExternalFilesDir(null) + "/toro_cache"),
                new NoOpCacheEvictor()
        );

        return simpleDownloadCache;
    }
}
