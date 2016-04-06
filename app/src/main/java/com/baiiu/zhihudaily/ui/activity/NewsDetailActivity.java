package com.baiiu.zhihudaily.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.ImageView;

import android.widget.TextView;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.base.BaseActivity;
import com.baiiu.zhihudaily.net.DailyClient;
import com.baiiu.zhihudaily.net.http.RequestCallBack;
import com.baiiu.zhihudaily.pojo.DailyDetail;
import com.baiiu.zhihudaily.util.HTMLUtil;
import com.bumptech.glide.Glide;

import butterknife.Bind;

public class NewsDetailActivity extends BaseActivity {
  private static final String ID = "id";

  public static Intent instance(Context context, String id) {
    return new Intent(context, NewsDetailActivity.class).putExtra(ID, id);
  }

  @Bind(R.id.collapsingToolbar) CollapsingToolbarLayout collapsingToolbar;
  @Bind(R.id.imageView) ImageView imageView;
  @Bind(R.id.tv_title) TextView tv_title;
  @Bind(R.id.tv_source) TextView tv_source;
  @Bind(R.id.webView) WebView webView;

  @Override public int provideLayoutId() {
    setCanSwipeBack(true);
    return R.layout.activity_news_detail;
  }

  @Override protected void initOnCreate(Bundle savedInstanceState) {
    String id = getIntent().getStringExtra(ID);
    if (TextUtils.isEmpty(id)) {
      //// TODO: 16/4/6 空页面
    } else {
      loadData(id);
    }
  }

  private void loadData(String id) {
    DailyClient.getNewsDetail(volleyTag, id, new RequestCallBack<DailyDetail>() {
      @Override public void onSuccess(DailyDetail response) {
        tv_title.setText(response.title);
        tv_source.setText(response.image_source);
        Glide.with(NewsDetailActivity.this).load(response.image).centerCrop().into(imageView);

        webView.loadDataWithBaseURL("", HTMLUtil.handleHtml(response.body, true).toString(),
            "text/html", "utf-8", null);
      }

      @Override public void onFailure(int statusCode, String errorString) {

      }
    });
  }
}
