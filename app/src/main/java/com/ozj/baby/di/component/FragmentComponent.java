package com.ozj.baby.di.component;

import com.ozj.baby.di.scope.FragmentScope;
import com.ozj.baby.mvp.views.home.fragment.SouvenirFragment;
import com.ozj.baby.mvp.views.navigation.fragment.GalleryFragment;

import dagger.Component;

/**
 * Created by Roger on 2016/4/19.
 * Fragment容器，依赖于ActivityComponent
 * 负责为注入的Fragment提供对象，限定对应的对象的生命周期
 */
@FragmentScope
@Component(dependencies = ActivityComponent.class)
public interface FragmentComponent {

    void inject(SouvenirFragment fragment);

    void inject(GalleryFragment fragment);

}

