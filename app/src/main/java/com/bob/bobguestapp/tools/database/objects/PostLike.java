package com.bob.bobguestapp.tools.database.objects;

import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.http.serverbeans.Hotel;
import com.bob.toolsmodule.http.serverbeans.User;

public class PostLike {

    //id
    private long likeId;

    //post
    private Post post;

    //hotel
    private Hotel hotel;

    //editor
    private User user;
    private Guest guest;

    //details
    private String timeStamp;

    public PostLike(long likeId, Post post, String timeStamp, Hotel hotel, User user, Guest guest) {

        //id
        this.likeId = likeId;

        //post
        this.post = post;

        //hotel
        this.hotel = hotel;

        //editor
        this.user = user;
        this.guest = guest;

        //details
        this.timeStamp = timeStamp;

    }

    public PostLike() {

        //id
        this.likeId = -1;

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

    public long getLikeId() {
        return likeId;
    }

    public void setLikeId(long likeId) {
        this.likeId = likeId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post postId) {
        this.post = postId;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
