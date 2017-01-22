package com.ozj.baby.mvp.presenter.navigation.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.avos.avoscloud.AVFile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.hyphenate.easeui.domain.User;
import com.orhanobut.logger.Logger;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.event.EventConstant;
import com.ozj.baby.event.GalleryEvent;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.presenter.navigation.IGalleryPresenter;
import com.ozj.baby.mvp.views.navigation.IGalleryView;
import com.ozj.baby.util.PreferenceManager;
import com.ozj.baby.util.SchedulersCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by YX201603-6 on 2016/4/25.
 */
public class GalleryPresenterImpl implements IGalleryPresenter {


    private final RxLeanCloud mRxLeanCloud;


    private final PreferenceManager mPreferenceManager;
    private final RxBus mRxBus;
    private IGalleryView mGalleryView;
    private final Context mContext;
    private Subscription mFetchAllPicture;

    @Inject
    public GalleryPresenterImpl(@ContextLife("Activity") Context context, RxLeanCloud rxLeanCloud, PreferenceManager PreferenceManager, RxBus rxBus) {
        this.mRxLeanCloud = rxLeanCloud;
        mPreferenceManager = PreferenceManager;
        mRxBus = rxBus;
        mContext = context;
        com.orhanobut.logger.Logger.init(this.getClass().getSimpleName());
    }


    @Override
    public void fetchDataFromNetwork(final boolean isFirst, int size, int page) {
        mGalleryView.showRefreshing();
        mFetchAllPicture = mRxLeanCloud.FetchAllPicture(mPreferenceManager.getCurrentUserId(), mPreferenceManager.GetLoverID(), isFirst, size, page)
                .observeOn(Schedulers.io())
                .map(new Func1<List<Gallery>, List<Gallery>>() {
                    @Override
                    public List<Gallery> call(List<Gallery> galleries) {
                        for (Gallery g : galleries) {
                            //如果是已经上传了高度和宽就直接跳过，加载相册速度会提高很多。
                            if (g.getHeight() != 0 && g.getWidth() != 0) {
                                continue;
                            }
                            getHeightAndWidth(g.getImgUrl(), g, true);
                        }
                        return galleries;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
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
                    public void onNext(List<Gallery> galleries) {
                        if (!galleries.isEmpty()) {
                            mRxBus.post(new GalleryEvent(galleries, null, EventConstant.REFRESH));
                        }
                    }
                });


    }

    @Override
    public void UploadPhoto(final Uri uri) {
        mGalleryView.showUpdating();
        AVFile file = null;
        try {
            file = AVFile.withFile(mPreferenceManager.getCurrentUserId(), new File(new URI(uri.toString())));
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
        mRxLeanCloud.UploadPicture(file)
                .observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<Gallery>>() {
                    @Override
                    public Observable<Gallery> call(String s) {
                        Gallery gallery = new Gallery();
                        gallery.setImgUrl(s);
                        gallery.setUser(User.getCurrentUser(User.class));
                        gallery.setAuthorId(mPreferenceManager.getCurrentUserId());
                        return mRxLeanCloud.saveGallery(getHeightAndWidth(uri.toString(), gallery, false));
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Gallery, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Gallery gallery) {
                        mRxBus.post(new GalleryEvent(null, gallery, EventConstant.ADD));
                        return mRxLeanCloud.PushToLover("Ta上传了一张新的相片", 1);
                    }
                }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                mGalleryView.UpdateComplete();
                mGalleryView.showSnackBar("上传成功");
            }

            @Override
            public void onError(Throwable e) {
                mGalleryView.UpdateComplete();
                mGalleryView.showSnackBar("上传失败，请稍后再试");
                com.orhanobut.logger.Logger.e(e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    Logger.d("上传图片成功");
                }
            }
        });
    }


    @Override
    public boolean isHavedLover() {

        return !TextUtils.isEmpty(mPreferenceManager.GetLoverID());
    }

    @Override
    public Gallery getHeightAndWidth(String url, Gallery gallery, boolean isUploaded) {
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(mContext).load(url).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            gallery.setHeight(bitmap.getHeight());
            gallery.setWidth(bitmap.getWidth());
            if (isUploaded) {
                gallery.saveInBackground();
            }
            bitmap.recycle();
        } else {
            gallery.setHeight(0);
            gallery.setWidth(0);
        }
        return gallery;
    }

    @Override
    public void deleteGalley(final Gallery gallery) {
        mRxLeanCloud.delete(gallery)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mGalleryView.showProgress("正在删除相片...");
                    }
                }).compose(SchedulersCompat.<Boolean>observeOnMainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        mGalleryView.showToast("删除成功");
                        mGalleryView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mGalleryView.hideProgress();
                        mGalleryView.showToast("删除失败");

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Logger.e("删除相片" + aBoolean);
                        mRxBus.post(new GalleryEvent(null, gallery, EventConstant.DELETE));
                    }
                });

    }


    @Override
    public void attachView(@NonNull BaseView view) {
        mGalleryView = (IGalleryView) view;
    }

    @Override
    public void detachView() {
        if (mFetchAllPicture != null && mFetchAllPicture.isUnsubscribed()) {
            mFetchAllPicture.unsubscribe();
        }
    }
}
