package com.baiiu.zhihudaily.newsDetail.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.view.base.BaseActivity;
import com.baiiu.zhihudaily.newsList.model.Story;
import com.baiiu.zhihudaily.util.CommonUtil;
import com.baiiu.zhihudaily.util.Constant;
import com.baiiu.zhihudaily.util.PreferenceUtil;
import com.bumptech.glide.Glide;

public class NewsDetailActivity extends BaseActivity {

  public static final String CONTENT_STORY = "content_story";

  @Bind(R.id.imageView) ImageView imageView;
  @Bind(R.id.tv_title) TextView tv_title;
  @Bind(R.id.tv_source) TextView tv_source;

  public static Intent instance(Context context, long id) {
    return new Intent(context, NewsDetailActivity.class).putExtra(NewsDetailFragment.NEWS_ID, id);
  }

  public static Intent instance(Context context, Story story) {
    return new Intent(context, NewsDetailActivity.class).putExtra(CONTENT_STORY, story);
  }

  @Override public int provideLayoutId() {
    setCanSwipeBack(true);

    if (PreferenceUtil.instance().get(Constant.UI_MODE, true)) {
      setTheme(R.style.DayTransparentTheme);
    } else {
      setTheme(R.style.NightTransparentTheme);
    }

    return R.layout.activity_news_detail;
  }

  @Override protected void initOnCreate(Bundle savedInstanceState) {
    long id = 0;
    if (getIntent().hasExtra(NewsDetailFragment.NEWS_ID)) {
      id = getIntent().getLongExtra(NewsDetailFragment.NEWS_ID, 0);
    }

    if (getIntent().hasExtra(CONTENT_STORY)) {
      Story story = getIntent().getParcelableExtra(CONTENT_STORY);
      id = story.id;

      setTopContent(story.title, null,
          CommonUtil.isEmpty(story.images) ? null : story.images.get(0));
    }

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, NewsDetailFragment.instance(id), "newsDetailFragment")
        .commit();
  }

  public void setTopContent(String title, String image_source, String image) {
    if (!TextUtils.isEmpty(title)) {
      tv_title.setText(title);
    }

    if (!TextUtils.isEmpty(image_source)) {
      tv_source.setText(image_source);
    }

    if (!TextUtils.isEmpty(image)) {
      Glide.with(this).load(image).centerCrop().into(imageView);
    }
  }
}
