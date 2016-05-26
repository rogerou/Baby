package com.ozj.baby.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2016/4/29.
 */
public class BabyReceiver extends BroadcastReceiver {

//    @Inject
//    RxBabyRealm mRealm;

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d("receive something");
        try {
            if (intent.getAction().equals("com.ozj.baby.Push")) {
//                News news = JSON.parseObject(intent.getExtras().getString("com.avos.avoscloud.Data"), News.class);
//                mRealm.saveNews(news);
//                mRealm.Close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }

    }

}
