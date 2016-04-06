package com.ozj.baby;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;

/**
 * Created by Roger ou on 2016/3/25.
 * 初始化leancloud
 * 
 */
public class BabyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this, "GpLTBKYub2ekB1GG2UUDdpmu-gzGzoHsz", "IjkswTLu60dF1rnnAHNoLM98");
        AVAnalytics.enableCrashReport(this, true);
    }
}
