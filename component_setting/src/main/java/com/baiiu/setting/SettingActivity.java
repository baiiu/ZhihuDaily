package com.baiiu.setting;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.viewpager.widget.ViewPager;
import com.baiiu.common.base.BaseActivity;

/**
 * author: baiiu
 * date: on 17/10/26 18:34
 * description:
 */
public class SettingActivity extends BaseActivity {
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigation;

    @Override public int provideLayoutId() {
        return R.layout.setting_activity_main;
    }

    @Override protected void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.viewPager);
        mBottomNavigation = findViewById(R.id.bottomNavigation);
    }

    @Override protected void initOnCreate(Bundle savedInstanceState) {
        mViewPager.setAdapter(new SettingAdapter(getSupportFragmentManager()));

        mBottomNavigation.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.zhiHu) {
                mViewPager.setCurrentItem(0, false);
            } else if (item.getItemId() == R.id.Gank) {
                mViewPager.setCurrentItem(1, false);
            }

            //switch (item.getItemId()) {
            //    case R.id.zhiHu:
            //        mViewPager.setCurrentItem(0, false);
            //        break;
            //    case R.id.Gank:
            //        mViewPager.setCurrentItem(1, false);
            //        break;
            //}

            return true;
        });
    }

}
