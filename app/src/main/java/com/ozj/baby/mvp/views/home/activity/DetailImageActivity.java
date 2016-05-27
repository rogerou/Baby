package com.ozj.baby.mvp.views.home.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ozj.baby.R;
import com.ozj.baby.base.BaseActivity;
import com.ozj.baby.mvp.views.home.fragment.DetailFragment;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/4/26.
 */
public class DetailImageActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    List<String> mList;

    DetailPageAdapter mAdapter;

    @Override
    protected void initData() {
        
    }

    @Override
    public void initDagger() {

    }

    @Override
    public void initContentView() {
        supportStartPostponedEnterTransition();
        setContentView(R.layout.activity_zoomimage);
    }

    @Override
    public void initViewsAndListener() {
        mList = getIntent().getStringArrayListExtra("imgurl");
        int index = getIntent().getIntExtra("index", 0);
        mAdapter = new DetailPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(index);
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                sharedElements.clear();
                sharedElements.put(mList.get(viewPager.getCurrentItem()), mAdapter.getCurrent().getSharedElement());
            }
        });

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
        data.putExtra("index", viewPager.getCurrentItem());
        setResult(RESULT_OK);
        super.supportFinishAfterTransition();
    }

    public class DetailPageAdapter extends FragmentStatePagerAdapter {


        public DetailPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DetailFragment.newInstance(mList.get(position));
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        public DetailFragment getCurrent() {
            return (DetailFragment) mAdapter.instantiateItem(viewPager, viewPager.getCurrentItem());

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mList.clear();
    }
}
