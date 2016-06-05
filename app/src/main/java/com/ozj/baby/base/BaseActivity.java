package com.ozj.baby.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.ozj.baby.BabyApplication;
import com.ozj.baby.R;
import com.ozj.baby.di.component.ActivityComponet;
import com.ozj.baby.di.component.DaggerActivityComponet;
import com.ozj.baby.di.module.ActivityModule;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Roger ou on 2016/3/25.
 * BaseAvtivity
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    public SweetAlertDialog mProgressdialog;
    public BasePresenter mIPresenter;
    public ActivityComponet mActivityComponet;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponet = DaggerActivityComponet.builder().activityModule(new ActivityModule(this)).applicationComponet(((BabyApplication) getApplication()).getAppComponet()).build();
        initContentView();
        unbinder = ButterKnife.bind(this);
        initDagger();
        initPresenter();
        initToolbar();
        initViewsAndListener();
        initData();
    }

    protected abstract void initData();

    public abstract void initDagger();

    public abstract void initContentView();

    public abstract void initViewsAndListener();

    public abstract void initPresenter();

    public abstract void initToolbar();


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
            mProgressdialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            mProgressdialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            mProgressdialog.setCancelable(false);
        }
        mProgressdialog.setTitleText(message);
        mProgressdialog.show();

    }

    @Override
    public void showProgress(String message, int progress) {
        mProgressdialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mProgressdialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        mProgressdialog.setTitleText(message);
        mProgressdialog.setCancelable(false);
        mProgressdialog.getProgressHelper().setProgress(progress);
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
        unbinder.unbind();

    }


    public void showWarningDialog(String title, String content, SweetAlertDialog.OnSweetClickListener listener) {
        SweetAlertDialog mWarningDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setConfirmText("确定")
                .setCancelText("取消")
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(listener);
        mWarningDialog.show();
    }

    public void showErrorDialog(String title, String content, SweetAlertDialog.OnSweetClickListener listener) {
        SweetAlertDialog mErrorDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setConfirmText("确定")
                .setTitleText(title)
                .setContentText(content)
                .setConfirmClickListener(listener);
        mErrorDialog.show();
    }
}
