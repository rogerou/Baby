package com.ozj.baby.mvp.presenter.login;

import android.support.design.widget.TextInputLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.ozj.baby.base.BasePresenter;

/**
 * Created by Administrator on 2016/3/25.
 */
public interface ISplashPresenter extends BasePresenter {
    void onActivityStart();

    void onActivityPause();

    void beginAnimation(ImageView imageView, TextView slogan, ShimmerFrameLayout shimmerFrameLayout);

    void Register(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerRepeatPasswd);

    void Login(TextInputLayout usernameLogin, TextInputLayout passwdLogin);

    void isLoginButtonVisable();

    void doingSplash();

    boolean isAnimationRunning();
}
