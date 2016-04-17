package com.ozj.baby.di.module;

import android.content.Context;

import com.ozj.baby.BabyApplication;
import com.ozj.baby.di.scope.ContextLife;
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


    public ApplicationModule(BabyApplication application) {
        mApplication = application;
        mPreferenceManager = new PreferenceManager(mApplication.getApplicationContext());
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
}
