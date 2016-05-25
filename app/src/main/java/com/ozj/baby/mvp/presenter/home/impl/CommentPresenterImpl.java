package com.ozj.baby.mvp.presenter.home.impl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.bean.Comment;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.presenter.home.ICommentPresenter;
import com.ozj.baby.mvp.views.ICommentView;

import java.util.List;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by RogerOu on 2016/5/25.
 * <p>
 * 评论Presenter
 */
public class CommentPresenterImpl implements ICommentPresenter {


    ICommentView mCommentView;

    Context mContext;
    RxLeanCloud mRxLeanCloud;
    Subscription mSubscription;

    @Inject
    public CommentPresenterImpl(@ContextLife("Activity") Context context, RxLeanCloud rxLeanCloud) {
        mContext = context;
        mRxLeanCloud = rxLeanCloud;
    }

    @Override
    public void fetchAllComments(Souvenir souvenir, int page, int size) {
        mSubscription = mRxLeanCloud.getComments(souvenir, page, size)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mCommentView.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Comment>>() {
                    @Override
                    public void onCompleted() {
                        mCommentView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCommentView.hideLoading();
                        mCommentView.showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Comment> comments) {

                    }
                });
    }


    @Override
    public void Comment(Comment comment) {
        mRxLeanCloud.createComment(comment)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mCommentView.showProgress("正在发表评论...");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Comment>() {
                    @Override
                    public void onCompleted() {
                        mCommentView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCommentView.hideProgress();
                    }

                    @Override
                    public void onNext(Comment comment) {

                    }
                });
    }


    @Override
    public void attachView(@NonNull BaseView view) {
        mCommentView = (ICommentView) view;
    }

    @Override
    public void detachView() {
        if (mSubscription != null && mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
