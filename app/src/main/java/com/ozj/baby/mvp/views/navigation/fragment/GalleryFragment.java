package com.ozj.baby.mvp.views.navigation.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.ozj.baby.R;
import com.ozj.baby.adapter.GalleryAdapter;
import com.ozj.baby.base.BaseFragment;
import com.ozj.baby.mvp.presenter.navigation.impl.GalleryPresenterImpl;
import com.ozj.baby.mvp.views.navigation.IGalleryView;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by Roger on 2016/4/24.
 */
public class GalleryFragment extends BaseFragment implements IGalleryView {
    @Bind(R.id.ry_gallgery)
    RecyclerView ryGallgery;
    @Bind(R.id.swipeFreshLayout)
    SwipeRefreshLayout swipeFreshLayout;

    @Inject
    GalleryPresenterImpl mGalleryPresenter;

    GalleryAdapter mAdapter;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_gallery;
    }

    @Override
    public void initViews() {
        ryGallgery.setLayoutManager(new LinearLayoutManager(getActivity()));
        ryGallgery.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void initDagger() {
        mFragmentComponet.inject(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initToolbar() {
    }

    @Override
    public void initPresenter() {
        mGalleryPresenter.attachView(this);

    }

    @Override
    public void showRefreshing() {
        swipeFreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeFreshLayout.setRefreshing(true);
            }
        });

    }

    @Override
    public void hideRefreshing() {
        swipeFreshLayout.setRefreshing(false);

    }

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

}
