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
import com.baiiu.zhihudaily.view.base.BaseActivity;
import com.baiiu.zhihudaily.data.net.http.NetWorkReceiver;
import com.baiiu.zhihudaily.util.SwitchModeActivity;
import com.baiiu.zhihudaily.util.Constant;
import com.baiiu.zhihudaily.util.PreferenceUtil;

public class MainActivity extends BaseActivity {

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

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, MainFragment.instance(), "MainFragment")
        .commit();
  }


  //=====================Menu===================================

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
