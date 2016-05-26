package com.ozj.baby.mvp.views.home.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVAnalytics;
import com.orhanobut.logger.Logger;
import com.ozj.baby.R;
import com.ozj.baby.adapter.SouvenirAdapter;
import com.ozj.baby.base.BaseFragment;
import com.ozj.baby.event.AddSouvenirEvent;
import com.ozj.baby.event.IncrementEvent;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.presenter.home.impl.SouvenirPresenterImpl;
import com.ozj.baby.mvp.views.home.ISouvenirVIew;
import com.ozj.baby.mvp.views.home.activity.AddSouvenirActivity;
import com.ozj.baby.mvp.views.home.activity.ProfileActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Rogerou on 2016/4/19.
 * 
 */
public class SouvenirFragment extends BaseFragment implements ISouvenirVIew, SwipeRefreshLayout.OnRefreshListener {
    @Inject
    SouvenirPresenterImpl mSouvenirPresenterImpl;
    @Inject
    RxBus mRxbus;
    SouvenirAdapter mAdapter;
    @Bind(R.id.ry_souvenir)
    RecyclerView rySouvenir;
    @Bind(R.id.swipeFreshLayout)
    SwipeRefreshLayout swipeFreshLayout;
    Subscription mSubscription;
    Subscription mIncrement;

    int page = 0;
    int size = 15;
    LinearLayoutManager layout;
    List<Souvenir> mList = new ArrayList<>();
    boolean isFirst;

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
    }

    public static SouvenirFragment newInsatance() {
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
        mFragmentComponet.inject(this);

    }

    @Override
    public void initData() {
        mSouvenirPresenterImpl.LoadingDataFromNet(true, size, 0);
        mSubscription = mRxbus.toObservable(AddSouvenirEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddSouvenirEvent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("出错啦，请稍后再试");
                        Logger.e(e.getMessage());

                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onNext(AddSouvenirEvent addSouvenirEvent) {
                        if (addSouvenirEvent.isList()) {
                            if (addSouvenirEvent.isRefresh()) {
                                mList.clear();
                                mList.addAll(addSouvenirEvent.getMlist());
                                mAdapter.notifyDataSetChanged();
                            } else {
                                for (Souvenir s : addSouvenirEvent.getMlist()) {
                                    if (!mList.contains(s)) {
                                        mList.add(s);
                                        mAdapter.notifyItemInserted(mList.size() - 1);
                                    }
                                }
                            }
                        } else {
                            mList.add(0, addSouvenirEvent.getSouvenir());
                            mAdapter.notifyItemInserted(0);
                        }

                    }
                });
        mIncrement = mRxbus.toObservable(IncrementEvent.class)
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
        if (mIncrement != null && mIncrement.isUnsubscribed()) {
            mIncrement.unsubscribe();
        }
        mSouvenirPresenterImpl.detachView();
    }

}
