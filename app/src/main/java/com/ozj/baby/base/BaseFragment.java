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

    /**
     * 初始化布局
     */
    public abstract int getLayoutRes();

    /**
     * 初始化Views
     */
    public abstract void initViews();

    public abstract void initDagger();


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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentComponet = DaggerFragmentComponet
                .builder()
                .applicationComponet(((BabyApplication) getBaseActivity()
                        .getApplication())
                        .getAppComponet())
                .fragmentModule(new FragmentModule(BaseFragment.this)).build();

        mLayoutView = getCreateView(inflater, container);
        ButterKnife.bind(this, mLayoutView);
        initViews();

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

}
