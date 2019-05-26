package com.bob.bobguestapp.activities.main.fragments.social.postpage.comments.commentslist.filter;


import com.bob.bobguestapp.tools.database.objects.PostComment;
import com.bob.uimodule.recyclerview.ListFilter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 14/09/2018.
 */

public class PostPageCommentsFilter extends ListFilter<PostComment> {

    private Date minDate;
    private Date maxDate;
    private String text;
    private boolean filterByMinDate;
    private boolean filterByMaxDate;
    private boolean filterByText;

    public PostPageCommentsFilter() {
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

    public ArrayList<PostComment> getFilteredList(List<PostComment> postsCommentsList) {
        ArrayList<PostComment> filteredPostCommentsList = new ArrayList<PostComment>();
        if (postsCommentsList != null) {
            for (PostComment postComment : postsCommentsList) {
                if (this.filterByMinDate) {
                    Date postDate =  Timestamp.valueOf(postComment.getTimeStamp());
                    if (this.minDate != null && postDate.before(this.minDate)) {
                        continue;
                    }
                }
                if (this.filterByMaxDate) {
                    Date postDate = Timestamp.valueOf(postComment.getTimeStamp());
                    if (this.maxDate!= null && postDate.after(this.maxDate)) {
                        continue;
                    }
                }
                if (this.filterByText) {
                    if (this.text != null && !postComment.getContent().toLowerCase().contains(this.text.toLowerCase())) {
                        continue;
                    }
                }
                filteredPostCommentsList.add(postComment);
            }
        }
        return filteredPostCommentsList;
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
