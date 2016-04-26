package com.ozj.baby.mvp.presenter.navigation.impl;

import android.support.annotation.NonNull;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.event.AddGalleryEvent;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.mvp.model.bean.User;
import com.ozj.baby.mvp.model.dao.GalleryDao;
import com.ozj.baby.mvp.model.rx.RxBabyRealm;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.presenter.navigation.IGalleryPersenter;
import com.ozj.baby.mvp.views.navigation.IGalleryView;
import com.ozj.baby.util.PreferenceManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by YX201603-6 on 2016/4/25.
 */
public class GalleryPresenterImpl implements IGalleryPersenter {


    private RxLeanCloud mRxleanCloud;

    private RxBabyRealm mRealm;

    private PreferenceManager mPreferenceManager;
    private RxBus mRxbus;
    private IGalleryView mGalleryView;

    @Inject
    public GalleryPresenterImpl(RxLeanCloud rxLeanCloud, RxBabyRealm babyRealm, PreferenceManager PreferenceManager, RxBus rxBus) {
        this.mRxleanCloud = rxLeanCloud;
        this.mRealm = babyRealm;
        mPreferenceManager = PreferenceManager;
        mRxbus = rxBus;
        com.orhanobut.logger.Logger.init(this.getClass().getSimpleName());
    }


    @Override
    public void fetchDataFromNetwork() {
        mGalleryView.showRefreshing();
        mRxleanCloud.FetchAllPicture(mPreferenceManager.getCurrentUserId(), mPreferenceManager.GetLoverID())
                .subscribe(new Observer<List<Gallery>>() {
                    @Override
                    public void onCompleted() {
                        mGalleryView.hideRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mGalleryView.hideRefreshing();
                        mGalleryView.showToast("出错啦，请稍后再试");
                    }

                    @Override
                    public void onNext(List<Gallery> list) {
                        if (list.size() != 0) {
                            mRxbus.post(new AddGalleryEvent(list, true));
                        }
                    }
                });


    }


    @Override
    public void attachView(@NonNull BaseView view) {
        mGalleryView = (IGalleryView) view;
    }

    @Override
    public void detachView() {

    }
}
