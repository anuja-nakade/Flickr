package com.mezi.flicker.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Anuja on 10/17/16.
 */
public class FlickrPics implements Parcelable {

    private FlickrPhotos photos;
    private String stat;

    protected FlickrPics(Parcel in) {
        photos = in.readParcelable(FlickrPhotos.class.getClassLoader());
        stat = in.readString();
    }

    public static final Creator<FlickrPics> CREATOR = new Creator<FlickrPics>() {
        @Override
        public FlickrPics createFromParcel(Parcel in) {
            return new FlickrPics(in);
        }

        @Override
        public FlickrPics[] newArray(int size) {
            return new FlickrPics[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(photos, i);
        parcel.writeString(stat);
    }

    public FlickrPhotos getPhotos() {
        return photos;
    }

    public void setPhotos(FlickrPhotos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
