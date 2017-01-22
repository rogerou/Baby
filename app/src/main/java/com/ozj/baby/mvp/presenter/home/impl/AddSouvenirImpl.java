package com.ozj.baby.mvp.presenter.home.impl;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.hyphenate.easeui.domain.User;
import com.orhanobut.logger.Logger;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.event.EventConstant;
import com.ozj.baby.event.SouvenirEvent;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.rx.RxBus;
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

    private final RxLeanCloud mRxLeanCloud;
    private final PreferenceManager manager;
    private final RxBus mRxBus;
    private IAddSouvenirView mView;

    @Inject
    public AddSouvenirImpl(RxBus mRxbus, RxLeanCloud rxLeanCloud, PreferenceManager preferenceManager) {
        this.mRxLeanCloud = rxLeanCloud;
        this.manager = preferenceManager;
        this.mRxBus = mRxbus;
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
                    .flatMap(new Func1<String, Observable<Souvenir>>() {
                        @Override
                        public Observable<Souvenir> call(String s) {
                            Souvenir object = new Souvenir();
                            object.setAuthorId(AVUser.getCurrentUser().getObjectId());
                            object.setAuthor(User.getCurrentUser(User.class));
                            object.setContent(content);
                            object.setLikedMine(false);
                            object.setLikedOther(false);
                            object.setOhterUserId(manager.GetLoverID());
                            object.setPicture(s);
                            object.setCommentcount(0);
                            return mRxLeanCloud.SaveSouvenirByLeanCloud(object);

                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Func1<Souvenir, Observable<Boolean>>() {
                        @Override
                        public Observable<Boolean> call(Souvenir souvenir) {
                            mRxBus.post(new SouvenirEvent(souvenir, null, EventConstant.ADD));
                            return mRxLeanCloud.PushToLover("Ta发布了一个新的Moment", 0);
                        }
                    })
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onCompleted() {
                            mView.showToast("发布成功");
                            mView.hideProgress();
                            mView.close();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e(e.getMessage());
                            mView.hideProgress();
                            mView.showToast("上传失败了，请稍后再试吧");
                        }

                        @Override
                        public void onNext(Boolean bollean) {
                            if (bollean) {
                                Logger.d("发布Moment并推送成功");
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
