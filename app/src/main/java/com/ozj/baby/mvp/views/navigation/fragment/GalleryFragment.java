package com.ozj.baby.mvp.views.navigation.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;
import com.github.jorgecastilloprz.FABProgressCircle;
import com.ozj.baby.R;
import com.ozj.baby.adapter.GalleryAdapter;
import com.ozj.baby.base.BaseFragment;
import com.ozj.baby.event.AddGalleryEvent;
import com.ozj.baby.event.UploadPhotoUri;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.presenter.navigation.impl.GalleryPresenterImpl;
import com.ozj.baby.mvp.views.home.activity.DetailImageActivity;
import com.ozj.baby.mvp.views.home.activity.ProfileActivity;
import com.ozj.baby.mvp.views.navigation.IGalleryView;
import com.ozj.baby.util.OnItemClickListener;
import com.ozj.baby.widget.ChoosePicDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by Roger on 2016/4/24.
 */
public class GalleryFragment extends BaseFragment implements IGalleryView, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {
    @Bind(R.id.ry_gallgery)
    RecyclerView ryGallgery;
    @Bind(R.id.swipeFreshLayout)
    SwipeRefreshLayout swipeFreshLayout;
    @Inject
    RxBus mRxbus;
    @Inject
    GalleryPresenterImpl mGalleryPresenter;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.fab_progress)
    FABProgressCircle fabProgress;

    @Inject
    ChoosePicDialog mDialog;
    StaggeredGridLayoutManager layout;
    int size = 20;
    int page = 0;

    GalleryAdapter mAdapter;

    List<Gallery> mList;

    Subscription mSubscription;

    Subscription mUploadPhoto;

    boolean isFirst = true;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_gallery;
    }

    @Override
    public void initViews() {
//        fabProgress.attachListener(this);
        layout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        ryGallgery.setLayoutManager(layout);
        ryGallgery.setItemAnimator(new FadeInUpAnimator());
        swipeFreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeFreshLayout.setOnRefreshListener(this);
        ryGallgery.setNestedScrollingEnabled(false);
        mList = new ArrayList<>();
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
        mAdapter.setOnItemActionListener(this);

    }

    @Override
    public void initDagger() {
        mFragmentComponet.inject(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("GalleryFragment");

    }

    @Override
    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart("GalleryFragment");
    }

    @Override
    public void initData() {
        mGalleryPresenter.fetchDataFromNetwork(true, size, page);
        mSubscription = mRxbus.toObservable(AddGalleryEvent.class)
                .subscribe(new Action1<AddGalleryEvent>() {
                    @Override
                    public void call(AddGalleryEvent addGalleryEvent) {
                        if (addGalleryEvent.isList()) {
                            if (addGalleryEvent.isFresh()) {
                                mList.clear();
                                for (Gallery g : addGalleryEvent.getList()) {
                                    if (!mList.contains(g)) {
                                        mList.add(g);
                                    }
                                }

                            } else {
                                for (Gallery g : addGalleryEvent.getList()) {
                                    if (!mList.contains(g)) {
                                        mList.add(g);
                                    }
                                }
                            }
                        } else {
                            mList.add(0, addGalleryEvent.getGallery());
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
        mUploadPhoto = mRxbus.toObservable(UploadPhotoUri.class)
                .subscribe(new Action1<UploadPhotoUri>() {
                    @Override
                    public void call(UploadPhotoUri uploadPhotoUri) {
                        if (uploadPhotoUri.getUri() != null && uploadPhotoUri.getType() == 0) {
                            mGalleryPresenter.UploadPhoto(uploadPhotoUri.getUri());
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

    @Override
    public void showUpdating() {
        fabProgress.post(new Runnable() {
            @Override
            public void run() {
                if (fabProgress != null) {
                    fabProgress.show();
                }
            }
        });
    }

    @Override
    public void UpdateCompelte() {
        fabProgress.beginFinalAnimation();
    }

    @Override
    public void showPicDialog() {
        mDialog.show();

    }

    @Override
    public void showSnackBar(String msg) {
        Snackbar.make(fabProgress, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void toProfileActivity() {

        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);
        showToast("请邀请另一半一起来玩吧");
    }

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        if (!mUploadPhoto.isUnsubscribed()) {
            mUploadPhoto.unsubscribe();
        }
        mGalleryPresenter.detachView();
        
    }

    @Override
    public void onRefresh() {
        mGalleryPresenter.fetchDataFromNetwork(true, size, 0);
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


    @OnClick(R.id.fab)
    public void onClick() {
        if (mGalleryPresenter.isHavedLover()) {
            showPicDialog();
        } else {
            toProfileActivity();
        }

    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(getActivity(), DetailImageActivity.class);
        ArrayList<String> imgurls = new ArrayList<>();
        for (Gallery gallery : mList) {
            imgurls.add(gallery.getImgUrl());
        }
        intent.putStringArrayListExtra("imgurl", imgurls);
        intent.putExtra("index", position);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, imgurls.get(position));
        startActivity(intent, optionsCompat.toBundle());

    }
}
