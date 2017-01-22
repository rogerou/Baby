package com.ozj.baby.di.component;

import android.content.Context;

import com.ozj.baby.BabyApplicationLike;
import com.ozj.baby.EaseUIHelper;
import com.ozj.baby.di.module.ApplicationModule;
import com.ozj.baby.di.module.EaseUiModule;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.util.PreferenceManager;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Roger on 2016/4/13.
 * <p>
 * Application容器，负责提供几个全局使用到的对象
 */
@Singleton
@Component(modules = {ApplicationModule.class, EaseUiModule.class})
public interface ApplicationComponent {
    @ContextLife()
    Context getContext();

    PreferenceManager getPreferenceManager();


    RxLeanCloud getRxLeanCLoud();

    RxBus getRxBus();

    EaseUIHelper getEaseUiHelper();


    void inject(BabyApplicationLike babyApplicationLike);
}
