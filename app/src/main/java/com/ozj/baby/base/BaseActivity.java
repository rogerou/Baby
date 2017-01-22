package com.ozj.baby.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.ozj.baby.BabyApplicationLike;
import com.ozj.baby.R;
import com.ozj.baby.di.component.ActivityComponent;
import com.ozj.baby.di.component.DaggerActivityComponent;
import com.ozj.baby.di.module.ActivityModule;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Roger ou on 2016/3/25.
 * BaseAvtivity
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    public SweetAlertDialog mProgressDialog;
    public ActivityComponent mActivityComponent;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerActivityComponent.builder().activityModule(new ActivityModule(this)).applicationComponent(BabyApplicationLike.getAppComponent()).build();
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
        if (mProgressDialog == null) {
            mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            mProgressDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setTitleText(message);
        mProgressDialog.show();

    }

    @Override
    public void showProgress(String message, int progress) {
        mProgressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mProgressDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        mProgressDialog.setTitleText(message);
        mProgressDialog.setCancelable(false);
        mProgressDialog.getProgressHelper().setProgress(progress);
        mProgressDialog.show();

    }

    @Override
    public void showToast(String msg) {
        if (!isFinishing())
            Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
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
