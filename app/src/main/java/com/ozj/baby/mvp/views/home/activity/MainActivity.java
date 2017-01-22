package com.ozj.baby.mvp.views.home.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.domain.UserDao;
import com.hyphenate.easeui.ui.ChatActivity;
import com.orhanobut.logger.Logger;
import com.ozj.baby.R;
import com.ozj.baby.base.BaseActivity;
import com.ozj.baby.event.UploadPhotoUri;
import com.ozj.baby.mvp.model.rx.RxBus;
import com.ozj.baby.mvp.presenter.home.impl.MainPresenterImpl;
import com.ozj.baby.mvp.views.home.IMainView;
import com.ozj.baby.mvp.views.home.fragment.SouvenirFragment;
import com.ozj.baby.mvp.views.navigation.fragment.GalleryFragment;
import com.ozj.baby.widget.ChoosePicDialog;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import pl.aprilapps.easyphotopicker.EasyImage;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainView {
    @Inject
    MainPresenterImpl mMainPresenter;
    @BindView(R.id.iv_album)
    ImageView ivAlbum;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collaspingToolBarlayout)
    CollapsingToolbarLayout collaspingToolBarlayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Inject
    ChoosePicDialog mPicDialog;
    @Inject
    RxBus mRxbus;


    ImageView iv_avatar;
    TextView tv_nick;
    SouvenirFragment souvenirFragment;
    GalleryFragment galleryFragment;
    static final int CHANGE_PROFILE = 8;
    static final int GALLERY_PHOTO = 9;
    static final int ALBUM_PHOTO = 10;

    boolean isAlbum;

    private boolean isConflictDialogShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAlbum = true;

    }

    @Override
    protected void initData() {
        mMainPresenter.initData(iv_avatar, tv_nick, ivAlbum);
    }

    @Override
    public void initDagger() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_main);
        if (getIntent().getBooleanExtra(EaseConstant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            ConflictAndRestart();
        }
    }

    @Override
    public void initViewsAndListener() {
        setSupportActionBar(toolbar);
        navView.setNavigationItemSelectedListener(this);
        //noinspection ConstantConditions
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        View headerview = navView.getHeaderView(0);
        iv_avatar = (ImageView) headerview.findViewById(R.id.iv_avatar);
        tv_nick = (TextView) headerview.findViewById(R.id.tv_nick);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toProfileActivity();
            }
        });
        navView.getMenu().getItem(0).setChecked(true);
        souvenirFragment = SouvenirFragment.newInstance();
        mMainPresenter.replaceFragment(souvenirFragment, "Moment", true);
    }

    @Override
    public void initPresenter() {
        mMainPresenter.attachView(this);
    }

    @Override
    public void initToolbar() {

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);

        }

        return true;
    }

    @SuppressWarnings({"StatementWithEmptyBody", "ConstantConditions"})
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_moment) {
            if (souvenirFragment == null) {
                souvenirFragment = SouvenirFragment.newInstance();
            }
            isAlbum = true;
            collaspingToolBarlayout.setTitle("Moment");
            mMainPresenter.replaceFragment(souvenirFragment, "Moment", true);
        } else if (id == R.id.nav_gallery) {
            if (galleryFragment == null) {
                galleryFragment = GalleryFragment.newInstance();
            }
            isAlbum = false;
            collaspingToolBarlayout.setTitle("相册");
            mMainPresenter.replaceFragment(galleryFragment, "Gallery", false);
        } else if (id == R.id.nav_manage) {
            toFeedBackActivity();
        } else if (id == R.id.nav_logout) {
            showWarningDialog("退出", "确定要退出吗？", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                    mMainPresenter.Logout();

                }
            });
        } else if (id == R.id.nav_share) {
            mMainPresenter.Share();
        } else if (id == R.id.nav_send) {
            if (mMainPresenter.isHadLover()) {
                toChatActivity();
            } else {
                toProfileActivity();
                showToast("老实说，这是个两个人使用的APP");
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @OnClick({R.id.iv_album, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_album:
                showPicDialog();
                break;
            case R.id.fab:
                mMainPresenter.fabOnclick();
                break;
            default:
                break;
        }
    }


    @Override
    public void showPicDialog() {
        mPicDialog.show();

    }

    @Override
    public void toProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivityForResult(intent, CHANGE_PROFILE);

    }

    @Override
    public void toChatActivity() {
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        intent.putExtra(EaseConstant.EXTRA_USER_ID, User.getCurrentUser(User.class).getLoverusername());
        startActivity(intent);
    }

    @Override
    public void toFeedBackActivity() {
        FeedbackAgent agent = new FeedbackAgent(MainActivity.this);
        agent.startDefaultThreadActivity();
    }

    @Override
    public void toAddSouvenirActivity() {
        Intent intent = new Intent(this, AddSouvenirActivity.class);
        startActivity(intent);
    }

    @Override
    public void showScrollView() {
        appBar.setExpanded(true, true);
    }

    @Override
    public void hideScrollView() {
        appBar.setExpanded(false, true);
    }

    @Override
    public void ConflictAndRestart() {
        /**
         * 显示帐号在别处登录dialog
         */
        isConflictDialogShow = true;
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            showErrorDialog(st, getString(R.string.connect_conflict), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    mMainPresenter.Logout();
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHANGE_PROFILE && resultCode == RESULT_OK) {
            tv_nick.setText(AVUser.getCurrentUser().getString(UserDao.NICK));
            Glide.with(this).load(AVUser.getCurrentUser().getString(UserDao.AVATARURL)).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(this)).into(iv_avatar);
        }
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource imageSource, int i) {
                showToast("出错啦，请稍后再试");
            }

            @Override
            public void onImagePicked(File file, EasyImage.ImageSource imageSource, int i) {
                UCrop.Options options = new UCrop.Options();
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                if (isAlbum) {
                    UCrop.of(Uri.fromFile(file), Uri.fromFile(file))
                            .withOptions(options)
                            .start(MainActivity.this, ALBUM_PHOTO);
                } else {
                    UCrop.of(Uri.fromFile(file), Uri.fromFile(file))
                            .withOptions(options)
                            .start(MainActivity.this, GALLERY_PHOTO);
                }

            }

            @Override
            public void onCanceled(EasyImage.ImageSource imageSource, int i) {
                if (imageSource == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MainActivity.this);
                    if (photoFile != null) {
                        photoFile.delete();
                    }

                }
            }
        });

        if (resultCode == RESULT_OK && requestCode == GALLERY_PHOTO) {
            mRxbus.post(new UploadPhotoUri(0, UCrop.getOutput(data)));
        } else if (resultCode == RESULT_OK && requestCode == ALBUM_PHOTO) {
            Glide.with(MainActivity.this).load(UCrop.getOutput(data)).crossFade().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).into(ivAlbum);
            mMainPresenter.UploadPic(UCrop.getOutput(data));
        } else if (resultCode == UCrop.RESULT_ERROR) {
            //noinspection ConstantConditions
            Logger.e(UCrop.getError(data).getMessage());
            showToast("出错啦，重新试试吧");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.detachView();
    }
}

