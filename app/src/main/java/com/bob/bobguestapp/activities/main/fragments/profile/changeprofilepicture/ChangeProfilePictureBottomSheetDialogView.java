package com.bob.bobguestapp.activities.main.fragments.profile.changeprofilepicture;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.tools.files.PathUtilsManager;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.views.loadingcontainer.ManagementLayout;
import com.bob.uimodule.views.loadingcontainer.ManagementViewContainer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ChangeProfilePictureBottomSheetDialogView extends ManagementLayout {

    private final static int CHANGE_PROFILE_PICTURE_OPTIONS = 10;

    private final static int TAKE_PICTURE_OPTION = 0;
    private final static int CHOOSE_FROM_GALLERY_OPTION = 1;

    //change profile picture bottom sheet dialog
    private RelativeLayout changeProfilePictureBottomSheetDialogLayout;
    private RecyclerView changeProfilePictureBottomSheetDialogOptionsRecyclerView;
    private ProfilePictureChangingOptionAdapter profilePictureChangingOptionAdapter;
    private OnChangeProfilePictureListener onChangeProfilePictureListener;
    private OnChangeProfilePictureChangingOptionListener onChangeProfilePictureChangingOptionListener;

    //constructors
    public ChangeProfilePictureBottomSheetDialogView(Context context) {
        this(context, null);
    }

    public ChangeProfilePictureBottomSheetDialogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeProfilePictureBottomSheetDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.getContext()).getSkin(appTheme, MyAppThemeUtilsManager.PROFILE_FRAGMENT_CHANGE_PICTURE_BOTTOM_SHEET_SKIN);

        this.initMainView();

        this.setScreenState(CHANGE_PROFILE_PICTURE_OPTIONS);

    }

    protected View onCreateMainView() {

        return this.initChangeProfilePictureBottomSheetView();

    }

    protected void setMainViewScreenState(int screenState) {

        this.changeProfilePictureBottomSheetDialogLayout.setVisibility(INVISIBLE);

        switch (screenState) {

            case CHANGE_PROFILE_PICTURE_OPTIONS:
                this.changeProfilePictureBottomSheetDialogLayout.setVisibility(VISIBLE);
                break;
            default:
                break;

        }

    }

    private View initChangeProfilePictureBottomSheetView() {

        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View view = inflater.inflate(R.layout.dialog_bottom_sheet_change_profile_picture, new RelativeLayout(this.getContext()), false);

        this.changeProfilePictureBottomSheetDialogLayout = (RelativeLayout) view.findViewById(R.id.profile_change_picture_bottom_sheet_dialog_layout_background);

        this.initChangeProfilePictureBottomSheetBackground();

        this.initChangeProfilePictureBottomSheetRecyclerView(view);

        return view;

    }

    private void initChangeProfilePictureBottomSheetBackground() {

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

        this.changeProfilePictureBottomSheetDialogLayout.setBackground(shapeDrawable);

    }

    private void initChangeProfilePictureBottomSheetRecyclerView(View view) {

        this.changeProfilePictureBottomSheetDialogOptionsRecyclerView = (RecyclerView) view.findViewById(R.id.profile_change_picture_bottom_sheet_dialog_options_recycler_view);

        //init adapter
        this.initChangeProfilePictureBottomSheetAdapter();

        //set adapter
        this.setChangeProfilePictureBottomSheetAdapter();

//        this.changeProfilePictureBottomSheetDialogOptionsRecyclerView.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
//        this.changeProfilePictureBottomSheetDialogOptionsRecyclerView.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));
//        this.getBackgroundLayout().setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.transparent));


    }

    private void initChangeProfilePictureBottomSheetAdapter() {

        this.profilePictureChangingOptionAdapter = new ProfilePictureChangingOptionAdapter(this.getContext());

        ArrayList<ProfilePictureChangingOption> profilePictureChangingOptions = new ArrayList<ProfilePictureChangingOption>();

        //take picture
        profilePictureChangingOptions.add(new ProfilePictureChangingOption("Take Picture", R.drawable.profile_change_picture_option_take_picture_black_24dp));

        //from gallery
        profilePictureChangingOptions.add(new ProfilePictureChangingOption("From Gallery", R.drawable.profile_change_picture_option_from_gallery_black_24dp));

        this.profilePictureChangingOptionAdapter.setProfilePictureChangingOptions(profilePictureChangingOptions);

        //on click item listener
        this.initOnChangeProfilePictureChangingOptionListener();
    }

    private void setChangeProfilePictureBottomSheetAdapter() {

        this.changeProfilePictureBottomSheetDialogOptionsRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));

        this.changeProfilePictureBottomSheetDialogOptionsRecyclerView.setAdapter(profilePictureChangingOptionAdapter);

        this.profilePictureChangingOptionAdapter.notifyDataSetChanged();

    }

    private void initOnChangeProfilePictureChangingOptionListener() {

        if (this.onChangeProfilePictureChangingOptionListener != null) {

            this.profilePictureChangingOptionAdapter.setOnChangeProfilePictureChangingOptionListener(onChangeProfilePictureChangingOptionListener);

        }

    }

    public void setOnChangeProfilePictureChangingOptionListener(OnChangeProfilePictureChangingOptionListener onChangeProfilePictureChangingOptionListener) {

        this.onChangeProfilePictureChangingOptionListener = onChangeProfilePictureChangingOptionListener;

        if (this.profilePictureChangingOptionAdapter != null) {

            this.profilePictureChangingOptionAdapter.setOnChangeProfilePictureChangingOptionListener(onChangeProfilePictureChangingOptionListener);

        }

    }

}
