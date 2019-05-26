package com.bob.bobguestapp.activities.main.fragments.social.likespage.likes.likeslist.comperators;



import com.bob.bobguestapp.tools.database.objects.PostLike;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by User on 14/09/2018.
 */

public class PostPageLikeTimeComparator implements Comparator<PostLike> {

    @Override
    public int compare(PostLike firstPostLike, PostLike secondPostLike) {

        Date firstPostDate = Timestamp.valueOf(firstPostLike.getTimeStamp());
        Date secondPostDate = Timestamp.valueOf(secondPostLike.getTimeStamp());

        if (firstPostDate.before(secondPostDate)) {
            return 1;
        } else if (firstPostDate.after(secondPostDate)) {
            return -1;
        }
        return 0;
    }

}
