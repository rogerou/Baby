package com.ozj.baby.mvp.views.home;

import com.ozj.baby.base.BaseView;

/**
 * Created by Administrator on 2016/4/20.
 */
public interface ISouvenirVIew extends BaseView {

    void showRefreshingLoading();

    void hideRefreshingLoading();

    void toAddNewSouvenirActivity();

    void ToProFileActivity();

}
