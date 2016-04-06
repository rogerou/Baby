package com.ozj.baby.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/4/13.
 */
public class PreferenceManager {
    private static final String SETTING = "babysetting";
    private static final String USERNAME = "username";
    private static final String FIRST_TIME = "firsttime";
    private static final boolean FIRST_TIME_DEFAULT = true;

    private static SharedPreferences mSharedPreferences;


    public PreferenceManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        sInstance = this;
    }

    private static volatile PreferenceManager sInstance = null;

    public static PreferenceManager getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (PreferenceManager.class) {
                if (sInstance == null) {
                    sInstance = new PreferenceManager(context);
                }
            }
        }
        return sInstance;
    }

    public boolean isFirstTime() {
        return mSharedPreferences.getBoolean(FIRST_TIME, FIRST_TIME_DEFAULT);
    }

    public void saveFirsttime(boolean isFirst) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(FIRST_TIME, FIRST_TIME_DEFAULT);
        editor.apply();

    }

}
