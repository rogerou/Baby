package com.ozj.baby.base;

import android.view.View;

/**
 * Created by Administrator on 2016/3/25.
 */
public interface BaseView {

    void showProgress(String message);

    void showProgress(String message, int progress);

    void hideProgress();

    void showToast(String msg);

    void close();



}
