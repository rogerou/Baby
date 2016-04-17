package com.ozj.baby;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.orhanobut.logger.AndroidLogTool;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.ozj.baby.di.component.ApplicationComponet;
import com.ozj.baby.di.component.DaggerApplicationComponet;
import com.ozj.baby.di.module.ApplicationModule;

/**
 * Created by Roger ou on 2016/3/25.
 * 初始化leancloud
 */
public class BabyApplication extends Application {
    private ApplicationComponet mAppComponet;


    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, "GpLTBKYub2ekB1GG2UUDdpmu-gzGzoHsz", "IjkswTLu60dF1rnnAHNoLM98");
        AVAnalytics.enableCrashReport(this, true);
        initComponet();
        Logger.init("Baby").logLevel(LogLevel.FULL).logTool(new AndroidLogTool());
    }

    private void initComponet() {
        mAppComponet = DaggerApplicationComponet.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponet getAppConponet() {
        return mAppComponet;

    }
}
