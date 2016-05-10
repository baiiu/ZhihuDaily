package com.baiiu.zhihudaily.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.async.TinyTaskManager;
import com.baiiu.zhihudaily.base.BaseFragment;
import com.baiiu.zhihudaily.db.DBManager;
import com.baiiu.zhihudaily.net.DailyClient;
import com.baiiu.zhihudaily.net.http.RequestCallBack;
import com.baiiu.zhihudaily.net.util.HttpNetUtil;
import com.baiiu.zhihudaily.pojo.DailyDetail;
import com.baiiu.zhihudaily.ui.activity.NewsDetailActivity;
import com.baiiu.zhihudaily.util.HTMLUtil;
import com.baiiu.zhihudaily.view.EmptyLayout;
import com.bumptech.glide.Glide;

/**
 * author: baiiu
 * date: on 16/5/10 17:34
 * description:
 */
public class NewsDetailFragment extends BaseFragment {
  public static final String NEWS_ID = "id";

  @Bind(R.id.imageView) ImageView imageView;
  @Bind(R.id.tv_title) TextView tv_title;
  @Bind(R.id.tv_source) TextView tv_source;
  @Bind(R.id.webViewContainer) FrameLayout webViewContainer;
  @Bind(R.id.emptyLayout) EmptyLayout emptyLayout;
  private long id;

  public static NewsDetailFragment instance(long newsId) {
    NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
    Bundle bundle = new Bundle();
    bundle.putLong(NEWS_ID, newsId);
    newsDetailFragment.setArguments(bundle);
    return newsDetailFragment;
  }

  private WebView webView;

  @Override public int provideLayoutId() {
    return R.layout.fragment_news_detail;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle arguments = getArguments();
    if (arguments != null) {
      id = arguments.getLong(NEWS_ID);
    }
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    webView = new WebView(mContext.getApplicationContext());
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

            ((NewsDetailActivity) mContext).runOnUiThread(new Runnable() {
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
    Glide.with(mContext).load(dailyDetail.image).centerCrop().into(imageView);

    webView.loadDataWithBaseURL("", HTMLUtil.handleHtml(dailyDetail.body, true).toString(),
        "text/html", "utf-8", null);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (webView != null) {
      webView.removeAllViews();
      webViewContainer.removeView(webView);
      webView.destroy();
      webView = null;
    }
  }
}
