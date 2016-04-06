package com.ozj.baby.mvp.presenters.login;

import com.ozj.baby.base.BasePresenter;

/**
 * Created by Administrator on 2016/3/25.
 */
public interface ISplashPresenter extends BasePresenter {
    void onActivityStart();

    void onActivityPause();

    void isWannaCloseSplash();

    void doingSplash();
}
