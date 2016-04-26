package com.ozj.baby.mvp.presenter.navigation;

import com.ozj.baby.base.BasePresenter;
import com.ozj.baby.mvp.model.bean.Gallery;

import java.io.File;
import java.util.List;

/**
 * Created by YX201603-6 on 2016/4/25.
 */
public interface IGalleryPersenter extends BasePresenter {

    void fetchDataFromNetwork();
}
