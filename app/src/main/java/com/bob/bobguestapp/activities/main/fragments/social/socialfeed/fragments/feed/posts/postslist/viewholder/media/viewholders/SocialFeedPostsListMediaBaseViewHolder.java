package com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.viewholders;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bob.toolsmodule.objects.MediaItem;

public abstract class SocialFeedPostsListMediaBaseViewHolder extends RecyclerView.ViewHolder {

  public SocialFeedPostsListMediaBaseViewHolder(View view) {
    super(view);
  }

  public abstract void setMediaItem(int position, MediaItem mediaItem);

  public abstract void onAttachedToRecyclerView();

  public abstract void onDetachedToRecyclerView();

}
