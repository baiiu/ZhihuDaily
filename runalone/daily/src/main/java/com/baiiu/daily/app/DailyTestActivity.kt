package com.baiiu.daily.app

import android.os.Bundle
import com.baiiu.common.base.BaseActivity
import com.baiiu.daily.NewsListFragment

class DailyTestActivity : BaseActivity() {

    override fun provideLayoutId(): Int {
        return R.layout.activity_test_list
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, NewsListFragment.instance(), NewsListFragment::class.java.name)
                .commit()
    }

}
