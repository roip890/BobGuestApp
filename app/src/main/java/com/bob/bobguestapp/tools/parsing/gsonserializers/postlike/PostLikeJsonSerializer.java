package com.bob.bobguestapp.tools.parsing.gsonserializers.postlike;

import com.bob.bobguestapp.tools.database.objects.Post;
import com.bob.bobguestapp.tools.database.objects.PostLike;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.http.serverbeans.Hotel;
import com.bob.toolsmodule.http.serverbeans.User;
import com.bob.toolsmodule.parsing.gsonserializers.BaseJsonSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PostLikeJsonSerializer extends BaseJsonSerializer<PostLike> {


    @Override
    public JsonObject createJsonObject(PostLike object) {

        JsonObject postLikeJsonObject = new JsonObject();

        postLikeJsonObject.addProperty("likeId", object.getLikeId());
        postLikeJsonObject.addProperty("insertTs", object.getTimeStamp());

        JsonElement postJsonObject = MyGsonParser.getParser().create().toJsonTree(object.getPost(), Post.class);
        postLikeJsonObject.add("post", postJsonObject);

        JsonElement hotelJsonObject = MyGsonParser.getParser().create().toJsonTree(object.getHotel(), Hotel.class);
        postLikeJsonObject.add("hotel", hotelJsonObject);

        JsonElement userJsonObject = MyGsonParser.getParser().create().toJsonTree(object.getUser(), User.class);
        postLikeJsonObject.add("user", userJsonObject);

        JsonElement guestJsonObject = MyGsonParser.getParser().create().toJsonTree(object.getGuest(), Guest.class);
        postLikeJsonObject.add("guest", guestJsonObject);

        return postLikeJsonObject;
    }

    @Override
    public PostLike createObject(JsonObject jsonObject) {

        PostLike postLike = new PostLike();

        if (jsonObject.get("likeId") != null) {
            postLike.setLikeId(jsonObject.get("likeId").getAsInt());
        }
        if (jsonObject.get("insertTs") != null) {
            postLike.setTimeStamp(jsonObject.get("insertTs").getAsString());
        }

        if (jsonObject.get("post") != null) {
            JsonObject postJsonObject = jsonObject.get("post").getAsJsonObject();
            postLike.setPost(MyGsonParser.getParser().create().fromJson(postJsonObject, Post.class));
        }

        if (jsonObject.get("hotel") != null) {
            JsonObject hotelJsonObject = jsonObject.get("hotel").getAsJsonObject();
            postLike.setHotel(MyGsonParser.getParser().create().fromJson(hotelJsonObject, Hotel.class));
        }

        if (jsonObject.get("user") != null) {
            JsonObject userJsonObject = jsonObject.get("user").getAsJsonObject();
            postLike.setUser(MyGsonParser.getParser().create().fromJson(userJsonObject, User.class));
        }

        if (jsonObject.get("guest") != null) {
            JsonObject guestJsonObject = jsonObject.get("guest").getAsJsonObject();
            postLike.setGuest(MyGsonParser.getParser().create().fromJson(guestJsonObject, Guest.class));
        }

        return postLike;
    }

    @Override
    protected void initObjectSerializers() {

    }
}
