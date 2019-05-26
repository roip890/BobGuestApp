package com.bob.bobguestapp.activities.main.fragments.profile.changeprofilepicture;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.tools.files.PathUtilsManager;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementViewContainer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class ChangeProfilePictureBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private final static int TAKE_PICTURE_OPTION = 0;
    private final static int CHOOSE_FROM_GALLERY_OPTION = 1;

    //change profile picture bottom sheet dialog
    private BottomSheetDialog changeProfilePictureBottomSheetDialog;
    private BottomSheetBehavior bottomSheetBehavior;
    private OnChangeProfilePictureListener onChangeProfilePictureListener;

    public ChangeProfilePictureBottomSheetDialogFragment() {

    }

    public static ChangeProfilePictureBottomSheetDialogFragment newInstance() {
        ChangeProfilePictureBottomSheetDialogFragment frag = new ChangeProfilePictureBottomSheetDialogFragment();
        Bundle argsBundle = new Bundle();
        frag.setArguments(argsBundle);
        return frag;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        ChangeProfilePictureBottomSheetDialogView changeProfilePictureBottomSheetDialogView = new ChangeProfilePictureBottomSheetDialogView(this.getContext());

        //init listener
        this.initOnChangeProfilePictureChangingOptionListener(changeProfilePictureBottomSheetDialogView);

        //init dialog
        this.initChangeProfilePictureBottomSheetDialog(changeProfilePictureBottomSheetDialogView);

        //init bottom sheet behavior
        this.initBottomSheetBehavior(changeProfilePictureBottomSheetDialogView);

        //set dialog round corners
        this.setDialogRoundCorners();



//        if (bottomSheet != null) {
//            BottomSheetBehavior.from(bottomSheet)
//                    .setState(BottomSheetBehavior.STATE_EXPANDED);
//        }


        return this.changeProfilePictureBottomSheetDialog;

    }

    //dialog
    protected void initOnChangeProfilePictureChangingOptionListener(ChangeProfilePictureBottomSheetDialogView changeProfilePictureBottomSheetDialogView) {

        changeProfilePictureBottomSheetDialogView.setOnChangeProfilePictureChangingOptionListener(new OnChangeProfilePictureChangingOptionListener() {
            @Override
            protected void onProfilePictureChangingOptionChoose(ProfilePictureChangingOption profilePictureChangingOption) {

                if (profilePictureChangingOption.getTitle().equals("Take Picture")) {

                    if (ChangeProfilePictureBottomSheetDialogFragment.this.hasTakePicturePermissions()) {
                        ChangeProfilePictureBottomSheetDialogFragment.this.takePic();
                    } else {
                        ActivityCompat.requestPermissions(ChangeProfilePictureBottomSheetDialogFragment.this.getActivity(),
                                new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                TAKE_PICTURE_OPTION);
                    }

                } else if (profilePictureChangingOption.getTitle().equals("From Gallery")) {
                    Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    ChangeProfilePictureBottomSheetDialogFragment.this.startActivityForResult(pickPhotoIntent , CHOOSE_FROM_GALLERY_OPTION);
                }

            }
        });

    }

    private void initChangeProfilePictureBottomSheetDialog(View view) {

        this.changeProfilePictureBottomSheetDialog = new BottomSheetDialog(this.getContext());
        this.changeProfilePictureBottomSheetDialog.setContentView(view);

    }

    private void initBottomSheetBehavior(View view) {

        this.bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    ChangeProfilePictureBottomSheetDialogFragment.this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        bottomSheetBehavior.setPeekHeight(320);

    }

    private void setDialogRoundCorners() {

        FrameLayout bottomSheet = (FrameLayout) this.changeProfilePictureBottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        GradientDrawable shapeDrawable = new GradientDrawable();
        shapeDrawable.setColor(MyAppThemeUtilsManager.get(this.getContext()).getColor(MyAppThemeUtilsManager.DEFAULT_DIALOG_BACKGROUND_COLOR_PRIMARY, MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN));
        shapeDrawable.setCornerRadii(new float[] {
                UIUtilsManager.get().convertDpToPixels(this.getContext(), 10),
                UIUtilsManager.get().convertDpToPixels(this.getContext(), 10),
                UIUtilsManager.get().convertDpToPixels(this.getContext(), 10),
                UIUtilsManager.get().convertDpToPixels(this.getContext(), 10),
                0,
                0,
                0,
                0,
        });

        if (bottomSheet != null) {
            bottomSheet.setBackground(shapeDrawable);
        }


    }

    public void setOnChangeProfilePictureListener(OnChangeProfilePictureListener onChangeProfilePictureListener) {
        this.onChangeProfilePictureListener = onChangeProfilePictureListener;
    }

    //permissions
    private boolean hasTakePicturePermissions() {
        if (ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case TAKE_PICTURE_OPTION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //takePicture.putExtra(MediaStore.EXTRA_OUTPUT, "profile.jpg");
                    ChangeProfilePictureBottomSheetDialogFragment.this.startActivityForResult(takePictureIntent, TAKE_PICTURE_OPTION);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case TAKE_PICTURE_OPTION:
                if(resultCode == RESULT_OK){
                    try {
                        if (this.createFolderIfNotExist("BobGuest") && this.createFolderIfNotExist("BobGuest"   + File.separator + "Images")) {
                            int guestId = BOBGuestApplication.get().getSecureSharedPreferences().getInt("guestId", -1);
                            if (guestId != -1) {
                                File file = new File(Environment.getExternalStorageDirectory()  + File.separator + "BobGuest" + File.separator + "Images", "/profile_" + Integer.toString(guestId) + ".png");
                                Uri imageUri = FileProvider.getUriForFile(
                                        this.getContext(),
                                        this.getContext().getApplicationContext().getPackageName() + ".com.bob.bobguestapp.provider",
                                        file);

                                Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                                        this.getContext().getContentResolver(), imageUri);
                                if (ChangeProfilePictureBottomSheetDialogFragment.this.onChangeProfilePictureListener != null) {
                                    this.onChangeProfilePictureListener.onProfilePictureChange(
                                            this.modifyOrientation(
                                                    thumbnail,
                                                    file.getAbsolutePath()
                                            ),
                                            file.getAbsolutePath()
                                    );
                                }

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_FROM_GALLERY_OPTION:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        if (ChangeProfilePictureBottomSheetDialogFragment.this.onChangeProfilePictureListener != null) {
                            try {
                                this.onChangeProfilePictureListener.onProfilePictureChange(
                                        this.modifyOrientation(
                                        MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), selectedImage),
                                        PathUtilsManager.getPath(this.getContext(), selectedImage)
                                        ),
                                        PathUtilsManager.getPath(this.getContext(), selectedImage)
                                    );
                            } catch (IOException E) {
                                E.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private boolean createFolderIfNotExist(String path) {
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + path);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void takePic() {
        if (this.createFolderIfNotExist("BobGuest") && this.createFolderIfNotExist("BobGuest"   + File.separator + "Images")) {
            int guestId = BOBGuestApplication.get().getSecureSharedPreferences().getInt("guestId", -1);
            if (guestId != -1) {
                File file = new File(Environment.getExternalStorageDirectory()  + File.separator + "BobGuest" + File.separator + "Images", "/profile_" + Integer.toString(guestId) + ".png");
                Uri imageUri = FileProvider.getUriForFile(
                        this.getContext(),
                        this.getContext().getApplicationContext().getPackageName() + ".com.bob.bobguestapp.provider",
                        file);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                this.startActivityForResult(intent, TAKE_PICTURE_OPTION);
            }
        }
    }

    private Bitmap modifyOrientation(Bitmap bitmap, String imageAbsolutePath) throws IOException {
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

}
