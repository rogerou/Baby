package com.ozj.baby.mvp.model.rx;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.ozj.baby.di.scope.ContextLife;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/20.
 */
public class RxLeanCloud {
    private static volatile RxLeanCloud mRxLeanCloud;
    private Context mContext;

    @Inject
    @Singleton
    public RxLeanCloud(@ContextLife("Application") Context context) {
        mContext = context;
        mRxLeanCloud = new RxLeanCloud(mContext);
    }


    public static RxLeanCloud getInstance(Context context) {
        if (mRxLeanCloud == null) {
            synchronized (RxLeanCloud.class) {
                if (mRxLeanCloud == null) {
                    mRxLeanCloud = new RxLeanCloud(context);

                }
            }
        }
        return mRxLeanCloud;
    }

    public Observable<Boolean> SaveByLeanCloud(final AVObject object) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    object.save();
                    subscriber.onNext(true);
                } catch (AVException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> SaveUserByLeanCloud(final AVUser user) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    user.save();
                    subscriber.onNext(true);
                } catch (AVException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());

    }
}
