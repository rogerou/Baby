package com.ozj.baby.mvp.presenter.home.impl;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.event.CommentsEvent;
import com.ozj.baby.event.EventConstant;
import com.ozj.baby.event.IncrementEvent;
import com.ozj.baby.mvp.model.bean.Comment;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.presenter.home.ICommentPresenter;
import com.ozj.baby.mvp.views.ICommentView;
import com.ozj.baby.util.SchedulersCompat;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by RogerOu on 2016/5/25.
 * <p>
 * 评论Presenter
 */
public class CommentPresenterImpl implements ICommentPresenter {


    private ICommentView mCommentView;

    private final RxLeanCloud mRxLeanCloud;
    private final RxBus mRxBus;
    private Subscription mSubscription;


    @Inject
    public CommentPresenterImpl(RxLeanCloud rxLeanCloud, RxBus rxBus) {
        mRxLeanCloud = rxLeanCloud;
        mRxBus = rxBus;
    }

    @Override
    public void fetchAllComments(Souvenir souvenir, final int page, int size) {
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
                    }

                    @Override
                    public void onNext(List<Comment> comments) {
                        if (page == 0) {
                            mRxBus.post(new CommentsEvent(null, comments, EventConstant.REFRESH));
                        } else {
                            mRxBus.post(new CommentsEvent(null, comments, EventConstant.LOADMORE));
                        }
                    }
                });
    }


    @Override
    public void Comment(final Comment comment, final int position) {
        mRxLeanCloud.incrementComments(comment.getSouvenir())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mCommentView.showProgress("正在发表评论...");
                    }
                })
                .flatMap(new Func1<Integer, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Integer integer) {
                        mRxBus.post(new IncrementEvent(integer, position));
                        return mRxLeanCloud.PushToLover(String.format("Ta回复你说 %s", comment.getComment()), 0);
                    }
                }).flatMap(new Func1<Boolean, Observable<Comment>>() {
            @Override
            public Observable<Comment> call(Boolean aBoolean) {
                return mRxLeanCloud.createComment(comment);
            }
        }).compose(SchedulersCompat.<Comment>observeOnMainThread())
                .subscribe(new Subscriber<Comment>() {
                    @Override
                    public void onCompleted() {
                        mCommentView.hideProgress();
                        mCommentView.showToast("评论成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCommentView.showToast("出错啦，请稍后再试");
                        mCommentView.hideProgress();
                    }

                    @Override
                    public void onNext(Comment com) {
                        mRxBus.post(new CommentsEvent(com, null, EventConstant.ADD));
                    }
                });
    }

    @Override
    public void deleteComment(final Comment comment) {
        mRxLeanCloud.delete(comment)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mCommentView.showProgress("正在删除评论");
                    }
                }).compose(SchedulersCompat.<Boolean>observeOnMainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        mCommentView.showToast("删除成功");
                        mCommentView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCommentView.hideProgress();
                        mCommentView.showToast("删除失败");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        Logger.e("删除评论" + aBoolean);
                        mRxBus.post(new CommentsEvent(comment, null, EventConstant.DELETE));
                    }
                });
    }


    @Override
    public void attachView(@NonNull BaseView view) {
        mCommentView = (ICommentView) view;
    }

    @Override
    public void detachView() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
