package com.bob.bobguestapp.activities.main.fragments.profile.changeprofilepicture;

public class ProfilePictureChangingOption {

    private String title;
    private int icon;
    private int color;


    public ProfilePictureChangingOption(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public ProfilePictureChangingOption() {

    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }


}
