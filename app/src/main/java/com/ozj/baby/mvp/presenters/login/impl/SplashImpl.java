package com.ozj.baby.mvp.presenters.login.impl;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;

import com.ozj.baby.mvp.presenters.login.ISplashPresenter;
import com.ozj.baby.mvp.views.login.ISplashView;

/**
 * Created by Administrator on 2016/3/25.
 */
public class SplashImpl implements ISplashPresenter, Handler.Callback {


    ISplashView splashView;

    @Override
    public void onActivityStart() {

    }

    @Override
    public void onActivityPause() {

    }

    @Override
    public void isWannaCloseSplash() {

    }

    @Override
    public void doingSplash() {

    }

    @Override
    public void attachView(@NonNull View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        
        
        return false;
    }
}
