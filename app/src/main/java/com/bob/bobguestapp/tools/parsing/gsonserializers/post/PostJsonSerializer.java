package com.bob.bobguestapp.tools.parsing.gsonserializers.post;

import com.bob.bobguestapp.tools.database.objects.Post;
import com.bob.bobguestapp.tools.parsing.MyGsonParser;
import com.bob.toolsmodule.http.serverbeans.Guest;
import com.bob.toolsmodule.http.serverbeans.Hotel;
import com.bob.toolsmodule.http.serverbeans.User;
import com.bob.toolsmodule.parsing.gsonserializers.BaseJsonSerializer;
import com.bob.toolsmodule.objects.MediaItem;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PostJsonSerializer extends BaseJsonSerializer<Post> {


    @Override
    public JsonObject createJsonObject(Post object) {

        JsonObject postJsonObject = new JsonObject();

        postJsonObject.addProperty("postId", object.getPostId());
        postJsonObject.addProperty("content", object.getText());
        postJsonObject.addProperty("insertTs", object.getTimeStamp());
        postJsonObject.addProperty("likesCount", object.getLikesCount());
        postJsonObject.addProperty("commentsCount", object.getCommentsCount());
        postJsonObject.addProperty("isLike", object.isLike());

        JsonElement mediaItemsJsonObject = MyGsonParser.getParser().create().toJsonTree(object.getMediaItems(), MediaItem[].class);
        postJsonObject.add("mediaItems", mediaItemsJsonObject);

        JsonElement hotelJsonObject = MyGsonParser.getParser().create().toJsonTree(object.getHotel(), Hotel.class);
        postJsonObject.add("hotel", hotelJsonObject);

        JsonElement userJsonObject = MyGsonParser.getParser().create().toJsonTree(object.getUser(), User.class);
        postJsonObject.add("user", userJsonObject);

        JsonElement guestJsonObject = MyGsonParser.getParser().create().toJsonTree(object.getGuest(), Guest.class);
        postJsonObject.add("guest", guestJsonObject);

        return postJsonObject;
    }

    @Override
    public Post createObject(JsonObject jsonObject) {

        Post post = new Post();

        if (jsonObject.get("postId") != null) {
            post.setPostId(jsonObject.get("postId").getAsInt());
        }
        if (jsonObject.get("content") != null) {
            post.setText(jsonObject.get("content").getAsString());
        }
        if (jsonObject.get("insertTs") != null) {
            post.setTimeStamp(jsonObject.get("insertTs").getAsString());
        }
        if (jsonObject.get("likesCount") != null) {
            post.setLikesCount(jsonObject.get("likesCount").getAsInt());
        }
        if (jsonObject.get("commentsCount") != null) {
            post.setCommentsCount(jsonObject.get("commentsCount").getAsInt());
        }
        if (jsonObject.get("isLike") != null) {
            post.setLike(jsonObject.get("isLike").getAsBoolean());
        }

        if (jsonObject.get("mediaItems") != null) {

            if (jsonObject.get("mediaItems").isJsonObject()) {

                JsonObject mediaItemsJsonObject = jsonObject.get("mediaItems").getAsJsonObject();
                post.setMediaItems(new MediaItem[]{
                        MyGsonParser.getParser().create().fromJson(mediaItemsJsonObject, MediaItem.class)
                });

            } else if (jsonObject.get("mediaItems").isJsonArray()) {

                JsonArray mediaItemsJsonArray = jsonObject.get("mediaItems").getAsJsonArray();
                post.setMediaItems(MyGsonParser.getParser().create().fromJson(mediaItemsJsonArray, MediaItem[].class));

            }

        }

        if (jsonObject.get("hotel") != null) {
            JsonObject hotelJsonObject = jsonObject.get("hotel").getAsJsonObject();
            post.setHotel(MyGsonParser.getParser().create().fromJson(hotelJsonObject, Hotel.class));
        }

        if (jsonObject.get("user") != null) {
            JsonObject userJsonObject = jsonObject.get("user").getAsJsonObject();
            post.setUser(MyGsonParser.getParser().create().fromJson(userJsonObject, User.class));
        }

        if (jsonObject.get("guest") != null) {
            JsonObject guestJsonObject = jsonObject.get("guest").getAsJsonObject();
            post.setGuest(MyGsonParser.getParser().create().fromJson(guestJsonObject, Guest.class));
        }

        return post;
    }

    @Override
    protected void initObjectSerializers() {

    }
}
