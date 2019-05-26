package com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.viewholders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.exifinterface.media.ExifInterface;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.gallery.mediagallerypreview.listeners.MediaGalleryItemEventsListener;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.drawable.DrawableUtilsManager;
import com.bob.uimodule.icons.Icons;
import com.bob.uimodule.image.bitmap.BitmapUtilsManager;
import com.bob.uimodule.image.cropper.BitmapResult;
import com.bob.uimodule.image.cropper.CropInfo;
import com.bob.uimodule.image.cropper.CropResult;
import com.bob.uimodule.image.cropper.CropState;
import com.bob.uimodule.image.cropper.CropperCallback;
import com.bob.uimodule.image.cropper.CropperView;
import com.bob.uimodule.image.cropper.ScaledCropper;
import com.mikepenz.iconics.IconicsDrawable;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MediaGalleryImagePreviewViewHolder extends MediaGalleryBasePreviewViewHolder {

    private static final String TAG = "SocialFeedPostsListMediaImageViewHolder";

    //context
    private Context context;

    private MediaGalleryItemEventsListener mediaGalleryItemEventsListener;

    //views
    private ConstraintLayout cropperLayout;
    private CropperView cropperView;
    private ImageButton cropperRotateButton;
    private View.OnTouchListener onTouchCropperRotateButtonListener;
    private ImageButton cropperSnapButton;
    private View.OnTouchListener onTouchCropperSnapButtonListener;
    private ImageButton cropperDeleteButton;
    private View.OnTouchListener onTouchCropperDeleteButtonListener;
    private CropperView.GridCallback onCropperGridCallbackListener;

    //bitmap
    private Bitmap selectedImageOriginalBitmap;
    private Bitmap selectedImageBitmap;
    private int selectedImageRotationCount = 0;
    private boolean isSelectedImageSnappedToCenter = false;

    //media
    private MediaItem mediaItem;

    //animations
    private Animation touchDownAnimation;
    private Animation touchUpAnimation;

    public MediaGalleryImagePreviewViewHolder(Context context, @NonNull View view, MediaGalleryItemEventsListener mediaGalleryItemEventsListener) {

        super(view);

        this.context = context;

        this.mediaGalleryItemEventsListener = mediaGalleryItemEventsListener;

        this.initViews(view);

        this.initAnimations();

    }

    private void initViews(View view) {

        this.initCropperLayout(view);

    }

    private void initCropperLayout(View view) {

        this.cropperLayout = (ConstraintLayout) view.findViewById(R.id.choose_from_gallery_image_preview_view_holder_cropper_layout);

        this.initCropperView(view);

        this.initCropperRotateButton(view);

        this.initCropperSnapButton(view);

        this.initCropperDeleteButton(view);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCropperView(View view) {

        this.cropperView = (CropperView) view.findViewById(R.id.choose_from_gallery_image_preview_view_holder_cropper_view);

        this.cropperView.setPaddingColor(ContextCompat.getColor(this.context, R.color.transparent));

        this.cropperView.setMakeSquare(false);

        this.cropperView.setGridCallback(new CropperView.GridCallback() {
            @Override
            public boolean onGestureStarted() {

                if (MediaGalleryImagePreviewViewHolder.this.onCropperGridCallbackListener != null) {

                    return MediaGalleryImagePreviewViewHolder.this.onCropperGridCallbackListener.onGestureStarted();

                }

                return true;

            }

            @Override
            public boolean onGestureCompleted() {

                if (MediaGalleryImagePreviewViewHolder.this.onCropperGridCallbackListener != null) {

                    return MediaGalleryImagePreviewViewHolder.this.onCropperGridCallbackListener.onGestureCompleted();

                }

                return false;
            }
        });

    }

    public void setOnCropperGridCallbackListener(CropperView.GridCallback onCropperGridCallbackListener) {
        this.onCropperGridCallbackListener = onCropperGridCallbackListener;
    }

    private void initCropperRotateButton(View view) {

        this.cropperRotateButton = (ImageButton) view.findViewById(R.id.choose_from_gallery_image_preview_view_holder_cropper_rotate_button);

        this.cropperRotateButton.setBackground(
                DrawableUtilsManager.get().getRoundSelectableDrawable(ContextCompat.getColor(this.context, R.color.primary_color))
        );

        ViewCompat.setElevation(
                this.cropperRotateButton,
                UIUtilsManager.get().convertDpToPixels(this.context, 10)
        );

        this.cropperRotateButton.setImageDrawable(
                this.context.getResources().getDrawable(R.drawable.ic_rotate_right_white_24dp)
        );

        this.initOnTouchCropperRotateButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchCropperRotateButtonListener() {

        this.onTouchCropperRotateButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(MediaGalleryImagePreviewViewHolder.this.touchDownAnimation);

                        MediaGalleryImagePreviewViewHolder.this.onImageRotateClicked();

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(MediaGalleryImagePreviewViewHolder.this.touchUpAnimation);

                        break;
                    }
                }
                return true;

            }
        };

        this.cropperRotateButton.setOnTouchListener(this.onTouchCropperRotateButtonListener);

    }

    public void onImageRotateClicked() {

        this.rotateImage();

    }

    private void initCropperSnapButton(View view) {

        this.cropperSnapButton = (ImageButton) view.findViewById(R.id.choose_from_gallery_image_preview_view_holder_cropper_snap_button);

        this.cropperSnapButton.setBackground(
                DrawableUtilsManager.get().getRoundSelectableDrawable(ContextCompat.getColor(this.context, R.color.primary_color))
        );

        ViewCompat.setElevation(
                this.cropperSnapButton,
                UIUtilsManager.get().convertDpToPixels(this.context, 10)
        );

        this.cropperSnapButton.setImageDrawable(
                this.context.getResources().getDrawable(R.drawable.ic_crop_free_white_24dp)
        );

        this.initOnTouchCropperSnapButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchCropperSnapButtonListener() {

        this.onTouchCropperSnapButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(MediaGalleryImagePreviewViewHolder.this.touchDownAnimation);

                        MediaGalleryImagePreviewViewHolder.this.onImageSnapClicked();

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(MediaGalleryImagePreviewViewHolder.this.touchUpAnimation);

                        break;
                    }
                }
                return true;

            }
        };

        this.cropperSnapButton.setOnTouchListener(this.onTouchCropperSnapButtonListener);

    }

    public void onImageSnapClicked() {

        this.snapImage();

    }

    private void initCropperDeleteButton(View view) {

        this.cropperDeleteButton = (ImageButton) view.findViewById(R.id.choose_from_gallery_image_preview_view_holder_cropper_delete_button);

        this.cropperDeleteButton.setBackground(
                DrawableUtilsManager.get().getRoundSelectableDrawable(ContextCompat.getColor(this.context, R.color.primary_color))
        );

        ViewCompat.setElevation(
                this.cropperDeleteButton,
                UIUtilsManager.get().convertDpToPixels(this.context, 10)
        );

        this.cropperDeleteButton.setImageDrawable(

                ((IconicsDrawable) Icons.get().findDrawable(
                        MediaGalleryImagePreviewViewHolder.this.context,
                        "gmd_close"))
                        .sizeDp(12).colorRes(R.color.light_primary_color)
        );

        this.initOnTouchCropperDeleteButtonListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initOnTouchCropperDeleteButtonListener() {

        this.onTouchCropperDeleteButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(MediaGalleryImagePreviewViewHolder.this.touchDownAnimation);

                        MediaGalleryImagePreviewViewHolder.this.onImageDeleteClicked();

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(MediaGalleryImagePreviewViewHolder.this.touchUpAnimation);

                        break;
                    }
                }
                return true;

            }
        };

        this.cropperDeleteButton.setOnTouchListener(this.onTouchCropperDeleteButtonListener);

    }

    public void onImageDeleteClicked() {

        if (this.mediaGalleryItemEventsListener != null
                && this.mediaItem != null) {
            this.mediaGalleryItemEventsListener.unCheckMediaItem(this.mediaItem);
        }

    }

    //toro
    @Override
    public void setMediaItem(int position, MediaItem mediaItem) {

        if (mediaItem != null) {

            this.mediaItem = mediaItem;

            this.itemView.setTag(this.mediaItem);

            if (this.mediaItem.getUrl() != null) {

                this.loadNewImageToCropperView(this.mediaItem.getUrl());

            }

        }

    }

    public MediaItem getMediaItem() {
        return this.mediaItem;
    }

    //tools
    //load new image to cropper view
    private void loadNewImageToCropperView(String filePath) {

        this.selectedImageRotationCount = 0;

        try {
            this.selectedImageBitmap = this.fixOrientation(BitmapFactory.decodeFile(filePath), filePath);
        } catch (IOException e) {
            e.printStackTrace();
            this.selectedImageBitmap = BitmapFactory.decodeFile(filePath);
        }


        this.selectedImageOriginalBitmap = this.selectedImageBitmap;

        if (this.cropperView.getWidth() != 0) {

            this.cropperView.setMaxZoom(10f);

        } else {

            ViewTreeObserver vto = this.cropperView.getViewTreeObserver();

            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    MediaGalleryImagePreviewViewHolder.this.cropperView.getViewTreeObserver().removeOnPreDrawListener(this);
                    MediaGalleryImagePreviewViewHolder.this.cropperView.setMaxZoom(10f);
                    return true;
                }

            });

        }

        this.cropperView.setImageBitmap(this.selectedImageBitmap);

    }

    //crop functions
    public Bitmap cropImage() {

        BitmapResult croppedBitmap = this.cropperView.getCroppedBitmap();

//        if (croppedBitmap != null && croppedBitmap.getBitmap() != null) {
//
//            try {
//                BitmapUtilsManager.writeBitmapToFile(croppedBitmap.getBitmap(), new File(Environment.getExternalStorageDirectory()
//                        + "/" + Long.toString(System.currentTimeMillis())
//                        + ".png"), 90);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//
//        }

        return croppedBitmap.getBitmap();

    }

    private void cropImageAsync() {

        CropState state = this.cropperView.getCroppedBitmapAsync(new CropperCallback() {
            @Override
            public void onCropped(Bitmap bitmap) {

                if (bitmap != null) {

                    try {
                        BitmapUtilsManager.writeBitmapToFile(bitmap, new File(Environment.getExternalStorageDirectory()
                                + Long.toString(System.currentTimeMillis())
                                + MediaGalleryImagePreviewViewHolder.this.getFileNameFromURL(MediaGalleryImagePreviewViewHolder.this.mediaItem.getUrl())), 90);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onOutOfMemoryError() {

            }
        });

        if (state == CropState.FAILURE_GESTURE_IN_PROCESS) {
            Toast.makeText(this.context, "unable to crop. Gesture in progress", Toast.LENGTH_SHORT).show();
        }

    }

    private void cropOriginalImageAsync() {

        if (this.selectedImageOriginalBitmap != null) {
            ScaledCropper cropper = prepareCropForOriginalImage();
            if (cropper == null) {
                return;
            }

            cropper.crop(new CropperCallback() {
                @Override
                public void onCropped(Bitmap bitmap) {
                    if (bitmap != null) {
                        try {
                            BitmapUtilsManager.writeBitmapToFile(bitmap, new File(BOBGuestApplication.get().getExternalFilesDir(null) + "/crop_test_info_orig.jpg"), 90);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

    }

    private ScaledCropper prepareCropForOriginalImage() {

        CropResult result = this.cropperView.getCropInfo();
        if (result.getCropInfo() == null) {
            return null;
        }

        float scale;
        if (this.selectedImageRotationCount % 2 == 0) {

            // same width and height
            scale = (float) this.selectedImageOriginalBitmap.getWidth() /
                    this.selectedImageBitmap.getWidth();

        } else {

            // width and height are interchanged
            scale = (float) this.selectedImageOriginalBitmap.getWidth() /
                    this.selectedImageBitmap.getHeight();

        }

        CropInfo cropInfo = result.getCropInfo().rotate90XTimes(this.selectedImageBitmap.getWidth(),
                this.selectedImageBitmap.getHeight(),
                this.selectedImageRotationCount);

        return new ScaledCropper(cropInfo, this.selectedImageOriginalBitmap, scale);

    }

    //rotate functions
    @SuppressLint("LongLogTag")
    private void rotateImage() {

        if (this.selectedImageBitmap == null) {
            Log.e(TAG, "bitmap is not loaded yet");
            return;
        }

        this.selectedImageBitmap = BitmapUtilsManager.rotateBitmap(this.selectedImageBitmap, 90);
        this.cropperView.setImageBitmap(this.selectedImageBitmap);
        this.selectedImageRotationCount++;

    }

    private Bitmap fixOrientation(Bitmap bitmap, String imageAbsolutePath) throws IOException {
        ExifInterface ei = new ExifInterface(imageAbsolutePath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    private Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //snap functions
    private void snapImage() {

        if (this.isSelectedImageSnappedToCenter) {
            this.cropperView.cropToCenter();
        } else {
            this.cropperView.fitToCenter();
        }

        this.isSelectedImageSnappedToCenter = !this.isSelectedImageSnappedToCenter;

    }

    //animations
    private void initAnimations() {

        this.initTouchDownAnimation();

        this.initTouchUpAnimation();

    }

    private void initTouchDownAnimation() {

        this.touchDownAnimation = new ScaleAnimation(1f, 0.80f, 1f, 0.80f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        this.touchDownAnimation.setDuration(300);
        this.touchDownAnimation.setInterpolator(new OvershootInterpolator());

    }

    private void initTouchUpAnimation() {

        this.touchUpAnimation = new ScaleAnimation(0.80f, 1f, 0.80f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        this.touchUpAnimation.setDuration(300);
        this.touchUpAnimation.setInterpolator(new OvershootInterpolator());

    }

    //file tools
    private String getFileNameFromURL(String url) {
        if (url == null) {
            return "";
        }
        try {
            URL resource = new URL(url);
            String host = resource.getHost();
            if (host.length() > 0 && url.endsWith(host)) {
                // handle ...example.com
                return "";
            }
        }
        catch(MalformedURLException e) {
            return "";
        }

        int startIndex = url.lastIndexOf('/') + 1;
        int length = url.length();

        // find end index for ?
        int lastQMPos = url.lastIndexOf('?');
        if (lastQMPos == -1) {
            lastQMPos = length;
        }

        // find end index for #
        int lastHashPos = url.lastIndexOf('#');
        if (lastHashPos == -1) {
            lastHashPos = length;
        }

        // calculate the end index
        int endIndex = Math.min(lastQMPos, lastHashPos);
        return url.substring(startIndex, endIndex);
    }


}