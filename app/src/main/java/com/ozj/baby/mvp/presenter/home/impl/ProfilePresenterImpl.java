package com.ozj.baby.mvp.presenter.home.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.ImageView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.domain.UserDao;
import com.orhanobut.logger.Logger;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
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

    private final Context mContext;
    private final Activity mActivity;
    private final RxLeanCloud mRxLeanCloud;
    //    private RxBabyRealm mRxBabyRealm;
    private final PreferenceManager mPreferenceManager;
    private IProfileView mProfileView;

    @Inject
    public ProfilePresenterImpl(@ContextLife("Activity") Context context, Activity activity, RxLeanCloud rxLeanCloud, PreferenceManager preferenceManager) {
        mContext = context;
        mActivity = activity;
//        mRxBabyRealm = rxBabyRealm;
        mRxLeanCloud = rxLeanCloud;
        mPreferenceManager = preferenceManager;
        Logger.init(this.getClass().getSimpleName());
    }


    @Override
    public void commit(String nick, final String lover, String city, String sex) {
        mProfileView.showProgress("更新中...");
        final AVUser avUser = AVUser.getCurrentUser();
        if (nick == null && lover == null && city == null && sex == null) {
            mProfileView.showToast("你全部都是空的你改什么呀");
            return;
        }
        if (!TextUtils.isEmpty(nick)) {
            avUser.put(UserDao.NICK, nick);
        }

        if (!TextUtils.isEmpty(city)) {
            avUser.put(UserDao.CITY, city);
        }
        if (!TextUtils.isEmpty(sex)) {
            avUser.put(UserDao.SEX, sex);
        }
        if (!TextUtils.isEmpty(lover)) {
            mRxLeanCloud.GetUserByUsername(lover).flatMap(new Func1<AVUser, Observable<AVUser>>() {
                @Override
                public Observable<AVUser> call(AVUser user) {
                    avUser.put(UserDao.LOVERUSERNAME, lover);
                    avUser.put(UserDao.LOVERID, user.getObjectId());
                    avUser.put(UserDao.LOVETIMESTAMP, System.currentTimeMillis());
                    avUser.put(UserDao.LOVERINSTALLATIONID, user.getString(UserDao.INSTALLATIONID));
                    avUser.put(UserDao.LOVERBACKGROUND, user.getString(UserDao.BACKGROUND));
                    avUser.put(UserDao.LOVERAVATAR, user.getString(UserDao.AVATARURL));
                    avUser.put(UserDao.LOVERNICK, user.getString(UserDao.NICK));
                    mPreferenceManager.SaveLoverId(user.getObjectId());
                    return mRxLeanCloud.SaveUserByLeanCloud(avUser);
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<AVUser>() {
                        @Override
                        public void onCompleted() {
                            mProfileView.hideProgress();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mProfileView.hideProgress();
                            mProfileView.showToast("出错啦，可能是另一般用户名输错了");
                        }

                        @Override
                        public void onNext(AVUser avUser1) {
                            if (avUser1 != null) {
                                mProfileView.showToast("保存资料成功");
                                mProfileView.setResultCode();
                            }
                        }
                    });
        } else {
            mRxLeanCloud.SaveUserByLeanCloud(avUser)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<AVUser>() {
                        @Override
                        public void onCompleted() {
                            mProfileView.hideProgress();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mProfileView.showToast("出错啦，请稍后再试");
                        }

                        @Override
                        public void onNext(AVUser user) {
                            if (user != null) {
                                mProfileView.showToast("保存资料成功");
                                mProfileView.setResultCode();
                            }
                        }
                    });
        }
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public void initData(final ImageView avatar, final TextInputLayout nick, final TextInputLayout loverid, final TextInputLayout city, final TextInputLayout sex) {
        mProfileView.showProgress("加载中...");
        User user = User.getCurrentUser(User.class);
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
                mRxLeanCloud.UploadPicture(file)
                        .flatMap(new Func1<String, Observable<AVUser>>() {
                            @Override
                            public Observable<AVUser> call(String s) {
                                AVUser user = AVUser.getCurrentUser();
                                user.put(UserDao.AVATARURL, s);
                                return mRxLeanCloud.SaveUserByLeanCloud(user);
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<AVUser>() {
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
                            public void onNext(AVUser user) {
                                if (user != null) {
                                    mProfileView.showToast("保存头像成功");
                                    Logger.d("更换头像成功");
                                    mProfileView.setResultCode();
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
