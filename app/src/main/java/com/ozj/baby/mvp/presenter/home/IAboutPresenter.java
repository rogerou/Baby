package com.ozj.baby.mvp.presenter.home;

import com.ozj.baby.base.BasePresenter;

/**
 * Created by YX201603-6 on 2016/5/3.
 */
public interface IAboutPresenter extends BasePresenter {

    void checkVersion();

    void downloadApk(String url);
}
