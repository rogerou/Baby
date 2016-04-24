package com.ozj.baby.di.module;

import android.content.Context;

import com.ozj.baby.BabyApplication;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.rx.RxBabyRealm;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.util.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/13.
 */
@Module
public class ApplicationModule {
    private BabyApplication mApplication;
    private PreferenceManager mPreferenceManager;
    private RxBabyRealm mRxBabyRealm;
    private RxLeanCloud mRxLeanCloud;
    private RxBus mRxBus;

    public ApplicationModule(BabyApplication application) {
        mApplication = application;
        mPreferenceManager = new PreferenceManager(mApplication.getApplicationContext());
        mRxBabyRealm = new RxBabyRealm(mApplication.getApplicationContext());
        mRxLeanCloud = new RxLeanCloud(mApplication.getApplicationContext());
        mRxBus = RxBus.getDefaultInstance();
    }

    @Provides
    @Singleton
    @ContextLife("Application")
    public Context provideConext() {
        return mApplication.getApplicationContext();
    }


    @Provides
    @Singleton
    public PreferenceManager providePreferceManager() {
        return mPreferenceManager;
    }

    @Provides
    @Singleton
    public RxBabyRealm provideBabyRealm() {
        return mRxBabyRealm;

    }

    @Provides
    @Singleton
    public RxLeanCloud provideRxLeanCloud() {
        return mRxLeanCloud;
    }

    @Provides
    @Singleton
    public RxBus provideRxBus() {
        return mRxBus;
    }
}
