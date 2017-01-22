package com.ozj.baby;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.PushService;
import com.hyphenate.easeui.domain.User;
import com.orhanobut.logger.AndroidLogTool;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.ozj.baby.di.component.ApplicationComponent;
import com.ozj.baby.di.component.DaggerApplicationComponent;
import com.ozj.baby.di.module.ApplicationModule;
import com.ozj.baby.mvp.model.bean.Comment;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.views.home.activity.MainActivity;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.service.PatchResult;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tinkerpatch.sdk.TinkerPatch;
import com.tinkerpatch.sdk.tinker.callback.ResultCallBack;

import javax.inject.Inject;

import im.fir.sdk.FIR;

/**
 * Created by Roger ou on 2016/3/25.
 * 初始化leancloud Easeui 第三方服务 leancloud
 * 接入Tinker，改造自己的Application
 */
@DefaultLifeCycle(application = "com.ozj.baby.BabyApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL
)
public class BabyApplicationLike extends DefaultApplicationLike {
    private static ApplicationComponent mAppComponent;

    @Inject
    EaseUIHelper mEaseUIHelper;

    public BabyApplicationLike(Application application, int i, boolean b, long l, long l1, Intent intent) {
        super(application, i, b, l, l1, intent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.TINKER_ENABLE) {
            TinkerPatch.init(this)
                    .reflectPatchLibrary()
                    .fetchPatchUpdate(true)
                    .setFetchPatchIntervalByHours(3)
                    .setFetchDynamicConfigIntervalByHours(3)
                    .setPatchRollbackOnScreenOff(true)
                    .setPatchRestartOnSrceenOff(true)
                    .setPatchResultCallback(new ResultCallBack() {
                        @Override
                        public void onPatchResult(PatchResult patchResult) {
                            Logger.e("Tinker补丁成功", patchResult.toString());
                        }
                    });
        }
        initThirdService();
        mEaseUIHelper.init();
    }

    @Override
    public void onBaseContextAttached(Context context) {
        super.onBaseContextAttached(context);
        MultiDex.install(context);
    }

    private void initThirdService() {
        AVUser.alwaysUseSubUserClass(User.class);
        AVObject.registerSubclass(Gallery.class);
        AVObject.registerSubclass(Souvenir.class);
        AVObject.registerSubclass(Comment.class);
        AVOSCloud.initialize(getApplication(), "GpLTBKYub2ekB1GG2UUDdpmu-gzGzoHsz", "IjkswTLu60dF1rnnAHNoLM98");
        AVAnalytics.enableCrashReport(getApplication(), true);
        initComponent();
        mAppComponent.inject(this);
        LeakCanary.install(getApplication());
        Logger.init("Baby").logLevel(LogLevel.FULL).logTool(new AndroidLogTool());
        AVOSCloud.setDebugLogEnabled(false);
        FIR.init(getApplication());
        PushService.setDefaultPushCallback(getApplication(), MainActivity.class);
    }


    private void initComponent() {
        if (mAppComponent == null)
            mAppComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(getApplication())).build();
    }

    public static ApplicationComponent getAppComponent() {
        return mAppComponent;
    }

}
