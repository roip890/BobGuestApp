package com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.container;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import im.ene.toro.widget.Container;

public class MediaGalleryPreviewContainer extends Container {

    private boolean disableTouchEvents = false;

    //constructors
    public MediaGalleryPreviewContainer(Context context) {
        this(context, null);
    }

    public MediaGalleryPreviewContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaGalleryPreviewContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return !this.disableTouchEvents && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !this.disableTouchEvents && super.onTouchEvent(event);
    }

    public void disableSwipe(Boolean disableTouchEvents){
        this.disableTouchEvents = disableTouchEvents;
    }

}
