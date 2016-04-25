package com.ozj.baby.di.module;

import android.app.Activity;
import android.content.Context;

import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2016/4/19.
 */
@Module
public class FragmentModule {

    private android.app.Fragment mFragment;

    public FragmentModule(android.app.Fragment fragment) {
        this.mFragment = fragment;
    }

    @Provides
    @FragmentScope
    @ContextLife("Activity")
    public Context provideContext() {
        return mFragment.getActivity();
    }

    @Provides
    @FragmentScope
    public Activity provideActivity() {
        return mFragment.getActivity();
    }

    @Provides
    @FragmentScope
    public android.app.Fragment provideFragment() {
        return mFragment;
    }


}
