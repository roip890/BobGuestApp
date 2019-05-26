package com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.viewholders;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bob.toolsmodule.objects.MediaItem;

public abstract class MediaGalleryBasePreviewViewHolder extends RecyclerView.ViewHolder {

  public MediaGalleryBasePreviewViewHolder(View view) {
    super(view);
  }

  public abstract void setMediaItem(int position, MediaItem mediaItem);

}
