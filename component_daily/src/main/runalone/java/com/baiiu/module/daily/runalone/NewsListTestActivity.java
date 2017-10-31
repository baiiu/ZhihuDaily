package com.baiiu.module.daily.runalone;

import android.os.Bundle;
import com.baiiu.common.base.BaseActivity;
import com.baiiu.module.daily.NewsListFragment;
import com.baiiu.module.daily.R;

/**
 * Activity将变成全局的Controller
 * 1. 负责创建View和Presenter
 * 2. 并负责绑定View和Presenter
 */
public class NewsListTestActivity extends BaseActivity {

    @Override public int provideLayoutId() {
        return R.layout.activity_test_list;
    }

    @Override protected void initOnCreate(Bundle savedInstanceState) {
        //1. 创建Fragment,这样写Activity在重新创建时不用重建Fragment
        NewsListFragment newsListFragment =
                (NewsListFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (newsListFragment == null) {
            newsListFragment = NewsListFragment.instance();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, newsListFragment, "NewsListFragment")
                    .commit();
        }

        //newsListFragment.setRetainInstance(true);


    }
}
