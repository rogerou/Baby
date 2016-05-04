package com.hyphenate.easeui.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.model.EmojiconExampleGroupData;
import com.hyphenate.easeui.receiver.CallReceiver;
import com.hyphenate.easeui.ui.ChatActivity;
import com.hyphenate.easeui.ui.VideoCallActivity;
import com.hyphenate.easeui.ui.VoiceCallActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

public final class EaseUI {
    private static final String TAG = EaseUI.class.getSimpleName();

    /**
     * the global EaseUI instance
     */
    private static EaseUI instance = null;

    /**
     * 用户属性提供者
     */
    private EaseUserProfileProvider userProvider;

    private EaseSettingsProvider settingsProvider;

    /**
     * application context
     */
    private Context appContext = null;

    /**
     * init flag: test if the sdk has been inited before, we don't need to init again
     */
    private boolean sdkInited = false;

    /**
     * the notifier
     */
    private EaseNotifier notifier = null;


    /**
     * 用来记录注册了eventlistener的foreground Activity
     */
    private List<Activity> activityList = new ArrayList<Activity>();

    public void pushActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(0, activity);
        }
    }


    public void popActivity(Activity activity) {
        activityList.remove(activity);
    }


    private EaseUI() {
    }

    /**
     * 获取EaseUI单实例对象
     *
     * @return
     */
    public synchronized static EaseUI getInstance() {
        if (instance == null) {
            instance = new EaseUI();
        }
        return instance;
    }

    /**
     * this function will initialize the HuanXin SDK
     *
     * @param context
     * @param options 聊天相关的设置，传null则使用默认的
     * @return
     */
    public synchronized boolean init(Context context, EMOptions options) {
        if (sdkInited) {
            return true;
        }
        appContext = context;

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);

        Log.d(TAG, "process app name : " + processAppName);

        // 如果app启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(appContext.getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return false;
        }
        if (options == null) {
            EMClient.getInstance().init(context, initChatOptions());
        } else {
            EMClient.getInstance().init(context, options);
        }
        initNotifier();
        //不设置，则使用easeui默认的

        if (settingsProvider == null) {
            settingsProvider = new DefaultSettingsProvider();
        }
    

        sdkInited = true;
        return true;
    }

    protected EMOptions initChatOptions() {
        Log.d(TAG, "init HuanXin Options");

        // 获取到EMChatOptions对象
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 设置是否需要已读回执
        options.setRequireAck(true);
        // 设置是否需要已送达回执
        options.setRequireDeliveryAck(false);

        return options;
//        notifier.setNotificationInfoProvider(getNotificationListener());
    }

    void initNotifier() {
        notifier = createNotifier();
        notifier.init(appContext);
    }

    protected EaseNotifier createNotifier() {
        return new EaseNotifier();
    }

    public EaseNotifier getNotifier() {
        return notifier;
    }

    public boolean hasForegroundActivies() {
        return activityList.size() != 0;
    }

    /**
     * 设置用户属性提供者
     */
    public void setUserProfileProvider(EaseUserProfileProvider userProvider) {
        this.userProvider = userProvider;
    }

    /**
     * 获取用户属性提供者
     *
     * @return
     */
    public EaseUserProfileProvider getUserProfileProvider() {
        return userProvider;
    }

    public void setSettingsProvider(EaseSettingsProvider settingsProvider) {
        this.settingsProvider = settingsProvider;
    }

    public EaseSettingsProvider getSettingsProvider() {
        return settingsProvider;
    }


    /**
     * check the application process name if process name is not qualified, then we think it is a service process and we will not init SDK
     *
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = appContext.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * 用户属性提供者
     *
     * @author wei
     */
    public interface EaseUserProfileProvider {
        /**
         * 返回此username对应的user
         *
         * @param username 环信id
         * @return
         */
        EaseUser getUser(String username);
    }

    /**
     * 表情信息提供者
     */
    public interface EaseEmojiconInfoProvider {
        /**
         * 根据唯一识别号返回此表情内容
         *
         * @param emojiconIdentityCode
         * @return
         */
        EaseEmojicon getEmojiconInfo(String emojiconIdentityCode);

        /**
         * 获取文字表情的映射Map,map的key为表情的emoji文本内容，value为对应的图片资源id或者本地路径(不能为网络地址)
         *
         * @return
         */
        Map<String, Object> getTextEmojiconMapping();
    }

    private EaseEmojiconInfoProvider emojiconInfoProvider;

    /**
     * 获取表情提供者
     *
     * @return
     */
    public EaseEmojiconInfoProvider getEmojiconInfoProvider() {
        return emojiconInfoProvider;
    }

    /**
     * 设置表情信息提供者
     *
     * @param emojiconInfoProvider
     */
    public void setEmojiconInfoProvider(EaseEmojiconInfoProvider emojiconInfoProvider) {
        this.emojiconInfoProvider = emojiconInfoProvider;
    }

    /**
     * 新消息提示设置的提供者
     */
    public interface EaseSettingsProvider {
        boolean isMsgNotifyAllowed(EMMessage message);

        boolean isMsgSoundAllowed(EMMessage message);

        boolean isMsgVibrateAllowed(EMMessage message);

        boolean isSpeakerOpened();
    }

    /**
     * default settings provider
     */
    protected class DefaultSettingsProvider implements EaseSettingsProvider {

        @Override
        public boolean isMsgNotifyAllowed(EMMessage message) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public boolean isMsgSoundAllowed(EMMessage message) {
            return true;
        }

        @Override
        public boolean isMsgVibrateAllowed(EMMessage message) {
            return true;
        }

        @Override
        public boolean isSpeakerOpened() {
            return true;
        }


    }

    /**
     * 全局事件监听
     * 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理
     * activityList.size() <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
     */
    protected void registerEventListener() {
        EMMessageListener messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    //应用在后台，不需要刷新UI,通知栏提示新消息
                    if (!EaseUI.getInstance().hasForegroundActivies()) {
                        getNotifier().onNewMsg(message);
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "收到透传消息");
                    //获取消息body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action

                    //获取扩展属性 此处省略
                    //message.getStringAttribute("");
                    EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action, message.toString()));
                    final String str = appContext.getString(R.string.receive_the_passthrough);

                    final String CMD_TOAST_BROADCAST = "hyphenate.demo.cmd.toast";
                    IntentFilter cmdFilter = new IntentFilter(CMD_TOAST_BROADCAST);

                    if (broadCastReceiver == null) {
                        broadCastReceiver = new BroadcastReceiver() {

                            @Override
                            public void onReceive(Context context, Intent intent) {
                                Toast.makeText(appContext, intent.getStringExtra("cmd_value"), Toast.LENGTH_SHORT).show();
                            }
                        };

                        //注册广播接收者
                        appContext.registerReceiver(broadCastReceiver, cmdFilter);
                    }

                    Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
                    broadcastIntent.putExtra("cmd_value", str + action);
                    appContext.sendBroadcast(broadcastIntent, null);
                }
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }


}
