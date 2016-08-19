//package com.ozj.baby.mvp.model.rx;
//
//import android.content.Context;
//
//import com.ozj.baby.di.scope.ContextLife;
//import com.ozj.baby.mvp.model.bean.News;
//
//import javax.inject.Inject;
//import javax.inject.Singleton;
//
//import io.realm.Realm;
//import io.realm.RealmConfiguration;
//import io.realm.RealmResults;
//import rx.Observable;
//
///**
// * Created by Administrator on 2016/4/20.
// */
//public class RxBabyRealm {
//    private Context mContext;
//    private Realm mRealm;
//    RealmConfiguration mRealmConfiguration;
//    private static volatile RxBabyRealm rxBabyRealm;
//
//    @Inject
//    @Singleton
//    public RxBabyRealm(@ContextLife("Application") Context context) {
//        mContext = context;
//        mRealmConfiguration = new RealmConfiguration.Builder(mContext)
//                .name("rxBabyRealm")
//                .schemaVersion(7)
//                .build();
//        mRealm = Realm.getInstance(mRealmConfiguration);
//    }
//
//    public static RxBabyRealm getInstance(Context context) {
//        if (rxBabyRealm == null) {
//            synchronized (RxBabyRealm.class) {
//                if (rxBabyRealm == null) {
//                    rxBabyRealm = new RxBabyRealm(context);
//                }
//            }
//
//        }
//        return rxBabyRealm;
//    }
//
//    public Realm getRealm() {
//        return mRealm;
//    }
//
////    public Observable<RealmResults<Souvenir>> getSouvenirALl() {
////        return mRealm.where(Souvenir.class).findAllAsync().asObservable();
////    }
////
////    public void saveSouvenList(List<Souvenir> mList) {
////        mRealm.beginTransaction();
////        mRealm.copyToRealmOrUpdate(mList);
////        mRealm.commitTransaction();
////    }
//
//
////    public void saveUser(User user) {
////        mRealm.beginTransaction();
////        mRealm.copyToRealmOrUpdate(user);
////        mRealm.commitTransaction();
////    }
//
////    public void saveSouvenir(Souvenir souvenir) {
////        mRealm.beginTransaction();
////        mRealm.copyToRealmOrUpdate(souvenir);
////        mRealm.commitTransaction();
////
////    }
//
////    public Observable<User> getUser(String id) {
////        return mRealm.where(User.class).equalTo("ID", id).findFirst().asObservable();
////
////    }
//
////    public RealmResults<Gallery> getAllGallery() {
////        return mRealm.where(Gallery.class).findAll();
////    }
////
////    public void saveGalleryList(final List<Gallery> mList) {
////        mRealm.executeTransactionAsync(new Realm.Transaction() {
////            @Override
////            public void execute(Realm realm) {
////                realm.copyToRealmOrUpdate(mList);
////            }
////        });
////
////    }
////
////    public void saveGallery(Gallery mGallery) {
////        mRealm.beginTransaction();
////        mRealm.copyToRealmOrUpdate(mGallery);
////        mRealm.commitTransaction();
////
////    }
//
//    public void saveNews(News news) {
//        mRealm.beginTransaction();
//        mRealm.copyToRealmOrUpdate(news);
//        mRealm.commitTransaction();
//
//    }
//
//    public Observable<RealmResults<News>> getAllNews() {
//        return mRealm.where(News.class).findAllSorted("time").asObservable();
//    }
//
//    public void Close() {
//        mRealm.close();
//    }
//
//}
