package com.baiiu.daily.serviceImpl;

import androidx.fragment.app.Fragment;
import com.baiiu.componentservice.service.DailyService;
import com.baiiu.daily.NewsListFragment;

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
