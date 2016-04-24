package com.ozj.baby.mvp.views.home.activity;

import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ozj.baby.R;
import com.ozj.baby.base.BaseActivity;
import com.ozj.baby.mvp.model.dao.UserDao;
import com.ozj.baby.mvp.presenter.home.impl.MainPresenterImpl;
import com.ozj.baby.mvp.views.home.IMainView;
import com.ozj.baby.mvp.views.home.fragment.SouvenirFragment;
import com.ozj.baby.widget.ChoosePicDialog;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainView {
    @Inject
    MainPresenterImpl mMainPersenter;
    @Bind(R.id.iv_album)
    ImageView ivAlbum;
    @Bind(R.id.fade_cover)
    View fadeCover;
    @Bind(R.id.rl_loverbackground)
    RelativeLayout rlLoverbackground;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collaspingToolBarlayout)
    CollapsingToolbarLayout collaspingToolBarlayout;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Inject
    ChoosePicDialog mPicDialog;

    ImageView iv_avatar;
    TextView tv_nick;
    SouvenirFragment souvenirFragment;
    static final int ChangeProfile = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void initDagger() {
        mActivityComponet.inject(this);
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_main);

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
        mMainPersenter.initData(iv_avatar, tv_nick);
        souvenirFragment = new SouvenirFragment();
        mMainPersenter.replaceFragment(souvenirFragment);
        navView.getMenu().getItem(0).setChecked(true);

    }

    @Override
    public void initPresenter() {
        mMainPersenter.attachView(this);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_moment) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
                mMainPersenter.fabOnclick();
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
        startActivityForResult(intent, ChangeProfile);

    }

    @Override
    public void toAddSouvenirActivity() {
        Intent intent = new Intent(this, AddSouvenirActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChangeProfile && resultCode == RESULT_OK) {
            tv_nick.setText(AVUser.getCurrentUser().getString(UserDao.NICK));
            Glide.with(this).load(AVUser.getCurrentUser().getString(UserDao.AVATARURL)).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(this)).into(iv_avatar);
        }

    }
}
