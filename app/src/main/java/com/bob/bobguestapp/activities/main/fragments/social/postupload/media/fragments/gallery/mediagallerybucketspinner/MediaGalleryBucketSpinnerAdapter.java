package com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerybucketspinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bob.bobguestapp.R;
import com.bob.toolsmodule.objects.MediaItemBucket;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MediaGalleryBucketSpinnerAdapter extends ArrayAdapter<MediaItemBucket> {

    //context
    private Context context;

    //header media iem bucket
    private MediaItemBucket currentHeaderMediaItemBucket;

    //dropdown views
    private ConstraintLayout headerMediaItemBucketBackgroundView;
    private TextView headerMediaItemBucketNameView;

    //dropdown media item bucket
    private MediaItemBucket currentDropdownMediaItemBucket;

    //dropdown views
    private ConstraintLayout dropdownMediaItemBucketBackgroundView;
    private ImageView dropdownMediaItemBucketImageView;
    private TextView dropdownMediaItemBucketNameView;

    public MediaGalleryBucketSpinnerAdapter(Context context, List<MediaItemBucket> mediaItemBuckets) {

        super(context, R.layout.list_item_media_bucket_spinner, mediaItemBuckets);

        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (position < this.getCount()
                && this.getItem(position) != null) {

            MediaItemBucket mediaItemBucket = getItem(position);

            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            View mediaItemBucketView = inflater.inflate(R.layout.list_item_media_bucket_spinner, viewGroup, false);


            this.initHeaderViews(mediaItemBucketView);

            this.bindHeaderMediaItemBucketOnView(mediaItemBucket);

            return mediaItemBucketView;

        } else {

            return null;

        }

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup viewGroup) {

        if (position < this.getCount()
                && this.getItem(position) != null) {

            MediaItemBucket mediaItemBucket = getItem(position);

            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            View mediaItemBucketView = inflater.inflate(R.layout.list_item_dropdown_media_bucket_spinner, viewGroup, false);

            this.initDropdownViews(mediaItemBucketView);

            this.bindDropdownMediaItemBucketOnView(mediaItemBucket);

            return mediaItemBucketView;

        } else {

            return null;

        }

    }

    //header
    private void initHeaderViews(View view) {

        this.initHeaderMediaBucketBackgroundView(view);

        this.initHeaderMediaBucketNameView(view);

    }

    private void initHeaderMediaBucketBackgroundView(View view) {

        this.headerMediaItemBucketBackgroundView = (ConstraintLayout) view.findViewById(R.id.media_bucket_list_item_background);

    }

    private void initHeaderMediaBucketNameView(View view) {

        this.headerMediaItemBucketNameView = (TextView) view.findViewById(R.id.media_bucket_list_item_name_view);

    }

    public void bindHeaderMediaItemBucketOnView(MediaItemBucket mediaItemBucket) {

        this.currentHeaderMediaItemBucket = mediaItemBucket;

        this.updateHeaderViews();

    }

    private void updateHeaderViews() {

        this.updateHeaderMediaBucketBackgroundView();

        this.updateHeaderMediaBucketNameView();

    }

    private void updateHeaderMediaBucketBackgroundView() {

        this.headerMediaItemBucketBackgroundView.setBackgroundColor(
                ContextCompat.getColor(this.context, R.color.primary_color)
        );

    }

    private void updateHeaderMediaBucketNameView() {

        if (this.currentHeaderMediaItemBucket != null && this.currentHeaderMediaItemBucket.getName() != null) {

            this.headerMediaItemBucketNameView.setText(this.currentHeaderMediaItemBucket.getName());

            this.headerMediaItemBucketNameView.setTextColor(
                    ContextCompat.getColor(this.context, R.color.light_primary_color)
            );

        } else {

            this.headerMediaItemBucketNameView.setText("Please Select");

            this.headerMediaItemBucketNameView.setTextColor(
                    ContextCompat.getColor(this.context, R.color.light_primary_color)
            );

        }

    }


    //dropdown
    private void initDropdownViews(View view) {

        this.initDropdownMediaBucketBackgroundView(view);

        this.initDropdownMediaBucketImageView(view);

        this.initDropdownMediaBucketNameView(view);

    }

    private void initDropdownMediaBucketBackgroundView(View view) {

        this.dropdownMediaItemBucketBackgroundView = (ConstraintLayout) view.findViewById(R.id.media_bucket_list_item_background);

    }

    private void initDropdownMediaBucketImageView(View view) {

        this.dropdownMediaItemBucketImageView = (ImageView) view.findViewById(R.id.media_bucket_list_item_image_view);

    }

    private void initDropdownMediaBucketNameView(View view) {

        this.dropdownMediaItemBucketNameView = (TextView) view.findViewById(R.id.media_bucket_list_item_name_view);

    }

    public void bindDropdownMediaItemBucketOnView(MediaItemBucket mediaItemBucket) {

        this.currentDropdownMediaItemBucket = mediaItemBucket;

        this.updateDropdownViews();

    }

    private void updateDropdownViews() {

        this.updateDropdownMediaBucketBackgroundView();

        this.updateDropdownMediaBucketImageView();

        this.updateDropdownMediaBucketNameView();

    }

    private void updateDropdownMediaBucketBackgroundView() {

        this.dropdownMediaItemBucketBackgroundView.setBackgroundColor(
                ContextCompat.getColor(this.context, R.color.primary_color)
        );
    }

    private void updateDropdownMediaBucketImageView() {

        if (this.currentDropdownMediaItemBucket != null && this.currentDropdownMediaItemBucket.getImagePath() != null) {

            Glide.with(this.context)
                    .asBitmap()
                    .load(this.currentDropdownMediaItemBucket.getImagePath())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .dontAnimate()
                            .skipMemoryCache(true))
//                    .transition(withCrossFade())
                    .into(this.dropdownMediaItemBucketImageView);

            this.dropdownMediaItemBucketImageView.setVisibility(View.VISIBLE);

        } else {

            this.dropdownMediaItemBucketImageView.setVisibility(View.GONE);

        }

    }

    private void updateDropdownMediaBucketNameView() {

        if (this.currentDropdownMediaItemBucket != null && this.currentDropdownMediaItemBucket.getName() != null) {

            this.dropdownMediaItemBucketNameView.setText(this.currentDropdownMediaItemBucket.getName());

            this.dropdownMediaItemBucketNameView.setTextColor(
                    ContextCompat.getColor(this.context, R.color.light_primary_color)
            );

            this.dropdownMediaItemBucketNameView.setVisibility(View.VISIBLE);

        } else {

            this.dropdownMediaItemBucketNameView.setVisibility(View.GONE);

        }

    }

}