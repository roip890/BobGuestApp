package com.bob.bobguestapp.activities.main.fragments.social.likespage.likes.likeslist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.likespage.likes.likeslist.comperators.PostPageLikeTimeComparator;
import com.bob.bobguestapp.activities.main.fragments.social.likespage.likes.likeslist.filter.PostPageLikesFilter;
import com.bob.bobguestapp.activities.main.fragments.social.likespage.likes.likeslist.viewholder.PostPageLikesListViewHolder;
import com.bob.bobguestapp.tools.database.objects.PostLike;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.uimodule.recyclerview.SortFilterAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class PostPageLikesListAdapter extends SortFilterAdapter<PostLike, PostPageLikesListViewHolder, PostPageLikesFilter> {

    //app theme
    private int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    private int screenSkin = MyAppThemeUtilsManager.LIGHT_COLOR_SKIN;

    private static final int POST_COMMENT_VIEW_TYPE = 0;
    private Context context;

    public PostPageLikesListAdapter(Context context, List<PostLike> items) {

		this.context = context;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_SKIN);

        if (items != null) {
            this.allObjects = items;
        } else {
		    this.allObjects = new ArrayList<PostLike>();
        }

        this.objectsToShow = new ArrayList<PostLike>();
		this.objectsToShow.addAll(this.allObjects);

		this.comparators = new HashMap<String, Comparator<PostLike>>();
        this.comparators.put("date", new PostPageLikeTimeComparator());

        this.filter = new PostPageLikesFilter();

		this.sortingType = "date";

		this.ascending = true;

	}

    public PostPageLikesListAdapter(Context context) {

        this(context, new ArrayList<PostLike>());

    }

    public void setScreenSkin(int screenSkin) {
        this.screenSkin = screenSkin;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return POST_COMMENT_VIEW_TYPE;
    }

    @NonNull
    @Override
    public PostPageLikesListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        PostPageLikesListViewHolder socialFeedPostsListViewHolder = new PostPageLikesListViewHolder(this.context, inflater.inflate(R.layout.view_holder_post_page_like, viewGroup, false));
        socialFeedPostsListViewHolder.setScreenSkin(this.screenSkin);
        return socialFeedPostsListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostPageLikesListViewHolder viewHolder, int position) {

        PostLike postLike = this.objectsToShow.get(position);

        viewHolder.setPostPageLike(postLike);

    }
    
    @Override
    public int getItemCount() {
        return objectsToShow.size();
	}

}
