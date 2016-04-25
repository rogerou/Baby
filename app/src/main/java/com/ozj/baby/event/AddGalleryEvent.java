package com.ozj.baby.event;

import com.ozj.baby.mvp.model.bean.Gallery;

import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class AddGalleryEvent {
    private List<Gallery> list;

    public Gallery getGallery() {
        return gallery;
    }

    public AddGalleryEvent(Gallery gallery, boolean isList) {
        this.gallery = gallery;
        this.isList = isList;
    }

    public AddGalleryEvent(List<Gallery> list, boolean isList) {
        this.list = list;
        this.isList = isList;
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

    private Gallery gallery;
    boolean isList;

}
