package com.ozj.baby.mvp.views.navigation.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.ozj.baby.R;
import com.ozj.baby.adapter.GalleryAdapter;
import com.ozj.baby.base.BaseFragment;
import com.ozj.baby.event.AddGalleryEvent;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.presenter.navigation.impl.GalleryPresenterImpl;
import com.ozj.baby.mvp.views.navigation.IGalleryView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Roger on 2016/4/24.
 */
public class GalleryFragment extends BaseFragment implements IGalleryView {
    @Bind(R.id.ry_gallgery)
    RecyclerView ryGallgery;
    @Bind(R.id.swipeFreshLayout)
    SwipeRefreshLayout swipeFreshLayout;
    @Inject
    RxBus mRxbus;
    @Inject
    GalleryPresenterImpl mGalleryPresenter;

    GalleryAdapter mAdapter;

    List<Gallery> mList;

    Subscription mSubscription;

    boolean isFirst = true;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_gallery;
    }

    @Override
    public void initViews() {
        ryGallgery.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        ryGallgery.setItemAnimator(new DefaultItemAnimator());
        swipeFreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    @Override
    public void initDagger() {
        mFragmentComponet.inject(this);
    }

    @Override
    public void initData() {
        mList = mGalleryPresenter.fetchDataFromLocal();
        if (mList.size() == 0) {
            mGalleryPresenter.fetchDataFromNetwork();
        } else {
            mAdapter = new GalleryAdapter(mList, getActivity());
            ryGallgery.setAdapter(mAdapter);
        }

        mSubscription = mRxbus.toObservable(AddGalleryEvent.class)
                .subscribe(new Action1<AddGalleryEvent>() {
                    @Override
                    public void call(AddGalleryEvent addGalleryEvent) {
                        if (isFirst) {
                            mList = mGalleryPresenter.fetchDataFromLocal();
                            mAdapter = new GalleryAdapter(mList, getActivity());
                            ryGallgery.setAdapter(mAdapter);
                        } else {
                            if (addGalleryEvent.isList()) {
                                mList.addAll(0, addGalleryEvent.getList());
                                mAdapter.notifyItemRangeInserted(0, addGalleryEvent.getList().size());
                            } else {
                                mList.add(0, addGalleryEvent.getGallery());
                                mAdapter.notifyItemInserted(0);
                            }
                        }

                    }
                });
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
        swipeFreshLayout.setEnabled(true);
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
        swipeFreshLayout.setEnabled(false);
    }

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.isUnsubscribed();
        }
    }
}
