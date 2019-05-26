package com.bob.bobguestapp.activities.main.fragments.social.postpage.comments.commentslist.comperators;



import com.bob.bobguestapp.tools.database.objects.PostComment;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by User on 14/09/2018.
 */

public class PostPageCommentTimeComparator implements Comparator<PostComment> {

    @Override
    public int compare(PostComment firstPostComment, PostComment secondPostComment) {

        Date firstPostDate = Timestamp.valueOf(firstPostComment.getTimeStamp());
        Date secondPostDate = Timestamp.valueOf(secondPostComment.getTimeStamp());

        if (firstPostDate.before(secondPostDate)) {
            return 1;
        } else if (firstPostDate.after(secondPostDate)) {
            return -1;
        }
        return 0;
    }

}
