package com.ozj.baby.mvp.presenter.home.impl;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.rx.RxRealm;
import com.ozj.baby.mvp.presenter.home.ISouvenirPresenter;
import com.ozj.baby.mvp.views.home.ISouvenirVIew;

import javax.inject.Inject;

import io.realm.RealmResults;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/4/20.
 */
public class SouvenirPresenterImpl implements ISouvenirPresenter {

    private Context mContext;
    private Activity mActivity;
    private RxRealm mRxRealm;

    ISouvenirVIew souvenirVIew;

    @Inject
    public SouvenirPresenterImpl(@ContextLife("Activity") Context context, Activity activity, RxRealm rxRealm) {
        mContext = context;
        mActivity = activity;
        mRxRealm = rxRealm;

    }


    @Override
    public void AutoLoadingMore() {
        souvenirVIew.showRefreshingLoading();
    }

    @Override
    public void RefreshingData() {
        souvenirVIew.showRefreshingLoading();
        

    }

    @Override
    public void ToDetailActivity() {

    }

    @Override
    public void LoadingComplete() {
        souvenirVIew.hideRefreshingLoading();
    }


    @Override
    public void LoadingData() {
        souvenirVIew.showRefreshingLoading();
        mRxRealm.getAllSouvenir().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RealmResults<Souvenir>>() {
                    @Override
                    public void call(RealmResults<Souvenir> souvenirs) {
                             
                    }
                });
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        souvenirVIew = (ISouvenirVIew) view;
    }

    @Override
    public void detachView() {

    }
}
