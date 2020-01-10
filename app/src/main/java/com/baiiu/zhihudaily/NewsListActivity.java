package com.baiiu.zhihudaily;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.baiiu.common.base.BaseActivity;
import com.baiiu.common.net.http.NetWorkReceiver;
import com.baiiu.common.util.Constant;
import com.baiiu.common.util.PreferenceUtil;
import com.baiiu.common.util.SwitchModeActivity;
import com.baiiu.common.util.UIUtil;
import com.baiiu.componentservice.Router;
import com.baiiu.componentservice.UIRouter;
import com.baiiu.componentservice.service.DailyService;
import com.baiiu.componentservice.service.SettingService;
import com.baiiu.tsnackbar.LUtils;
import com.baiiu.tsnackbar.ScreenUtil;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

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
        Toast.makeText(this, "patach here", Toast.LENGTH_SHORT)
                .show();

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
        Fragment newsListFragment = null;

        Object service = Router.INSTANCE.getService(DailyService.class.getName());
        if (service != null) {
            newsListFragment = ((DailyService) service).getNewsListFragment();
        }

        if (newsListFragment != null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, newsListFragment, "MainFragment")
                    .commit();

            newsListFragment.setRetainInstance(true);
        }

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
                //Object service = Router.INSTANCE.getService(SettingService.class.getName());
                //if (service != null) {
                //    ((SettingService) service).toSettingPage(this);
                //}
                UIRouter.router(this, SettingService.URL_TO_SETTING);

                return true;
            case R.id.action_patch:
                TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/patch_signed_7zip.apk");

                break;
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
