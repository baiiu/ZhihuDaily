package com.baiiu.zhihudaily.newsList.view;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.baiiu.tsnackbar.LUtils;
import com.baiiu.tsnackbar.ScreenUtil;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.newsList.presenter.NewsListPresenter;
import com.baiiu.zhihudaily.view.base.BaseActivity;
import com.baiiu.zhihudaily.util.net.http.NetWorkReceiver;
import com.baiiu.zhihudaily.util.SwitchModeActivity;
import com.baiiu.zhihudaily.util.Constant;
import com.baiiu.zhihudaily.util.PreferenceUtil;

/**
 * Activity将变成全局的Controller
 * 1. 负责创建View和Presenter
 * 2. 并负责绑定View和Presenter
 */
public class NewsListActivity extends BaseActivity {

  @Override public int provideLayoutId() {
    if (PreferenceUtil.instance().get(Constant.UI_MODE, true)) {
      setTheme(R.style.DayTheme);
    } else {
      setTheme(R.style.NightTheme);
    }

    return R.layout.activity_main;
  }

  @Override protected void initOnCreate(Bundle savedInstanceState) {
    initBroadCast();

    //1. 创建Fragment
    NewsListFragment newsListFragment = NewsListFragment.instance();
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, newsListFragment, "MainFragment")
        .commit();

    /*
    2.创建Presenter,并在Presenter构造函数中绑定View.
    这样View中持有Presenter,Presenter中持有View,双向关联
    */

    //Presenter绑定View
    NewsListPresenter newsListPresenter = new NewsListPresenter(newsListFragment);
    //View绑定Presenter
    newsListFragment.setPresenter(newsListPresenter);

    //这个绑定方式也可以在View(Fragment)中绑定,放在Activity绑定是突出了Activity的Controller作用.之后使用依赖注入可能不用这么费劲.
  }

  //=====================Menu===================================
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
        return true;
      case R.id.action_theme:
        if (PreferenceUtil.instance().get(Constant.UI_MODE, true)) {
          setTheme(R.style.NightTheme);
          PreferenceUtil.instance().put(Constant.UI_MODE, false).commit();
        } else {
          setTheme(R.style.DayTheme);
          PreferenceUtil.instance().put(Constant.UI_MODE, true).commit();
        }

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
