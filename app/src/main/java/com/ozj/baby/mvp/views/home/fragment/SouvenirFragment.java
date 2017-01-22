package com.ozj.baby.mvp.views.home.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.avos.avoscloud.AVAnalytics;
import com.orhanobut.logger.Logger;
import com.ozj.baby.R;
import com.ozj.baby.adapter.SouvenirAdapter;
import com.ozj.baby.base.BaseFragment;
import com.ozj.baby.event.EventConstant;
import com.ozj.baby.event.SouvenirEvent;
import com.ozj.baby.event.IncrementEvent;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.presenter.home.impl.SouvenirPresenterImpl;
import com.ozj.baby.mvp.views.home.ISouvenirVIew;
import com.ozj.baby.mvp.views.home.activity.AddSouvenirActivity;
import com.ozj.baby.mvp.views.home.activity.ProfileActivity;
import com.ozj.baby.util.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Rogerou on 2016/4/19.
 */
public class SouvenirFragment extends BaseFragment implements ISouvenirVIew, SwipeRefreshLayout.OnRefreshListener, OnItemLongClickListener {
    @Inject
    SouvenirPresenterImpl mSouvenirPresenterImpl;
    @Inject
    RxBus mRxBus;
    @BindView(R.id.ry_souvenir)
    RecyclerView rySouvenir;
    @BindView(R.id.swipeFreshLayout)
    SwipeRefreshLayout swipeFreshLayout;
    Subscription mSubscription;
    Subscription mIncrement;
    SouvenirAdapter mAdapter;
    int page;
    int size = 15;
    LinearLayoutManager layout;
    List<Souvenir> mList = new ArrayList<>();

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main;
    }

    @Override
    public void initViews() {
        layout = new LinearLayoutManager(getActivity());
        swipeFreshLayout.setOnRefreshListener(this);
        rySouvenir.setLayoutManager(layout);
        rySouvenir.setItemAnimator(new FadeInUpAnimator());
        swipeFreshLayout.setColorSchemeResources(R.color.colorPrimary);
        Logger.init(this.getClass().getSimpleName());
        mAdapter = new SouvenirAdapter(mList, getActivity());
        rySouvenir.setAdapter(mAdapter);
        rySouvenir.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && layout.findLastVisibleItemPosition() + 1 == mAdapter.getItemCount()) {
                    swipeFreshLayout.setRefreshing(true);
                    page++;
                    mSouvenirPresenterImpl.LoadingDataFromNet(false, size, page);
                }
            }
        });

        mAdapter.setOnItemLongClickListener(this);
    }

    public static SouvenirFragment newInstance() {
        return new SouvenirFragment();

    }

    @Override
    public void onResume() {
        super.onResume();
        AVAnalytics.onFragmentStart("SouvenirFragment");

    }

    @Override
    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("SouvenirFragment");
    }

    @Override
    public void initDagger() {
        mFragmentComponent.inject(this);
    }

    @Override
    public void initData() {
        mSouvenirPresenterImpl.LoadingDataFromNet(true, size, 0);
        mSubscription = mRxBus.toObservable(SouvenirEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SouvenirEvent>() {
                    @Override
                    public void call(SouvenirEvent souvenirEvent) {
                        switch (souvenirEvent.mAction) {
                            case EventConstant.REFRESH:
                                mList.clear();
                                mList.addAll(souvenirEvent.mlist);
                                break;
                            case EventConstant.LOADMORE:
                                mList.addAll(souvenirEvent.mlist);
                                break;
                            case EventConstant.DELETE:
                                mList.remove(souvenirEvent.mSouvenir);
                                break;
                            case EventConstant.ADD:
                                mList.add(0, souvenirEvent.mSouvenir);
                                break;
                            default:
                                break;
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });

        mIncrement = mRxBus.toObservable(IncrementEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<IncrementEvent>() {
                    @Override
                    public void call(IncrementEvent IncrementEvent) {
                        mList.get(IncrementEvent.position).setCommentcount(IncrementEvent.count);
                        mAdapter.notifyItemChanged(IncrementEvent.position);
                    }
                });

    }


    @Override
    public void initToolbar() {
        ActionBar toolbar = getBaseActivity().getSupportActionBar();
        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void initPresenter() {
        mSouvenirPresenterImpl.attachView(this);
    }


    @Override
    public void showRefreshingLoading() {
        swipeFreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (swipeFreshLayout != null)
                    swipeFreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void hideRefreshingLoading() {
        if (swipeFreshLayout != null)
            swipeFreshLayout.setRefreshing(false);
    }

    @Override
    public void toAddNewSouvenirActivity() {
        Intent intent = new Intent(getActivity(), AddSouvenirActivity.class);
        startActivity(intent);
    }

    @Override
    public void ToProFileActivity() {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mSouvenirPresenterImpl.LoadingDataFromNet(true, size, 0);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        if (mIncrement != null && !mIncrement.isUnsubscribed()) {
            mIncrement.unsubscribe();
        }
        mSouvenirPresenterImpl.detachView();
    }

    @Override
    public void onLongClick(View view, int position) {
        final Souvenir souvenir = mList.get(position);
        showWarningDialog("删除", "确定要删除这条Moment吗？", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
                mSouvenirPresenterImpl.delete(souvenir);
            }
        });

    }
}
