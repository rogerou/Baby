package com.ozj.baby.mvp.presenter.home;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.widget.ImageView;

import com.ozj.baby.base.BasePresenter;

import java.io.File;

import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Created by Administrator on 2016/4/21.
 */
public interface IProfilePresenter extends BasePresenter {
    void commit(String nick, String lover, String city, String sex);

    void initData(ImageView avatar, TextInputLayout nick, TextInputLayout loverid, TextInputLayout city, TextInputLayout sex);

    void HandleReturnPic(File imageFile);

    void onError();

    void onCanceled(EasyImage.ImageSource source);

    void UpLoadAvatar(ImageView avatar, Intent data);

}
