package com.bob.bobguestapp.tools.parsing;

import com.bob.bobguestapp.tools.database.objects.GuestAppNotification;
import com.bob.bobguestapp.tools.database.objects.GuestBooking;
import com.bob.bobguestapp.tools.database.objects.Post;
import com.bob.bobguestapp.tools.database.objects.PostComment;
import com.bob.bobguestapp.tools.database.objects.PostLike;
import com.bob.bobguestapp.tools.parsing.gsonserializers.appnotification.AppNotificationJsonSerializer;
import com.bob.bobguestapp.tools.parsing.gsonserializers.appnotification.AppNotificationListJsonSerializer;
import com.bob.bobguestapp.tools.parsing.gsonserializers.guestbooking.GuestBookingJsonSerializer;
import com.bob.bobguestapp.tools.parsing.gsonserializers.guestbooking.GuestBookingListJsonSerializer;
import com.bob.bobguestapp.tools.parsing.gsonserializers.post.PostJsonSerializer;
import com.bob.bobguestapp.tools.parsing.gsonserializers.post.PostListJsonSerializer;
import com.bob.bobguestapp.tools.parsing.gsonserializers.postcomment.PostCommentJsonSerializer;
import com.bob.bobguestapp.tools.parsing.gsonserializers.postcomment.PostCommentListJsonSerializer;
import com.bob.bobguestapp.tools.parsing.gsonserializers.postlike.PostLikeJsonSerializer;
import com.bob.bobguestapp.tools.parsing.gsonserializers.postlike.PostLikeListJsonSerializer;
import com.bob.toolsmodule.objects.MediaItem;
import com.bob.toolsmodule.parsing.GsonParser;
import com.bob.toolsmodule.parsing.gsonserializers.mediaitem.MediaItemJsonSerializer;
import com.bob.toolsmodule.parsing.gsonserializers.mediaitem.MediaItemListJsonSerializer;
import com.google.gson.GsonBuilder;

public class MyGsonParser extends GsonParser {

    protected MyGsonParser() {
        customGson = new GsonBuilder();
        this.initCustomGson(customGson);
    }

    public static GsonBuilder getParser() {
        if (customGson == null) {
            instance = new MyGsonParser();
        }
        return customGson;
    }

    @Override
    protected void initCustomGson(GsonBuilder customGson) {
        super.initCustomGson(customGson);

        customGson.registerTypeAdapter(GuestAppNotification.class, new AppNotificationJsonSerializer());
        customGson.registerTypeAdapter(GuestAppNotification[].class, new AppNotificationListJsonSerializer());
        customGson.registerTypeAdapter(GuestBooking.class, new GuestBookingJsonSerializer());
        customGson.registerTypeAdapter(GuestBooking[].class, new GuestBookingListJsonSerializer());
        customGson.registerTypeAdapter(MediaItem.class, new MediaItemJsonSerializer());
        customGson.registerTypeAdapter(MediaItem[].class, new MediaItemListJsonSerializer());
        customGson.registerTypeAdapter(Post.class, new PostJsonSerializer());
        customGson.registerTypeAdapter(Post[].class, new PostListJsonSerializer());
        customGson.registerTypeAdapter(PostComment.class, new PostCommentJsonSerializer());
        customGson.registerTypeAdapter(PostComment[].class, new PostCommentListJsonSerializer());
        customGson.registerTypeAdapter(PostLike.class, new PostLikeJsonSerializer());
        customGson.registerTypeAdapter(PostLike[].class, new PostLikeListJsonSerializer());

    }


}
