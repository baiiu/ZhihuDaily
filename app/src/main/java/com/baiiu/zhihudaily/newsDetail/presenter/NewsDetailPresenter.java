package com.baiiu.zhihudaily.newsDetail.presenter;

import android.os.Bundle;
import com.baiiu.zhihudaily.newsDetail.NewsDetailContract;
import com.baiiu.zhihudaily.newsDetail.model.DailyDetail;
import com.baiiu.zhihudaily.newsDetail.model.INewsDetailRepository;
import com.baiiu.zhihudaily.newsDetail.model.NewsDetailRepository;
import com.baiiu.zhihudaily.newsDetail.view.NewsDetailFragment;

/**
 * author: baiiu
 * date: on 16/5/12 15:05
 * description:
 */
public class NewsDetailPresenter implements NewsDetailContract.Presenter {

  private NewsDetailContract.View mNewsDetailView;
  private final NewsDetailRepository mNewsDetailRepository;
  private long id;

  public NewsDetailPresenter(NewsDetailContract.View newsDetailView) {
    //构造函数注入
    this.mNewsDetailView = newsDetailView;

    //双向绑定
    newsDetailView.setPresenter(this);

    //硬编码注入
    mNewsDetailRepository = new NewsDetailRepository();
  }

  @Override public void processArguments(Bundle arguments) {
    if (arguments != null) {
      id = arguments.getLong(NewsDetailFragment.NEWS_ID);
    }
  }

  @Override public void start() {
    if (id == 0) {
      mNewsDetailView.showErrorPage();
    } else {
      mNewsDetailView.showLoadingPage();

      mNewsDetailRepository.loadNewsDetail(id, new INewsDetailRepository.LoadNewsDetailCallback() {
        @Override public void onSuccess(DailyDetail daily) {
          mNewsDetailView.showNewsDetail(daily);
        }

        @Override public void onFailure() {
          mNewsDetailView.showErrorPage();
          mNewsDetailView.showErrorInfo("some error");
        }
      });
    }
  }
}
