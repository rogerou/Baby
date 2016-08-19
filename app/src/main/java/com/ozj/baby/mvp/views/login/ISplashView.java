package com.ozj.baby.mvp.views.login;

import android.support.design.widget.TextInputLayout;

import com.ozj.baby.base.BaseView;

/**
 * Created by Administrator on 2016/3/25.
 */
public interface ISplashView extends BaseView {

    void toMainActivity();


    boolean isLoginViewisShowing();

    void showLoginView();

    void dismissLoginView();

    void hideLoginButton();

    void showLoginButton();


}
