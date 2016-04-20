package com.ozj.baby.mvp.views.home.fragment;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.ozj.baby.R;
import com.ozj.baby.base.BaseFragment;
import com.ozj.baby.mvp.presenter.home.impl.SouvenirPresenterImpl;
import com.ozj.baby.mvp.views.home.ISouvenirVIew;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/4/19.
 */
public class SouvenirFragment extends BaseFragment implements ISouvenirVIew {
    @Inject
    SouvenirPresenterImpl mSouvenirPresenterImpl;

    @Bind(R.id.iv_album)
    ImageView ivAlbum;

    @Bind(R.id.fade_cover)
    View fadeCover;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collaspingToolBarlayout)
    CollapsingToolbarLayout collaspingToolBarlayout;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.ry_souvenir)
    RecyclerView rySouvenir;
    @Bind(R.id.swipeFreshLayout)
    SwipeRefreshLayout swipeFreshLayout;
    @Bind(R.id.nested_scroll)
    NestedScrollView nestedScroll;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main;
    }

    @Override
    public void initViews() {
        mSouvenirPresenterImpl.attachView(this);
    }

    @Override
    public void initDagger() {
        mFragmentComponet.inject(this);

    }


    @Override
    public void showRefreshingLoading() {
        swipeFreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeFreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void hideRefreshingLoading() {
        swipeFreshLayout.setRefreshing(false);
    }

}
