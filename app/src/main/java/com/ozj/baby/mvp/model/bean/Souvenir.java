package com.ozj.baby.mvp.model.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Administrator on 2016/4/20.
 */
public class Souvenir extends RealmObject {
    @Required
    private String Content;
    private long timeStamp;
    private String Picture;
    private User Author;
    private boolean IsLikedMine;
    @PrimaryKey
    private String objectId;
    private boolean IsLikedOther;
    private String theOtherUserID;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
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
}
