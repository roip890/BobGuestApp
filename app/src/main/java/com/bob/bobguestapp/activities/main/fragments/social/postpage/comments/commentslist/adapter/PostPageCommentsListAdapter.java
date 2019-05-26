package com.bob.bobguestapp.activities.main.fragments.social.postpage.comments.commentslist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bob.bobguestapp.BOBGuestApplication;
import com.bob.bobguestapp.R;
import com.bob.bobguestapp.activities.main.fragments.social.postpage.comments.commentslist.comperators.PostPageCommentTimeComparator;
import com.bob.bobguestapp.activities.main.fragments.social.postpage.comments.commentslist.filter.PostPageCommentsFilter;
import com.bob.bobguestapp.activities.main.fragments.social.postpage.comments.commentslist.viewholder.PostPageCommentsListViewHolder;
import com.bob.bobguestapp.tools.database.objects.PostComment;
import com.bob.bobguestapp.tools.theme.MyAppThemeUtilsManager;
import com.bob.uimodule.popup.PowerMenuListener;
import com.bob.uimodule.recyclerview.SortFilterAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class PostPageCommentsListAdapter extends SortFilterAdapter<PostComment, PostPageCommentsListViewHolder, PostPageCommentsFilter> {

    //app theme
    private int appTheme = MyAppThemeUtilsManager.DEFAULT_THEME;
    private int screenSkin = MyAppThemeUtilsManager.LIGHT_COLOR_SKIN;

    private static final int POST_COMMENT_VIEW_TYPE = 0;
    private Context context;

    private PowerMenuListener powerMenuListener;

    public PostPageCommentsListAdapter(Context context, List<PostComment> items, PowerMenuListener powerMenuListener) {

		this.context = context;

		this.powerMenuListener = powerMenuListener;

        //theme
        this.appTheme = BOBGuestApplication.get().getSecureSharedPreferences().getInt("appTheme", MyAppThemeUtilsManager.DEFAULT_THEME);
        this.screenSkin = MyAppThemeUtilsManager.get(this.context).getSkin(appTheme, MyAppThemeUtilsManager.REQUESTS_FRAGMENT_SKIN);

        if (items != null) {
            this.allObjects = items;
        } else {
		    this.allObjects = new ArrayList<PostComment>();
        }

        this.objectsToShow = new ArrayList<PostComment>();
		this.objectsToShow.addAll(this.allObjects);

		this.comparators = new HashMap<String, Comparator<PostComment>>();
        this.comparators.put("date", new PostPageCommentTimeComparator());

        this.filter = new PostPageCommentsFilter();

		this.sortingType = "date";

		this.ascending = true;

	}

    public PostPageCommentsListAdapter(Context context, PowerMenuListener powerMenuListener) {

        this(context, new ArrayList<PostComment>(), powerMenuListener);

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
    public PostPageCommentsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        PostPageCommentsListViewHolder socialFeedPostsListViewHolder = new PostPageCommentsListViewHolder(this.context,
                inflater.inflate(R.layout.view_holder_post_page_comment, viewGroup, false),
                this.powerMenuListener);
        socialFeedPostsListViewHolder.setScreenSkin(this.screenSkin);
        return socialFeedPostsListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostPageCommentsListViewHolder viewHolder, int position) {

        PostComment postComment = this.objectsToShow.get(position);

        viewHolder.setPostPageComment(postComment);

    }
    
    @Override
    public int getItemCount() {
        return objectsToShow.size();
	}

}
