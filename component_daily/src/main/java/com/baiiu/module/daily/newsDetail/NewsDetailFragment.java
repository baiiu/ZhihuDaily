package com.baiiu.module.daily.newsDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import butterknife.BindView;
import com.baiiu.common.base.BaseFragment;
import com.baiiu.common.util.HTMLUtil;
import com.baiiu.common.view.LoadFrameLayout;
import com.baiiu.module.daily.R;
import com.baiiu.module.daily.R2;
import com.baiiu.module.daily.data.bean.DailyDetail;
import com.baiiu.tsnackbar.Prompt;
import com.baiiu.tsnackbar.TSnackbar;

/**
 * author: baiiu
 * date: on 16/5/10 17:34
 * description:
 */
public class NewsDetailFragment extends BaseFragment implements NewsDetailContract.IView {
    public static final String NEWS_ID = "id";

    NewsDetailPresenter mNewsDetailPresenter;
    @BindView(R2.id.loadFrameLayout) LoadFrameLayout loadFrameLayout;

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
        loadFrameLayout.setContentView(webView);
        loadFrameLayout.bind(LoadFrameLayout.LOADING);
        loadFrameLayout.setOnErrorClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mNewsDetailPresenter.start();
            }
        });

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
            loadFrameLayout.removeView(webView);
            webView.destroy();
            webView = null;
        }

        mNewsDetailPresenter.detachView();
    }

    @Override public void showSuccessInfo(String info) {
        TSnackbar.make(loadFrameLayout, info, Prompt.SUCCESS)
                .show();
    }

    @Override public void showErrorInfo(String info) {
        TSnackbar.make(loadFrameLayout, info, Prompt.ERROR)
                .show();
    }

    @Override public void showErrorPage() {
        loadFrameLayout.bind(LoadFrameLayout.ERROR);
    }

    @Override public void showLoadingPage() {
        loadFrameLayout.bind(LoadFrameLayout.LOADING);
    }

    @Override public void showNewsDetail(DailyDetail dailyDetail) {
        loadFrameLayout.bind(LoadFrameLayout.CONTENT);
        ((NewsDetailActivity) mContext).setTopContent(dailyDetail.title, dailyDetail.image_source, dailyDetail.image);

        webView.loadDataWithBaseURL("", HTMLUtil.handleHtml(dailyDetail.body, true)
                .toString(), "text/html", "utf-8", null);
    }
}
