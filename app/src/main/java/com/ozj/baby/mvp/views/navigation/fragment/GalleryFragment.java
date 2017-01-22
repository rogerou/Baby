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
import com.ozj.baby.event.EventConstant;
import com.ozj.baby.event.GalleryEvent;
import com.ozj.baby.event.UploadPhotoUri;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.presenter.navigation.impl.GalleryPresenterImpl;
import com.ozj.baby.mvp.views.home.activity.DetailImageActivity;
import com.ozj.baby.mvp.views.home.activity.ProfileActivity;
import com.ozj.baby.mvp.views.navigation.IGalleryView;
import com.ozj.baby.util.OnItemClickListener;
import com.ozj.baby.util.OnItemLongClickListener;
import com.ozj.baby.widget.ChoosePicDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Roger on 2016/4/24.
 */
public class GalleryFragment extends BaseFragment implements IGalleryView, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener, OnItemLongClickListener {
    @BindView(R.id.ry_gallery)
    RecyclerView ryGallery;
    @BindView(R.id.swipeFreshLayout)
    SwipeRefreshLayout swipeFreshLayout;
    @Inject
    RxBus mRxBus;
    @Inject
    GalleryPresenterImpl mGalleryPresenter;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.fab_progress)
    FABProgressCircle fabProgress;

    @Inject
    ChoosePicDialog mDialog;
    StaggeredGridLayoutManager layout;
    int size = 20;
    int page;

    GalleryAdapter mAdapter;

    List<Gallery> mList;

    Subscription mSubscription;
    Subscription mUploadPhoto;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_gallery;
    }

    @Override
    public void initViews() {
//        fabProgress.attachListener(this);
        layout = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        ryGallery.setLayoutManager(layout);
        ryGallery.setItemAnimator(new FadeInUpAnimator());
        swipeFreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeFreshLayout.setOnRefreshListener(this);
        ryGallery.setNestedScrollingEnabled(false);
        mList = new ArrayList<>();
        mAdapter = new GalleryAdapter(mList, getActivity());
        ryGallery.setAdapter(mAdapter);
        ryGallery.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        mAdapter.setOnItemLongClickListener(this);

    }

    @Override
    public void initDagger() {
        mFragmentComponent.inject(this);

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
        mSubscription = mRxBus.toObservable(GalleryEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<GalleryEvent>() {
                    @Override
                    public void call(GalleryEvent galleryEvent) {
                        switch (galleryEvent.mAction) {
                            case EventConstant.REFRESH:
                                mList.clear();
                                mList.addAll(galleryEvent.mList);
                                break;
                            case EventConstant.LOADMORE:
                                mList.addAll(galleryEvent.mList);
                                break;
                            case EventConstant.DELETE:
                                mList.remove(galleryEvent.mGallery);
                                break;
                            case EventConstant.ADD:
                                mList.add(0, galleryEvent.mGallery);
                                break;
                            default:
                                break;
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
        mUploadPhoto = mRxBus.toObservable(UploadPhotoUri.class)
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
    public void UpdateComplete() {
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
        int maxVal = Integer.MIN_VALUE;
        for (int anArr : arr) {
            if (anArr > maxVal)
                maxVal = anArr;
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

    @Override
    public void onLongClick(View view, int position) {
        final Gallery gallery = mList.get(position);
        showWarningDialog("确定要删除这个相片吗？", "删除", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
                mGalleryPresenter.deleteGalley(gallery);
            }
        });
    }
}
