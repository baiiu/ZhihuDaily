package com.baiiu.zhihudaily.newsDetail.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import butterknife.BindView;
import com.baiiu.tsnackbar.Prompt;
import com.baiiu.tsnackbar.TSnackbar;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.newsDetail.NewsDetailContract;
import com.baiiu.zhihudaily.newsDetail.model.DailyDetail;
import com.baiiu.zhihudaily.newsDetail.presenter.NewsDetailPresenter;
import com.baiiu.zhihudaily.util.HTMLUtil;
import com.baiiu.zhihudaily.view.EmptyLayout;
import com.baiiu.zhihudaily.view.base.BaseFragment;

/**
 * author: baiiu
 * date: on 16/5/10 17:34
 * description:
 */
public class NewsDetailFragment extends BaseFragment implements NewsDetailContract.IView {
    public static final String NEWS_ID = "id";

    private NewsDetailContract.IPresenter mNewsDetailPresenter;

    @BindView(R.id.webViewContainer) FrameLayout webViewContainer;
    @BindView(R.id.emptyLayout) EmptyLayout emptyLayout;

    private long id;

    public static NewsDetailFragment instance(long newsId) {
        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(NEWS_ID, newsId);
        newsDetailFragment.setArguments(bundle);
        return newsDetailFragment;
    }

    private WebView webView;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        mNewsDetailPresenter = new NewsDetailPresenter();
        mNewsDetailPresenter.attachView(this);

        mNewsDetailPresenter.processArguments(arguments);
    }

    @Override public int provideLayoutId() {
        return R.layout.fragment_news_detail;
    }

    @Override protected void initOnCreateView() {
        webView = new WebView(mContext.getApplicationContext());
        webViewContainer.addView(webView, -1, -1);

        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDefaultTextEncodingName("UTF-8");
        mWebSettings.setLoadsImagesAutomatically(true);
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNewsDetailPresenter.start();
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

    @Override public void showSuccessInfo(String info) {
        TSnackbar.make(webViewContainer, info, Prompt.SUCCESS)
                .show();
    }

    @Override public void showErrorInfo(String info) {
        TSnackbar.make(webViewContainer, info, Prompt.ERROR)
                .show();
    }

    @Override public void showErrorPage() {
        emptyLayout.setVisibility(View.VISIBLE);
        webViewContainer.setVisibility(View.GONE);
        emptyLayout.setState(EmptyLayout.TYPE_ERROR);
    }

    @Override public void showLoadingPage() {
        emptyLayout.setVisibility(View.VISIBLE);
        webViewContainer.setVisibility(View.GONE);
        emptyLayout.setState(EmptyLayout.TYPE_LOADING);
    }

    @Override public void showNewsDetail(DailyDetail dailyDetail) {
        emptyLayout.setVisibility(View.GONE);
        webViewContainer.setVisibility(View.VISIBLE);

        ((NewsDetailActivity) mContext).setTopContent(dailyDetail.title, dailyDetail.image_source,
                                                      dailyDetail.image);

        webView.loadDataWithBaseURL("", HTMLUtil.handleHtml(dailyDetail.body, true)
                .toString(), "text/html", "utf-8", null);
    }
}
