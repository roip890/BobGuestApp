package com.bob.bobguestapp.activities.main.fragments.profile.changeprofilepicture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfilePictureChangingOptionAdapter extends RecyclerView.Adapter<ProfilePictureChangingOptionViewHolder> {

    //app theme
    protected int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    protected int screenSkin = MyAppThemeUtilsManager.PRIMARY_COLOR_SKIN;


    private final int CHANGE_PROFILE_PICTURE_OPTION_VIEW = 0;


    // The items to display in your RecyclerView
    private ArrayList<ProfilePictureChangingOption> profilePictureChangingOptions;
    private ProfilePictureChangingOptionTitleComparator profilePictureChangingOptionTitleComparator;
    private OnChangeProfilePictureChangingOptionListener onChangeProfilePictureChangingOptionListener;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProfilePictureChangingOptionAdapter(Context context) {
        this.context = context;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.PROFILE_FRAGMENT_CHANGE_PICTURE_BOTTOM_SHEET_SKIN);

        this.profilePictureChangingOptionTitleComparator = new ProfilePictureChangingOptionTitleComparator();
        this.profilePictureChangingOptions = new ArrayList<ProfilePictureChangingOption>();
        this.onChangeProfilePictureChangingOptionListener = null;
        this.setProfilePictureChangingOptions(this.profilePictureChangingOptions);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.profilePictureChangingOptions.size();
    }

    @Override
    public int getItemViewType(int position) {
        return CHANGE_PROFILE_PICTURE_OPTION_VIEW;
    }

    @Override
    public ProfilePictureChangingOptionViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        ProfilePictureChangingOptionViewHolder profilePictureChangingOptionViewHolder;

        switch (viewType) {
            case CHANGE_PROFILE_PICTURE_OPTION_VIEW:
                profilePictureChangingOptionViewHolder = new ProfilePictureChangingOptionViewHolder(context, inflater.inflate(R.layout.list_item_change_profile_picture, viewGroup, false));
            default:
//                return null;
                profilePictureChangingOptionViewHolder = new ProfilePictureChangingOptionViewHolder(context, inflater.inflate(R.layout.list_item_change_profile_picture, viewGroup, false));
        }
        profilePictureChangingOptionViewHolder.setScreenSkin(this.screenSkin);
        profilePictureChangingOptionViewHolder.setOnChangeProfilePictureChangingOptionListener(this.onChangeProfilePictureChangingOptionListener);
        return profilePictureChangingOptionViewHolder;
    }

    @Override
    public void onBindViewHolder(ProfilePictureChangingOptionViewHolder viewHolder, int position) {
        ProfilePictureChangingOption profilePictureChangingOption = this.profilePictureChangingOptions.get(position);
        viewHolder.configureProfilePictureChangingOption(profilePictureChangingOption, position);
    }

    public void setProfilePictureChangingOptions(List<ProfilePictureChangingOption> profilePictureChangingOptions) {
        if (profilePictureChangingOptions != null) {
            this.profilePictureChangingOptions.clear();
            this.profilePictureChangingOptions.addAll(profilePictureChangingOptions);
            Collections.sort(this.profilePictureChangingOptions, this.profilePictureChangingOptionTitleComparator);
            this.notifyDataSetChanged();
        }
    }

    public void setScreenSkin(int screenSkin) {
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, screenSkin);
        this.notifyDataSetChanged();
    }

    public void setOnChangeProfilePictureChangingOptionListener(OnChangeProfilePictureChangingOptionListener onChangeProfilePictureChangingOptionListener) {
        this.onChangeProfilePictureChangingOptionListener = onChangeProfilePictureChangingOptionListener;
        this.notifyDataSetChanged();
    }


}
