package com.ozj.baby.di.module;

import android.content.Context;

import com.ozj.baby.EaseUIHelper;
import com.ozj.baby.di.scope.ContextLife;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Roger on 2017/1/22.
 */

@Module
public class EaseUiModule {

    @Singleton
    @Provides
    public EaseUIHelper provideEaseUi(@ContextLife() Context context) {
        return new EaseUIHelper(context);
    }

}


