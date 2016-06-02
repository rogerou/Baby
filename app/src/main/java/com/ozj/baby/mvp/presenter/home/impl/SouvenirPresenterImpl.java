package com.ozj.baby.mvp.presenter.home.impl;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.event.AddSouvenirEvent;
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
import rx.Subscription;

/**
 * Created by Roger on 2016/4/20.
 */
public class SouvenirPresenterImpl implements ISouvenirPresenter {

    private RxLeanCloud mRxleanCloud;
    private RxBus mRxBus;

    private PreferenceManager mPreferencepManager;
    ISouvenirVIew mSouvenirView;
    Subscription getAllSouvenir;

    @Inject
    public SouvenirPresenterImpl(RxLeanCloud rxLeanCloud, PreferenceManager preferenceManager, RxBus rxbus) {
        mRxBus = rxbus;
        mRxleanCloud = rxLeanCloud;
        mPreferencepManager = preferenceManager;
        Logger.init(this.getClass().getSimpleName());
    }


    @Override
    public void AutoLoadingMore() {
        mSouvenirView.showRefreshingLoading();
    }


    @Override
    public void LoadingDataFromNet(final boolean isFresh, final int size, final int page) {
        mSouvenirView.showRefreshingLoading();
        getAllSouvenir = mRxleanCloud.GetALlSouvenirByLeanCloud(mPreferencepManager.getCurrentUserId(), mPreferencepManager.GetLoverID(), size, page)
               
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
                        if (!list.isEmpty()) {
                            mRxBus.post(new AddSouvenirEvent(isFresh, true, list));
                        }
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
