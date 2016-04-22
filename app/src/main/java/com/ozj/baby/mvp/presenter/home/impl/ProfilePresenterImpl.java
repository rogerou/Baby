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
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.model.rx.RxRealm;
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
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/4/21.
 */
public class ProfilePresenterImpl implements IProfilePresenter {

    private Context mContext;
    private Activity mActivity;
    IProfileView mProfileView;
    private RxLeanCloud mRxleanCloud;
    private RxRealm mRxRealm;
    private PreferenceManager mPreferenceManager;

    @Inject
    public ProfilePresenterImpl(@ContextLife("Activity") Context context, Activity activity, RxLeanCloud rxLeanCloud, RxRealm rxRealm, PreferenceManager preferenmanager) {
        mContext = context;
        mActivity = activity;
        mRxRealm = rxRealm;
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
            mRxleanCloud.GetUserByUsername(lover).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<AVUser>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            mProfileView.showToast("和另一半确定情侣关系失败，请检查网络和用户名是否输错");
                        }

                        @Override
                        public void onNext(AVUser user) {
                            avUser.put(UserDao.LOVER, user);
                            avUser.put(UserDao.LOVETIMESTAMP, System.currentTimeMillis());
                            mPreferenceManager.SaveLoverId(user.getObjectId());
                            
                        }
                    });


        }
        if (city != null) {
            avUser.put(UserDao.CITY, city);
        }
        if (sex != null) {
            avUser.put(UserDao.SEX, sex);
        }
        mRxleanCloud.SaveUserByLeanCloud(avUser).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    Logger.d("保存用户资料到LeanCloud成功");
                } else {
                    Logger.e("保存用户资料到LeanCloud失败");
                }
            }
        });
        User realmuser = new User(avUser);
        realmuser.setAnotheruser(new User((AVUser) avUser.get(UserDao.LOVER)));
        realmuser.setAnotherUserID(realmuser.getAnotheruser().getID());
        mRxRealm.SaveUser(realmuser).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    Logger.d("保存用户资料到本地成功");
                } else {
                    Logger.e("保存用户资料到本地失败");
                }
            }
        });
        mProfileView.hideProgress();
    }


    @Override
    public void initData(final ImageView avatar, final TextInputLayout nick, final TextInputLayout loverid, final TextInputLayout city, final TextInputLayout sex) {
        mProfileView.showProgress("加载中...");
        mRxleanCloud.GetUserByLeanCloud(mPreferenceManager.getCurrentUserId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AVUser>() {
                    @Override
                    public void onCompleted() {
                        mProfileView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());

                    }

                    @Override
                    public void onNext(AVUser user) {
                        HandlerData(user, avatar, city, sex, loverid, nick);
                    }
                });


    }

    @Override
    public void HandleReturnPic(File imageFile) {
        UCrop.of(Uri.fromFile(imageFile), Uri.fromFile(imageFile.getParentFile()))
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
                mRxleanCloud.UploadFile(file, AVUser.getCurrentUser()).subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        mProfileView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProfileView.showToast("出错啦，请稍后再试");
                        Logger.e(e.getMessage());
                        mProfileView.hideProgress();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        mProfileView.showProgress("上传中...", integer);
                    }
                });
            }
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }

        Glide.with(mContext).load(uri).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(mContext)).into(avatar);

    }


    @SuppressWarnings("ConstantConditions")
    private void HandlerData(AVUser user, ImageView avatar, TextInputLayout
            city, TextInputLayout sex, TextInputLayout loverid, TextInputLayout nick) {
        final User realmUser = new User(user);
        realmUser.setAnotheruser(new User((AVUser) user.get(UserDao.LOVER)));
        String avatarUrl = user.getAVFile(UserDao.AVATARFILE).getUrl();
        String citylocation = user.getString(UserDao.CITY);
        String loverusername = user.getString(UserDao.LOVERUSERNAME);
        String sexs = user.getString(UserDao.SEX);
        String nickname = user.getString(UserDao.NICK);
        if (avatarUrl != null) {
            Glide.with(mContext).load(avatarUrl).crossFade().bitmapTransform(new CropCircleTransformation(mContext)).diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar);
        }
        if (citylocation != null) {
            city.getEditText().setText(citylocation);
        }
        if (sexs != null) {
            sex.getEditText().setText(sexs);
        }
        if (loverusername != null) {
            loverid.getEditText().setEnabled(false);
            loverid.getEditText().setText(loverusername);
            mRxleanCloud.GetUserByUsername(loverusername)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<AVUser>() {
                        @Override
                        public void call(AVUser user) {
                            realmUser.setAnotheruser(new User(user));
                            realmUser.setAnotherUserID(user.getObjectId());
                        }
                    });
        }
        if (user.get(UserDao.NICK) != null) {
            nick.getEditText().setText(nickname);
        }
        mRxRealm.SaveUser(realmUser).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            Logger.d("更新当前用户资料成功");

                        } else {
                            Logger.e("更新当前用户资料失败");
                        }
                    }
                });
    }


    @Override
    public void attachView(@NonNull BaseView view) {
        mProfileView = (IProfileView) view;

    }

    @Override
    public void detachView() {

    }
}
