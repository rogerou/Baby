package com.ozj.baby.mvp.presenter.home.impl;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.avos.avoscloud.AVObject;
import com.orhanobut.logger.Logger;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.event.UpdateComPlete;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.realm.BabyRealm;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.presenter.home.ISouvenirPresenter;
import com.ozj.baby.mvp.views.home.ISouvenirVIew;
import com.ozj.baby.util.PreferenceManager;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Roger on 2016/4/20.
 */
public class SouvenirPresenterImpl implements ISouvenirPresenter {

    private Context mContext;
    private Activity mActivity;
    private BabyRealm mBabyRealm;
    private RxLeanCloud mRxleanCloud;
    private RxBus mRxBus;

    private PreferenceManager mPreferencepManager;
    ISouvenirVIew mSouvenirView;
    int size = 20;
    int page = 0;

    @Inject
    public SouvenirPresenterImpl(@ContextLife("Activity") Context context, Activity activity, BabyRealm babyRealm, RxLeanCloud rxLeanCloud, PreferenceManager preferenceManager, RxBus rxbus) {
        mContext = context;
        mActivity = activity;
        mBabyRealm = babyRealm;
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
    public void RefreshingData() {
        mSouvenirView.showRefreshingLoading();


    }

    @Override
    public List<Souvenir> getDataFromLocal() {
        mSouvenirView.showRefreshingLoading();
        return mBabyRealm.getSouvenirALl();
    }


    @Override
    public void LoadingDataFromNet() {
        mSouvenirView.showRefreshingLoading();
        mRxleanCloud.GetALlSouvenirByLeanCloud(mPreferencepManager.getCurrentUserId(), mPreferencepManager.GetLoverID(), size, page++)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AVObject>>() {
                    @Override
                    public void onCompleted() {
                        mSouvenirView.hideRefreshingLoading();
                        mRxBus.post(new UpdateComPlete(true));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        mSouvenirView.showToast("先检查网络有没有问题，没有就是我的问题（坏笑脸）");
                    }

                    @Override
                    public void onNext(List<AVObject> avObjects) {
                        if (avObjects.size() == 0) {
                            return;
                        }
                        for (int i = 0; i < avObjects.size(); i++) {
                            mBabyRealm.saveSouvenir(new Souvenir(avObjects.get(i)));
                        }

                    }
                });
    }


    @Override
    public boolean isHavedLover() {
        return !TextUtils.isEmpty(mPreferencepManager.GetLoverID());
    }

    @Override
    public void addNewSouvenir() {
        if (isHavedLover()) {
            mSouvenirView.toAddNewSouvenirActivity();
        } else {
            mSouvenirView.ToProFileActivity();
            mSouvenirView.showToast("请先设置您的另一半才能使用哦");
        }


    }


    @Override
    public void attachView(@NonNull BaseView view) {
        mSouvenirView = (ISouvenirVIew) view;
    }

    @Override
    public void detachView() {

    }
}
