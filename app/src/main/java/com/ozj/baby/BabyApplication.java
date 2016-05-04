package com.ozj.baby;

import android.app.Application;
import android.content.Intent;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.orhanobut.logger.AndroidLogTool;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.ozj.baby.di.component.ApplicationComponet;
import com.ozj.baby.di.component.DaggerApplicationComponet;
import com.ozj.baby.di.module.ApplicationModule;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.hyphenate.easeui.domain.User;
import com.ozj.baby.mvp.views.home.activity.MainActivity;
import com.squareup.leakcanary.LeakCanary;


import javax.inject.Inject;

import im.fir.sdk.FIR;

/**
 * Created by Roger ou on 2016/3/25.
 * 初始化leancloud
 */
public class BabyApplication extends Application {
    private ApplicationComponet mAppComponet;
    @Inject
    private InitEaseUI easeUI;

    @Override
    public void onCreate() {
        super.onCreate();
        initThirdService();
        initEaseUI();
    }

    private void initThirdService() {
        AVUser.alwaysUseSubUserClass(User.class);
        AVObject.registerSubclass(Gallery.class);
        AVObject.registerSubclass(Souvenir.class);
        AVOSCloud.initialize(this, "GpLTBKYub2ekB1GG2UUDdpmu-gzGzoHsz", "IjkswTLu60dF1rnnAHNoLM98");
        AVAnalytics.enableCrashReport(this, true);
        initComponet();
        LeakCanary.install(this);
        Logger.init("Baby").logLevel(LogLevel.FULL).logTool(new AndroidLogTool());
        AVOSCloud.setDebugLogEnabled(false);
        FIR.init(this);
    }

    private void initEaseUI() {
        easeUI.init();

    }


    private void initComponet() {
        mAppComponet = DaggerApplicationComponet.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponet getAppComponet() {
        return mAppComponet;

    }


}
