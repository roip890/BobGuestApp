package com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerygrid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MediaGalleryRecyclerView extends RecyclerView {

    protected GestureDetector gestureDetector;
    protected OnItemClickListener onItemClickListener;

    //constructors
    public MediaGalleryRecyclerView(Context context) {
        this(context, null);
    }

    public MediaGalleryRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaGalleryRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.init();
    }

    protected void init() {

        this.initGestureDetector();

        this.initOnItemTouchListener();

    }

    protected void initOnItemTouchListener() {

        this.addOnItemTouchListener(new OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && MediaGalleryRecyclerView.this.onItemClickListener != null
                        && MediaGalleryRecyclerView.this.gestureDetector.onTouchEvent(e)) {

                    MediaGalleryRecyclerView.this.onItemClickListener.onClick(child, rv.getChildAdapterPosition(child));
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    protected void initGestureDetector() {

        this.gestureDetector = new GestureDetector(this.getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {

                View child = MediaGalleryRecyclerView.this.findChildViewUnder(e.getX(), e.getY());

                if (child != null && MediaGalleryRecyclerView.this.onItemClickListener != null) {
                    MediaGalleryRecyclerView.this.onItemClickListener.onLongClick(
                            child, MediaGalleryRecyclerView.this.getChildAdapterPosition(child)
                    );
                }

            }
        });

    }

    public interface OnItemClickListener {

        void onClick(View view, int position);

        void onLongClick(View view, int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;

    }

}
