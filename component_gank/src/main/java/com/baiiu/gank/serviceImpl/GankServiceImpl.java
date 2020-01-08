package com.baiiu.gank.serviceImpl;

import androidx.fragment.app.Fragment;
import com.baiiu.componentservice.service.GankService;
import com.baiiu.gank.GankListFragment;

/**
 * author: baiiu
 * date: on 17/10/31 14:04
 * description:
 */
public class GankServiceImpl implements GankService {
    @Override public Fragment getGankFragment() {
        return new GankListFragment();
    }
}
