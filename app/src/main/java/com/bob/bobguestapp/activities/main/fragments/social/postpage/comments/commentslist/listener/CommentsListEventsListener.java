package com.bob.bobguestapp.activities.main.fragments.social.postpage.comments.commentslist.listener;


import com.bob.bobguestapp.tools.database.objects.PostComment;

public interface CommentsListEventsListener {

    public void onCommentDelete(PostComment comment);

    public void onCommentEdit(PostComment comment);

}
