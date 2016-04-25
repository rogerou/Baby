package com.ozj.baby.mvp.model.rx;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.orhanobut.logger.Logger;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.mvp.model.dao.GalleryDao;
import com.ozj.baby.mvp.model.dao.SouvenirDao;
import com.ozj.baby.mvp.model.dao.UserDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/20.
 */
public class RxLeanCloud {
    private static volatile RxLeanCloud mRxLeanCloud;
    private Context mContext;

    @Singleton
    @Inject
    public RxLeanCloud(@ContextLife("Application") Context context) {
        mContext = context;
    }


    public RxLeanCloud getInstance(Context context) {
        if (mRxLeanCloud == null) {
            synchronized (RxLeanCloud.class) {
                if (mRxLeanCloud == null) {
                    mRxLeanCloud = new RxLeanCloud(context);

                }
            }
        }
        return mRxLeanCloud;
    }

    public Observable<AVObject> SaveByLeanCloud(final AVObject object) {
        return Observable.create(new Observable.OnSubscribe<AVObject>() {
            @Override
            public void call(final Subscriber<? super AVObject> subscriber) {
                object.setFetchWhenSave(true);
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(object);
                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Boolean> SaveUserByLeanCloud(final AVUser user) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {

                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(true);
                        } else {
                            subscriber.onError(e);
                            Logger.e(e.getMessage());
                        }
                        subscriber.onCompleted();
                    }
                });

            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<AVUser> GetUserByLeanCloud(final String objectId) {

        return Observable.create(new Observable.OnSubscribe<AVUser>() {
            @Override
            public void call(Subscriber<? super AVUser> subscriber) {

                AVQuery<AVUser> query = AVUser.getQuery();

                try {
                    AVUser user = query.include(UserDao.AVATARURL).get(objectId);
                    subscriber.onNext(user);

                } catch (AVException e) {
                    e.printStackTrace();
                    subscriber.onError(e);

                }
                subscriber.onCompleted();


            }
        }).subscribeOn(Schedulers.io());

    }

    public Observable<AVUser> GetUserByUsername(final String username) {
        return Observable.create(new Observable.OnSubscribe<AVUser>() {
            @Override
            public void call(Subscriber<? super AVUser> subscriber) {
                AVQuery<AVUser> query = AVUser.getQuery();
                try {
                    AVUser user = query.whereEqualTo(UserDao.USERNAME, username).getFirst();
                    subscriber.onNext(user);
                } catch (AVException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
                subscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.io());

    }

    public Observable<List<AVObject>> GetALlSouvenirByLeanCloud(final String authorId, final String loverid, final int size, final int page) {

        return Observable.create(new Observable.OnSubscribe<List<AVObject>>() {
            @Override
            public void call(final Subscriber<? super List<AVObject>> subscriber) {

                AVQuery<AVObject> query = AVQuery.getQuery(SouvenirDao.TABLENAME);
                query.whereEqualTo(SouvenirDao.SOUVENIR_AUTHORID, authorId);
                AVQuery<AVObject> query1 = AVQuery.getQuery(SouvenirDao.TABLENAME);
                query1.whereEqualTo(SouvenirDao.SOUVENIR_AUTHORID, loverid);
                List<AVQuery<AVObject>> queries = new ArrayList<>();
                queries.add(query);
                queries.add(query1);
                AVQuery<AVObject> mainquery = AVQuery.or(queries);
                mainquery.setLimit(size);
                mainquery.setSkip(size * page);
                mainquery.include(SouvenirDao.SOUVENIR_AUTHOR);
                mainquery.orderByDescending("createdAt");
                mainquery.setCachePolicy(AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
                mainquery.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {
                            subscriber.onNext(list);
                        } else {
                            subscriber.onError(e);
                            Logger.e(e.getMessage());
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());

    }


    public Observable<String> UploadPicture(final AVFile avFile) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                avFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            subscriber.onNext(avFile.getUrl());
                        } else {
                            subscriber.onError(e);
                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(AndroidSchedulers.mainThread());

    }

    public Observable<List<AVObject>> FetchAllPicture(final String authorId, final String theotherone) {
        return Observable.create(new Observable.OnSubscribe<List<AVObject>>() {
            @Override
            public void call(final Subscriber<? super List<AVObject>> subscriber) {
                AVQuery<AVObject> query = AVQuery.getQuery(GalleryDao.TABLENAME);
                query.whereEqualTo(GalleryDao.AUTHORID, authorId);
                AVQuery<AVObject> query1 = AVQuery.getQuery(GalleryDao.TABLENAME);
                query1.whereEqualTo(GalleryDao.AUTHORID, theotherone);
                List<AVQuery<AVObject>> queries = new ArrayList<>();
                AVQuery<AVObject> mainquery = AVQuery.or(queries);
                mainquery.orderByDescending("createdAt");
                mainquery.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {
                            subscriber.onNext(list);
                        } else {
                            subscriber.onError(e);

                        }
                        subscriber.onCompleted();
                    }
                });

            }
        }).subscribeOn(AndroidSchedulers.mainThread());


    }


}
