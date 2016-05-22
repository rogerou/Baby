package com.ozj.baby.event;

import com.ozj.baby.mvp.model.bean.Gallery;

import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class AddGalleryEvent {
    private List<Gallery> list;
    private Gallery gallery;
    boolean isList;
    boolean isFresh;

    public AddGalleryEvent(List<Gallery> list, boolean isList, boolean isFresh) {
        this.list = list;
        this.isList = isList;
        this.isFresh = isFresh;
    }

    public AddGalleryEvent(Gallery gallery, boolean isList, boolean isFresh) {
        this.gallery = gallery;
        this.isList = isList;
        this.isFresh = isFresh;
    }

    public Gallery getGallery() {
        return gallery;
    }

    public void setGallery(Gallery gallery) {

        this.gallery = gallery;
    }

    public List<Gallery> getList() {
        return list;
    }

    public void setList(List<Gallery> list) {
        this.list = list;
    }

    public boolean isList() {
        return isList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public boolean isFresh() {
        return isFresh;
    }

    public void setFresh(boolean fresh) {
        isFresh = fresh;
    }

}
