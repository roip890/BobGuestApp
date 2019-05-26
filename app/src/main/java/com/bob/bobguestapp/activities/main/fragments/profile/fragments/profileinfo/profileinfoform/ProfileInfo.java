package com.bob.bobguestapp.activities.main.fragments.profile.fragments.profileinfo.profileinfoform;

public class ProfileInfo {

    private String key;
    private String value;
    private String type;
    private boolean editable;
    private int tag;


    public ProfileInfo(String key, String value, String type, boolean editable, int tag) {

        this.key = key;

        this.value = value;

        this.type = type;

        this.editable = editable;

        this.tag = tag;

    }

    public ProfileInfo() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
