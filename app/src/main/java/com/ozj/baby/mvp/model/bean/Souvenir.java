package com.ozj.baby.mvp.model.bean;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.ozj.baby.mvp.model.dao.SouvenirDao;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2016/4/20.
 */
public class Souvenir extends RealmObject {
    private String Content;
    private long timeStamp;
    private String Picture;
    private User Author;
    private boolean IsLikedMine;
    @PrimaryKey
    private String objectId;
    private boolean IsLikedOther;
    private String theOtherUserID;
    private String AuthorId;

    public Souvenir(AVObject object) {
        AVUser user = (AVUser) object.get(SouvenirDao.SOUVENIR_AUTHOR);
        this.Content = object.getString(SouvenirDao.SOUVENIR_CONTENT);
        this.timeStamp = object.getCreatedAt().getTime();
        this.Picture = object.getAVFile(SouvenirDao.SOUVENIR_PICTUREURL).getUrl();
        this.Author = new User(user);
        this.IsLikedMine = object.getBoolean(SouvenirDao.SOUVENIR_ISLIKEME);
        this.objectId = object.getObjectId();
        this.theOtherUserID = object.getString(SouvenirDao.SOUVENIR_THEOTHERID);
        this.IsLikedOther = object.getBoolean(SouvenirDao.SOUVENIR_ISLIKEOTHER);
        this.AuthorId = user.getObjectId();
    }

    public Souvenir() {
        
    }

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


    public String getTheOtherUserID() {
        return theOtherUserID;
    }

    public void setTheOtherUserID(String theOtherUserID) {
        this.theOtherUserID = theOtherUserID;
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
}
