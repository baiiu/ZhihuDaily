package com.baiiu.zhihudaily;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.baiiu.zhihudaily.gankList.GankListFragment;

/**
 * author: baiiu
 * date: on 17/10/27 11:16
 * description:
 */
public class MainAdapter extends FragmentPagerAdapter {
    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int position) {
        //if (position == 0) {
        //return NewsListFragment.instance();
        //} else {
        return new GankListFragment();
        //}
    }

    @Override public int getCount() {
        return 2;
    }
}
