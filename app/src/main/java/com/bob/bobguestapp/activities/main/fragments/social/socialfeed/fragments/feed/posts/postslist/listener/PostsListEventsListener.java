package com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.listener;


import com.bob.bobguestapp.tools.database.objects.Post;

public interface PostsListEventsListener {

    public void onPostDelete(Post post);

    public void onPostEdit(Post post);

    public void showPostPage(Post post);

}
