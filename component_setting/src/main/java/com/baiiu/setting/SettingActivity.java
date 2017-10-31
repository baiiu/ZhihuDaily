package com.baiiu.setting;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import com.baiiu.common.base.BaseActivity;

/**
 * author: baiiu
 * date: on 17/10/26 18:34
 * description:
 */
public class SettingActivity extends BaseActivity {
    @BindView(R2.id.viewPager) ViewPager mViewPager;
    @BindView(R2.id.bottomNavigation) BottomNavigationView mBottomNavigation;

    @Override public int provideLayoutId() {
        return R.layout.setting_activity_main;
    }

    @Override protected void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
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
