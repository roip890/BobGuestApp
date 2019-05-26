package com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.filter;


import com.bob.bobguestapp.tools.database.objects.Post;
import com.bob.uimodule.recyclerview.ListFilter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 14/09/2018.
 */

public class SocialFeedPostsFilter extends ListFilter<Post> {

    private Date minDate;
    private Date maxDate;
    private String text;
    private boolean filterByMinDate;
    private boolean filterByMaxDate;
    private boolean filterByText;

    public SocialFeedPostsFilter() {
        this.minDate = null;
        this.maxDate = null;
        this.text = null;

        this.filterByMinDate = false;
        this.filterByMaxDate = false;
        this.filterByText = false;
    }

    public void filterByMinDate(boolean filterByMinDate) {
        this.filterByMinDate = filterByMinDate;
    }

    public void filterByMaxDate(boolean filterByMaxDate) {
        this.filterByMaxDate = filterByMaxDate;
    }

    public void filterByText(boolean filterByText) {
        this.filterByText = filterByText;
    }

    public ArrayList<Post> getFilteredList(List<Post> postsList) {
        ArrayList<Post> filteredPostsList = new ArrayList<Post>();
        if (postsList != null) {
            for (Post post: postsList) {
                if (this.filterByMinDate) {
                    Date postDate =  Timestamp.valueOf(post.getTimeStamp());
                    if (this.minDate != null && postDate.before(this.minDate)) {
                        continue;
                    }
                }
                if (this.filterByMaxDate) {
                    Date postDate = Timestamp.valueOf(post.getTimeStamp());
                    if (this.maxDate!= null && postDate.after(this.maxDate)) {
                        continue;
                    }
                }
                if (this.filterByText) {
                    if (this.text != null && !post.getText().toLowerCase().contains(this.text.toLowerCase())) {
                        continue;
                    }
                }
                filteredPostsList.add(post);
            }
        }
        return filteredPostsList;
    }


    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text= text;
    }

    public boolean isFilterByMinDate() {
        return filterByMinDate;
    }

    public boolean isFilterByMaxDate() {
        return filterByMaxDate;
    }

    public boolean isFilterByText() {
        return filterByText;
    }

}
