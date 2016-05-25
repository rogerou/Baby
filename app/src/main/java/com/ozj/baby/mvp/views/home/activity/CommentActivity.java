package com.ozj.baby.mvp.views.home.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ozj.baby.R;
import com.ozj.baby.adapter.CommentAdapter;
import com.ozj.baby.base.BaseActivity;
import com.ozj.baby.event.AddCommentsEvent;
import com.ozj.baby.mvp.model.bean.Comment;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.presenter.home.impl.CommentPresenterImpl;
import com.ozj.baby.mvp.views.ICommentView;
import com.ozj.baby.util.SchedulersCompat;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import rx.Observer;
import rx.Subscription;

/**
 * Created by Rogerou on 2016/5/25.
 */
public class CommentActivity extends BaseActivity implements ICommentView, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ry_comment)
    RecyclerView mRyComment;
    @Bind(R.id.swipeFreshLayout)
    SwipeRefreshLayout mSwipeFreshLayout;
    @Bind(R.id.et_comment)
    EditText mEtComment;
    @Bind(R.id.btn_send)
    Button mBtnSend;
    boolean isReplied = false;
    @Inject
    CommentPresenterImpl mCommentPresenter;
    @Inject
    RxBus mRxBus;

    Subscription mSubscription;
    int size = 15, page = 0;
    Souvenir mSouvenir;
    List<Comment> mList;
    CommentAdapter mAdapter;

    @Override
    public void initDagger() {
        mActivityComponet.inject(this);
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_comment);
        mSouvenir = (Souvenir) getIntent().getExtras().get("moment");
        mSubscription = RegisterEvent();
    }

    private Subscription RegisterEvent() {
        return mRxBus.toObservable(AddCommentsEvent.class)
                .compose(SchedulersCompat.<AddCommentsEvent>observeOnMainThread())
                .subscribe(new Observer<AddCommentsEvent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("出错啦，请稍后再试");
                    }

                    @Override
                    public void onNext(AddCommentsEvent addCommentsEvent) {
                        if (addCommentsEvent.isRefresh && addCommentsEvent.mCommentList.size() != 0) {
                            mList.clear();
                            mAdapter.notifyItemRangeRemoved(0, mAdapter.getItemCount());
                            mList.addAll(addCommentsEvent.mCommentList);
                            mAdapter.notifyItemRangeInserted(0, addCommentsEvent.mCommentList.size());
                        } else {
                            int size = mAdapter.getItemCount();
                            mList.addAll(addCommentsEvent.mCommentList);
                            mAdapter.notifyItemRangeInserted(size - 1, addCommentsEvent.mCommentList.size());
                        }
                    }
                });
    }

    @Override
    public void initViewsAndListener() {
        final LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRyComment.setLayoutManager(mLinearLayoutManager);
        mRyComment.setItemAnimator(new FadeInUpAnimator());
        mRyComment.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
        mSwipeFreshLayout.setOnRefreshListener(this);
        mSwipeFreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mList = new ArrayList<>();
        mAdapter = new CommentAdapter(mList, this);
        mRyComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && mLinearLayoutManager.findLastVisibleItemPosition() + 1 == mAdapter.getItemCount()) {
                    mSwipeFreshLayout.setRefreshing(true);
                    page++;
                    mCommentPresenter.fetchAllComments(mSouvenir, size, page);
                }
            }
        });
        mEtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(mEtComment.getText().toString())) {
                    mBtnSend.setEnabled(false);
                } else {
                    mBtnSend.setEnabled(true);
                }
            }
        });
        mCommentPresenter.fetchAllComments(mSouvenir, page, size);
    }


    @Override
    public void initPresenter() {
        mCommentPresenter.attachView(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setVisible(isReplied);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.cancel_comment) {

        }
        return true;
    }

    @Override
    public void initToolbar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void showLoading() {
        mSwipeFreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeFreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void hideLoading() {
        mSwipeFreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeFreshLayout.setRefreshing(false);
            }
        }, 1000);
    }


    @OnClick(R.id.btn_send)
    public void onClick() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCommentPresenter.detachView();
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onRefresh() {
        mCommentPresenter.fetchAllComments(mSouvenir, 0, size);
    }
}
