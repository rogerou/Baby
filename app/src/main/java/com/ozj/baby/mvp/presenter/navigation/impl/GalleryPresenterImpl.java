package com.ozj.baby.mvp.presenter.navigation.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.avos.avoscloud.AVFile;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.event.AddGalleryEvent;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.mvp.model.bean.User;
import com.ozj.baby.mvp.model.rx.RxBabyRealm;
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
    public void fetchDataFromNetwork(boolean isFirst, int size, int page) {
        mGalleryView.showRefreshing();
        mRxleanCloud.FetchAllPicture(mPreferenceManager.getCurrentUserId(), mPreferenceManager.GetLoverID(), isFirst, size, page)
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
                            mRxbus.post(new AddGalleryEvent(list, true, false));
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
                .flatMap(new Func1<String, Observable<Gallery>>() {
                    @Override
                    public Observable<Gallery> call(String s) {
                        Gallery gallery = new Gallery();
                        gallery.setImgUrl(s);
                        gallery.setUser(User.getCurrentUser(User.class));
                        gallery.setAuthorId(mPreferenceManager.getCurrentUserId());
                        try {
                            Bitmap bitmap = Glide.with(mContext).load(s).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                            if (bitmap != null) {
                                gallery.setHeight(bitmap.getHeight());
                                gallery.setWidth(bitmap.getWidth());
                            } else {
                                gallery.setHeight(0);
                                gallery.setWidth(0);
                            }

                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        return mRxleanCloud.saveGallery(gallery);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Gallery>() {
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
                    public void onNext(Gallery gallery) {
                        mRxbus.post(new AddGalleryEvent(gallery, false, true));
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
