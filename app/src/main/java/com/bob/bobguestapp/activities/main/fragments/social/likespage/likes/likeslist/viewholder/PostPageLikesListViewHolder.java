package com.bob.bobguestapp.activities.main.fragments.social.likespage.likes.likeslist.viewholder;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.tools.database.objects.PostLike;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by User on 07/09/2018.
 */

public class PostPageLikesListViewHolder extends RecyclerView.ViewHolder {

    //http finals
    private static String BOB_SERVER_IP_ADDRESS = "159.65.87.128";
    private static String BOB_SERVER_USER_PORT = "8080";
    private static String BOB_SERVER_DESIGN_PORT = "3000";
    private static String BOB_SERVER_MOBILE_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/MobileAppServices/services";
    private static String BOB_SERVER_WEB_SERVICES_URL = "http://" + BOB_SERVER_IP_ADDRESS + ":"
            + BOB_SERVER_USER_PORT + "/WebAppServices/services";

    //get hotels url
    private static final String GET_HOTEL_BY_EMAIL = BOB_SERVER_MOBILE_SERVICES_URL + "/login/getHotelsAndIcons";
    private static final String GET_GUEST_BY_EMAIL = BOB_SERVER_MOBILE_SERVICES_URL + "/login/getHotelsAndIcons";


    //app theme
    private int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    private int screenSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;

    //context
    protected Context context;

    //views
    protected View view;
    protected ConstraintLayout likeBackgroundLayout;

    //profile picture
    protected ImageView editorProfilePictureImageView;

    //post content header layout
    protected TextView editorNameTextView;
    
    //post
    protected PostLike postPageLike;

    public PostPageLikesListViewHolder(Context context, View view) {
        super(view);

        this.context = context;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_SKIN);

        this.initView(view);
    }

    public void onResume() {


    }

    public void onPause() {


    }

    //general
    protected void initView(View view) {

        this.view = view;

        this.initLikeBackgroundLayout(view);

    }

    private void initLikeBackgroundLayout(View view) {

        this.likeBackgroundLayout = (ConstraintLayout) view.findViewById(R.id.post_page_like_view_holder_background_layout);

        this.initLikerProfilePictureImageView(view);

        this.initLikerNameTextView(view);

    }

    //profile picture
    private void initLikerProfilePictureImageView(View view) {

        this.editorProfilePictureImageView = (ImageView) view.findViewById(R.id.post_page_like_view_holder_liker_profile_picture_view);

    }

    private void initLikerNameTextView(View view) {

        this.editorNameTextView = (TextView) view.findViewById(R.id.post_page_like_view_holder_liker_name_view);

    }

    //social feed post
    public void setPostPageLike(PostLike postPageLike) {

        if (postPageLike != null) {

            this.postPageLike = postPageLike;

        }

        this.updateLike();

    }

    //update like
    public void updateLike() {

        if (this.postPageLike != null) {

            //like liker profile picture
            this.updateLikerProfilePictureImageView();

            //like liker name
            this.updateLikerNameTextView();

        }

    }

    //like liker profile picture
    private void updateLikerProfilePictureImageView() {

        if (this.postPageLike != null && this.postPageLike.getGuest() != null && this.postPageLike.getGuest().getImageUrl() != null) {

            Glide.with(this.context)
                    .asBitmap()
                    .load(this.postPageLike.getGuest().getImageUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.circleCropTransform())
                    .into(this.editorProfilePictureImageView);

        } else if (this.postPageLike != null && this.postPageLike.getHotel() != null && this.postPageLike.getHotel().getImageUrl() != null) {

            Glide.with(this.context)
                    .asBitmap()
                    .load(this.postPageLike.getHotel().getImageUrl())
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.circleCropTransform())
                    .into(this.editorProfilePictureImageView);

        } else {

            //put an placeholder default image
//            Glide.with(this.context)
//                    .asBitmap()
//                    .load(placeholderimage)
//                    .into(this.editorProfilePictureImageView);

        }

    }

    //like liker name
    private void updateLikerNameTextView() {

        if (this.postPageLike != null && this.postPageLike.getGuest() != null
                && this.postPageLike.getGuest().getName() != null) {

            this.editorNameTextView.setText(this.postPageLike.getGuest().getName());

        } else if (this.postPageLike != null && this.postPageLike.getHotel() != null
                && this.postPageLike.getHotel().getName() != null) {

            this.editorNameTextView.setText(this.postPageLike.getHotel().getName());

        } else {

            this.editorNameTextView.setText("No User!");

        }

        this.editorNameTextView.setTypeface(this.editorNameTextView.getTypeface(), Typeface.BOLD);

    }
    
    //skins
    public void setScreenSkin(int screenSkin) {
        this.screenSkin = screenSkin;

        this.updateLike();

    }

}