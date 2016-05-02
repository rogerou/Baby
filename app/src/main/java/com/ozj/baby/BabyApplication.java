package com.ozj.baby;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.hyphenate.easeui.controller.EaseUI;
import com.orhanobut.logger.AndroidLogTool;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.ozj.baby.di.component.ApplicationComponet;
import com.ozj.baby.di.component.DaggerApplicationComponet;
import com.ozj.baby.di.module.ApplicationModule;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.bean.User;
import com.hyphenate.easeui.receiver.CallReceiver;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Roger ou on 2016/3/25.
 * 初始化leancloud
 */
public class BabyApplication extends Application {
    private ApplicationComponet mAppComponet;

    public boolean isVideoCalling = false;
    public boolean isVoiceCalling = false;
    private static volatile BabyApplication AppConext;
    CallReceiver callReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        AppConext = (BabyApplication) this.getApplicationContext();
        AVUser.alwaysUseSubUserClass(User.class);
        AVObject.registerSubclass(Gallery.class);
        AVObject.registerSubclass(Souvenir.class);
        AVOSCloud.initialize(this, "GpLTBKYub2ekB1GG2UUDdpmu-gzGzoHsz", "IjkswTLu60dF1rnnAHNoLM98");
        AVAnalytics.enableCrashReport(this, true);
        initComponet();
        LeakCanary.install(this);
        Logger.init("Baby").logLevel(LogLevel.FULL).logTool(new AndroidLogTool());
        AVOSCloud.setDebugLogEnabled(true);
        initEaseUI();
    }

    private void initEaseUI() {
        EaseUI.getInstance().init(this, null);

      
    }

    public static BabyApplication getInstanace() {

        return AppConext;
    }

    private void initComponet() {
        mAppComponet = DaggerApplicationComponet.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponet getAppComponet() {
        return mAppComponet;

    }
}
