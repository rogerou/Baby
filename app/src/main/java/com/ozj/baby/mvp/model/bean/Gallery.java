package com.ozj.baby.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;


import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.ozj.baby.mvp.model.dao.GalleryDao;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by YX201603-6 on 2016/4/25.
 */

@AVClassName(GalleryDao.TABLENAME)
public class Gallery extends AVObject implements Parcelable {
    private String imgUrl;
    private String authorId;
    private User user;

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

    @Override
    public int describeContents() {
        return 0;
    }


    public Gallery() {
    }


    public String getAuthorId() {
        return getString(GalleryDao.AUTHORID);
    }

    public void setAuthorId(String authorId) {
        put(GalleryDao.AUTHORID, authorId);
    }
}
