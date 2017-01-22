package com.ozj.baby.mvp.presenter.home;

import com.ozj.baby.base.BasePresenter;
import com.ozj.baby.mvp.model.bean.News;

import java.util.List;

/**
 * Created by Administrator on 2016/4/29.
 */
public interface INewsPresenter extends BasePresenter {

    List<News> getAllNews();
    

}
