package com.ozj.baby.mvp.views.navigation;

import com.ozj.baby.base.BaseView;

/**
 * Created by YX201603-6 on 2016/4/25.
 */
public interface IGalleryView extends BaseView {
    void showRefreshing();

    void hideRefreshing();

    void showUpdating();

    void UpdateComplete();


    void showPicDialog();

    void showSnackBar(String msg);

    void toProfileActivity();

}
