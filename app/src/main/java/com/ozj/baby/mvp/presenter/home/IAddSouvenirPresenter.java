package com.ozj.baby.mvp.presenter.home;

import com.ozj.baby.base.BasePresenter;

import java.io.File;

/**
 * Created by Administrator on 2016/4/23.
 */
public interface IAddSouvenirPresenter extends BasePresenter {

    void commit(String content, File file);

}
