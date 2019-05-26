package com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerygrid;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.toolsmodule.objects.MediaItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bob.bobguestapp.R;

public class MediaGalleryViewHolder extends RecyclerView.ViewHolder {

    //context
    private Context context;

    //views
    private ImageView mediaImageView;
    private ImageButton checkedImageButton;
    private ImageButton mediaTypeButtonImageButton;

    //media item
    private MediaItem mediaItem;

    public MediaGalleryViewHolder(Context context, View view) {
        super(view);

        this.context = context;

        this.initViews(view);

    }

    private void initViews(View view) {

        this.initMediaImageView(view);

        this.initCheckedButton(view);

        this.initMediaTypeButton(view);
    }

    private void initMediaImageView(View view) {
        this.mediaImageView = (ImageView) view.findViewById(R.id.media_gallery_item_image);

    }

    private void initCheckedButton(View view) {

        this.checkedImageButton = (ImageButton) view.findViewById(R.id.media_gallery_item_checked_button);

    }

    private void initMediaTypeButton(View view) {

        this.mediaTypeButtonImageButton = (ImageButton) view.findViewById(R.id.media_gallery_item_media_type_button);

    }

    public void setMediaItem(MediaItem mediaItem) {

        this.mediaItem = mediaItem;

        this.itemView.setTag(mediaItem);

        this.updateView();

    }

    private void updateView() {

        this.updateMediaImageView();

        this.updateCheckedButton();

        this.updateMediaTypeButton();

    }

    private void updateMediaImageView() {

        if (this.mediaItem != null && this.mediaItem.getUrl() != null) {

            Glide.with(this.context)
                    .asBitmap()
                    .load(this.mediaItem.getUrl())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .dontAnimate()
                            .skipMemoryCache(true))
//                    .transition(withCrossFade())
                    .into(this.mediaImageView);

        }

    }

    private void updateCheckedButton() {

        if (this.mediaItem != null) {

            this.checkedImageButton.setImageDrawable(
                    ContextCompat.getDrawable(this.context, R.drawable.ic_check_white_24dp)
            );

            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(GradientDrawable.OVAL);
            gradientDrawable.setColor(ContextCompat.getColor(this.context, R.color.success_primary));

            this.checkedImageButton.setBackground(gradientDrawable);

            if (this.mediaItem.isChecked()) {

                this.checkedImageButton.setVisibility(View.VISIBLE);

            } else {

                this.checkedImageButton.setVisibility(View.GONE);

            }

        } else {

            this.checkedImageButton.setVisibility(View.GONE);

        }

    }

    private void updateMediaTypeButton() {

        if (this.mediaItem != null) {


            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(GradientDrawable.OVAL);
            gradientDrawable.setColor(ContextCompat.getColor(this.context, R.color.primary_color));

            this.mediaTypeButtonImageButton.setBackground(gradientDrawable);

            switch (this.mediaItem.getType()) {

                case IMAGE_TYPE:
                    this.mediaTypeButtonImageButton.setImageDrawable(
                            ContextCompat.getDrawable(this.context, R.drawable.ic_image_white_16dp)
                    );
                    this.mediaTypeButtonImageButton.setVisibility(View.VISIBLE);
                    break;
                case VIDEO_TYPE:
                    this.mediaTypeButtonImageButton.setImageDrawable(
                            ContextCompat.getDrawable(this.context, R.drawable.ic_play_arrow_white_16dp)
                    );
                    this.mediaTypeButtonImageButton.setVisibility(View.VISIBLE);
                    break;
                case NO_TYPE:
                    this.mediaTypeButtonImageButton.setVisibility(View.GONE);
                    break;
                default:
                    this.mediaTypeButtonImageButton.setVisibility(View.GONE);
                    break;

            }

        } else {

            this.mediaTypeButtonImageButton.setVisibility(View.GONE);

        }

    }


}
