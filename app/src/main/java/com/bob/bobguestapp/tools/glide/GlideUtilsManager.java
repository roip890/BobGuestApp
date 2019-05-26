package com.bob.bobguestapp.tools.glide;

public class GlideUtilsManager {

    protected static GlideUtilsManager instance;

    //initialization
    protected GlideUtilsManager() {

    }

    public static GlideUtilsManager get() {
        if (instance == null) {
            instance = new GlideUtilsManager();
        }
        return instance;
    }


}
