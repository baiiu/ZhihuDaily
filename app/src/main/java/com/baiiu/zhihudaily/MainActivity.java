package com.baiiu.zhihudaily;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import butterknife.BindView;
import com.baiiu.common.base.BaseActivity;

/**
 * author: baiiu
 * date: on 17/10/26 18:34
 * description:
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.viewPager) ViewPager mViewPager;
    @BindView(R.id.bottomNavigation) BottomNavigationView mBottomNavigation;

    @Override public int provideLayoutId() {
        return R.layout.activity_main;
    }

    @Override protected void initOnCreate(Bundle savedInstanceState) {
        mViewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));

        mBottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.zhiHu:
                    mViewPager.setCurrentItem(0, false);
                    break;
                case R.id.Gank:
                    mViewPager.setCurrentItem(1, false);
                    break;
            }

            return true;
        });
    }

}
