package com.ozj.baby.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ozj.baby.mvp.views.home.fragment.DetailFragment;

import java.util.List;

/**
 * Created by YX201603-6 on 2016/4/27.
 */
public class DetailPageAdapter extends FragmentStatePagerAdapter {

    List<String> mList;

    public DetailPageAdapter(FragmentManager fm, List<String> list) {
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return DetailFragment.newInstance(mList.get(position));
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
