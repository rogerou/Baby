package com.ozj.baby.mvp.presenter.home.impl;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ozj.baby.R;
import com.ozj.baby.base.BaseView;
import com.ozj.baby.di.scope.ContextLife;
import com.ozj.baby.event.AddGalleryEvent;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.mvp.model.bean.User;
import com.ozj.baby.mvp.model.dao.UserDao;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.model.rx.RxLeanCloud;
import com.ozj.baby.mvp.presenter.home.IMainPresenter;
import com.ozj.baby.mvp.views.home.IMainView;
import com.ozj.baby.util.PreferenceManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/4/20.
 */
public class MainPresenterImpl implements IMainPresenter {
    IMainView mMainView;
    private Context mContext;
    private Activity mActivity;
    private PreferenceManager mPreferenceManager;
    private RxLeanCloud mRxLeanCloud;
    private RxBus mRxbus;
    String currentFragmentTag;

    @Inject
    public MainPresenterImpl(@ContextLife("Activity") Context context, Activity activity, PreferenceManager preferenceManager, RxLeanCloud rxLeanCloud, RxBus rxBus) {
        mRxLeanCloud = rxLeanCloud;
        mContext = context;
        mActivity = activity;
        mPreferenceManager = preferenceManager;
        mRxbus = rxBus;
    }


    @Override
    public void attachView(BaseView view) {
        mMainView = (IMainView) view;


    }

    @Override
    public void detachView() {

    }


    @Override
    public void replaceFragment(Fragment to, String tag, boolean isExpanded) {
        FragmentManager fragmentManager = mActivity.getFragmentManager();
        Fragment currentfragment = fragmentManager.findFragmentByTag(currentFragmentTag);

        if (currentfragment == null || !TextUtils.equals(tag, currentFragmentTag)) {
            currentFragmentTag = tag;
            fragmentManager.beginTransaction().replace(R.id.fragment_container, to, currentFragmentTag).commit();


        }
        if (isExpanded) {
            mMainView.showScrollView();
        } else {
            mMainView.hideScrollView();
        }


    }

    @Override
    public void fabOnclick() {

        if (isHavedLover()) {

            mMainView.toAddSouvenirActivity();
        } else {
            mMainView.toProfileActivity();
            mMainView.showToast("请先设置另一半才能发日记哦");
        }

    }

    @Override
    public boolean isHavedLover() {
        return !TextUtils.isEmpty(mPreferenceManager.GetLoverID());
    }

    @Override
    public void initData(ImageView avatar, TextView nick) {
        AVUser user = AVUser.getCurrentUser();
        String avatarurl = user.getString(UserDao.AVATARURL);
        if (avatarurl != null) {
            Glide.with(mContext).load(user.getString(UserDao.AVATARURL)).bitmapTransform(new CropCircleTransformation(mContext)).diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar);
        }
        nick.setText(user.getString(UserDao.NICK));

    }

    @Override
    public void UploadPicTure(Uri uri) {
        mMainView.showProgress("上传中...");
        try {
            AVFile file = AVFile.withFile(mPreferenceManager.getCurrentUserId(), new File(new URI(uri.toString())));
            mRxLeanCloud.UploadPicture(file)
                    .flatMap(new Func1<String, Observable<Gallery>>() {
                        @Override
                        public Observable<Gallery> call(String s) {
                            Gallery gallery = new Gallery();
                            gallery.setImgUrl(s);
                            gallery.setUser(User.getCurrentUser(User.class));
                            gallery.setAuthorId(mPreferenceManager.getCurrentUserId());
                            return mRxLeanCloud.saveGallery(gallery);
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Gallery>() {
                        @Override
                        public void onCompleted() {
                            mMainView.hideProgress();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mMainView.hideProgress();
                            com.orhanobut.logger.Logger.e(e.getMessage());
                        }

                        @Override
                        public void onNext(Gallery gallery) {
                            mMainView.showToast("保存成功");
                            mRxbus.post(new AddGalleryEvent(gallery, true));
                        }
                    });
        } catch (FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


}
