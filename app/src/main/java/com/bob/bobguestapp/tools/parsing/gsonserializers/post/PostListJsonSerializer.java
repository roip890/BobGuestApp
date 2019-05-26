package com.bob.bobguestapp.tools.parsing.gsonserializers.post;


import com.bob.bobguestapp.tools.database.objects.Post;
import com.bob.toolsmodule.parsing.gsonserializers.BaseListJsonSerializer;

public class PostListJsonSerializer extends BaseListJsonSerializer<Post> {

    @Override
    protected void initObjectSerializer() {
        this.objectSerializer = new PostJsonSerializer();
    }

    @Override
    protected Post[] getArrayOfType(int arraySize) {
        return new Post[arraySize];
    }

}
