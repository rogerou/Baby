package com.ozj.baby.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ozj.baby.di.scope.ContextLife;

/**
 * Created by Administrator on 2016/4/13.
 */
public class PreferenceManager {

    private static final String SETTING = "babysetting";
    private static final String ID = "ID";
    private static final String FIRST_TIME = "firsttime";
    private static final String IS_LOGIN = "islogin";
    private static final boolean FIRST_TIME_DEFAULT = true;
    private static final String LOVER_ID = "loverid";

    private static SharedPreferences mSharedPreferences;

    public PreferenceManager(@ContextLife() Context context) {
        mSharedPreferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
    }


    public void saveCurrentUserId(String id) {
        mSharedPreferences.edit().putString(ID, id).apply();
    }

    public String getCurrentUserId() {
        return mSharedPreferences.getString(ID, null);

    }

    public boolean isLogined() {
        return mSharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void setIslogin(boolean islogin) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(IS_LOGIN, islogin);
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
        return mSharedPreferences.getString(LOVER_ID, null);
    }

    public void SaveLoverId(String ID) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(LOVER_ID, ID);
        editor.apply();
    }

    public void Clear() {
        mSharedPreferences.edit().clear().apply();
    }

}
