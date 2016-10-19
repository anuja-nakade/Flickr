package com.mezi.flicker.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anuja on 10/17/16.
 */
public class FlickrPhotoInfo implements Parcelable {
    private String id;
    private String owner;
    private String secret;
    private String server;
    private String farm;
    private String title;

    @SerializedName(value = "ispublic")
    private int isPublic;

    @SerializedName(value = "isfriend")
    private int isFriend;

    @SerializedName(value = "isfamily")
    private int isFamily;

    @SerializedName(value = "url_n")
    private String url;

    @SerializedName(value = "height_n")
    private Object height;

    @SerializedName(value = "width_n")
    private Object width;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public int getIsFamily() {
        return isFamily;
    }

    public void setIsFamily(int isFamily) {
        this.isFamily = isFamily;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Object getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    private FlickrPhotoInfo(Parcel in){
        this.id = in.readString();
        this.owner = in.readString();
        this.secret = in.readString();
        this.server = in.readString();
        this.farm = in.readString();
        this.title = in.readString();
        this.isPublic = in.readInt();
        this.isFriend = in.readInt();
        this.isFamily = in.readInt();
        this.url = in.readString();
        this.height = in.readValue(ClassLoader.getSystemClassLoader());
        this.width = in.readValue(ClassLoader.getSystemClassLoader());
    }

    public static final Creator<FlickrPhotoInfo> CREATOR = new Creator<FlickrPhotoInfo>() {
        @Override
        public FlickrPhotoInfo createFromParcel(Parcel in) {
            return new FlickrPhotoInfo(in);
        }

        @Override
        public FlickrPhotoInfo[] newArray(int size) {
            return new FlickrPhotoInfo[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.owner);
        parcel.writeString(this.secret);
        parcel.writeString(this.server);
        parcel.writeString(this.farm);
        parcel.writeString(this.title);
        parcel.writeInt(this.isPublic);
        parcel.writeInt(this.isFriend);
        parcel.writeInt(this.isFamily);
        parcel.writeString(this.url);
        parcel.writeValue(this.height);
        parcel.writeValue(this.width);
    }
}
