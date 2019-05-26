package com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.comperators;



import com.bob.bobguestapp.tools.database.objects.Post;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by User on 14/09/2018.
 */

public class SocialFeedPostTimeComparator implements Comparator<Post> {

    @Override
    public int compare(Post firstPost, Post secondPost) {

        Date firstPostDate = Timestamp.valueOf(firstPost.getTimeStamp());
        Date secondPostDate = Timestamp.valueOf(secondPost.getTimeStamp());

        if (firstPostDate.before(secondPostDate)) {
            return 1;
        } else if (firstPostDate.after(secondPostDate)) {
            return -1;
        }
        return 0;
    }

}
