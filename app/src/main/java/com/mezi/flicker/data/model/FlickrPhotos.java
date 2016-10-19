package com.mezi.flicker.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Anuja on 10/17/16.
 */
public class FlickrPhotos implements Parcelable {
    private String page ;
    private String pages ;
    private String perpage ;
    private String total ;
    private ArrayList<FlickrPhotoInfo> photo ;


    protected FlickrPhotos(Parcel in) {
        page = in.readString();
        pages = in.readString();
        perpage = in.readString();
        total = in.readString();
        photo = in.readArrayList(FlickrPhotoInfo.class.getClassLoader());
    }

    public static final Creator<FlickrPhotos> CREATOR = new Creator<FlickrPhotos>() {
        @Override
        public FlickrPhotos createFromParcel(Parcel in) {
            return new FlickrPhotos(in);
        }

        @Override
        public FlickrPhotos[] newArray(int size) {
            return new FlickrPhotos[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(page);
        parcel.writeString(pages);
        parcel.writeString(perpage);
        parcel.writeString(total);
        parcel.writeList(photo);
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getPerpage() {
        return perpage;
    }

    public void setPerpage(String perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<FlickrPhotoInfo> getPhoto() {
        return photo;
    }

    public void setPhoto(ArrayList<FlickrPhotoInfo> photo) {
        this.photo = photo;
    }
}
