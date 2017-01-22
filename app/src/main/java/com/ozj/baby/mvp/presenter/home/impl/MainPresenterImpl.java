package com.ozj.baby.mvp.presenter.home.impl;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.domain.UserDao;
import com.ozj.baby.R;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.presenter.home.IMainPresenter;
import com.ozj.baby.mvp.views.home.IMainView;
import com.ozj.baby.mvp.views.login.activity.SplashActivity;
import com.ozj.baby.util.PreferenceManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/4/20.
 */
public class MainPresenterImpl implements IMainPresenter {
    private IMainView mMainView;
    private final Context mContext;
    private final Activity mActivity;
    private final PreferenceManager mPreferenceManager;
    private final RxLeanCloud mRxLeanCloud;
    private String currentFragmentTag;

    @Inject
    public MainPresenterImpl(@ContextLife() Context context, Activity activity, PreferenceManager preferenceManager, RxLeanCloud rxLeanCloud) {
        mRxLeanCloud = rxLeanCloud;
        mContext = context;
        mActivity = activity;
        mPreferenceManager = preferenceManager;
    }


    @Override
    public void attachView(BaseView view) {
        mMainView = (IMainView) view;


    }

    @Override
    public void detachView() {

    }


    @Override
    public void replaceFragment(Fragment to, String tag, boolean isExpanded) {
        FragmentManager fragmentManager = mActivity.getFragmentManager();
        Fragment currentfragment = fragmentManager.findFragmentByTag(currentFragmentTag);

        if (currentfragment == null || !TextUtils.equals(tag, currentFragmentTag)) {
            currentFragmentTag = tag;
            fragmentManager.beginTransaction().replace(R.id.fragment_container, to, currentFragmentTag).commit();


        }
        if (isExpanded) {
            mMainView.showScrollView();
        } else {
            mMainView.hideScrollView();
        }


    }

    @Override
    public void fabOnclick() {
        if (isHadLover()) {
            mMainView.toAddSouvenirActivity();
        } else {
            mMainView.toProfileActivity();
            mMainView.showToast("请先设置另一半才能发日记哦");
        }
    }

    @Override
    public boolean isHadLover() {
        return !TextUtils.isEmpty(mPreferenceManager.GetLoverID());
    }

    @Override
    public void initData(ImageView avatar, TextView nick, ImageView iv_album) {
        User user = User.getCurrentUser(User.class);

        String avatarurl = user.getString(UserDao.AVATARURL);
        if (avatarurl != null) {
            Glide.with(mContext).load(user.getString(UserDao.AVATARURL)).bitmapTransform(new CropCircleTransformation(mContext)).diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar);
        }
        if (user.getBackground() != null) {
            Glide.with(mContext).load(user.getBackground()).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(iv_album);
        }
        if (user.getBackground() == null && user.getLoverBackGround() != null) {
            Glide.with(mContext).load(user.getLoverBackGround()).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(iv_album);
        }
        nick.setText(user.getString(UserDao.NICK));
    }

    @Override
    public void UploadPic(Uri uri) {
        mMainView.showProgress("上传中...");
        try {
            AVFile file = AVFile.withFile(mPreferenceManager.getCurrentUserId(), new File(new URI(uri.toString())));
            mRxLeanCloud.UploadPicture(file)
                    .flatMap(new Func1<String, Observable<AVUser>>() {
                        @Override
                        public Observable<AVUser> call(String s) {
                            User user = User.getCurrentUser(User.class);
                            user.setBackground(s);
                            return mRxLeanCloud.SaveUserByLeanCloud(user);
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<AVUser>() {
                        @Override
                        public void onCompleted() {
                            mMainView.hideProgress();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mMainView.hideProgress();
                            com.orhanobut.logger.Logger.e(e.getMessage());
                        }

                        @Override
                        public void onNext(AVUser user) {
                            mMainView.showToast("保存成功");
                        }
                    });
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Baby");
        intent.putExtra(Intent.EXTRA_TEXT, "和我一起来Baby玩耍吧,下载地址：http://fir.im/b9u8");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(Intent.createChooser(intent, "把Baby分享给朋友吧"));
    }

    @Override
    public void Logout() {
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                AVUser.logOut();
                mPreferenceManager.Clear();
                mActivity.startActivity(new Intent(mActivity, SplashActivity.class));
                mActivity.finish();
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

    }


}
