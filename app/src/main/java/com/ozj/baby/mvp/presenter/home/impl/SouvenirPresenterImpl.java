package com.ozj.baby.mvp.presenter.home.impl;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.event.EventConstant;
import com.ozj.baby.event.SouvenirEvent;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.presenter.home.ISouvenirPresenter;
import com.ozj.baby.mvp.views.home.ISouvenirVIew;
import com.ozj.baby.util.PreferenceManager;
import com.ozj.baby.util.SchedulersCompat;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;

/**
 * Created by Roger on 2016/4/20.
 */
public class SouvenirPresenterImpl implements ISouvenirPresenter {

    private final RxLeanCloud mRxLeanCloud;
    private final RxBus mRxBus;
    private final PreferenceManager mPreferenceManager;
    private ISouvenirVIew mSouvenirView;
    private Subscription getAllSouvenir;

    @Inject
    public SouvenirPresenterImpl(RxLeanCloud rxLeanCloud, PreferenceManager preferenceManager, RxBus rxbus) {
        mRxBus = rxbus;
        mRxLeanCloud = rxLeanCloud;
        mPreferenceManager = preferenceManager;
        Logger.init(this.getClass().getSimpleName());
    }


    @Override
    public void LoadingDataFromNet(final boolean isFresh, final int size, final int page) {
        mSouvenirView.showRefreshingLoading();
        getAllSouvenir = mRxLeanCloud.GetALlSouvenirByLeanCloud(mPreferenceManager.getCurrentUserId(), mPreferenceManager.GetLoverID(), size, page)

                .compose(SchedulersCompat.<List<Souvenir>>applyIoSchedulers())
                .subscribe(new Observer<List<Souvenir>>() {
                    @Override
                    public void onCompleted() {
                        mSouvenirView.hideRefreshingLoading();

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSouvenirView.hideRefreshingLoading();
                        mSouvenirView.showToast("可能出了点错误哦");
                    }

                    @Override
                    public void onNext(List<Souvenir> list) {
                        if (page == 0) {
                            mRxBus.post(new SouvenirEvent(null, list, EventConstant.REFRESH));
                        } else {
                            mRxBus.post(new SouvenirEvent(null, list, EventConstant.LOADMORE));
                        }

                    }
                });


    }

    @Override
    public void delete(final Souvenir souvenir) {
        mRxLeanCloud.delete(souvenir)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mSouvenirView.showProgress("正在删除Moment...");
                    }
                }).compose(SchedulersCompat.<Boolean>observeOnMainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        mSouvenirView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSouvenirView.hideProgress();
                        mSouvenirView.showToast("出错啦");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        mSouvenirView.showToast("删除成功");
                        mRxBus.post(new SouvenirEvent(souvenir, null, EventConstant.DELETE));
                    }
                });
    }


    @Override
    public void attachView(@NonNull BaseView view) {
        mSouvenirView = (ISouvenirVIew) view;
    }

    @Override
    public void detachView() {
        if (getAllSouvenir != null && !getAllSouvenir.isUnsubscribed()) {
            getAllSouvenir.unsubscribe();
        }
    }
}
