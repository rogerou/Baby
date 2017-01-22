package com.ozj.baby.mvp.views.home.activity;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.hyphenate.easeui.domain.User;
import com.jaeger.library.StatusBarUtil;
import com.ozj.baby.R;
import com.ozj.baby.adapter.CommentAdapter;
import com.ozj.baby.base.BaseActivity;
import com.ozj.baby.event.CommentsEvent;
import com.ozj.baby.event.EventConstant;
import com.ozj.baby.mvp.model.bean.Comment;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.presenter.home.impl.CommentPresenterImpl;
import com.ozj.baby.mvp.views.ICommentView;
import com.ozj.baby.util.OnItemClickListener;
import com.ozj.baby.util.OnItemLongClickListener;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

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
 * Created by Rogerou on 2016/5/25.
 */
public class CommentActivity extends BaseActivity implements ICommentView, SwipeRefreshLayout.OnRefreshListener, OnItemLongClickListener, OnItemClickListener {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ry_comment)
    RecyclerView mRyComment;
    @BindView(R.id.swipeFreshLayout)
    SwipeRefreshLayout mSwipeFreshLayout;
    @BindView(R.id.et_comment)
    EditText mEtComment;
    @BindView(R.id.btn_send)
    Button mBtnSend;
    boolean isReplied;
    @Inject
    CommentPresenterImpl mCommentPresenter;
    @Inject
    RxBus mRxBus;

    Subscription mSubscription;
    final int size = 15;
    int page;
    Souvenir mSouvenir;
    List<Comment> mList;
    CommentAdapter mAdapter;
    Comment mComment;
    int position;
    InputMethodManager inputMethodManager;

    @Override
    protected void initData() {
        String souvenirId = getIntent().getExtras().getString("moment");
        try {
            mSouvenir = AVObject.createWithoutData(Souvenir.class, souvenirId);
        } catch (AVException e) {
            e.printStackTrace();
        }
        position = getIntent().getExtras().getInt("position");
        mCommentPresenter.fetchAllComments(mSouvenir, page, size);
        mSubscription = registerEvent();
        mComment = new Comment();
        mComment.setSouvenir(mSouvenir);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void initDagger() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_comment);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    private Subscription registerEvent() {
        return mRxBus.toObservable(CommentsEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CommentsEvent>() {
                    @Override
                    public void call(CommentsEvent event) {
                        switch (event.mAction) {
                            case EventConstant.REFRESH:
                                mList.clear();
                                mList.addAll(event.mCommentList);
                                break;
                            case EventConstant.LOADMORE:
                                mList.addAll(event.mCommentList);
                                break;
                            case EventConstant.DELETE:
                                mList.remove(event.mComment);
                                break;
                            case EventConstant.ADD:
                                mList.add(0, event.mComment);
                                break;
                            default:
                                break;
                        }
                        mComment = new Comment();
                        mComment.setSouvenir(mSouvenir);
                        mAdapter.notifyDataSetChanged();
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
        mRyComment.setAdapter(mAdapter);
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
        mAdapter.setItemOnclicklistener(this);
        mAdapter.setItemOnLongClickListener(this);
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
            mEtComment.setHint(getString(R.string.add_comment));
            mComment.setReply(null);
            isReplied = false;
            invalidateOptionsMenu();
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
        if (mSwipeFreshLayout!=null)
        mSwipeFreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeFreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void hideLoading() {
        if (mSwipeFreshLayout != null)
            mSwipeFreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeFreshLayout.setRefreshing(false);
                }
            }, 1000);
    }


    @OnClick(R.id.btn_send)
    public void onClick() {
        mComment.setComment(mEtComment.getText().toString());
        mComment.setAuhtor(User.getCurrentUser(User.class));
        mCommentPresenter.Comment(mComment, position);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCommentPresenter.detachView();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onRefresh() {
        mCommentPresenter.fetchAllComments(mSouvenir, 0, size);
    }

    @Override
    public void onClick(View view, int position) {
        if (mList.get(position).getAuhtor().equals(User.getCurrentUser(User.class))) {
            showToast("不自己回复自己");
            return;
        }
        mEtComment.setHint(String.format("回复 %s 的评论:", mList.get(position).getAuhtor().getNick()));
        mEtComment.requestFocus();

        inputMethodManager.showSoftInput(mEtComment, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        mComment.setReply(mList.get(position).getAuhtor());
        isReplied = true;
        invalidateOptionsMenu();
    }

    @Override
    public void onLongClick(View view, int position) {
        final Comment comment = mList.get(position);
        showWarningDialog("删除", "确定要删除评论吗？", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
                mCommentPresenter.deleteComment(comment);
            }
        });
    }
}
