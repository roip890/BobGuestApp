package com.bob.bobguestapp.tools.parsing.gsonserializers.postcomment;

import com.bob.bobguestapp.tools.database.objects.Post;
import com.bob.bobguestapp.tools.database.objects.PostComment;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.http.serverbeans.Hotel;
import com.bob.toolsmodule.http.serverbeans.User;
import com.bob.toolsmodule.parsing.gsonserializers.BaseJsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PostCommentJsonSerializer extends BaseJsonSerializer<PostComment> {


    @Override
    public JsonObject createJsonObject(PostComment object) {

        JsonObject postCommentJsonObject = new JsonObject();

        postCommentJsonObject.addProperty("commentId", object.getCommentId());
        postCommentJsonObject.addProperty("content", object.getContent());
        postCommentJsonObject.addProperty("insertTs", object.getTimeStamp());
        postCommentJsonObject.addProperty("likesCount", object.getLikes());
        postCommentJsonObject.addProperty("commentsCount", object.getComments());

        JsonElement postJsonObject = MyGsonParser.getParser().create().toJsonTree(object.getPost(), Post.class);
        postCommentJsonObject.add("post", postJsonObject);

        JsonElement hotelJsonObject = MyGsonParser.getParser().create().toJsonTree(object.getHotel(), Hotel.class);
        postCommentJsonObject.add("hotel", hotelJsonObject);

        JsonElement userJsonObject = MyGsonParser.getParser().create().toJsonTree(object.getUser(), User.class);
        postCommentJsonObject.add("user", userJsonObject);

        JsonElement guestJsonObject = MyGsonParser.getParser().create().toJsonTree(object.getGuest(), Guest.class);
        postCommentJsonObject.add("guest", guestJsonObject);

        return postCommentJsonObject;
    }

    @Override
    public PostComment createObject(JsonObject jsonObject) {

        PostComment postComment = new PostComment();

        if (jsonObject.get("commentId") != null) {
            postComment.setCommentId(jsonObject.get("commentId").getAsInt());
        }
        if (jsonObject.get("content") != null) {
            postComment.setContent(jsonObject.get("content").getAsString());
        }
        if (jsonObject.get("insertTs") != null) {
            postComment.setTimeStamp(jsonObject.get("insertTs").getAsString());
        }
        if (jsonObject.get("likesCount") != null) {
            postComment.setLikes(jsonObject.get("likesCount").getAsInt());
        }
        if (jsonObject.get("commentsCount") != null) {
            postComment.setComments(jsonObject.get("commentsCount").getAsInt());
        }

        if (jsonObject.get("post") != null) {
            JsonObject postJsonObject = jsonObject.get("post").getAsJsonObject();
            postComment.setPost(MyGsonParser.getParser().create().fromJson(postJsonObject, Post.class));
        }

        if (jsonObject.get("hotel") != null) {
            JsonObject hotelJsonObject = jsonObject.get("hotel").getAsJsonObject();
            postComment.setHotel(MyGsonParser.getParser().create().fromJson(hotelJsonObject, Hotel.class));
        }

        if (jsonObject.get("user") != null) {
            JsonObject userJsonObject = jsonObject.get("user").getAsJsonObject();
            postComment.setUser(MyGsonParser.getParser().create().fromJson(userJsonObject, User.class));
        }

        if (jsonObject.get("guest") != null) {
            JsonObject guestJsonObject = jsonObject.get("guest").getAsJsonObject();
            postComment.setGuest(MyGsonParser.getParser().create().fromJson(guestJsonObject, Guest.class));
        }

        return postComment;
    }

    @Override
    protected void initObjectSerializers() {

    }
}
