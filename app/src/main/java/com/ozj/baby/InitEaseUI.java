package com.ozj.baby;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.model.EmojiconExampleGroupData;
import com.hyphenate.easeui.receiver.CallReceiver;
import com.hyphenate.easeui.ui.ChatActivity;
import com.hyphenate.easeui.ui.VideoCallActivity;
import com.hyphenate.easeui.ui.VoiceCallActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.mvp.views.home.activity.MainActivity;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by YX201603-6 on 2016/5/4.
 */
public class InitEaseUI {
    private EaseUI easeUI;
    public boolean isVideoCalling;
    public boolean isVoiceCalling;
    private Context mAppContext;

    @Singleton
    @Inject
    public InitEaseUI(@ContextLife("Application") Context context) {
        this.mAppContext = context;
    }

    public void init() {

        if (EaseUI.getInstance().init(mAppContext, null)) {
            easeUI = EaseUI.getInstance();

            setNotifyProvider();
            EMConnectionListener connectionListener = new EMConnectionListener() {
                @Override
                public void onConnected() {

                }

                @Override
                public void onDisconnected(int i) {
                    if (i == EMError.USER_REMOVED) {
                        onCurrentAccountRemoved();
                    } else if (i == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        onConnectionConflict();
                    }

                }
            };
            //注册连接监听
            EMClient.getInstance().addConnectionListener(connectionListener);
            CallReceiver callReceiver = null;
            IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
            if (callReceiver == null) {
                callReceiver = new CallReceiver();
            }

            //注册通话广播接收者
            mAppContext.registerReceiver(callReceiver, callFilter);

        }


    }

    private void setNotifyProvider() {
        easeUI.setEmojiconInfoProvider(new EaseUI.EaseEmojiconInfoProvider() {
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
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //修改标题,这里使用默认
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //设置小图标，这里为默认
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                String ticker = EaseCommonUtils.getMessageDigest(message, mAppContext);
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                String nick = null;
                try {
                    nick = message.getStringAttribute("nick");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                return nick + ": " + ticker;
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return null;
                // return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                //设置点击通知栏跳转事件
                Intent intent = new Intent(mAppContext, ChatActivity.class);
                //有电话时优先跳转到通话页面
                if (isVideoCalling) {
                    intent = new Intent(mAppContext, VideoCallActivity.class);
                } else if (isVoiceCalling) {
                    intent = new Intent(mAppContext, VoiceCallActivity.class);
                } else {
                    EMMessage.ChatType chatType = message.getChatType();
                    if (chatType == EMMessage.ChatType.Chat) { // 单聊信息
                        intent.putExtra("userId", message.getFrom());
                        intent.putExtra("chatType", EaseConstant.CHATTYPE_SINGLE);
                    } else { // 群聊信息
                        // message.getTo()为群聊id
                        intent.putExtra("userId", message.getTo());
                        if (chatType == EMMessage.ChatType.GroupChat) {
                            intent.putExtra("chatType", EaseConstant.CHATTYPE_GROUP);
                        } else {
                            intent.putExtra("chatType", EaseConstant.CHATTYPE_CHATROOM);
                        }

                    }
                }
                return intent;
            }
        });
    }

    /**
     * 账号在别的设备登录
     */
    protected void onConnectionConflict() {
        Intent intent = new Intent(mAppContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EaseConstant.ACCOUNT_CONFLICT, true);
        mAppContext.startActivity(intent);
    }

    /**
     * 账号被移除
     */
    protected void onCurrentAccountRemoved() {
        Intent intent = new Intent(mAppContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EaseConstant.ACCOUNT_REMOVED, true);
        mAppContext.startActivity(intent);
    }


}

