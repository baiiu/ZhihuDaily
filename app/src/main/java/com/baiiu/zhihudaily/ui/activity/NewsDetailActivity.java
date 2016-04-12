package com.baiiu.zhihudaily.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.async.TinyTaskManager;
import com.baiiu.zhihudaily.base.BaseActivity;
import com.baiiu.zhihudaily.db.DBManager;
import com.baiiu.zhihudaily.net.DailyClient;
import com.baiiu.zhihudaily.net.http.RequestCallBack;
import com.baiiu.zhihudaily.net.util.HttpNetUtil;
import com.baiiu.zhihudaily.pojo.DailyDetail;
import com.baiiu.zhihudaily.util.HTMLUtil;
import com.baiiu.zhihudaily.view.EmptyLayout;
import com.bumptech.glide.Glide;

public class NewsDetailActivity extends BaseActivity {
  private static final String ID = "id";

  public static Intent instance(Context context, long id) {
    return new Intent(context, NewsDetailActivity.class).putExtra(ID, id);
  }

  @Bind(R.id.imageView) ImageView imageView;
  @Bind(R.id.tv_title) TextView tv_title;
  @Bind(R.id.tv_source) TextView tv_source;
  @Bind(R.id.webViewContainer) FrameLayout webViewContainer;
  @Bind(R.id.emptyLayout) EmptyLayout emptyLayout;

  private WebView webView;

  @Override public int provideLayoutId() {
    setCanSwipeBack(true);
    return R.layout.activity_news_detail;
  }

  @Override protected void initOnCreate(Bundle savedInstanceState) {
    final long id = getIntent().getLongExtra(ID, 0);

    webView = new WebView(getApplicationContext());
    webViewContainer.addView(webView, -1, -1);

    WebSettings mWebSettings = webView.getSettings();
    mWebSettings.setSupportZoom(true);
    mWebSettings.setUseWideViewPort(true);
    mWebSettings.setDefaultTextEncodingName("UTF-8");
    mWebSettings.setLoadsImagesAutomatically(true);

    if (id == 0) {
      emptyLayout.setVisibility(View.VISIBLE);
      webViewContainer.setVisibility(View.GONE);
      emptyLayout.setState(EmptyLayout.TYPE_ERROR);
    } else {
      emptyLayout.setVisibility(View.GONE);
      webViewContainer.setVisibility(View.VISIBLE);

      if (HttpNetUtil.isConnected()) {
        loadData(id);
      } else {
        TinyTaskManager.instance().postAtFrontOfQueue(new Runnable() {
          @Override public void run() {
            final DailyDetail dailyDetail = DBManager.instance().getDetialStory(id);

            runOnUiThread(new Runnable() {
              @Override public void run() {
                setData(dailyDetail);
              }
            });
          }
        });
      }
    }
  }

  private void loadData(long id) {
    DailyClient.getNewsDetail(volleyTag, id, new RequestCallBack<DailyDetail>() {
      @Override public void onSuccess(final DailyDetail response) {

        setData(response);

        TinyTaskManager.instance().post(new Runnable() {
          @Override public void run() {
            DBManager.instance().saveDetailStory(response);
          }
        });
      }

      @Override public void onFailure(int statusCode, String errorString) {
        emptyLayout.setVisibility(View.VISIBLE);
        webViewContainer.setVisibility(View.GONE);
        emptyLayout.setState(EmptyLayout.TYPE_ERROR);
      }
    });
  }

  private void setData(DailyDetail dailyDetail) {

    if (dailyDetail == null) {
      emptyLayout.setVisibility(View.VISIBLE);
      webViewContainer.setVisibility(View.GONE);
      emptyLayout.setState(EmptyLayout.TYPE_EMPTY);
      return;
    }

    tv_title.setText(dailyDetail.title);
    tv_source.setText(dailyDetail.image_source);
    Glide.with(NewsDetailActivity.this).load(dailyDetail.image).centerCrop().into(imageView);

    webView.loadDataWithBaseURL("", HTMLUtil.handleHtml(dailyDetail.body, true).toString(),
        "text/html", "utf-8", null);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (webView != null) {
      webView.removeAllViews();
      webViewContainer.removeView(webView);
      webView.destroy();
      webView = null;
    }
  }
}
