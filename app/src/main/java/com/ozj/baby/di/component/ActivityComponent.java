package com.ozj.baby.di.component;

import android.app.Activity;
import android.content.Context;

import com.ozj.baby.di.module.ActivityModule;
import com.ozj.baby.di.scope.ActivityScope;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.views.home.activity.AboutActivity;
import com.ozj.baby.mvp.views.home.activity.AddSouvenirActivity;
import com.ozj.baby.mvp.views.home.activity.CommentActivity;
import com.ozj.baby.mvp.views.home.activity.MainActivity;
import com.ozj.baby.mvp.views.home.activity.ProfileActivity;
import com.ozj.baby.mvp.views.login.activity.SplashActivity;
import com.ozj.baby.util.PreferenceManager;

import dagger.Component;

/**
 * Created by Roger on 2016/4/13.
 * Activity的容器，依赖ApplicationComponent
 * 负责Activity的注入对象的生命周期
 */
@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife()
    Context getApplicationContext();

    PreferenceManager getPreferenceManager();

//    RxBabyRealm getBabyRealm();

    Activity getActivity();

    RxLeanCloud getRxLeanCLoud();

    RxBus getRxBus();

    void inject(SplashActivity activity);

    void inject(MainActivity activity);

    void inject(ProfileActivity activity);

    void inject(AddSouvenirActivity activity);

    void inject(AboutActivity activity);

    void inject(CommentActivity activity);
}
