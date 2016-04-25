package com.ozj.baby.di.component;

import android.content.Context;

import com.ozj.baby.di.module.FragmentModule;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.di.scope.FragmentScope;
import com.ozj.baby.mvp.model.rx.RxBabyRealm;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.views.home.fragment.SouvenirFragment;
import com.ozj.baby.mvp.views.navigation.fragment.GalleryFragment;
import com.ozj.baby.util.PreferenceManager;

import dagger.Component;

/**
 * Created by Administrator on 2016/4/19.
 */
@FragmentScope
@Component(modules = FragmentModule.class, dependencies = ApplicationComponet.class)
public interface FragmentComponet {
    @ContextLife("Application")
    Context getContext();

    @ContextLife("Activity")
    Context getActivityContext();

    RxBabyRealm getBabyRealm();

    PreferenceManager getPreferenceManager();

    RxBus getRxBus();

    RxLeanCloud getRxLeanCloud();

    void inject(SouvenirFragment fragment);

    void inject(GalleryFragment fragment);

}

