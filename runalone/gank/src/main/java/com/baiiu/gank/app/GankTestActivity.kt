package com.baiiu.gank.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.baiiu.gank.GankListFragment

class GankTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gank_test_main)


        supportFragmentManager.beginTransaction()
                .replace(R.id.container, GankListFragment(), GankListFragment::class.java.getName())
                .commitAllowingStateLoss()

    }
}
