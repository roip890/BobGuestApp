package com.bob.bobguestapp.activities.main.fragments.profile.changeprofilepicture;

import java.util.Comparator;

public class ProfilePictureChangingOptionTitleComparator implements Comparator<ProfilePictureChangingOption> {

    @Override
    public int compare(ProfilePictureChangingOption profilePictureChangingOption1, ProfilePictureChangingOption profilePictureChangingOption2) {

        int compare = profilePictureChangingOption1.getTitle().compareToIgnoreCase(profilePictureChangingOption2.getTitle());
        if (compare < 0) {
            return -1;
        } else if (compare > 0) {
            return 1;
        }
        return 0;
    }

}
