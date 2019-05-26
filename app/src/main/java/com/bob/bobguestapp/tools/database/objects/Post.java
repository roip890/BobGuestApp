package com.bob.bobguestapp.tools.database.objects;

import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.http.serverbeans.Hotel;
import com.bob.toolsmodule.http.serverbeans.User;
import com.bob.toolsmodule.objects.MediaItem;

public class Post {

    //id
    private long postId;

    //content
    private String text;
    private MediaItem[] mediaItems;
    private int likesCount;
    private int commentsCount;

    //hotel
    private Hotel hotel;

    //editor
    private User user;
    private Guest guest;

    //details
    private String timeStamp;

    //is like
    private boolean isLike;

    public Post(long postId, String text, MediaItem[] mediaItems, int likesCount, int commentsCount, Hotel hotel, User user, Guest guest, String timeStamp, boolean isLike) {

        this.postId = postId;

        //content
        this.text = text;
        this.mediaItems = mediaItems;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;

        //hotel
        this.hotel = hotel;

        //editor
        this.user = user;
        this.guest = guest;

        //details
        this.timeStamp = timeStamp;

        //is like
        this.isLike = isLike;

    }

    public Post() {

        this.postId = -1;

        //content
        this.text = null;
        this.mediaItems = null;
        this.likesCount = 0;
        this.commentsCount = 0;

        //hotel
        this.hotel = null;

        //editor
        this.user = null;
        this.guest = null;

        //details
        this.timeStamp = null;

        //is like
        this.isLike = false;

    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MediaItem[] getMediaItems() {
        return mediaItems;
    }

    public void setMediaItems(MediaItem[] mediaItems) {
        this.mediaItems = mediaItems;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
