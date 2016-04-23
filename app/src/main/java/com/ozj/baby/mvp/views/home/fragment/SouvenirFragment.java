package com.ozj.baby.mvp.views.home.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ozj.baby.R;
import com.ozj.baby.adapter.SouvenirAdapter;
import com.ozj.baby.base.BaseFragment;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.realm.BabyRealm;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.presenter.home.impl.SouvenirPresenterImpl;
import com.ozj.baby.mvp.views.home.ISouvenirVIew;
import com.ozj.baby.mvp.views.home.activity.AddSouvenirActivity;
import com.ozj.baby.mvp.views.home.activity.ProfileActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import rx.Subscription;

/**
 * Created by Administrator on 2016/4/19.
 */
public class SouvenirFragment extends BaseFragment implements ISouvenirVIew, SwipeRefreshLayout.OnRefreshListener {
    @Inject
    SouvenirPresenterImpl mSouvenirPresenterImpl;
    @Inject
    BabyRealm mBabyRealm;
    @Inject
    RxBus mRxbus;
    SouvenirAdapter mAdapter;
    @Bind(R.id.ry_souvenir)
    RecyclerView rySouvenir;
    @Bind(R.id.swipeFreshLayout)
    SwipeRefreshLayout swipeFreshLayout;
    List<Souvenir> mlist;
    Subscription mSubscription;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main;
    }

    @Override
    public void initViews() {
        mSouvenirPresenterImpl.attachView(this);
        swipeFreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rySouvenir.setLayoutManager(linearLayoutManager);
        rySouvenir.setItemAnimator(new DefaultItemAnimator());
        rySouvenir.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .colorResId(R.color.DividerColor)
                .size(1)
                .build());

    }

    @Override
    public void initDagger() {
        mFragmentComponet.inject(this);

    }

    @Override
    public void initData() {

        mlist = new ArrayList<>();
//        if (mlist.size() == 0) {
//            showToast("这里之间什么记录都没有哦，赶紧去增加几个吧");
//        }
        Souvenir souvenir = new Souvenir();
        souvenir.setContent("qweqdasdasdasdsa");
        souvenir.setLikedMine(false);
        souvenir.setLikedOther(false);
        souvenir.setTimeStamp(System.currentTimeMillis());
        mlist.add(souvenir);
        mAdapter = new SouvenirAdapter(mlist, getActivity());
        rySouvenir.setAdapter(mAdapter);
//        mSubscription = mRxbus.toObservable(UpdateComPlete.class)
//                .subscribe(new Action1<UpdateComPlete>() {
//                    @SuppressWarnings("unchecked")
//                    @Override
//                    public void call(UpdateComPlete updateComPlete) {
//                        if (updateComPlete.isComPlete()) {
//                            for (Souvenir souvenir : mSouvenirPresenterImpl.getDataFromLocal()) {
//                                if (!mlist.contains(souvenir)) {
//                                    mlist.add(souvenir);
//                                }
//                            }
//                            Collections.sort(mlist);
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
    }

    @Override
    public void initToolbar() {
        ActionBar toolbar = getBaseActivity().getSupportActionBar();
        if (toolbar != null) {
            toolbar.setDisplayHomeAsUpEnabled(true);
        }

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
        mSouvenirPresenterImpl.LoadingDataFromNet();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.isUnsubscribed();
        }

    }
}
