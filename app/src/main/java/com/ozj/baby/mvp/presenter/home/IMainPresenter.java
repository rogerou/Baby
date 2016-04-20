package com.ozj.baby.mvp.presenter.home;

import android.app.Fragment;

import com.ozj.baby.base.BasePresenter;

/**
 * Created by Administrator on 2016/4/20.
 */
public interface IMainPresenter extends BasePresenter {

    void addFragment(Fragment fragment);

    void replaceFragment(Fragment fragment);


}
