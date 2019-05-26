package com.bob.bobguestapp.activities.main.fragments.social.likespage.likes.likeslist.filter;


import com.bob.bobguestapp.tools.database.objects.PostLike;
import com.bob.uimodule.recyclerview.ListFilter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 14/09/2018.
 */

public class PostPageLikesFilter extends ListFilter<PostLike> {

    private Date minDate;
    private Date maxDate;
    private boolean filterByMinDate;
    private boolean filterByMaxDate;

    public PostPageLikesFilter() {
        this.minDate = null;
        this.maxDate = null;

        this.filterByMinDate = false;
        this.filterByMaxDate = false;
    }

    public void filterByMinDate(boolean filterByMinDate) {
        this.filterByMinDate = filterByMinDate;
    }

    public void filterByMaxDate(boolean filterByMaxDate) {
        this.filterByMaxDate = filterByMaxDate;
    }

    public ArrayList<PostLike> getFilteredList(List<PostLike> postsLikesList) {
        ArrayList<PostLike> filteredPostLikesList = new ArrayList<PostLike>();
        if (postsLikesList != null) {
            for (PostLike postLike : postsLikesList) {
                if (this.filterByMinDate) {
                    Date postDate =  Timestamp.valueOf(postLike.getTimeStamp());
                    if (this.minDate != null && postDate.before(this.minDate)) {
                        continue;
                    }
                }
                if (this.filterByMaxDate) {
                    Date postDate = Timestamp.valueOf(postLike.getTimeStamp());
                    if (this.maxDate!= null && postDate.after(this.maxDate)) {
                        continue;
                    }
                }

                filteredPostLikesList.add(postLike);
            }
        }
        return filteredPostLikesList;
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

    public boolean isFilterByMinDate() {
        return filterByMinDate;
    }

    public boolean isFilterByMaxDate() {
        return filterByMaxDate;
    }

}
