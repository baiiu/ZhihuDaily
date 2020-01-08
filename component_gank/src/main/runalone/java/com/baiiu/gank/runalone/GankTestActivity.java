package com.baiiu.gank.runalone;

import android.os.Bundle;
import com.baiiu.common.base.BaseActivity;
import com.baiiu.gank.R;

/**
 * author: baiiu
 * date: on 17/10/31 13:25
 * description:
 */
public class GankTestActivity extends BaseActivity {
    @Override public int provideLayoutId() {
        return R.layout.activity_test;
    }

    @Override protected void initOnCreate(Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, GankListFragment.instance(), GankListFragment.class.getName())
                .commitAllowingStateLoss();
    }
}
