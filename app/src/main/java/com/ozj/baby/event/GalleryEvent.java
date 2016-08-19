package com.ozj.baby.event;

import android.support.annotation.Nullable;

import com.ozj.baby.mvp.model.bean.Gallery;

import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class GalleryEvent {
    public List<Gallery> mList;
    public Gallery mGallery;
    public int mAction;

    public GalleryEvent(@Nullable List<Gallery> mList, @Nullable Gallery mGallery, int action) {
        this.mList = mList;
        this.mAction = action;
        this.mGallery = mGallery;
    }


}
