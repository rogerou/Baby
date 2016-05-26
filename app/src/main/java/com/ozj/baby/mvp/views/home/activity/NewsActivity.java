package com.ozj.baby.mvp.views.home.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.ozj.baby.R;
import com.ozj.baby.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/4/29.
 */
public class NewsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.ry_news)
    RecyclerView ryNews;
    @Bind(R.id.swipeFreshLayout)
    SwipeRefreshLayout swipeFreshLayout;


    @Override
    protected void initData() {
        
        
    }

    @Override
    public void initDagger() {

    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activivty_news);

    }

    @Override
    public void initViewsAndListener() {

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initToolbar() {
        setSupportActionBar(toolbar);


    }
}
