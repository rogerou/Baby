package com.ozj.baby.di.component;

import android.content.Context;

import com.ozj.baby.di.module.ActivityModule;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.views.home.SplashActivity;
import com.ozj.baby.util.PreferenceManager;

import dagger.Component;

/**
 * Created by Administrator on 2016/4/13.
 */
@Component(dependencies = ApplicationComponet.class, modules = ActivityModule.class)
public interface ActivityComponet {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    PreferenceManager getPreferenceManager();

    void inject(SplashActivity activity);

}
