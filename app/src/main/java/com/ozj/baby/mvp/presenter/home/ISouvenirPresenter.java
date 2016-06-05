package com.ozj.baby.mvp.presenter.home;

import com.ozj.baby.base.BasePresenter;
import com.ozj.baby.mvp.model.bean.Souvenir;

/**
 * Created by Administrator on 2016/4/20.
 */
public interface ISouvenirPresenter extends BasePresenter {


    void LoadingDataFromNet(boolean isReFresh, int size, int page);

    void delete(Souvenir souvenir);


}
