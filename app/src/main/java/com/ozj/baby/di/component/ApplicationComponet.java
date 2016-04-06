package com.ozj.baby.di.component;

import android.content.Context;

import com.ozj.baby.di.module.ApplicationModule;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.util.PreferenceManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2016/4/13.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponet {
    @ContextLife("Application")
    Context getContext();

    PreferenceManager getPreferenceManager();

}
