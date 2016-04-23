package com.ozj.baby.mvp.model.realm;

import android.content.Context;

import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.bean.User;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Administrator on 2016/4/20.
 */
public class BabyRealm {
    private Context mContext;
    private Realm mRealm;
    RealmConfiguration mRealmConfiguration;
    private static volatile BabyRealm babyRealm;

    @Inject
    @Singleton
    public BabyRealm(@ContextLife("Application") Context context) {
        mContext = context;
        mRealmConfiguration = new RealmConfiguration.Builder(mContext)
                .name("babyRealm")
                .schemaVersion(7)
                .build();
        mRealm = Realm.getInstance(mRealmConfiguration);
    }

    public static BabyRealm getInstance(Context context) {
        if (babyRealm == null) {
            synchronized (BabyRealm.class) {
                if (babyRealm == null) {
                    babyRealm = new BabyRealm(context);
                }
            }

        }
        return babyRealm;
    }

    public List<Souvenir> getSouvenirALl() {
        return mRealm.where(Souvenir.class).findAll();
    }

    public void saveSouvenList(List<Souvenir> list) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(list);
        mRealm.commitTransaction();
    }


    public void saveUser(User user) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(user);
        mRealm.commitTransaction();
    }

    public void saveSouvenir(Souvenir souvenir) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(souvenir);
        mRealm.commitTransaction();

    }

    public User getUser(String id) {
        return mRealm.where(User.class).equalTo("ID", id).findFirst();

    }

    public void closeRealm() {
        mRealm.close();
    }
}
