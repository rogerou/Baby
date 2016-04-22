package com.ozj.baby.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ozj.baby.BabyApplication;
import com.ozj.baby.di.component.DaggerFragmentComponet;
import com.ozj.baby.di.component.FragmentComponet;
import com.ozj.baby.di.module.FragmentModule;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/25.
 */
public abstract class BaseFragment extends Fragment implements BaseView {

    private BaseActivity mActivity;
    private View mLayoutView;
    public FragmentComponet mFragmentComponet;
    protected BasePresenter mPresenter;

    /**
     * 初始化布局
     */
    public abstract int getLayoutRes();

    /**
     * 初始化Views
     */
    public abstract void initViews();

    public abstract void initDagger();

    public abstract void initData();

    public abstract void initToolbar();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 获取Fragment布局文件的View
     *
     * @param inflater
     * @param container
     * @return
     */
    private View getCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mFragmentComponet = DaggerFragmentComponet
                .builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponet(((BabyApplication) getActivity()
                        .getApplication())
                        .getAppComponet())
                .build();
        initDagger();
        initViews();
        initToolbar();
        initData();
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutView = getCreateView(inflater, container);
        ButterKnife.bind(this, mLayoutView);
        return mLayoutView;
    }

    /**
     * 获取当前Fragment状态
     *
     * @return true为正常 false为未加载或正在删除
     */
    private boolean getStatus() {
        return (isAdded() && !isRemoving());
    }

    @Override
    public void showProgress(String message) {
        if (getStatus() && getBaseActivity() != null) {
            getBaseActivity().showProgress(message);
        }
    }

    @Override
    public void showProgress(String message, int progress) {
        if (getStatus() && getBaseActivity() != null) {
            getBaseActivity().showProgress(message, progress);
        }
    }

    @Override
    public void hideProgress() {
        if (getStatus() && getBaseActivity() != null) {

            getBaseActivity().hideProgress();
        }

    }

    @Override
    public void showToast(String msg) {
        if (getStatus() && getBaseActivity() != null) {
            getBaseActivity().showToast(msg);

        }
    }

    @Override
    public void close() {

    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    public BaseActivity getBaseActivity() {
        if (mActivity == null) {
            mActivity = (BaseActivity) getActivity();
        }
        return mActivity;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();

        }
    }
}
