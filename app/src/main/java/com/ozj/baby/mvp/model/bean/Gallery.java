package com.ozj.baby.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;


import com.avos.avoscloud.AVObject;
import com.ozj.baby.mvp.model.dao.GalleryDao;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by YX201603-6 on 2016/4/25.
 */
public class Gallery extends RealmObject implements Parcelable {
    private String imgUrl;
    private long timement;
    @PrimaryKey
    private String objectId;
    private String authorId;

    public Gallery(AVObject object) {
        this.imgUrl = object.getString(GalleryDao.IMGURL);
        this.timement = object.getCreatedAt().getTime();
        this.objectId = object.getObjectId();
        this.authorId = object.getString(GalleryDao.AUTHORID);
    }


    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public long getTimement() {
        return timement;
    }

    public void setTimement(long timement) {
        this.timement = timement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gallery gallery = (Gallery) o;

        return objectId.equals(gallery.objectId);

    }

    @Override
    public int hashCode() {
        return objectId.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgUrl);
        dest.writeLong(this.timement);
        dest.writeString(this.objectId);
    }

    public Gallery() {
    }

    protected Gallery(Parcel in) {
        this.imgUrl = in.readString();
        this.timement = in.readLong();
        this.objectId = in.readString();
    }

    public static final Creator<Gallery> CREATOR = new Creator<Gallery>() {
        @Override
        public Gallery createFromParcel(Parcel source) {
            return new Gallery(source);
        }

        @Override
        public Gallery[] newArray(int size) {
            return new Gallery[size];
        }
    };

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}
