package com.ozj.baby.mvp.presenter.home.impl;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.model.rx.RxRealm;
import com.ozj.baby.mvp.presenter.home.ISouvenirPresenter;
import com.ozj.baby.mvp.views.home.ISouvenirVIew;
import com.ozj.baby.util.PreferenceManager;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Roger on 2016/4/20.
 */
public class SouvenirPresenterImpl implements ISouvenirPresenter {

    private Context mContext;
    private Activity mActivity;
    private RxRealm mRxRealm;
    private RxLeanCloud mRxleanCloud;
    private PreferenceManager mPreferencepManager;
    ISouvenirVIew mSouvenirView;
    int size = 20;
    int page = 0;
    List<Souvenir> mlist;

    @Inject
    public SouvenirPresenterImpl(@ContextLife("Activity") Context context, Activity activity, RxRealm rxRealm, RxLeanCloud rxLeanCloud, PreferenceManager preferenceManager) {
        mContext = context;
        mActivity = activity;
        mRxRealm = rxRealm;
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
    public List<Souvenir> GetDataFromLoad() {
        mSouvenirView.showProgress("加载中...");
        mRxRealm.getAllSouvenir().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<Souvenir>>() {
            @Override
            public void call(List<Souvenir> souvenirs) {
                if (souvenirs.size() != 0) {
                    mlist = souvenirs;
                }
            }
        });
        mSouvenirView.hideProgress();
        return mlist;
    }


    @Override
    public void LoadingData() {
        mSouvenirView.showRefreshingLoading();
        mRxRealm.getAllSouvenir().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Souvenir>>() {
                    @Override
                    public void call(List<Souvenir> souvenirs) {
                        if (souvenirs.size() == 0) {
                            return;
                        }

                    }
                });
//        mRxleanCloud.GetALlSouvenirByLeanCloud(mPreferencepManager.getCurrentUserId(), mPreferencepManager.GetLoverID(), size, page)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<AVObject>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Logger.e(e.getMessage());
//                        mSouvenirView.showToast("先检查网络有没有问题，没有就是我的问题（坏笑脸）");
//                    }
//
//                    @Override
//                    public void onNext(List<AVObject> avObjects) {
//                        if (avObjects.size() == 0) {
//                            mSouvenirView.showToast("你们之间什么记录都没有啦，赶紧加几个阿");
//                            return;
//                        }
//                        mRxRealm.SaveSouvenir(avObjects);
//                    }
//                });
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
