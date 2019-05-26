package com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerygrid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.R;
import com.bob.toolsmodule.objects.MediaItem;

import java.util.List;

public class MediaGalleryAdapter extends RecyclerView.Adapter<MediaGalleryViewHolder>{

//    private List<String> bitmapList;
//    private List<Boolean> selected;
    private Context context;
    private List<MediaItem> mediaItems;

    public MediaGalleryAdapter(Context context, List<MediaItem> mediaItems) {

        this.context = context;

        this.mediaItems = mediaItems;

    }

    @NonNull
    @Override
    public MediaGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        return new MediaGalleryViewHolder(this.context, inflater.inflate(R.layout.list_item_media_item, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(MediaGalleryViewHolder holder, int position) {

        if (holder != null
                && position < this.mediaItems.size()
                && this.mediaItems.get(position) != null) {

            holder.setMediaItem(this.mediaItems.get(position));

        }

    }

    @Override
    public int getItemCount() {
        return this.mediaItems.size();
    }

    public List<MediaItem> getMediaItems() {

        return this.mediaItems;

    }

    public void setMediaItems(List<MediaItem> mediaItems) {

        this.mediaItems = mediaItems;

        this.notifyDataSetChanged();

    }
}

