package com.ozj.baby.mvp.views.login;

import com.ozj.baby.base.BaseView;

/**
 * Created by Administrator on 2016/3/25.
 */
public interface ISplashView extends BaseView {

    void toMainActivity();


    boolean isLoginViewShowing();

    void showLoginView();

    void dismissLoginView();

    void hideLoginButton();

    void showLoginButton();


}
