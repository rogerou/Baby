package com.ozj.baby.mvp.presenter.navigation;

import android.net.Uri;

import com.ozj.baby.base.BasePresenter;
import com.ozj.baby.mvp.model.bean.Gallery;

/**
 * Created by YX201603-6 on 2016/4/25.
 */
public interface IGalleryPresenter extends BasePresenter {

    void fetchDataFromNetwork(boolean isFirst, int size, int page);

    void UploadPhoto(Uri uri);


    boolean isHavedLover();

    Gallery getHeightAndWidth(String url, Gallery gallery, boolean isUploaded);

    void deleteGalley(Gallery gallery);

}
