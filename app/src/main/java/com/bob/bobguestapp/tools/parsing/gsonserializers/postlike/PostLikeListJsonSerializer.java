package com.bob.bobguestapp.tools.parsing.gsonserializers.postlike;


import com.bob.bobguestapp.tools.database.objects.PostLike;
import com.bob.toolsmodule.parsing.gsonserializers.BaseListJsonSerializer;

public class PostLikeListJsonSerializer extends BaseListJsonSerializer<PostLike> {

    @Override
    protected void initObjectSerializer() {
        this.objectSerializer = new PostLikeJsonSerializer();
    }

    @Override
    protected PostLike[] getArrayOfType(int arraySize) {
        return new PostLike[arraySize];
    }

}
