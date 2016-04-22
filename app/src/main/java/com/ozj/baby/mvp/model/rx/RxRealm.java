package com.ozj.baby.mvp.model.rx;

import android.content.Context;

import com.avos.avoscloud.AVObject;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.bean.User;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/20.
 */
public class RxRealm {
    private Context mContext;
    private Realm mRealm;
    RealmConfiguration mRealmConfiguration;
    private static volatile RxRealm rxRealm;

    @Inject
    @Singleton
    public RxRealm(@ContextLife("Application") Context context) {
        mContext = context;
        mRealmConfiguration = new RealmConfiguration.Builder(mContext)
                .schemaVersion(1)
                .build();
        mRealm = Realm.getInstance(mRealmConfiguration);
    }

    public static RxRealm getInstance(Context context) {
        if (rxRealm == null) {
            synchronized (RxRealm.class) {
                if (rxRealm == null) {
                    rxRealm = new RxRealm(context);
                }
            }

        }
        return rxRealm;
    }

    public List<Souvenir> getALLSouvenir() {
        return mRealm.where(Souvenir.class).findAll();
    }

    public rx.Observable<Boolean> SaveUser(final User user) {
        return rx.Observable.create(new rx.Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(user);
                mRealm.commitTransaction();
                subscriber.onNext(true);

                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<User> getUserByID(final String id) {

        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> subscriber) {
                mRealm.beginTransaction();
                User user = mRealm.where(User.class).equalTo("ID", id).findFirst();
                mRealm.commitTransaction();
                subscriber.onNext(user);
                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread());

    }

    public Observable<Boolean> SaveSouvenir(final List<AVObject> list) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                mRealm.beginTransaction();
                for (AVObject souvenir : list) {
                    mRealm.copyToRealmOrUpdate(new Souvenir(souvenir));
                }
                mRealm.commitTransaction();
                subscriber.onNext(true);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io());

    }

    public Observable<List<Souvenir>> getAllSouvenir() {

        return Observable.create(new Observable.OnSubscribe<List<Souvenir>>() {
            @Override
            public void call(Subscriber<? super List<Souvenir>> subscriber) {
                mRealm.beginTransaction();
                List<Souvenir> souvenirlist = mRealm.where(Souvenir.class).findAll();
                mRealm.commitTransaction();
                subscriber.onNext(souvenirlist);
                subscriber.onCompleted();
            }
        }).subscribeOn(AndroidSchedulers.mainThread());

    }

    public Observable<Boolean> SaveSouvenir(final Souvenir souvenir) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(souvenir);
                mRealm.commitTransaction();
                subscriber.onNext(true);
                subscriber.onCompleted();

            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }


    public void closeRealm() {
        mRealm.close();
    }
}
