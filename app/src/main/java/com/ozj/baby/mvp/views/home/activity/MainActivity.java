package com.ozj.baby.mvp.views.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jaeger.library.StatusBarUtil;
import com.ozj.baby.R;
import com.ozj.baby.base.BaseActivity;
import com.ozj.baby.mvp.presenter.home.impl.MainPresenterImpl;
import com.ozj.baby.mvp.views.home.IMainView;
import com.ozj.baby.mvp.views.home.fragment.SouvenirFragment;
import com.ozj.baby.widget.ChoosePicDialog;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

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
    @Bind(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @Bind(R.id.nested_scroll)
    NestedScrollView nestedScroll;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Inject
    ChoosePicDialog mPicDialog;

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
        mMainPersenter.replaceFragment(new SouvenirFragment());
        StatusBarUtil.setTransparent(this);
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

        if (id == R.id.nav_camera) {
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
        startActivity(intent);

    }

    @Override
    public void toAddSouvenirActivity() {
        Intent intent = new Intent(this, AddSouvenirActivity.class);
        startActivity(intent);

    }
}
