package com.ozj.baby.mvp.presenter.home.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.presenter.home.IAboutPresenter;
import com.ozj.baby.mvp.views.home.IAboutView;

import javax.inject.Inject;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

/**
 * Created by YX201603-6 on 2016/5/3.
 */
public class AboutPresenterImpl implements IAboutPresenter {


    private Context mContext;

    @Inject
    public AboutPresenterImpl(@ContextLife("Activity") Context context) {
        mContext = context;
    }

    IAboutView mAboutView;

    @Override
    public void checkVersion() {
        FIR.checkForUpdateInFIR("85d4be69c008c7b81331a0476728d459", new VersionCheckCallback() {
            @Override
            public void onSuccess(String versionJson) {
                Log.i("fir", "check from fir.im success! " + "\n" + versionJson);
            }

            @Override
            public void onFail(Exception exception) {
                Log.i("fir", "check fir.im fail! " + "\n" + exception.getMessage());
            }

            @Override
            public void onStart() {
                mAboutView.showToast("正在获取");
            }

            @Override
            public void onFinish() {
                mAboutView.showToast("检查更新完成");
            }
        });
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mAboutView = (IAboutView) view;
    }

    @Override
    public void detachView() {

    }
}
