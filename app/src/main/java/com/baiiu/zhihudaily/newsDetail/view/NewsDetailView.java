package com.baiiu.zhihudaily.newsDetail.view;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import butterknife.Bind;
import com.baiiu.tsnackbar.Prompt;
import com.baiiu.tsnackbar.TSnackbar;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.mvpbase.view.BaseFragmentViewDelegate;
import com.baiiu.zhihudaily.newsDetail.NewsDetailContract;
import com.baiiu.zhihudaily.newsDetail.model.DailyDetail;
import com.baiiu.zhihudaily.util.HTMLUtil;
import com.baiiu.zhihudaily.view.EmptyLayout;

/**
 * author: baiiu
 * date: on 16/5/13 16:42
 * description:
 */
public class NewsDetailView extends BaseFragmentViewDelegate<NewsDetailContract.Presenter> {

  private WebView webView;
  @Bind(R.id.webViewContainer) FrameLayout webViewContainer;
  @Bind(R.id.emptyLayout) EmptyLayout emptyLayout;

  @Override public int provideLayoutId() {
    return R.layout.fragment_news_detail;
  }

  @Override public void initView() {
    webView = new WebView(mContext.getApplicationContext());
    webViewContainer.addView(webView, -1, -1);

    WebSettings mWebSettings = webView.getSettings();
    mWebSettings.setSupportZoom(true);
    mWebSettings.setUseWideViewPort(true);
    mWebSettings.setDefaultTextEncodingName("UTF-8");
    mWebSettings.setLoadsImagesAutomatically(true);
  }

  public void showSuccessInfo(String info) {
    TSnackbar.make(webViewContainer, info, Prompt.SUCCESS).show();
  }

  public void showErrorInfo(String info) {
    TSnackbar.make(webViewContainer, info, Prompt.ERROR).show();
  }

  public void showErrorPage() {
    emptyLayout.setVisibility(View.VISIBLE);
    webViewContainer.setVisibility(View.GONE);
    emptyLayout.setState(EmptyLayout.TYPE_ERROR);
  }

  public void showLoadingPage() {
    emptyLayout.setVisibility(View.VISIBLE);
    webViewContainer.setVisibility(View.GONE);
    emptyLayout.setState(EmptyLayout.TYPE_LOADING);
  }

  public void showNewsDetail(DailyDetail dailyDetail) {
    emptyLayout.setVisibility(View.GONE);
    webViewContainer.setVisibility(View.VISIBLE);

    ((NewsDetailActivity) mContext).setTopContent(dailyDetail.title, dailyDetail.image_source,
        dailyDetail.image);

    webView.loadDataWithBaseURL("", HTMLUtil.handleHtml(dailyDetail.body, true).toString(),
        "text/html", "utf-8", null);
  }

  public void destoryView() {
    if (webView != null) {
      webView.removeAllViews();
      webViewContainer.removeView(webView);
      webView.destroy();
      webView = null;
    }
  }
}
