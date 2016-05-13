package com.baiiu.zhihudaily.newsDetail.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.baiiu.zhihudaily.mvpbase.presenter.BasePresenterFragment;
import com.baiiu.zhihudaily.newsDetail.NewsDetailContract;
import com.baiiu.zhihudaily.newsDetail.model.DailyDetail;
import com.baiiu.zhihudaily.newsDetail.model.INewsDetailRepository;
import com.baiiu.zhihudaily.newsDetail.model.NewsDetailRepository;
import com.baiiu.zhihudaily.newsDetail.view.NewsDetailView;

/**
 * author: baiiu
 * date: on 16/5/10 17:34
 * description:
 */
public class NewsDetailFragment extends BasePresenterFragment<NewsDetailView>
    implements NewsDetailContract.Presenter {
  public static final String NEWS_ID = "id";

  private NewsDetailRepository mNewsDetailRepository;
  private long id;

  public static NewsDetailFragment instance(long newsId) {
    NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
    Bundle bundle = new Bundle();
    bundle.putLong(NEWS_ID, newsId);
    newsDetailFragment.setArguments(bundle);
    return newsDetailFragment;
  }


  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle arguments = getArguments();
    if (arguments != null) {
      id = arguments.getLong(NewsDetailFragment.NEWS_ID);
    }

    //硬编码注入
    mNewsDetailRepository = new NewsDetailRepository();
  }

  @Override protected Class<NewsDetailView> getDelegateViewClass() {
    return NewsDetailView.class;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (id == 0) {
      viewDelegate.showErrorPage();
    } else {
      viewDelegate.showLoadingPage();

      mNewsDetailRepository.loadNewsDetail(id, new INewsDetailRepository.LoadNewsDetailCallback() {
        @Override public void onSuccess(DailyDetail daily) {
          viewDelegate.showNewsDetail(daily);
        }

        @Override public void onFailure() {
          viewDelegate.showErrorPage();
          viewDelegate.showErrorInfo("some error");
        }
      });
    }
  }

  @Override public void onDestroy() {
    super.onDestroy();

    viewDelegate.destoryView();

  }
}
