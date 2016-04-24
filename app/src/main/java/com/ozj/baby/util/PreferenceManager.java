package com.ozj.baby.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ozj.baby.di.scope.ContextLife;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Administrator on 2016/4/13.
 */
public class PreferenceManager {
    private static final String SETTING = "babysetting";
    private static final String ID = "ID";
    private static final String FIRST_TIME = "firsttime";
    private static final String ISLOGIN = "islogin";
    private static final boolean FIRST_TIME_DEFAULT = true;
    private static final String LOVERID = "loverid";

    private static SharedPreferences mSharedPreferences;

    @Inject
    @Singleton
    public PreferenceManager(@ContextLife("Application") Context context) {
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

    public void saveCurrentUserId(String id) {
        mSharedPreferences.edit().putString(ID, id).apply();
    }

    public String getCurrentUserId() {
        return mSharedPreferences.getString(ID, null);

    }

    public boolean isLogined() {
        return mSharedPreferences.getBoolean(ISLOGIN, false);
    }

    public void setIslogin(boolean islogin) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(ISLOGIN, islogin);
        editor.apply();

    }

    public boolean isFirstTime() {
        return mSharedPreferences.getBoolean(FIRST_TIME, FIRST_TIME_DEFAULT);
    }

    public void saveFirsttime(boolean isFirst) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(FIRST_TIME, isFirst);
        editor.apply();

    }


    public String GetLoverID() {
        return mSharedPreferences.getString(LOVERID, null);
    }

    public void SaveLoverId(String ID) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LOVERID, ID);
        editor.apply();
    }

}
