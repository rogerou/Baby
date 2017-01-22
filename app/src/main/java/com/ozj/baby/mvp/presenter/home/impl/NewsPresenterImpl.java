package com.ozj.baby.mvp.presenter.home.impl;

import android.support.annotation.NonNull;

import com.ozj.baby.base.BaseView;
import com.ozj.baby.mvp.model.bean.News;
import com.ozj.baby.mvp.presenter.home.INewsPresenter;
import com.ozj.baby.mvp.views.home.INewsView;

import java.util.List;

/**
 * Created by Administrator on 2016/4/29.
 */
public class NewsPresenterImpl implements INewsPresenter {
    private INewsView mNewsView;

    @Override
    public List<News> getAllNews() {
        return null;
        
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        mNewsView = (INewsView) view;
    }

    @Override
    public void detachView() {

    }
}
