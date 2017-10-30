package com.baiiu.zhihudaily;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.baiiu.componentservice.Router;
import com.baiiu.componentservice.service.DailyService;
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
        if (position == 0) {
            //return NewsListFragment.instance();
            Object service = Router.INSTANCE.getService(DailyService.class.getName());
            if (service != null) {
                return ((DailyService) service).getNewsListFragment();
            }

        } else {
            return new GankListFragment();
        }

        return null;
    }

    @Override public int getCount() {
        return 2;
    }
}
