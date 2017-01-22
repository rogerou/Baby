package com.ozj.baby.mvp.views.home;

import com.ozj.baby.base.BaseView;

/**
 * Created by Administrator on 2016/4/20.
 */
public interface IMainView extends BaseView {

    void showPicDialog();

    void toProfileActivity();

    void toChatActivity();

    void toFeedBackActivity();

    void toAddSouvenirActivity();

    void showScrollView();

    void hideScrollView();


    void ConflictAndRestart();

}
