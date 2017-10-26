package com.baiiu.zhihudaily.newsList;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.baiiu.tsnackbar.LUtils;
import com.baiiu.tsnackbar.ScreenUtil;
import com.baiiu.zhihudaily.MainActivity;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.base.BaseActivity;
import com.baiiu.zhihudaily.data.net.http.NetWorkReceiver;
import com.baiiu.zhihudaily.data.util.PreferenceUtil;
import com.baiiu.zhihudaily.util.Constant;
import com.baiiu.zhihudaily.util.SwitchModeActivity;
import com.baiiu.zhihudaily.util.UIUtil;

/**
 * Activity将变成全局的Controller
 * 1. 负责创建View和Presenter
 * 2. 并负责绑定View和Presenter
 */
public class NewsListActivity extends BaseActivity {

    @Override public int provideLayoutId() {
        // @formatter:off
        if (PreferenceUtil.instance().get(Constant.UI_MODE, true)) {
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }
        // @formatter:on


        return R.layout.activity_list;
    }

    @Override protected void initOnCreate(Bundle savedInstanceState) {

        // @formatter:off
        if (LUtils.hasKitKat()) {
            if (PreferenceUtil.instance().get(Constant.UI_MODE, true)) {
                LUtils.instance(this).setStatusBarColor(UIUtil.getColor(R.color.colorPrimaryDark_Day));
            } else {
                LUtils.instance(this).setStatusBarColor(UIUtil.getColor(R.color.colorPrimaryDark_Night));
            }
        }
        // @formatter:on


        initBroadCast();

        //1. 创建Fragment,这样写Activity在重新创建时不用重建Fragment
        NewsListFragment newsListFragment =
                (NewsListFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (newsListFragment == null) {
            newsListFragment = NewsListFragment.instance();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, newsListFragment, "MainFragment")
                    .commit();
        }

        //newsListFragment.setRetainInstance(true);


    }

    // =====================Menu===================================
  /*
        个人认为,Menu里面涉及的操作全部是View相关,所拥有的逻辑也仅仅是与UI相关,但不与业务逻辑UI相关,所以放在Activity中实现.
        这样的好处是:在业务变化时需要替换Fragment时,不需要修改这段代码.
   */

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.action_theme:
                // @formatter:off
                if (PreferenceUtil.instance().get(Constant.UI_MODE, true)) {
                    setTheme(R.style.NightTheme);
                    PreferenceUtil.instance().put(Constant.UI_MODE, false).commit();
                } else {
                    setTheme(R.style.DayTheme);
                    PreferenceUtil.instance().put(Constant.UI_MODE, true).commit();
                }
                // @formatter:on


                Constant.bitmap = ScreenUtil.snapShotWithoutStatusBar(this);
                startActivity(new Intent(this, SwitchModeActivity.class));
                overridePendingTransition(0, 0);

                recreate();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //=====================网络=====================================
    private NetWorkReceiver netWorkReceiver;

    private void initBroadCast() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        netWorkReceiver = new NetWorkReceiver();
        registerReceiver(netWorkReceiver, filter);
    }

    @Override protected void onDestroy() {
        LUtils.clear();
        unregisterReceiver(netWorkReceiver);
        super.onDestroy();
    }
}
