package com.ozj.baby.mvp.presenter.home.impl;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import com.ozj.baby.R;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.presenter.home.IMainPresenter;
import com.ozj.baby.mvp.views.home.IMainView;
import com.ozj.baby.util.PreferenceManager;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/4/20.
 */
public class MainPresenterImpl implements IMainPresenter {
    IMainView mMainView;
    private Context mContext;
    private Activity mActivity;
    private PreferenceManager mPreferenceManager;

    @Inject
    public MainPresenterImpl(@ContextLife("Activity") Context context, Activity activity, PreferenceManager preferenceManager) {
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
    public void addFragment(Fragment fragment) {
        if (fragment != null) {
            mActivity.getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

    }

    @Override
    public void replaceFragment(Fragment fragment) {
        if (fragment == null) {
            mActivity.getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        }
    }
}
