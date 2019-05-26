package com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.comperators.SocialFeedPostTimeComparator;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.filter.SocialFeedPostsFilter;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.listener.PostsListEventsListener;
import com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.SocialFeedPostsListViewHolder;
import com.bob.bobguestapp.tools.database.objects.Post;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.uimodule.popup.PowerMenuListener;
import com.bob.uimodule.recyclerview.SortFilterAdapter;
import com.bob.uimodule.video.exoplayer.ExoPlayerFullScreenListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class SocialFeedPostsListAdapter extends SortFilterAdapter<Post, SocialFeedPostsListViewHolder, SocialFeedPostsFilter> {

    //app theme
    private int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    private int screenSkin = MyAppThemeUtilsManager.LIGHT_COLOR_SKIN;

    private static final int POST_VIEW_TYPE = 0;

    private Context context;

    private FragmentManager fragmentManager;

    private PowerMenuListener powerMenuListener;

    private PostsListEventsListener postsListEventsListener;

    private ExoPlayerFullScreenListener exoPlayerFullScreenListener;

    public SocialFeedPostsListAdapter(Context context, FragmentManager fragmentManager, List<Post> items, PowerMenuListener powerMenuListener, PostsListEventsListener postsListEventsListener) {

		this.context = context;

		this.fragmentManager = fragmentManager;

        this.powerMenuListener = powerMenuListener;

        this.postsListEventsListener = postsListEventsListener;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_SKIN);

        if (items != null) {
            this.allObjects = items;
        } else {
		    this.allObjects = new ArrayList<Post>();
        }

        this.objectsToShow = new ArrayList<Post>();
		this.objectsToShow.addAll(this.allObjects);

		this.comparators = new HashMap<String, Comparator<Post>>();
        this.comparators.put("date", new SocialFeedPostTimeComparator());

        this.filter = new SocialFeedPostsFilter();

		this.sortingType = "date";

		this.ascending = true;

	}

    public SocialFeedPostsListAdapter(Context context, FragmentManager fragmentManager, PowerMenuListener powerMenuListener, PostsListEventsListener postsListEventsListener) {

        this(context, fragmentManager, new ArrayList<Post>(), powerMenuListener, postsListEventsListener);

    }

    public void setScreenSkin(int screenSkin) {
        this.screenSkin = screenSkin;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return POST_VIEW_TYPE;
    }

    @NonNull
    @Override
    public SocialFeedPostsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        SocialFeedPostsListViewHolder socialFeedPostsListViewHolder = new SocialFeedPostsListViewHolder(
                this.context,
                this.fragmentManager,
                inflater.inflate(R.layout.view_holder_social_feed_post, viewGroup, false),
                this.powerMenuListener,
                this.postsListEventsListener
        );
        socialFeedPostsListViewHolder.setScreenSkin(this.screenSkin);
        return socialFeedPostsListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SocialFeedPostsListViewHolder viewHolder, int position) {

        Post post = this.objectsToShow.get(position);

        viewHolder.setSocialFeedPost(post);

    }
    
    @Override
    public int getItemCount() {
        return objectsToShow.size();
	}

}
