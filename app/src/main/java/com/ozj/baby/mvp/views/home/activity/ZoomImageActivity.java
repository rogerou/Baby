package com.ozj.baby.mvp.views.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.jaeger.library.StatusBarUtil;
import com.ozj.baby.R;
import com.ozj.baby.adapter.DetailPageAdapter;
import com.ozj.baby.base.BaseActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ZoomImageActivity extends BaseActivity {

    @Bind(R.id.view_pager)
    ViewPager viewPager;

    List<String> mList;

    DetailPageAdapter mAdapter;

    @Override
    public void initDagger() {

    }

    @Override
    public void initContentView() {
        supportStartPostponedEnterTransition();
        setContentView(R.layout.activity_zoomimage);
        StatusBarUtil.setTranslucent(this);
    }

    @Override
    public void initViewsAndListener() {
        mList = getIntent().getStringArrayListExtra("imgurl");
        int index = getIntent().getIntExtra("index", 0);
        mAdapter = new DetailPageAdapter(getSupportFragmentManager(), mList);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(index);
        

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void supportFinishAfterTransition() {

        Intent data = new Intent();
        super.supportFinishAfterTransition();
    }
}
