package com.baiiu.daily.runalone;

import android.os.Bundle;
import com.baiiu.common.base.BaseActivity;
import com.baiiu.daily.NewsListFragment;
import com.baiiu.daily.R;

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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, NewsListFragment.instance(), NewsListFragment.class.getName())
                .commit();
    }

}
