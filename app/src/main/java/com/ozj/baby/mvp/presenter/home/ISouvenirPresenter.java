package com.ozj.baby.mvp.presenter.home;

import com.ozj.baby.base.BasePresenter;
import com.ozj.baby.mvp.model.bean.Souvenir;

import java.util.List;

/**
 * Created by Administrator on 2016/4/20.
 */
public interface ISouvenirPresenter extends BasePresenter {

    void AutoLoadingMore();

    void RefreshingData();

    List<Souvenir> getDataFromLocal();

    void LoadingDataFromNet();

    boolean isHavedLover();

    void addNewSouvenir();


}
