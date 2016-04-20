package com.ozj.baby.di.module;

import android.content.Context;

import com.ozj.baby.BabyApplication;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.model.rx.RxRealm;
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
    private RxRealm mRxRealm;
    private RxLeanCloud mRxLeanCloud;

    public ApplicationModule(BabyApplication application) {
        mApplication = application;
        mPreferenceManager = new PreferenceManager(mApplication.getApplicationContext());
        mRxRealm = new RxRealm(mApplication.getApplicationContext());
        mRxLeanCloud = new RxLeanCloud(mApplication.getApplicationContext());
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
    public RxRealm provideRxRealm() {
        return mRxRealm;

    }
}
