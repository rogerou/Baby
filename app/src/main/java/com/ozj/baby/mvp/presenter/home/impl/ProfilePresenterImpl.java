package com.ozj.baby.mvp.presenter.home.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.widget.ImageView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.bean.User;
import com.ozj.baby.mvp.model.dao.UserDao;
import com.ozj.baby.mvp.model.realm.BabyRealm;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.presenter.home.IProfilePresenter;
import com.ozj.baby.mvp.views.home.IProfileView;
import com.ozj.baby.util.PreferenceManager;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import pl.aprilapps.easyphotopicker.EasyImage;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/4/21.
 */
public class ProfilePresenterImpl implements IProfilePresenter {

    private Context mContext;
    private Activity mActivity;
    IProfileView mProfileView;
    private RxLeanCloud mRxleanCloud;
    private BabyRealm mBabyRealm;
    private PreferenceManager mPreferenceManager;

    @Inject
    public ProfilePresenterImpl(@ContextLife("Activity") Context context, Activity activity, RxLeanCloud rxLeanCloud, BabyRealm babyRealm, PreferenceManager preferenmanager) {
        mContext = context;
        mActivity = activity;
        mBabyRealm = babyRealm;
        mRxleanCloud = rxLeanCloud;
        mPreferenceManager = preferenmanager;
        Logger.init(this.getClass().getSimpleName());
    }


    @Override
    public void commit(String nick, String lover, String city, String sex) {
        mProfileView.showProgress("更新中...");
        final AVUser avUser = AVUser.getCurrentUser();
        if (nick == null && lover == null && city == null && sex == null) {
            mProfileView.showToast("你全部都是空的你改什么呀");
            return;
        }
        if (nick != null) {
            avUser.put(UserDao.NICK, nick);
        }
        if (lover != null) {
            avUser.put(UserDao.LOVERUSERNAME, lover);
        }
        if (city != null) {
            avUser.put(UserDao.CITY, city);
        }
        if (sex != null) {
            avUser.put(UserDao.SEX, sex);
        }
        
        User realmuser = new User(avUser);
        mBabyRealm.saveUser(realmuser);
        mProfileView.hideProgress();
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public void initData(final ImageView avatar, final TextInputLayout nick, final TextInputLayout loverid, final TextInputLayout city, final TextInputLayout sex) {
        mProfileView.showProgress("加载中...");
        User user = new User(AVUser.getCurrentUser());
        if (user.getAvatar() != null) {
            Glide.with(mContext).load(user.getAvatar()).crossFade().bitmapTransform(new CropCircleTransformation(mContext)).diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar);
        }
        if (user.getCity() != null) {
            city.getEditText().setText(user.getCity());
        }
        if (user.getSex() != null) {
            sex.getEditText().setText(user.getSex());
        }
        if (user.getLoverusername() != null) {
            loverid.getEditText().setEnabled(false);
            loverid.getEditText().setText(user.getLoverusername());
        }
        if (user.getNick() != null) {
            nick.getEditText().setText(user.getNick());
        }
        mProfileView.hideProgress();


    }

    @Override
    public void HandleReturnPic(File imageFile) {
        UCrop.of(Uri.fromFile(imageFile), Uri.fromFile(imageFile))
                .withMaxResultSize(320, 480)
                .start(mActivity);
    }

    @Override
    public void onError() {
        mProfileView.showToast("出错啦，请稍后再试");
    }

    @Override
    public void onCanceled(EasyImage.ImageSource source) {
        if (source == EasyImage.ImageSource.CAMERA) {
            File photoFile = EasyImage.lastlyTakenButCanceledPhoto(mContext);
            if (photoFile != null) {
                photoFile.delete();
            }

        }

    }

    @Override
    public void UpLoadAvatar(ImageView avatar, Intent data) {
        mProfileView.showProgress("等待上传...");
        Uri uri = UCrop.getOutput(data);
        String name = AVUser.getCurrentUser().getString(UserDao.NICK);
        try {
            if (uri != null) {
                AVFile file = AVFile.withFile(name, new File(new URI(uri.toString())));
                mRxleanCloud.UploadPicture(file)
                        .flatMap(new Func1<String, Observable<Boolean>>() {
                            @Override
                            public Observable<Boolean> call(String s) {
                                AVUser user = AVUser.getCurrentUser();
                                user.put(UserDao.AVATARURL, s);
                                return mRxleanCloud.SaveUserByLeanCloud(user);
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onCompleted() {
                                mProfileView.hideProgress();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mProfileView.showToast("出错啦，请稍后再试");
                                Logger.e(e.getMessage());
                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {
                                    Logger.d("更换头像成功");
                                }
                            }
                        });
            }
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }

        Glide.with(mContext).load(uri).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(mContext)).into(avatar);

    }


    @Override
    public void attachView(@NonNull BaseView view) {
        mProfileView = (IProfileView) view;

    }

    @Override
    public void detachView() {

    }
}
