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
import com.orhanobut.logger.Logger;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.event.AddGalleryEvent;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.mvp.model.bean.User;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.presenter.navigation.IGalleryPersenter;
import com.ozj.baby.mvp.views.navigation.IGalleryView;
import com.ozj.baby.util.PreferenceManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by YX201603-6 on 2016/4/25.
 */
public class GalleryPresenterImpl implements IGalleryPersenter {


    private RxLeanCloud mRxleanCloud;


    private PreferenceManager mPreferenceManager;
    private RxBus mRxbus;
    private IGalleryView mGalleryView;
    private Context mContext;


    @Inject
    public GalleryPresenterImpl(@ContextLife("Activity") Context context, RxLeanCloud rxLeanCloud, PreferenceManager PreferenceManager, RxBus rxBus) {
        this.mRxleanCloud = rxLeanCloud;
        mPreferenceManager = PreferenceManager;
        mRxbus = rxBus;
        mContext = context;
        com.orhanobut.logger.Logger.init(this.getClass().getSimpleName());
    }


    @Override
    public void fetchDataFromNetwork(final boolean isFirst, int size, int page) {
        mGalleryView.showRefreshing();
        mRxleanCloud.FetchAllPicture(mPreferenceManager.getCurrentUserId(), mPreferenceManager.GetLoverID(), isFirst, size, page)
                .observeOn(Schedulers.io())
                .map(new Func1<List<Gallery>, List<Gallery>>() {


                    @Override
                    public List<Gallery> call(List<Gallery> galleries) {
                        try {
                            for (Gallery g :
                                    galleries) {
                                Bitmap bitmap = Glide.with(mContext).load(g.getImgUrl()).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                                if (bitmap != null) {
                                    g.setHeight(bitmap.getHeight());
                                    g.setWidth(bitmap.getWidth());
                                } else {
                                    g.setHeight(0);
                                    g.setWidth(0);
                                }

                            }

                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
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
                        if (galleries.size() != 0) {
                            mRxbus.post(new AddGalleryEvent(galleries, true, isFirst));
                        }
                    }
                });


    }

    @Override
    public void UploadPhoto(Uri uri) {
        mGalleryView.showUpdating();
        AVFile file = null;
        try {
            file = AVFile.withFile(mPreferenceManager.getCurrentUserId(), new File(new URI(uri.toString())));
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
        mRxleanCloud.UploadPicture(file)
                .observeOn(Schedulers.io()
                )
                .flatMap(new Func1<String, Observable<Gallery>>() {
                    @Override
                    public Observable<Gallery> call(String s) {
                        Gallery gallery = new Gallery();
                        gallery.setImgUrl(s);
                        gallery.setUser(User.getCurrentUser(User.class));
                        gallery.setAuthorId(mPreferenceManager.getCurrentUserId());
                        Bitmap bitmap = null;
                        try {
                            bitmap = Glide.with(mContext).load(s).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        if (bitmap != null) {
                            gallery.setHeight(bitmap.getHeight());
                            gallery.setWidth(bitmap.getWidth());
                        } else {
                            gallery.setHeight(0);
                            gallery.setWidth(0);
                        }

                        return mRxleanCloud.saveGallery(gallery);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Gallery, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Gallery gallery) {
                        mRxbus.post(new AddGalleryEvent(gallery, false, true));
                        return mRxleanCloud.PushToLover("Ta上传了一张新的相片", 1);
                    }
                }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                mGalleryView.UpdateCompelte();
                mGalleryView.showSnackBar("上传成功");
            }

            @Override
            public void onError(Throwable e) {
                mGalleryView.UpdateCompelte();
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
    public void attachView(@NonNull BaseView view) {
        mGalleryView = (IGalleryView) view;
    }

    @Override
    public void detachView() {

    }
}
