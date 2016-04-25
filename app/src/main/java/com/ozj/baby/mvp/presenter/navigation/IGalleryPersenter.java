package com.ozj.baby.mvp.presenter.navigation;

import com.ozj.baby.base.BasePresenter;

import java.io.File;

/**
 * Created by YX201603-6 on 2016/4/25.
 */
public interface IGalleryPersenter extends BasePresenter {

    void UploadPhoto(File imgfile);

    void fetchDataFromNetwork();

    void fetchDataFromLocal();

}
