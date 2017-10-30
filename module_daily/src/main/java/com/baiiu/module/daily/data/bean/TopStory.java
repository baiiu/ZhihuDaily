package com.baiiu.module.daily.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: baiiu
 * date: on 16/4/5 15:24
 * description:
 */
public class TopStory implements Parcelable {
    public String image;
    public int type;
    public long id;
    public String ga_prefix;
    public String title;

    public TopStory() {
    }

    public TopStory(long id, String image, String title) {
        this.image = image;
        this.title = title;
        this.id = id;
    }

    protected TopStory(Parcel in) {
        image = in.readString();
        type = in.readInt();
        id = in.readLong();
        ga_prefix = in.readString();
        title = in.readString();
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeInt(type);
        dest.writeLong(id);
        dest.writeString(ga_prefix);
        dest.writeString(title);
    }

    @Override public int describeContents() {
        return 0;
    }

    public static final Creator<TopStory> CREATOR = new Creator<TopStory>() {
        @Override public TopStory createFromParcel(Parcel in) {
            return new TopStory(in);
        }

        @Override public TopStory[] newArray(int size) {
            return new TopStory[size];
        }
    };
}
