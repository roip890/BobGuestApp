package com.bob.bobguestapp.activities.main.fragments.social.postupload.media.connector;

import com.bob.toolsmodule.objects.MediaItem;

import java.util.ArrayList;

public interface PostMediaConnector {

    public void disableViewPagerSwipe(boolean disable);

//    public void goToContentFragment();

    public void continueToContentActivity(ArrayList<MediaItem> mediaItems);

}
