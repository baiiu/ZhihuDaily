package com.baiiu.zhihudaily.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.base.BaseActivity;
import com.baiiu.zhihudaily.ui.fragment.NewsDetailFragment;
import com.baiiu.zhihudaily.util.Constant;
import com.baiiu.zhihudaily.util.PreferenceUtil;

public class NewsDetailActivity extends BaseActivity {

  public static Intent instance(Context context, long id) {
    return new Intent(context, NewsDetailActivity.class).putExtra(NewsDetailFragment.NEWS_ID, id);
  }

  @Override public int provideLayoutId() {
    setCanSwipeBack(true);

    if (PreferenceUtil.instance().get(Constant.UI_MODE, true)) {
      setTheme(R.style.DayTransparentTheme);
    } else {
      setTheme(R.style.NightTransparentTheme);
    }

    return R.layout.content_main;
  }

  @Override protected void initOnCreate(Bundle savedInstanceState) {
    final long id = getIntent().getLongExtra(NewsDetailFragment.NEWS_ID, 0);

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, NewsDetailFragment.instance(id), "newsDetailFragment")
        .commit();
  }
}
