package com.ozj.baby.mvp.presenter.navigation;

import android.net.Uri;

import com.ozj.baby.base.BasePresenter;

/**
 * Created by YX201603-6 on 2016/4/25.
 */
public interface IGalleryPersenter extends BasePresenter {

    void fetchDataFromNetwork(boolean isFirst, int size, int page);

    void UploadPhoto(Uri uri);


    boolean isHavedLover();
    
}
