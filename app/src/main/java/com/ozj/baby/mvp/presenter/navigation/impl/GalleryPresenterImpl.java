package com.ozj.baby.mvp.presenter.navigation.impl;

import android.support.annotation.NonNull;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.mvp.model.dao.GalleryDao;
import com.ozj.baby.mvp.model.rx.RxBabyRealm;
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
    private
    IGalleryView mGalleryView;

    @Inject
    public GalleryPresenterImpl(RxLeanCloud rxLeanCloud, RxBabyRealm babyRealm, PreferenceManager PreferenceManager) {
        this.mRxleanCloud = rxLeanCloud;
        this.mRealm = babyRealm;
        mPreferenceManager = PreferenceManager;
        com.orhanobut.logger.Logger.init(this.getClass().getSimpleName());
    }

    @Override

    public void UploadPhoto(File imgfile) {
        mGalleryView.showProgress("上传中...");
        try {
            AVFile file = AVFile.withFile(mPreferenceManager.getCurrentUserId(), imgfile);
            mRxleanCloud.UploadPicture(file)
                    .flatMap(new Func1<String, Observable<AVObject>>() {
                        @Override
                        public Observable<AVObject> call(String s) {
                            AVObject avObject = new AVObject(GalleryDao.TABLENAME);
                            avObject.put(GalleryDao.AUTHORID, mPreferenceManager.getCurrentUserId());
                            avObject.put(GalleryDao.IMGURL, s);
                            return mRxleanCloud.SaveByLeanCloud(avObject);
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<AVObject>() {
                        @Override
                        public void onCompleted() {
                            mGalleryView.hideProgress();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mGalleryView.hideProgress();
                            com.orhanobut.logger.Logger.e(e.getMessage());
                        }

                        @Override
                        public void onNext(AVObject avObject) {
                            Gallery gallery = new Gallery(avObject);
                            mRealm.saveGallery(gallery);
                            mGalleryView.showToast("保存成功");

                        }
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void fetchDataFromNetwork() {
        mGalleryView.showRefreshing();
        mRxleanCloud.FetchAllPicture(mPreferenceManager.getCurrentUserId(), mPreferenceManager.GetLoverID())
                .subscribe(new Observer<List<AVObject>>() {
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
                    public void onNext(List<AVObject> list) {
                        List<Gallery> mlist = new ArrayList<>();
                        for (AVObject o : list) {
                            mlist.add(new Gallery(o));
                        }
                        mRealm.saveGalleryList(mlist);
                    }
                });


    }

    @Override
    public List<Gallery> fetchDataFromLocal() {
        return mRealm.getAllGallery();

    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mGalleryView = (IGalleryView) view;
    }

    @Override
    public void detachView() {

    }
}
