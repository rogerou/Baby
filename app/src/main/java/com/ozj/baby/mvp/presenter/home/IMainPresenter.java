package com.ozj.baby.mvp.presenter.home;

import android.app.Fragment;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import com.ozj.baby.base.BasePresenter;

/**
 * Created by Administrator on 2016/4/20.
 */
public interface IMainPresenter extends BasePresenter {


    void replaceFragment(Fragment to, String tag, boolean isExpanded);

    void fabOnclick();

    boolean isHadLover();

    void initData(ImageView avatar, TextView nick, ImageView ivAlbum);

    void UploadPic(Uri uri);

    void Share();

    void Logout();


}
