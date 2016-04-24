package com.ozj.baby.mvp.presenter.home;

import com.ozj.baby.base.BasePresenter;

/**
 * Created by Administrator on 2016/4/20.
 */
public interface ISouvenirPresenter extends BasePresenter {

    void AutoLoadingMore();


    void LoadingDataFromNet(int size, int page);


}
