package com.ozj.baby.mvp.presenters.login.impl;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.avos.avoscloud.AVUser;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.presenters.login.ISplashPresenter;
import com.ozj.baby.mvp.views.login.ISplashView;
import com.ozj.baby.util.PreferenceManager;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import rx.functions.Action1;

/**
 * Created by Roger ou on 2016/3/25.
 */
public class SplashPresenterImpl implements ISplashPresenter, Handler.Callback {

    ISplashView mSplashView;
    private Context mContext;
    private Activity mActivity;
    private PreferenceManager mPreferenceManager;
    private Handler mHandler;
    private static final int MESSAGE_WHAT = 1;

    @Inject
    public SplashPresenterImpl(@ContextLife("Activity") Context context, Activity activity, PreferenceManager preferenceManager) {
        mContext = context;
        mActivity = activity;
        mPreferenceManager = preferenceManager;

    }

    @Override
    public void onActivityStart() {
        if (mHandler != null && !mHandler.hasMessages(MESSAGE_WHAT)) {
            mHandler.sendEmptyMessage(MESSAGE_WHAT);
        }
    }

    @Override
    public void onActivityPause() {
        if (mHandler != null && mHandler.hasMessages(MESSAGE_WHAT)) {
            mHandler.removeMessages(MESSAGE_WHAT);
        }
    }

    @Override
    public void isLoginButtonVisable() {
        mHandler = new Handler(this);
        if (mPreferenceManager.isLogined()) {
            mSplashView.hideLoginButton();
        } else {
            mSplashView.showLoginButton();
        }
    }

    @Override
    public void doingSplash() {
        if (mPreferenceManager.isFirstTime()) {
            RxPermissions.getInstance(mContext)
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (!aBoolean) {
                                mSplashView.showToast("你拒绝了相关的权限");
                                mSplashView.close();
                            } else {
                                mPreferenceManager.saveFirsttime(false);
                            }
                        }
                    });
        }
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, 3000);
        }
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mSplashView = (ISplashView) view;

    }

    @Override
    public void detachView() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == MESSAGE_WHAT) {
            if (mSplashView.isAnimationRunning()) {
                mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, 300);
                return false;
            }
            if (mPreferenceManager.isLogined() && AVUser.getCurrentUser() != null) {
                mSplashView.toMainActivity();
            }
        }

        return false;
    }
}
