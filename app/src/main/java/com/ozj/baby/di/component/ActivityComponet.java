package com.ozj.baby.di.component;

import android.app.Activity;
import android.content.Context;

import com.ozj.baby.di.module.ActivityModule;
import com.ozj.baby.di.scope.ActivityScope;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.views.login.activity.SplashActivity;
import com.ozj.baby.util.PreferenceManager;

import dagger.Component;

/**
 * Created by Administrator on 2016/4/13.
 */
@ActivityScope
@Component(dependencies = ApplicationComponet.class, modules = ActivityModule.class)
public interface ActivityComponet {
    @ContextLife("Activity")
    Context getActivityContext();


    @ContextLife("Application")
    Context getApplicationContext();

    PreferenceManager getPreferenceManager();

    Activity getActivity();

    void inject(SplashActivity activity);


}
