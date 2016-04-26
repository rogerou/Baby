package com.ozj.baby.mvp.views.navigation.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Roger on 2016/4/24.
 */
public class GalleryFragment extends BaseFragment implements IGalleryView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.ry_gallgery)
    RecyclerView ryGallgery;
    @Bind(R.id.swipeFreshLayout)
    SwipeRefreshLayout swipeFreshLayout;
    @Inject
    RxBus mRxbus;
    @Inject
    GalleryPresenterImpl mGalleryPresenter;

    GalleryAdapter mAdapter;

    List<Gallery> mList = new ArrayList<>();

    Subscription mSubscription;

    boolean isFirst = true;
    StaggeredGridLayoutManager layout;
    int size = 20;
    int page = 0;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_gallery;
    }

    @Override
    public void initViews() {
        layout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        ryGallgery.setLayoutManager(layout);
        ryGallgery.setItemAnimator(new FadeInUpAnimator());
        swipeFreshLayout.setColorSchemeResources(R.color.colorPrimary);
        ryGallgery.setNestedScrollingEnabled(false);
        swipeFreshLayout.setOnRefreshListener(this);
        mAdapter = new GalleryAdapter(mList, getActivity());
        ryGallgery.setAdapter(mAdapter);
        ryGallgery.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && getMaxElem(layout.findLastVisibleItemPositions(new int[layout.getSpanCount()])) == layout.getItemCount() - 1) {
                    swipeFreshLayout.setRefreshing(true);
                    page++;
                    mGalleryPresenter.fetchDataFromNetwork(false, size, page);
                }
            }
        });
    }

    @Override
    public void initDagger() {
        mFragmentComponet.inject(this);
    }

    @Override
    public void initData() {
        mGalleryPresenter.fetchDataFromNetwork(true, size, page);
        mSubscription = mRxbus.toObservable(AddGalleryEvent.class)
                .subscribe(new Action1<AddGalleryEvent>() {
                    @Override
                    public void call(AddGalleryEvent addGalleryEvent) {
                        if (addGalleryEvent.isFresh()) {
                            if (addGalleryEvent.isList()) {
                                for (Gallery g : addGalleryEvent.getList()) {
                                    if (!mList.contains(g)) {
                                        mList.add(0, g);
                                        mAdapter.notifyItemInserted(0);
                                    }
                                }

                            } else {
                                mList.add(0, addGalleryEvent.getGallery());
                                mAdapter.notifyItemInserted(0);
                            }
                        } else {
                            for (Gallery s : addGalleryEvent.getList()) {
                                if (!mList.contains(s)) {
                                    mList.add(s);
                                    mAdapter.notifyItemInserted(mList.size() - 1);
                                }
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
        swipeFreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (swipeFreshLayout != null)
                    swipeFreshLayout.setRefreshing(true);
            }
        });

    }

    @Override
    public void hideRefreshing() {
        if (swipeFreshLayout != null)
            swipeFreshLayout.setRefreshing(false);
    }

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscription != null) {
            mSubscription.isUnsubscribed();
        }
        isFirst = true;
        mList.clear();
        size = 20;
        page = 0;
    }

    @Override
    public void onRefresh() {
        mGalleryPresenter.fetchDataFromNetwork(true, size, page);
    }

    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i] > maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }
}
