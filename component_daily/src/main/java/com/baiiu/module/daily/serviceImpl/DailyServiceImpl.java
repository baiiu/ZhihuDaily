package com.baiiu.module.daily.serviceImpl;

import android.support.v4.app.Fragment;
import com.baiiu.componentservice.service.DailyService;
import com.baiiu.module.daily.NewsListFragment;

/**
 * auther: baiiu
 * time: 17/10/30 30 23:03
 * description:
 */
public class DailyServiceImpl implements DailyService {
    @Override public Fragment getNewsListFragment() {
        return NewsListFragment.instance();
    }
}
