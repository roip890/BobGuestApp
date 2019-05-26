package com.bob.bobguestapp.tools.parsing.gsonserializers.postcomment;


import com.bob.bobguestapp.tools.database.objects.PostComment;
import com.bob.toolsmodule.parsing.gsonserializers.BaseListJsonSerializer;

public class PostCommentListJsonSerializer extends BaseListJsonSerializer<PostComment> {

    @Override
    protected void initObjectSerializer() {
        this.objectSerializer = new PostCommentJsonSerializer();
    }

    @Override
    protected PostComment[] getArrayOfType(int arraySize) {
        return new PostComment[arraySize];
    }

}
