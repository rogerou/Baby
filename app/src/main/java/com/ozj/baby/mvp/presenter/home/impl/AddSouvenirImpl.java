package com.ozj.baby.mvp.presenter.home.impl;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.orhanobut.logger.Logger;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.dao.SouvenirDao;
import com.ozj.baby.mvp.model.realm.BabyRealm;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.presenter.home.IAddSouvenirPresenter;
import com.ozj.baby.mvp.views.home.IAddSouvenirView;
import com.ozj.baby.util.PreferenceManager;

import java.io.File;
import java.io.FileNotFoundException;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/4/23.
 */
public class AddSouvenirImpl implements IAddSouvenirPresenter {

    private BabyRealm mBabyRealm;
    private RxLeanCloud mRxLeanCloud;
    private Activity mActivity;
    private PreferenceManager manager;
    IAddSouvenirView mView;

    @Inject
    public AddSouvenirImpl(Activity activity, BabyRealm babyRealm, RxLeanCloud rxLeanCloud, PreferenceManager preferenceManager) {
        this.mActivity = activity;
        this.mBabyRealm = babyRealm;
        this.mRxLeanCloud = rxLeanCloud;
        this.manager = preferenceManager;
    }

    @Override
    public void commit(final String content, File file) {
        if (TextUtils.isEmpty(content)) {
            mView.showToast("写点什么吧?");
            return;
        }
        if (file == null) {
            mView.showToast("选张好看的图片吧");
            return;
        }
        mView.showProgress("上传中...");
        AVFile avFile = null;
        try {
            avFile = AVFile.withFile(manager.getCurrentUserId(), file);
            mRxLeanCloud.UploadPicture(avFile)
                    .flatMap(new Func1<String, Observable<Boolean>>() {
                        @Override
                        public Observable<Boolean> call(String s) {
                            AVObject object = new AVObject(SouvenirDao.TABLENAME);
                            object.put(SouvenirDao.SOUVENIR_AUTHOR, AVUser.getCurrentUser());
                            object.put(SouvenirDao.SOUVENIR_AUTHORID, AVUser.getCurrentUser().getObjectId());
                            object.put(SouvenirDao.SOUVENIR_CONTENT, content);
                            object.put(SouvenirDao.SOUVENIR_ISLIKEME, false);
                            object.put(SouvenirDao.SOUVENIR_ISLIKEOTHER, false);
                            object.put(SouvenirDao.SOUVENIR_OTHERUSERID, manager.GetLoverID());
                            object.put(SouvenirDao.SOUVENIR_PICTUREURL, s);
                            Souvenir souvenir = new Souvenir(object);
                            souvenir.setTimeStamp(System.currentTimeMillis());
                            mBabyRealm.saveSouvenir(souvenir);
                            return mRxLeanCloud.SaveByLeanCloud(object);

                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onCompleted() {
                            mView.hideProgress();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.showToast("出错啦，请检查一下网络是否有问题？");
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                Logger.d("保存纪念册成功");
                            }
                        }
                    });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void attachView(@NonNull BaseView view) {

        mView = (IAddSouvenirView) view;

    }

    @Override
    public void detachView() {

    }
}
