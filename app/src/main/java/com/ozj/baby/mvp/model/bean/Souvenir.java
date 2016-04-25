package com.ozj.baby.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.ozj.baby.mvp.model.dao.SouvenirDao;

import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2016/4/20.
 */
public class Souvenir  implements Comparable, Parcelable {
    private String Content;
    private Long timeStamp;
    private String Picture;
    private User Author;
    private boolean IsLikedMine;
    @PrimaryKey
    private String objectId;
    private boolean IsLikedOther;
    private String AuthorId;
    private String ohterUserId;

    public Souvenir(AVObject object, AVUser avUser) {
        this.Content = object.getString(SouvenirDao.SOUVENIR_CONTENT);
        if (object.getCreatedAt() != null) {
            this.timeStamp = object.getCreatedAt().getTime();
        }
        this.Picture = object.getString(SouvenirDao.SOUVENIR_PICTUREURL);
        this.Author = new User(avUser);
        this.IsLikedMine = object.getBoolean(SouvenirDao.SOUVENIR_ISLIKEME);
        this.objectId = object.getObjectId();
        this.IsLikedOther = object.getBoolean(SouvenirDao.SOUVENIR_ISLIKEOTHER);
        this.AuthorId = avUser.getObjectId();
        this.ohterUserId = avUser.getString(SouvenirDao.SOUVENIR_OTHERUSERID);
    }

    public Souvenir() {

    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public User getAuthor() {
        return Author;
    }

    public void setAuthor(User author) {
        Author = author;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }


    public boolean isLikedOther() {
        return IsLikedOther;
    }

    public void setLikedOther(boolean likedOther) {
        IsLikedOther = likedOther;
    }

    public boolean isLikedMine() {
        return IsLikedMine;
    }

    public void setLikedMine(boolean likedMine) {
        IsLikedMine = likedMine;
    }


    public String getAuthorId() {
        return AuthorId;
    }

    public void setAuthorId(String authorId) {
        AuthorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Souvenir souvenir = (Souvenir) o;

        return objectId.equals(souvenir.objectId);

    }

    @Override
    public int hashCode() {
        return objectId.hashCode();
    }

    @Override
    public int compareTo(Object another) {
        return this.timeStamp.compareTo(((Souvenir) another).getTimeStamp());
    }

    public String getOhterUserId() {
        return ohterUserId;
    }

    public void setOhterUserId(String ohterUserId) {
        this.ohterUserId = ohterUserId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Content);
        dest.writeValue(this.timeStamp);
        dest.writeString(this.Picture);
        dest.writeParcelable(this.Author, flags);
        dest.writeByte(IsLikedMine ? (byte) 1 : (byte) 0);
        dest.writeString(this.objectId);
        dest.writeByte(IsLikedOther ? (byte) 1 : (byte) 0);
        dest.writeString(this.AuthorId);
        dest.writeString(this.ohterUserId);
    }

    public Souvenir(Parcel in) {
        this.Content = in.readString();
        this.timeStamp = (Long) in.readValue(Long.class.getClassLoader());
        this.Picture = in.readString();
        this.Author = in.readParcelable(User.class.getClassLoader());
        this.IsLikedMine = in.readByte() != 0;
        this.objectId = in.readString();
        this.IsLikedOther = in.readByte() != 0;
        this.AuthorId = in.readString();
        this.ohterUserId = in.readString();
    }

    public static final Parcelable.Creator<Souvenir> CREATOR = new Parcelable.Creator<Souvenir>() {
        @Override
        public Souvenir createFromParcel(Parcel source) {
            return new Souvenir(source);
        }

        @Override
        public Souvenir[] newArray(int size) {
            return new Souvenir[size];
        }
    };
}
