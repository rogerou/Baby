package com.ozj.baby.mvp.views.login.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

import com.facebook.shimmer.ShimmerFrameLayout;
import com.hyphenate.chat.EMClient;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.ozj.baby.R;
import com.ozj.baby.base.BaseActivity;
import com.ozj.baby.mvp.presenter.login.impl.SplashPresenterImpl;
import com.ozj.baby.mvp.views.home.activity.MainActivity;
import com.ozj.baby.mvp.views.login.ISplashView;
import com.ozj.baby.util.PreferenceManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;
import shem.com.materiallogin.MaterialLoginView;
import shem.com.materiallogin.MaterialLoginViewListener;

/**
 * Created by Rpger ou on 2016/4/13.
 * <p>
 * 开屏页
 */
public class SplashActivity extends BaseActivity implements ISplashView {
    @Inject
    SplashPresenterImpl mSplashPresenter;
    @Inject
    PreferenceManager mPreferenceManager;
    @BindView(R.id.splash_bg)
    ImageView splashBg;
    @BindView(R.id.tv_loginOrRegister)
    TextView tvLoginOrRegister;
    @BindView(R.id.shimmer_layout)
    ShimmerFrameLayout shimmerLayout;
    @BindView(R.id.login)
    MaterialLoginView login;
    @BindView(R.id.tv_slogan)
    TextView tvSlogan;
    @BindView(R.id.rootview)
    RelativeLayout rootview;


    boolean isBlured;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.init(this.getClass().getSimpleName());
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initDagger() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_splash);
    }


    @Override
    public void initViewsAndListener() {
        mSplashPresenter.isLoginButtonVisable();
        mSplashPresenter.beginAnimation(splashBg, tvSlogan, shimmerLayout);
        mSplashPresenter.doingSplash();
        EMClient.getInstance().chatManager().loadAllConversations();
        login.setListener(new MaterialLoginViewListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onRegister(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerPassRep) {
                mSplashPresenter.Register(registerUser, registerPass, registerPassRep);

            }

            @Override
            public void onLogin(TextInputLayout loginUser, TextInputLayout loginPass) {
                mSplashPresenter.Login(loginUser, loginPass);
            }
        });

    }

    @Override
    public void initPresenter() {
        mSplashPresenter.attachView(this);
    }

    @Override
    public void initToolbar() {

    }


    @Override
    public void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean isLoginViewShowing() {

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
        if (!isLoginViewShowing()) {
            showLoginView();
        } else {
            dismissLoginView();
        }
    }

}
