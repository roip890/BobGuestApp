package com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.listeners.MediaGalleryItemEventsListener;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.viewholders.MediaGalleryBasePreviewViewHolder;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.viewholders.MediaGalleryImagePreviewViewHolder;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.viewholders.MediaGalleryTextPreviewViewHolder;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.viewholders.MediaGalleryVideoPreviewViewHolder;
import com.bob.toolsmodule.enums.MediaItemType;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.uimodule.image.cropper.CropperView;

import java.util.List;

import im.ene.toro.widget.PressablePlayerSelector;

public class MediaGalleryPreviewAdapter extends RecyclerView.Adapter<MediaGalleryBasePreviewViewHolder> {

    private static final int PREVIEW_NO_TYPE = -1;
    private static final int PREVIEW_TYPE_TEXT = 0;
    private static final int PREVIEW_TYPE_VIDEO = 1;
    private static final int PREVIEW_TYPE_IMAGE = 2;

    @Nullable
    private final PressablePlayerSelector selector;

    //context
    private Context context;

    private MediaGalleryItemEventsListener mediaGalleryItemEventsListener;

    //media items
    private List<MediaItem> mediaItems;

    private CropperView.GridCallback onChildTouchListener;

    public MediaGalleryPreviewAdapter(Context context, @Nullable PressablePlayerSelector selector, List<MediaItem> mediaItems, MediaGalleryItemEventsListener mediaGalleryItemEventsListener) {

        this.context = context;

        this.mediaGalleryItemEventsListener = mediaGalleryItemEventsListener;

        this.selector = selector;

        this.mediaItems = mediaItems;

    }

    @NonNull
    @Override
    public MediaGalleryBasePreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case PREVIEW_TYPE_VIDEO:
                return new MediaGalleryVideoPreviewViewHolder(this.context,
                        this.selector,
                        layoutInflater.inflate(R.layout.view_holder_choose_from_gallery_video_preview, parent, false),
                        this.mediaGalleryItemEventsListener);
            case PREVIEW_TYPE_IMAGE:
                return new MediaGalleryImagePreviewViewHolder(this.context,
                        layoutInflater.inflate(R.layout.view_holder_choose_from_gallery_image_preview, parent, false),
                        this.mediaGalleryItemEventsListener);
            default:
                return new MediaGalleryTextPreviewViewHolder(this.context,
                        layoutInflater.inflate(R.layout.view_holder_choose_from_gallery_text_preview, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MediaGalleryBasePreviewViewHolder mediaGalleryBasePreviewViewHolder, int position) {

        mediaGalleryBasePreviewViewHolder.setMediaItem(position, this.mediaItems.get(position));

        if (mediaGalleryBasePreviewViewHolder instanceof MediaGalleryImagePreviewViewHolder) {

            if (this.onChildTouchListener != null) {

                ((MediaGalleryImagePreviewViewHolder)mediaGalleryBasePreviewViewHolder).setOnCropperGridCallbackListener(this.onChildTouchListener);

            }

        }

    }

    @Override
    public int getItemViewType(int position) {

        if (position < this.mediaItems.size() && this.mediaItems.get(position) != null) {

            if (this.mediaItems.get(position).getType() == MediaItemType.VIDEO_TYPE) {

                return PREVIEW_TYPE_VIDEO;

            } else if (this.mediaItems.get(position).getType() == MediaItemType.IMAGE_TYPE) {

                return PREVIEW_TYPE_IMAGE;

            } else {

                return PREVIEW_NO_TYPE;

            }

        } else {

            return PREVIEW_NO_TYPE;

        }

    }

    @Override
    public int getItemCount() {

        return this.mediaItems.size();

    }

    public List<MediaItem> getMediaItems() {
        return this.mediaItems;
    }

    public void setOnCropperGridCallbackListener(CropperView.GridCallback onChildTouchListener) {
        this.onChildTouchListener = onChildTouchListener;
    }
}
