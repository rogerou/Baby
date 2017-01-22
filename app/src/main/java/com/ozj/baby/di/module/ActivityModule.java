package com.ozj.baby.di.module;

import android.app.Activity;
import android.content.Context;

import com.ozj.baby.di.scope.ActivityScope;
import com.ozj.baby.di.scope.ContextLife;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Roger on 2016/4/13.
 * 提供Activity和Context
 */
@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity activity) {

        mActivity = activity;

    }

    @Provides
    @ActivityScope
    @ContextLife("Activity")
    public Context provideContext() {
        return mActivity;
    }

    @Provides
    @ActivityScope
    public Activity provideActivity() {
        return mActivity;
    }


}
