package com.ozj.baby.mvp.presenter.login.impl;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.orhanobut.logger.Logger;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.bean.User;
import com.ozj.baby.mvp.model.dao.UserDao;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.presenter.login.ISplashPresenter;
import com.ozj.baby.mvp.views.home.activity.MainActivity;
import com.ozj.baby.mvp.views.login.ISplashView;
import com.ozj.baby.mvp.views.login.activity.SplashActivity;
import com.ozj.baby.util.PreferenceManager;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Roger ou on 2016/3/25.
 */
public class SplashPresenterImpl implements ISplashPresenter, Handler.Callback {

    ISplashView mSplashView;
    private Context mContext;
    private PreferenceManager mPreferenceManager;

    private RxLeanCloud mRxleanCloud;
    private Handler mHandler;
    private static final int MESSAGE_WHAT = 1;
    AnimatorSet mAnimatorSet;

    @Inject
    public SplashPresenterImpl(@ContextLife("Activity") Context context, PreferenceManager preferenceManager, RxLeanCloud mRxleanCloud) {
        this.mRxleanCloud = mRxleanCloud;
        mContext = context;
        mPreferenceManager = preferenceManager;

    }

    @Override
    public void onActivityStart() {
        if (mHandler != null && !mHandler.hasMessages(MESSAGE_WHAT)) {
            mHandler.sendEmptyMessage(MESSAGE_WHAT);
        }
    }

    @Override
    public void onActivityPause() {
        if (mHandler != null && mHandler.hasMessages(MESSAGE_WHAT)) {
            mHandler.removeMessages(MESSAGE_WHAT);
        }
    }

    @Override
    public void beginAnimation(ImageView imageView, TextView slogan, ShimmerFrameLayout shimmerFrameLayout) {

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(2000);
        mAnimatorSet.playTogether(
                ObjectAnimator.ofFloat(slogan, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(slogan, "translationY", 300, 0),
                ObjectAnimator.ofFloat(imageView, "scaleX", 1.5f, 1.05f),
                ObjectAnimator.ofFloat(imageView, "scaleY", 1.5f, 1.05f)

        );
        mAnimatorSet.start();
        shimmerFrameLayout.startShimmerAnimation();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void Register(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerRepeatPasswd) {

        String username = registerUser.getEditText().getText().toString();
        String passwd = registerPass.getEditText().getText().toString();
        String repeatPassWd = registerRepeatPasswd.getEditText().getText().toString();
        if (username.isEmpty()) {
            registerUser.setErrorEnabled(true);
            registerUser.setError("用户名不能为空");
            return;
        }
        if (passwd.isEmpty()) {
            registerPass.setErrorEnabled(true);
            registerPass.setError("密码不能为空");
            return;
        }
        if (repeatPassWd.isEmpty()) {
            registerRepeatPasswd.setErrorEnabled(true);
            registerRepeatPasswd.setError("重复密码不能为空");
            return;
        }
        if (!passwd.equals(repeatPassWd)) {
            registerRepeatPasswd.setErrorEnabled(true);
            registerRepeatPasswd.setError("两段密码不一致");
            return;
        }
        mSplashView.showProgress("注册中...");
        AVUser user = new AVUser();
        user.setUsername(username);
        user.setPassword(passwd);
        user.put(UserDao.NICK, username);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                mSplashView.hideProgress();
                if (e == null) {
                    mSplashView.showToast("注册成功");
                } else {
                    mSplashView.showToast("注册失败，请稍后再试");
                }

            }
        });

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void Login(TextInputLayout usernameLogin, TextInputLayout passwdLogin) {
        String username = usernameLogin.getEditText().getText().toString();
        String passwd = passwdLogin.getEditText().getText().toString();
        if (username.isEmpty()) {
            usernameLogin.setErrorEnabled(true);
            usernameLogin.setError("用户名不能为空");
            return;
        }
        if (passwd.isEmpty()) {
            passwdLogin.setErrorEnabled(true);
            passwdLogin.setError("密码不能为空");
            return;
        }
        mSplashView.showProgress("登陆中...");
        mRxleanCloud.Login(username, passwd)
                .flatMap(new Func1<User, Observable<String>>() {
                    @Override
                    public Observable<String> call(User user) {
                        mPreferenceManager.setIslogin(true);
                        mPreferenceManager.saveCurrentUserId(user.getObjectId());
                        mPreferenceManager.SaveLoverId(user.getString(UserDao.LOVERID));

                        return mRxleanCloud.SaveInstallationId();
                    }
                }).flatMap(new Func1<String, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(String s) {
                User user = User.getCurrentUser(User.class);
                user.setInstallationId(s);

                return mRxleanCloud.SaveUserByLeanCloud(user);
            }
        }).subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {
                mSplashView.toMainActivity();
                mSplashView.hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                mSplashView.showToast("登陆失败，检查一下账号密码和网络");
                Logger.e(e.getMessage());
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    Logger.d("登陆成功");
                }
            }
        });
//     
    }


    @Override
    public void isLoginButtonVisable() {
        mHandler = new Handler(this);
        if (mPreferenceManager.isLogined()) {
            mSplashView.hideLoginButton();
        } else {
            mSplashView.showLoginButton();
        }
    }

    @Override
    public void doingSplash() {
        if (mPreferenceManager.isFirstTime()) {
            RxPermissions.getInstance(mContext)
                    .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean aBoolean) {
                            if (!aBoolean) {
                                mSplashView.showToast("你拒绝了相关的权限");
                                mSplashView.close();
                            } else {
                                mPreferenceManager.saveFirsttime(false);
                            }
                        }
                    });
        }
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, 3000);
        }
    }

    @Override
    public boolean isAnimationRunning() {
        if (mAnimatorSet != null) {
            return mAnimatorSet.isRunning();
        }
        return true;
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mSplashView = (ISplashView) view;

    }

    @Override
    public void detachView() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == MESSAGE_WHAT) {
            if (isAnimationRunning()) {
                mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT, 300);
                return false;
            }
            if (mPreferenceManager.isLogined() && AVUser.getCurrentUser() != null) {
                mSplashView.toMainActivity();
            }
        }

        return false;
    }
}
