/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ozj.baby.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.util.EMLog;
import com.ozj.baby.mvp.views.home.activity.VideoCallActivity;
import com.ozj.baby.mvp.views.home.activity.VoiceCallActivity;
import com.ozj.baby.util.PreferenceManager;

import javax.inject.Inject;

public class CallReceiver extends BroadcastReceiver {
    @Inject
    PreferenceManager mPreferenceManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!mPreferenceManager.isLogined()) {
            return;
        }

        //拨打方username
        String from = intent.getStringExtra("from");
        //call type
        String type = intent.getStringExtra("type");
        if ("video".equals(type)) { //视频通话
            context.startActivity(new Intent(context, VideoCallActivity.class).
                    putExtra("username", from).putExtra("isComingCall", true).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } else { //音频通话
            context.startActivity(new Intent(context, VoiceCallActivity.class).
                    putExtra("username", from).putExtra("isComingCall", true).
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        EMLog.d("CallReceiver", "app received a incoming call");
    }

}
