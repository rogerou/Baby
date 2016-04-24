package com.ozj.baby.mvp.views.home;

import android.content.Intent;

import com.ozj.baby.base.BaseView;

/**
 * Created by Administrator on 2016/4/23.
 */
public interface IAddSouvenirView extends BaseView {

    void showDialog();

    void hideDialog();

    void setResultCode(Intent intent);
}
