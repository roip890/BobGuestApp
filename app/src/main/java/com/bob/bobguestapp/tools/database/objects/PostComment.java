package com.bob.bobguestapp.tools.database.objects;

import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.http.serverbeans.Hotel;
import com.bob.toolsmodule.http.serverbeans.User;

public class PostComment {

    //id
    private long commentId;

    //post
    private Post post;

    //content
    private String content;

    //likes
    private int likes;

    //comments
    private int comments;

    //timestamp
    private String timeStamp;

    //hotel
    private Hotel hotel;

    //editor
    private User user;
    private Guest guest;

    public PostComment(long commentId, String content, int likes, String timeStamp, Post post, Hotel hotel, User user, Guest guest) {

        //id
        this.commentId = commentId;

        //post
        this.post = post;

        //content
        this.content = content;

        //likes
        this.likes = likes;

        //hotel
        this.hotel = hotel;

        //editor
        this.user = user;
        this.guest = guest;

        //details
        this.timeStamp = timeStamp;

    }

    public PostComment() {

        //id
        this.commentId = -1;

        //post
        this.post = null;

        //hotel
        this.hotel = null;

        //editor
        this.user = null;
        this.guest = null;

        //details
        this.timeStamp = null;

    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post postId) {
        this.post = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
