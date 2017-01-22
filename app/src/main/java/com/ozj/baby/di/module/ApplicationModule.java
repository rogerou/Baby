package com.ozj.baby.di.module;

import android.app.Application;
import android.content.Context;

import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.util.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Roger on 2016/4/13.
 * 在Application里边生成的全局对象
 */
@Module
public class ApplicationModule {
    private final Application mApplication;
    private final PreferenceManager mPreferenceManager;
    private final RxLeanCloud mRxLeanCloud;
    private final RxBus mRxBus;

    public ApplicationModule(Application application) {
        mApplication = application;
        mPreferenceManager = new PreferenceManager(mApplication.getApplicationContext());
        mRxLeanCloud = new RxLeanCloud();
        mRxBus = new RxBus();
    }

    @Provides
    @Singleton
    @ContextLife()
    public Context provideContext() {
        return mApplication.getApplicationContext();
    }


    @Provides
    @Singleton
    public PreferenceManager providePreferenceManager() {
        return mPreferenceManager;
    }

//    @Provides
//    @Singleton
//    public RxBabyRealm provideBabyRealm() {
//        return mRxBabyRealm;
//
//    }

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
