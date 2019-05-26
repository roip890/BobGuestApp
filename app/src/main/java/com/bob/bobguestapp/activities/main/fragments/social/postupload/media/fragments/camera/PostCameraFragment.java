package com.bob.bobguestapp.activities.main.fragments.social.postupload.media.fragments.camera;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.postupload.media.connector.PostMediaConnector;
import com.bob.toolsmodule.enums.MediaItemType;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.uimodule.drawable.DrawableHelper;
import com.bob.uimodule.image.bitmap.BitmapUtilsManager;
import com.bob.uimodule.theme.ThemeUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementFragment;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.Mode;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class PostCameraFragment extends ManagementFragment {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //create wish url
    private static final String CREATE_REQUEST_URL = BOB_SERVER_MOBILE_SERVICES_URL + "/wishes/create";

    //main view screen states
    public static final int CAMERA = 10;

    //post media commands
    private PostMediaConnector postMediaConnector;

    //camera layout
    private ConstraintLayout postCameraLayout;

    //camera view
    private CameraView cameraView;

    //cover view
    private View cameraCoverView;

    //camera controls
    private ConstraintLayout cameraControlsLayout;
    private ImageView cameraControlsFlashlightButton;
    private ImageView cameraControlsFacingButton;
    private ImageView cameraControlsPhotoButton;
    private ImageView cameraControlsVideoButton;
    private View.OnTouchListener onTouchCameraControlsFlashlightButtonListener;
    private View.OnTouchListener onTouchCameraControlsFacingButtonListener;
    private View.OnTouchListener onTouchCameraControlsPhotoButtonListener;
    private View.OnTouchListener onTouchCameraControlsVideoButtonListener;
    private Animation touchDownAnimation;
    private Animation touchUpAnimation;
    private Animation changeIconAnimation;
    private Animation cameraCoverAnimation;

    private long captureStartTime;




    public PostCameraFragment() {

        //theme
        this.appTheme = ThemeUtilsManager.DEFAULT_THEME;
        this.screenSkin = ThemeUtilsManager.PRIMARY_COLOR_SKIN;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        this.managementViewContainer.setScreenState(CAMERA);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        this.cameraView.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.cameraView.close();
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        this.cameraView.destroy();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.cameraView.destroy();
    }


    @Override
    public View onCreateMainView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //init management skin
        this.managementViewContainer.setScreenSkin(this.screenSkin);

        //view
        View view = inflater.inflate(R.layout.fragment_post_camera, container, false);

        //init views
        this.initViews(view);

        this.initAnimations();

        return view;

    }

    //screen state
    @Override
    protected void setMainViewScreenState(int screenState) {

        this.postCameraLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case CAMERA:
                this.postCameraLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    //post commands
    public void setPostMediaConnector(PostMediaConnector postMediaConnector) {
        this.postMediaConnector = postMediaConnector;
    }

    //post gallery
    private void initViews(View view) {

        this.postCameraLayout = (ConstraintLayout) view.findViewById(R.id.post_camera_fragment_background);

        this.initBackgroundLayout(view);

    }

    private void initBackgroundLayout(View view) {

        this.postCameraLayout = (ConstraintLayout) view.findViewById(R.id.post_camera_fragment_background);

        this.postCameraLayout.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.primary_color));

        this.initCameraView(view);

        this.initCameraCoverView(view);

        this.initCameraControlsLayout(view);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCameraView(View view) {

        this.cameraView = (CameraView) view.findViewById(R.id.post_camera_fragment_header_camera_view);

        this.cameraView.setFacing(Facing.BACK);

        this.cameraView.setFlash(Flash.OFF);

        this.cameraView.setLifecycleOwner(getViewLifecycleOwner());

        this.cameraView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {

                boolean onTouchResult = PostCameraFragment.this.cameraView.onTouchEvent(motionEvent);

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN: {

                        PostCameraFragment.this.postMediaConnector.disableViewPagerSwipe(true);

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        PostCameraFragment.this.postMediaConnector.disableViewPagerSwipe(false);

                        break;
                    }
                }
                return onTouchResult;
            }
        });

    }

    private void initCameraCoverView(View view) {

        this.cameraCoverView = (View) view.findViewById(R.id.post_camera_fragment_header_cover_view);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColors(new int[]{
                ContextCompat.getColor(this.getContext(), R.color.primary_color),
                ContextCompat.getColor(this.getContext(), R.color.secondary_color)
        });
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        this.cameraCoverView.setBackground(gradientDrawable);

        this.cameraCoverView.setVisibility(View.INVISIBLE);

    }

    private void initCameraControlsLayout(View view) {

        this.cameraControlsLayout = (ConstraintLayout) view.findViewById(R.id.post_camera_fragment_controls_layout);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColors(new int[]{
                ContextCompat.getColor(this.getContext(), R.color.secondary_color),
                ContextCompat.getColor(this.getContext(), R.color.primary_color)
        });
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        this.cameraControlsLayout.setBackground(gradientDrawable);

//        this.cameraControlsLayout.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.primary_color));

        this.initCameraControlsFlashlightButton(view);

        this.initCameraControlsFacingButton(view);

        this.initCameraControlsPhotoButton(view);

        this.initCameraControlsVideoButton(view);

    }

    private void initCameraControlsFlashlightButton(View view) {

        this.cameraControlsFlashlightButton = (ImageView) view.findViewById(R.id.post_camera_fragment_controls_flashlight_button);

        Drawable flashlightOnDrawable = DrawableHelper.withContext(this.getContext())
                .withDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_flash_on_white_24dp))
                .withColor(ContextCompat.getColor(this.getContext(), R.color.light_primary_color))
                .tint()
                .get();

        this.cameraControlsFlashlightButton.setImageDrawable(flashlightOnDrawable);

        this.initCameraControlsFlashlightButtonOnTouchListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCameraControlsFlashlightButtonOnTouchListener() {

        this.onTouchCameraControlsFlashlightButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_UP: {

                        PostCameraFragment.this.onCameraControlsFlashlightButtonClick();
                        break;

                    }

                }

                return true;
            }

        };

        this.cameraControlsFlashlightButton.setOnTouchListener(this.onTouchCameraControlsFlashlightButtonListener);

    }

    private void onCameraControlsFlashlightButtonClick() {

        PostCameraFragment.this.toggleCameraFlashlight();

    }

    private void initCameraControlsFacingButton(View view) {

        this.cameraControlsFacingButton = (ImageView) view.findViewById(R.id.post_camera_fragment_controls_facing_button);

        Drawable facingFrontDrawable = DrawableHelper.withContext(this.getContext())
                .withDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_facing_front))
                .withColor(ContextCompat.getColor(this.getContext(), R.color.light_primary_color))
                .tint()
                .get();

        this.cameraControlsFacingButton.setImageDrawable(facingFrontDrawable);

        this.initCameraControlsFacingButtonOnTouchListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCameraControlsFacingButtonOnTouchListener() {

        this.onTouchCameraControlsFacingButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP: {

                        PostCameraFragment.this.onCameraControlsFacingButtonClick();

                        break;

                    }

                }

                return true;
            }

        };

        this.cameraControlsFacingButton.setOnTouchListener(this.onTouchCameraControlsFacingButtonListener);

    }

    private void onCameraControlsFacingButtonClick() {

        PostCameraFragment.this.cameraCoverView.startAnimation(PostCameraFragment.this.cameraCoverAnimation);

    }

    private void initCameraControlsPhotoButton(View view) {

        this.cameraControlsPhotoButton = (ImageView) view.findViewById(R.id.post_camera_fragment_controls_image_button);

        Drawable capturePhotoDrawable = DrawableHelper.withContext(this.getContext())
                .withDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_circle_green_24dp))
                .withColor(ContextCompat.getColor(this.getContext(), R.color.light_primary_color))
                .tint()
                .get();

        this.cameraControlsPhotoButton.setImageDrawable(capturePhotoDrawable);

        this.initCameraControlsPhotoButtonOnTouchListener();

        this.initOnPhotoCaptureListener();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCameraControlsPhotoButtonOnTouchListener() {

        this.onTouchCameraControlsPhotoButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostCameraFragment.this.touchDownAnimation);

                        if (!PostCameraFragment.this.cameraView.isTakingPicture()
                                && !PostCameraFragment.this.cameraView.isTakingVideo()) {

                            PostCameraFragment.this.cameraView.setMode(Mode.PICTURE);

                            PostCameraFragment.this.captureStartTime = System.currentTimeMillis();

                            PostCameraFragment.this.cameraView.takePicture();

                        }

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        view.startAnimation(PostCameraFragment.this.touchUpAnimation);

                        break;
                    }
                }
                return true;
            }
        };

        this.cameraControlsPhotoButton.setOnTouchListener(this.onTouchCameraControlsPhotoButtonListener);

    }

    private void initOnPhotoCaptureListener() {

        this.cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(PictureResult result) {

                PostCameraFragment.this.handleCapturedImage(result);

            }
        });

    }

    private void initCameraControlsVideoButton(View view) {

        this.cameraControlsVideoButton = (ImageView) view.findViewById(R.id.post_camera_fragment_controls_video_button);



        Drawable captureVideoDrawable = DrawableHelper.withContext(this.getContext())
                .withDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_circle_red_24dp))
                .withColor(ContextCompat.getColor(this.getContext(), R.color.lava_red))
                .tint()
                .get();

        this.cameraControlsVideoButton.setImageDrawable(captureVideoDrawable);

        this.initCameraControlsVideoButtonOnTouchListener();

        this.initOnVideoCaptureListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCameraControlsVideoButtonOnTouchListener() {

        this.onTouchCameraControlsVideoButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {

                    case MotionEvent.ACTION_DOWN: {

                        view.startAnimation(PostCameraFragment.this.touchDownAnimation);

                        if (!PostCameraFragment.this.cameraView.isTakingPicture()
                                && !PostCameraFragment.this.cameraView.isTakingVideo()) {

                            PostCameraFragment.this.cameraView.setMode(Mode.VIDEO);

//                            cameraView.takeVideo(
//                                    new File(BOBGuestApplication.get().getExternalFilesDir(null)
//                                            + File.separator
//                                            + "BobSample"
//                                            + File.separator
//                                            + "Videos",
//                                            "/sample_video"
//                                                    + Long.toString(System.currentTimeMillis())
//                                                    + ".mp4"),
//                                    60000
//                            );

                            String videoUrl = Environment.getExternalStorageDirectory()
                                    + "/" + Long.toString(System.currentTimeMillis())
                                    + ".mp4";

                            PostCameraFragment.this.cameraView.takeVideo(new File(videoUrl));

                        }

                        break;
                    }

                    case MotionEvent.ACTION_UP: {

                        if (PostCameraFragment.this.cameraView.isTakingVideo()) {

                            PostCameraFragment.this.cameraView.stopVideo();

                        }

                        view.startAnimation(PostCameraFragment.this.touchUpAnimation);

                    }
                }
                return true;
            }
        };

        this.cameraControlsVideoButton.setOnTouchListener(this.onTouchCameraControlsVideoButtonListener);

    }

    private void initOnVideoCaptureListener() {

        this.cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onVideoTaken(VideoResult result) {

                PostCameraFragment.this.handleCapturedVideo(result);

            }
        });

    }

    //temp
    public void handleCapturedImage(PictureResult result) {

        if (this.cameraView.isTakingVideo()) {
            return;
        }

        // This can happen if picture was taken with a gesture.
        long callbackTime = System.currentTimeMillis();
        if (this.captureStartTime == 0) this.captureStartTime = callbackTime - 300;

        result.toBitmap(new BitmapCallback() {
            @Override
            public void onBitmapReady(Bitmap bitmap) {

                ArrayList<MediaItem> mediaItems = new ArrayList<MediaItem>();

                try {
                    String imageUrl = Environment.getExternalStorageDirectory()
                            + "/" + Long.toString(System.currentTimeMillis())
                            + ".png";

                    BitmapUtilsManager.writeBitmapToFile(bitmap, new File(imageUrl), 90);

                    mediaItems.add(new MediaItem(imageUrl , MediaItemType.IMAGE_TYPE, true));

                } catch (IOException e) {

                    e.printStackTrace();

                }

                PostCameraFragment.this.postMediaConnector.continueToContentActivity(
                        (ArrayList<MediaItem>) mediaItems
                );

            }
        });

        this.captureStartTime = 0;

//        PicturePreviewActivity.setPictureResult(result);
//        Intent intent = new Intent(PostCameraFragment.this.getContext(), PicturePreviewActivity.class);
//        intent.putExtra("delay", callbackTime - this.captureStartTime);
//        startActivity(intent);
//        this.captureStartTime = 0;

//        byte[] jpeg = result.getData();
//
//        long callbackTime = System.currentTimeMillis();
//        ResultHolder.dispose();
//        ResultHolder.setImage(jpeg);
//        ResultHolder.setNativeCaptureSize(this.cameraView.getPictureSize());
//        ResultHolder.setTimeToCallback(callbackTime - captureStartTime);
//        Intent intent = new Intent(this, PreviewActivity.class);
//        this.startActivity(intent);

    }

    public void handleCapturedVideo(VideoResult result) {

        ArrayList<MediaItem> mediaItems = new ArrayList<MediaItem>();

        mediaItems.add(new MediaItem(result.getFile().getAbsolutePath() , MediaItemType.VIDEO_TYPE, true));

        PostCameraFragment.this.postMediaConnector.continueToContentActivity(
                (ArrayList<MediaItem>) mediaItems
        );



//        VideoPreviewActivity.setVideoResult(result);
//        Intent intent = new Intent(PostCameraFragment.this.getContext(), VideoPreviewActivity.class);
//        startActivity(intent);

        //        File videoFile = video.getVideoFile();
//        if (videoFile != null) {
//            ResultHolder.dispose();
//            ResultHolder.setVideo(videoFile);
//            ResultHolder.setNativeCaptureSize(cameraView.getCaptureSize());
//            Intent intent = new Intent(this, PreviewActivity.class);
//            this.startActivity(intent);
//        }

    }

    //tools
    private void toggleCameraFlashlight() {

        if (this.cameraView.getFacing() == Facing.FRONT) {

            this.cameraView.setFlash(Flash.OFF);

        } else {

            switch (this.cameraView.getFlash()) {

                case OFF:
                    this.cameraView.setFlash(Flash.ON);
                    break;
                case ON:
                    this.cameraView.setFlash(Flash.AUTO);
                    break;
                case AUTO:
                    this.cameraView.setFlash(Flash.OFF);
                    break;
                default:
                    this.cameraView.setFlash(Flash.OFF);
                    break;

            }

        }

        switch (this.cameraView.getFlash()) {

            case OFF:
                this.changeIconWithAnimation(this.cameraControlsFlashlightButton, DrawableHelper.withContext(this.getContext())
                        .withDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_flash_off_white_24dp))
                        .withColor(ContextCompat.getColor(this.getContext(), R.color.light_primary_color))
                        .tint()
                        .get());
                break;
            case ON:
                this.changeIconWithAnimation(this.cameraControlsFlashlightButton, DrawableHelper.withContext(this.getContext())
                        .withDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_flash_on_white_24dp))
                        .withColor(ContextCompat.getColor(this.getContext(), R.color.light_primary_color))
                        .tint()
                        .get());
                break;
            case AUTO:
                this.changeIconWithAnimation(this.cameraControlsFlashlightButton, DrawableHelper.withContext(this.getContext())
                        .withDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_flash_auto_white_24dp))
                        .withColor(ContextCompat.getColor(this.getContext(), R.color.light_primary_color))
                        .tint()
                        .get());
                break;
            default:
                this.changeIconWithAnimation(this.cameraControlsFlashlightButton, DrawableHelper.withContext(this.getContext())
                        .withDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_flash_off_white_24dp))
                        .withColor(ContextCompat.getColor(this.getContext(), R.color.light_primary_color))
                        .tint()
                        .get());
                break;

        }


    }

    private void toggleCameraFacing() {

        if (this.cameraView.isTakingPicture() || this.cameraView.isTakingVideo()) return;
        switch (this.cameraView.getFacing()) {

            case BACK:

                this.cameraView.setFacing(Facing.FRONT);

                changeIconWithAnimation(this.cameraControlsFacingButton,
                        DrawableHelper.withContext(this.getContext())
                                .withDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_facing_back))
                                .withColor(ContextCompat.getColor(this.getContext(), R.color.light_primary_color))
                                .tint()
                                .get());

                this.cameraView.setFlash(Flash.OFF);
                this.changeIconWithAnimation(this.cameraControlsFlashlightButton, DrawableHelper.withContext(this.getContext())
                        .withDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_flash_off_white_24dp))
                        .withColor(ContextCompat.getColor(this.getContext(), R.color.light_primary_color))
                        .tint()
                        .get());

                break;

            case FRONT:

                this.cameraView.setFacing(Facing.BACK);

                changeIconWithAnimation(this.cameraControlsFacingButton,
                        DrawableHelper.withContext(this.getContext())
                                .withDrawable(ContextCompat.getDrawable(this.getContext(), R.drawable.ic_facing_front))
                                .withColor(ContextCompat.getColor(this.getContext(), R.color.light_primary_color))
                                .tint()
                                .get());

                break;

        }

    }

    //animations
    private void initAnimations() {

        this.initTouchDownAnimation();

        this.initTouchUpAnimation();

        this.initChangeIconAnimation();

        this.initCameraCoverAnimation();

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

    private void initChangeIconAnimation() {

        this.changeIconAnimation = new RotateAnimation(0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        this.changeIconAnimation.setDuration(400);
        this.changeIconAnimation.setInterpolator(new OvershootInterpolator());

    }

    private void initCameraCoverAnimation() {

        this.cameraCoverAnimation = new AlphaAnimation(0f, 1f);
        this.cameraCoverAnimation.setStartOffset(200);
        this.cameraCoverAnimation.setDuration(300);

        this.cameraCoverAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                if (PostCameraFragment.this.cameraCoverView != null) {

                    PostCameraFragment.this.cameraCoverView.setVisibility(VISIBLE);

                }

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                PostCameraFragment.this.toggleCameraFacing();

                Animation cameraCoverFadeOutAnimation = new AlphaAnimation(1f, 0f);
                cameraCoverFadeOutAnimation.setStartOffset(200);
                cameraCoverFadeOutAnimation.setDuration(300);
                cameraCoverFadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        PostCameraFragment.this.cameraCoverView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                PostCameraFragment.this.cameraCoverView.startAnimation(cameraCoverFadeOutAnimation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void changeIconWithAnimation(ImageView imageButton, Drawable drawableIcon) {

        imageButton.startAnimation(this.changeIconAnimation);

        imageButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageButton.setImageDrawable(drawableIcon);
            }
        }, 120);

    }

    //permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        for (int grantResult : grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
        }
        if (valid && !this.cameraView.isOpened()) {
            this.cameraView.open();
        }
    }


    //back pressed handler
    public void onBackPressed() {

    }

}
