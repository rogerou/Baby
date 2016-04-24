package com.ozj.baby.mvp.presenter.home;

import android.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.ozj.baby.base.BasePresenter;

/**
 * Created by Administrator on 2016/4/20.
 */
public interface IMainPresenter extends BasePresenter {

    void addFragment(Fragment fragment);

    void replaceFragment(Fragment fragment);

    void fabOnclick();

    boolean isHavedLover();

    void initData(ImageView avatar, TextView nick);


}
