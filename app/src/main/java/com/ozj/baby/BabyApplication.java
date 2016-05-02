package com.ozj.baby;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.orhanobut.logger.AndroidLogTool;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.ozj.baby.di.component.ApplicationComponet;
import com.ozj.baby.di.component.DaggerApplicationComponet;
import com.ozj.baby.di.module.ApplicationModule;
import com.ozj.baby.event.HxDisconnectEvent;
import com.ozj.baby.mvp.model.bean.EmojiconExampleGroupData;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.bean.User;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.views.home.activity.VideoCallActivity;
import com.ozj.baby.mvp.views.home.activity.VoiceCallActivity;
import com.ozj.baby.mvp.views.navigation.activity.ChatActivity;
import com.ozj.baby.receiver.CallReceiver;
import com.squareup.leakcanary.LeakCanary;

import java.util.Map;

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

        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }

        //注册通话广播接收者
        AppConext.registerReceiver(callReceiver, callFilter);

        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int i) {
                if (i == EMError.USER_REMOVED) {
                    RxBus.getDefaultInstance().post(new HxDisconnectEvent(HxDisconnectEvent.USER_REMOVED));

                } else if (i == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    RxBus.getDefaultInstance().post(new HxDisconnectEvent(HxDisconnectEvent.USER_LOGIN_ANOTHER_DEVICE));
                } else {
                    RxBus.getDefaultInstance().post(new HxDisconnectEvent(HxDisconnectEvent.NONETWORK));
                }
            }
        });
        EaseUI.getInstance().setEmojiconInfoProvider(new EaseUI.EaseEmojiconInfoProvider() {
            @Override
            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
                EaseEmojiconGroupEntity data = EmojiconExampleGroupData.getData();
                for (EaseEmojicon emojicon : data.getEmojiconList()) {
                    if (emojicon.getIdentityCode().equals(emojiconIdentityCode)) {
                        return emojicon;
                    }
                }
                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                return null;
            }
        });
        EaseUI.getInstance().getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {
            @Override
            public String getDisplayedText(EMMessage message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                String ticker = EaseCommonUtils.getMessageDigest(message, BabyApplication.this);
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                User user = User.getCurrentUser(User.class);
                return user.getLoverNick() + ": " + ticker;

            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return null;
            }

            @Override
            public String getTitle(EMMessage message) {
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                return 0;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                //设置点击通知栏跳转事件
                Intent intent = new Intent(BabyApplication.this, ChatActivity.class);
                //有电话时优先跳转到通话页面
                if (isVideoCalling) {
                    intent = new Intent(BabyApplication.this, VideoCallActivity.class);
                } else if (isVoiceCalling) {
                    intent = new Intent(BabyApplication.this, VoiceCallActivity.class);
                } else {
                    EMMessage.ChatType chatType = message.getChatType();
                    if (chatType == EMMessage.ChatType.Chat) { // 单聊信息
                        intent.putExtra("userId", message.getFrom());
                        intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                    } else { // 群聊信息
                        // message.getTo()为群聊id
                        intent.putExtra("userId", message.getTo());
                        if (chatType == EMMessage.ChatType.GroupChat) {
                            intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                        } else {
                            intent.putExtra("chatType", Constant.CHATTYPE_CHATROOM);
                        }

                    }
                }
                return intent;
            }
        });
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
