package com.ozj.baby.mvp.views.login.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.ozj.baby.R;
import com.ozj.baby.base.BaseActivity;
import com.ozj.baby.mvp.model.UserDao;
import com.ozj.baby.mvp.presenters.login.impl.SplashPresenterImpl;
import com.ozj.baby.mvp.views.home.MainActivity;
import com.ozj.baby.mvp.views.login.ISplashView;
import com.ozj.baby.util.PreferenceManager;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;
import shem.com.materiallogin.MaterialLoginView;
import shem.com.materiallogin.MaterialLoginViewListener;

/**
 * Created by Rpger ou on 2016/4/13.
 * <p/>
 * 开屏页
 */
public class SplashActivity extends BaseActivity implements ISplashView {
    @Inject
    SplashPresenterImpl mSplashPresenter;
    @Inject
    PreferenceManager mPreferenceManager;
    @Bind(R.id.splash_bg)
    ImageView splashBg;
    @Bind(R.id.tv_loginOrRegister)
    TextView tvLoginOrRegister;
    @Bind(R.id.shimmer_layout)
    ShimmerFrameLayout shimmerLayout;
    @Bind(R.id.login)
    MaterialLoginView login;
    @Bind(R.id.tv_slogan)
    TextView tvSlogan;
    @Bind(R.id.rootview)
    RelativeLayout rootview;


    AnimatorSet mAnimatorSet;
    String username, passwd, repeatPassWd;
    boolean isBlured = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.init(this.getClass().getSimpleName());
    }

    @Override
    public void initDagger() {
        mActivityComponet.inject(this);
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_splash);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark),200);

    }


    @Override
    public void initViewsAndListener() {
        mSplashPresenter.attachView(this);
        mSplashPresenter.isLoginButtonVisable();
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(2000);
        mAnimatorSet.playTogether(
                ObjectAnimator.ofFloat(tvSlogan, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(tvSlogan, "translationY", 300, 0),
                ObjectAnimator.ofFloat(splashBg, "scaleX", 1.5f, 1.05f),
                ObjectAnimator.ofFloat(splashBg, "scaleY", 1.5f, 1.05f)

        );
        mAnimatorSet.start();
        shimmerLayout.startShimmerAnimation();
        mSplashPresenter.doingSplash();
        login.setListener(new MaterialLoginViewListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onRegister(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerPassRep) {
                registerByLeanCloud(registerUser, registerPass, registerPassRep);

            }

            @Override
            public void onLogin(TextInputLayout loginUser, TextInputLayout loginPass) {
                loginByLeanCloud(loginUser, loginPass);
            }
        });

    }

    private void loginByLeanCloud(TextInputLayout loginUser, TextInputLayout loginPass) {
        username = loginUser.getEditText().getText().toString();
        passwd = loginPass.getEditText().getText().toString();
        if (username.isEmpty()) {
            loginUser.setErrorEnabled(true);
            loginUser.setError("用户名不能为空");
            return;
        }
        if (passwd.isEmpty()) {
            loginPass.setErrorEnabled(true);
            loginPass.setError("密码不能为空");
            return;
        }
        showProgress("登陆中...");
        AVUser.logInInBackground(username, passwd, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                hideProgress();
                if (e == null && avUser != null) {
                    mPreferenceManager.setIslogin(true);
                    mPreferenceManager.saveCurrentUserId(avUser.getObjectId());
                    toMainActivity();
                    finish();
                } else {
                    showToast("登陆失败，请稍后再试");
                }


            }
        });
    }

    private void registerByLeanCloud(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerPassRep) {

        username = registerUser.getEditText().getText().toString();
        passwd = registerPass.getEditText().getText().toString();
        repeatPassWd = registerPassRep.getEditText().getText().toString();
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
            registerPassRep.setErrorEnabled(true);
            registerPassRep.setError("重复密码不能为空");
            return;
        }
        if (!passwd.equals(repeatPassWd)) {
            registerPassRep.setErrorEnabled(true);
            registerPassRep.setError("两段密码不一致");
            return;
        }
        showProgress("注册中...");
        AVUser user = new AVUser();
        user.setUsername(username);
        user.setPassword(passwd);
        user.put(UserDao.NICK, username);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                hideProgress();
                if (e == null) {
                    showToast("注册成功");
                } else {
                    showToast("注册失败，请稍后再试");
                }

            }
        });
    }

    @Override
    public void initPresenter() {
        mIPresenter = mSplashPresenter;
    }


    @Override
    public void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean isAnimationRunning() {

        return mAnimatorSet.isRunning();
    }

    @Override
    public boolean isLoginViewisShowing() {

        return login.getVisibility() == View.VISIBLE;
    }

    @Override
    public void showLoginView() {
        if (!isBlured) {
            Blurry.with(SplashActivity.this).radius(25).sampling(2).async().capture(splashBg).into(splashBg);
            isBlured = true;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(login, "alpha", 0.1f, 1f);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Logger.d("onAnimationEnd:visable");
                ViewGroup parent = (ViewGroup) login.getParent();
                if (parent != null) {
                    login.setVisibility(View.VISIBLE);
                }
            }
        });
        animator.start();

    }

    @Override
    public void dismissLoginView() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(login, "alpha", 1f, 0.1f);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Logger.d("onAnimationEnd:dismiss");

                ViewGroup parent = (ViewGroup) login.getParent();
                if (parent != null) {
                    login.setVisibility(View.GONE);
                }
            }
        });
        animator.start();

    }

    @Override
    public void hideLoginButton() {
        tvLoginOrRegister.setVisibility(View.GONE);
    }

    @Override
    public void showLoginButton() {
        tvLoginOrRegister.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_loginOrRegister)
    public void onClick() {
        if (!isLoginViewisShowing()) {
            showLoginView();
        } else {
            dismissLoginView();
        }
    }
}
