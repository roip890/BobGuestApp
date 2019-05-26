package com.bob.bobguestapp.activities.main.fragments.profile.changeprofilepicture;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bob.uimodule.UIUtilsManager;
import com.bob.uimodule.drawable.DrawableHelper;
import com.bumptech.glide.Glide;
import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;

public class ProfilePictureChangingOptionViewHolder extends RecyclerView.ViewHolder{

    //app theme
    protected int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    protected int screenSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;

    protected Context context;
    protected TextView profilePictureChangingOptionTitle;
    protected ImageView profilePictureChangingOptionIcon;
    protected RelativeLayout backgroundLayout;
    protected OnChangeProfilePictureChangingOptionListener onChangeProfilePictureChangingOptionListener;
    protected ProfilePictureChangingOption profilePictureChangingOption;

    public ProfilePictureChangingOptionViewHolder(Context context, @NonNull View itemView) {
        super(itemView);

        this.context = context;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.PROFILE_FRAGMENT_CHANGE_PICTURE_BOTTOM_SHEET_SKIN);

        initView(itemView);
        initialize();
    }

    private void initView(View itemView) {

        this.backgroundLayout = (RelativeLayout) itemView.findViewById(R.id.list_item_change_profile_picture_background);
        this.profilePictureChangingOptionTitle = (TextView) itemView.findViewById(R.id.list_item_change_profile_picture_option_title);
        this.profilePictureChangingOptionIcon = (ImageView) itemView.findViewById(R.id.list_item_change_profile_picture_option_icon);
        this.onChangeProfilePictureChangingOptionListener = null;
        this.profilePictureChangingOption = null;

        this.backgroundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChangeProfilePictureChangingOptionListener != null && profilePictureChangingOption != null) {
                    onChangeProfilePictureChangingOptionListener.onProfilePictureChangingOptionChoose(profilePictureChangingOption);
                }
            }
        });

    }

    private void initialize() {

        this.backgroundLayout.setBackgroundColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_VIEW_BACKGROUND_COLOR_PRIMARY, this.screenSkin));

        this.profilePictureChangingOptionTitle.setTextColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_BASE_TEXT_COLOR, this.screenSkin));

    }

    public void configureProfilePictureChangingOption(ProfilePictureChangingOption profilePictureChangingOption, int position) {

        this.profilePictureChangingOption = profilePictureChangingOption;

        if (position == 0) {
            GradientDrawable shapeDrawable = new GradientDrawable();
            shapeDrawable.setColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_VIEW_BACKGROUND_COLOR_PRIMARY, this.screenSkin));
            shapeDrawable.setCornerRadii(new float[] {
                    UIUtilsManager.get().convertDpToPixels(this.context, 10),
                    UIUtilsManager.get().convertDpToPixels(this.context, 10),
                    UIUtilsManager.get().convertDpToPixels(this.context, 10),
                    UIUtilsManager.get().convertDpToPixels(this.context, 10),
                    0,
                    0,
                    0,
                    0,
            });
            this.backgroundLayout.setBackground(shapeDrawable);

        } else {

            this.backgroundLayout.setBackgroundColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_VIEW_BACKGROUND_COLOR_PRIMARY, this.screenSkin));
        }


        if (profilePictureChangingOption != null) {

            this.profilePictureChangingOptionTitle.setText(profilePictureChangingOption.getTitle());

            Glide.with(this.context)
                    .load(DrawableHelper.withContext(this.context)
                            .withColor(MyAppThemeUtilsManager.get(this.context).getColor(MyAppThemeUtilsManager.DEFAULT_BASE_TEXT_COLOR, this.screenSkin))
                            .withDrawable(profilePictureChangingOption.getIcon())
                            .tint()
                            .get())
                    .into(this.profilePictureChangingOptionIcon);



        }

    }

    public void setOnChangeProfilePictureChangingOptionListener(OnChangeProfilePictureChangingOptionListener onChangeProfilePictureChangingOptionListener) {
        this.onChangeProfilePictureChangingOptionListener = onChangeProfilePictureChangingOptionListener;
    }

    public void setScreenSkin(int screenSkin) {
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, screenSkin);
        this.initialize();
    }

}
