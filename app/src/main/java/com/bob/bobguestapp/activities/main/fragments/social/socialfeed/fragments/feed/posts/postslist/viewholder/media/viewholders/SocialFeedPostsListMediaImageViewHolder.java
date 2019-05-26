package com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.viewholders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.container.OnMediaLoadListener;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.uimodule.image.ImageViewFullScreenDialogFragment;
import com.bob.uimodule.image.ImageViewFullScreenListener;
import com.bob.uimodule.video.exoplayer.ExoPlayerFullScreenDialogFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class SocialFeedPostsListMediaImageViewHolder extends SocialFeedPostsListMediaBaseViewHolder {

    private static final String TAG = "SocialFeedPostsListMediaImageViewHolder";

    //context
    private Context context;

    //fragment manager
    private FragmentManager fragmentManager;

    //on media load listener
    private OnMediaLoadListener onMediaLoadListener;

    //full screen listener
    private ImageViewFullScreenListener imageViewFullScreenListener;

    //views
    private ConstraintLayout imageLayout;
    private ImageView imageView;

    //media
    private MediaItem mediaItem;


    public SocialFeedPostsListMediaImageViewHolder(Context context, FragmentManager fragmentManager, OnMediaLoadListener onMediaLoadListener, @NonNull View view) {

        super(view);

        this.context = context;

        this.fragmentManager = fragmentManager;

        this.onMediaLoadListener = onMediaLoadListener;

        this.initImageViewFullScreenListener();

        this.initViews(view);

    }

    //full screen listener
    protected void initImageViewFullScreenListener() {

        this.imageViewFullScreenListener = new ImageViewFullScreenListener() {

            int orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;

            @Override
            public void startFullScreen() {

                if (SocialFeedPostsListMediaImageViewHolder.this.context instanceof Activity) {

                    this.orientation = ((Activity)SocialFeedPostsListMediaImageViewHolder.this.context).getRequestedOrientation();

                    ((Activity)SocialFeedPostsListMediaImageViewHolder.this.context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);

                }

            }

            @Override
            public void exitFullScreen() {

                if (SocialFeedPostsListMediaImageViewHolder.this.context instanceof Activity) {

                    ((Activity)SocialFeedPostsListMediaImageViewHolder.this.context).setRequestedOrientation(this.orientation);

                }

            }
        };

    }


    //views
    private void initViews(View view) {

        this.initImageViewLayout(view);

    }

    private void initImageViewLayout(View view) {

        this.imageLayout = (ConstraintLayout) view.findViewById(R.id.social_feed_posts_list_media_image_view_holder_cropper_layout);

        this.initImageView(view);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initImageView(View view) {

        this.imageView = (ImageView) view.findViewById(R.id.social_feed_posts_list_media_image_view_holder_cropper_view);

        this.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SocialFeedPostsListMediaImageViewHolder.this.imageView != null
                        && SocialFeedPostsListMediaImageViewHolder.this.mediaItem.getUrl() != null) {

                    ImageViewFullScreenDialogFragment fullScreenDialogFragment = ImageViewFullScreenDialogFragment.newInstance(
                            SocialFeedPostsListMediaImageViewHolder.this.mediaItem.getUrl()
                    );

                    fullScreenDialogFragment.setFullScreenListener(
                            SocialFeedPostsListMediaImageViewHolder.this.imageViewFullScreenListener
                    );

                    fullScreenDialogFragment.show(
                            SocialFeedPostsListMediaImageViewHolder.this.fragmentManager,
                            ExoPlayerFullScreenDialogFragment.TAG
                    );

                }

            }
        });

//        PhotoViewAttacher attacher = new PhotoViewAttacher(this.imageView);
//
//        attacher.setZoomable(true);

    }

    //toro
    @Override
    public void setMediaItem(int position, MediaItem mediaItem) {

        if (mediaItem != null) {

            this.mediaItem = mediaItem;

            if (this.mediaItem.getUrl() != null) {

                Glide.with(this.context)
                        .asBitmap()
                        .load(this.mediaItem.getUrl())
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                                if (SocialFeedPostsListMediaImageViewHolder.this.imageView != null
                                        && SocialFeedPostsListMediaImageViewHolder.this.imageView.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {

                                    ConstraintLayout.LayoutParams imageViewLayoutParams =
                                            ((ConstraintLayout.LayoutParams)SocialFeedPostsListMediaImageViewHolder.this.imageView.getLayoutParams());

                                    imageViewLayoutParams.dimensionRatio = "H," + resource.getWidth() + ":" + resource.getHeight();

                                    SocialFeedPostsListMediaImageViewHolder.this.imageView.setImageBitmap(resource);
                                }

                                return true;
                            }
                        })
                        .into(this.imageView);

            }

        }

    }

    @Override
    public void onAttachedToRecyclerView() {

    }

    @Override
    public void onDetachedToRecyclerView() {

    }

}
