package com.ozj.baby.mvp.model.bean;

import android.os.Parcelable;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.hyphenate.easeui.domain.User;
import com.ozj.baby.mvp.model.dao.GalleryDao;

/**
 * Created by YX201603-6 on 2016/4/25.
 */

@AVClassName(GalleryDao.TABLENAME)
public class Gallery extends AVObject implements Parcelable {
    private String imgUrl;
    private String authorId;
    private User user;
    private int height;
    private int width;

    public Gallery() {
    }

    public int getHeight() {
        return getInt(GalleryDao.HEIGHT);
    }

    public void setHeight(int height) {
        put(GalleryDao.HEIGHT, height);
    }

    public int getWidth() {
        return getInt(GalleryDao.WIDTH);
    }

    public void setWidth(int width) {
        put(GalleryDao.WIDTH, width);
    }

    public User getUser() {
        return (User) get(GalleryDao.AUTHOR);
    }

    public void setUser(User user) {
        put(GalleryDao.AUTHOR, user);
    }


    public String getImgUrl() {
        return getString(GalleryDao.IMGURL);
    }

    public void setImgUrl(String imgUrl) {
        put(GalleryDao.IMGURL, imgUrl);

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

    public String getAuthorId() {
        return getString(GalleryDao.AUTHORID);
    }

    public void setAuthorId(String authorId) {
        put(GalleryDao.AUTHORID, authorId);
    }
}
