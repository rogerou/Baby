package com.ozj.baby.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.ozj.baby.BabyApplication;
import com.ozj.baby.di.component.ActivityComponet;
import com.ozj.baby.di.component.DaggerActivityComponet;
import com.ozj.baby.di.module.ActivityModule;

import butterknife.ButterKnife;

/**
 * Created by Roger ou on 2016/3/25.
 * BaseAvtivity
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    public ProgressDialog mProgressdialog;
    public BasePresenter mIPresenter;
    public ActivityComponet mActivityComponet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponet = DaggerActivityComponet.builder().activityModule(new ActivityModule(this)).applicationComponet(((BabyApplication) getApplication()).getAppConponet()).build();
        initContentView();
        ButterKnife.bind(this);
        initDagger();
        initPresenter();
        initViewsAndListener();
    }

    public abstract void initDagger();

    public abstract void initContentView();

    public abstract void initViewsAndListener();

    public abstract void initPresenter();


    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);

    }

    @Override
    public void showProgress(String message) {
        if (mProgressdialog == null) {
            mProgressdialog = new ProgressDialog(this);
            mProgressdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressdialog.setMessage(message);
        }
        mProgressdialog.show();

    }

    @Override
    public void showToast(String msg) {
        if (!isFinishing())
            Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgress() {
        if (mProgressdialog != null) {
            mProgressdialog.dismiss();
        }

    }

    @Override
    public void close() {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);

    }
}
