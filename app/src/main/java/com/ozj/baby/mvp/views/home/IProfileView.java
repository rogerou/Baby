package com.ozj.baby.mvp.views.home;

import com.ozj.baby.base.BaseView;

/**
 * Created by Administrator on 2016/4/21.
 */
public interface IProfileView extends BaseView {

    void ShowPicChoiceDialog();

    void HidePicChoiceDialog();


    void setResultCode();
}
