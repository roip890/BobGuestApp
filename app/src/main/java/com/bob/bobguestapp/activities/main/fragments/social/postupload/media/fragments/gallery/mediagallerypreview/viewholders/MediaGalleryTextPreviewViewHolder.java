package com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.viewholders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bob.bobguestapp.R;
import com.bob.toolsmodule.objects.MediaItem;

public class MediaGalleryTextPreviewViewHolder extends MediaGalleryBasePreviewViewHolder {

  private static final String TAG = "SocialFeedPostsListMediaTextViewHolder";

  //context
  private Context context;

  //views
  private ConstraintLayout textLayout;
  private TextView textView;

  //media
  private MediaItem mediaItem;

  public MediaGalleryTextPreviewViewHolder(Context context, View view) {

    super(view);

    this.context = context;

    this.initViews(view);

  }

  private void initViews(View view) {

    this.initTextLayout(view);

  }

  private void initTextLayout(View view) {

    this.textLayout = (ConstraintLayout) view.findViewById(R.id.choose_from_gallery_text_preview_view_holder_text_layout);

    this.initTextView(view);

  }

  private void initTextView(View view) {

    this.textView = (TextView) view.findViewById(R.id.choose_from_gallery_text_preview_view_holder_text_view);

  }

  //bind view holder
  @SuppressLint("SetTextI18n")
  @Override
  public void setMediaItem(int position, MediaItem mediaItem) {

    if (mediaItem != null) {

      this.mediaItem = mediaItem;

      this.itemView.setTag(this.mediaItem);

    }

  }

}
