package com.baiiu.module.daily.newsDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.baiiu.common.base.BaseActivity;
import com.baiiu.common.util.CommonUtil;
import com.baiiu.common.util.Constant;
import com.baiiu.common.util.PreferenceUtil;
import com.baiiu.common.util.UIUtil;
import com.baiiu.module.daily.R;
import com.baiiu.module.daily.data.bean.Story;
import com.baiiu.tsnackbar.LUtils;
import com.bumptech.glide.Glide;

public class NewsDetailActivity extends BaseActivity {
    public static final String CONTENT_STORY = "content_story";

    @BindView(R.id.imageView) ImageView imageView;
    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.tv_source) TextView tv_source;

    public static Intent instance(Context context, long id) {
        return new Intent(context, NewsDetailActivity.class).putExtra(NewsDetailFragment.NEWS_ID, id);
    }

    public static Intent instance(Context context, Story story) {
        return new Intent(context, NewsDetailActivity.class).putExtra(CONTENT_STORY, story);
    }

    @Override public int provideLayoutId() {
        setCanSwipeBack(true);

        // @formatter:off
        if (PreferenceUtil.instance().get(Constant.UI_MODE, true)) {
            setTheme(R.style.DayTransparentTheme);
        } else {
            setTheme(R.style.NightTransparentTheme);
        }
        // @formatter:on


        return R.layout.activity_news_detail;
    }

    @Override protected void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @Override protected void initOnCreate(Bundle savedInstanceState) {
        // @formatter:off
        if (LUtils.hasKitKat()) {
            ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0).setFitsSystemWindows(true);
            if (PreferenceUtil.instance().get(Constant.UI_MODE, true)) {
                LUtils.instance(this).setStatusBarColor(UIUtil.getColor(R.color.colorPrimaryDark_Day));
            } else {
                LUtils.instance(this).setStatusBarColor(UIUtil.getColor(R.color.colorPrimaryDark_Night));
            }
        }
        // @formatter:on


        long id = 0;
        if (getIntent().hasExtra(NewsDetailFragment.NEWS_ID)) {
            id = getIntent().getLongExtra(NewsDetailFragment.NEWS_ID, 0);
        }

        if (getIntent().hasExtra(CONTENT_STORY)) {
            Story story = getIntent().getParcelableExtra(CONTENT_STORY);
            id = story.id;

            setTopContent(story.title, null, CommonUtil.isEmpty(story.images) ? null : story.images.get(0));
        }

        NewsDetailFragment newsDetailFragment =
                (NewsDetailFragment) getSupportFragmentManager().findFragmentById(R.id.container);

        if (newsDetailFragment == null) {
            newsDetailFragment = NewsDetailFragment.instance(id);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, newsDetailFragment, "newsDetailFragment")
                    .commit();
        }
    }

    public void setTopContent(String title, String image_source, String image) {
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }

        if (!TextUtils.isEmpty(image_source)) {
            tv_source.setText(image_source);
        }

        if (!TextUtils.isEmpty(image)) {
            Glide.with(this)
                    .load(image)
                    .centerCrop()
                    .into(imageView);
        }
    }
}
