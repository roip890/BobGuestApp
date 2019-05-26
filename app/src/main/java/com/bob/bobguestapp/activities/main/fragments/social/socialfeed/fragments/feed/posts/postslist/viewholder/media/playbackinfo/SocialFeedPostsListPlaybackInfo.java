package com.bob.bobguestapp.activities.main.fragments.social.socialfeed.fragments.feed.posts.postslist.viewholder.media.playbackinfo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import im.ene.toro.media.PlaybackInfo;

@SuppressWarnings("WeakerAccess") //
public class SocialFeedPostsListPlaybackInfo extends PlaybackInfo {

    final SparseArray actualInfo;

    public SocialFeedPostsListPlaybackInfo(SparseArray<PlaybackInfo> actualInfo) {
        this.actualInfo = actualInfo;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        //noinspection unchecked
        dest.writeSparseArray(this.actualInfo);
    }

    protected SocialFeedPostsListPlaybackInfo(Parcel in) {
        super(in);
        this.actualInfo = in.readSparseArray(PlaybackInfo.class.getClassLoader());
    }

    public SparseArray getActualInfo() {
        return this.actualInfo;
    }

    public static final Parcelable.Creator<SocialFeedPostsListPlaybackInfo> CREATOR =
            new Parcelable.ClassLoaderCreator<SocialFeedPostsListPlaybackInfo>() {
                @Override public SocialFeedPostsListPlaybackInfo createFromParcel(Parcel source) {
                    return new SocialFeedPostsListPlaybackInfo(source);
                }

                @Override public SocialFeedPostsListPlaybackInfo createFromParcel(Parcel source, ClassLoader loader) {
                    return new SocialFeedPostsListPlaybackInfo(source);
                }

                @Override public SocialFeedPostsListPlaybackInfo[] newArray(int size) {
                    return new SocialFeedPostsListPlaybackInfo[size];
                }
            };

    @Override public String toString() {
        return "ExtraInfo{" + "actualInfo=" + actualInfo + '}';
    }
}
