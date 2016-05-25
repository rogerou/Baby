package com.ozj.baby.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.hyphenate.easeui.domain.User;
import com.ozj.baby.mvp.model.dao.SouvenirDao;


/**
 * Created by Administrator on 2016/4/20.
 */
@AVClassName(SouvenirDao.TABLENAME)
public class Souvenir extends AVObject implements Parcelable {
    private String Content;
    private String Picture;
    private User Author;
    private boolean IsLikedMine;
    private boolean IsLikedOther;
    private String AuthorId;
    private String ohterUserId;
    private int commentcount;

    public int getCommentcount() {
        return getInt(SouvenirDao.SOUVENIR_COMMENTCOUNT);
    }

    public void setCommentcount(int commentcount) {
        put(SouvenirDao.SOUVENIR_COMMENTCOUNT, commentcount);
    }


    public Souvenir() {
        super();

    }

    protected Souvenir(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Souvenir> CREATOR = AVObjectCreator.instance;

    public String getContent() {
        return getString(SouvenirDao.SOUVENIR_CONTENT);
    }

    public void setContent(String content) {
        put(SouvenirDao.SOUVENIR_CONTENT, content);
    }


    public String getPicture() {
        return getString(SouvenirDao.SOUVENIR_PICTUREURL);
    }

    public void setPicture(String picture) {
        put(SouvenirDao.SOUVENIR_PICTUREURL, picture);
    }

    public User getAuthor() {
        return (User) get(SouvenirDao.SOUVENIR_AUTHOR);
    }

    public void setAuthor(User author) {
        put(SouvenirDao.SOUVENIR_AUTHOR, author);
    }


    public boolean isLikedOther() {
        return getBoolean(SouvenirDao.SOUVENIR_ISLIKEOTHER);
    }

    public void setLikedOther(boolean likedOther) {
        put(SouvenirDao.SOUVENIR_ISLIKEOTHER, likedOther);
    }

    public boolean isLikedMine() {
        return getBoolean(SouvenirDao.SOUVENIR_ISLIKEME);
    }

    public void setLikedMine(boolean likedMine) {
        put(SouvenirDao.SOUVENIR_ISLIKEME, likedMine);
    }


    public String getAuthorId() {
        return getString(SouvenirDao.SOUVENIR_AUTHORID);
    }

    public void setAuthorId(String authorId) {
        put(SouvenirDao.SOUVENIR_AUTHORID, authorId);
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


    public String getOhterUserId() {
        return getString(SouvenirDao.SOUVENIR_OTHERUSERID);
    }

    public void setOhterUserId(String ohterUserId) {
        put(SouvenirDao.SOUVENIR_OTHERUSERID, ohterUserId);
    }


}
