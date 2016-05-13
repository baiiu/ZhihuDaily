package com.baiiu.zhihudaily.newsList.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.mvpbase.presenter.BasePresenterFragment;
import com.baiiu.zhihudaily.newsDetail.view.NewsDetailActivity;
import com.baiiu.zhihudaily.newsList.model.Daily;
import com.baiiu.zhihudaily.newsList.model.source.INewsListDataSource;
import com.baiiu.zhihudaily.newsList.model.source.NewsListRepository;
import com.baiiu.zhihudaily.newsList.view.IRefreshLoadMore;
import com.baiiu.zhihudaily.newsList.view.NewsListView;
import com.baiiu.zhihudaily.newsList.view.holder.NewsViewHolder;
import com.baiiu.zhihudaily.util.CommonUtil;
import com.baiiu.zhihudaily.util.ReadedListUtil;
import com.baiiu.zhihudaily.util.UIUtil;
import com.baiiu.zhihudaily.util.net.util.HttpNetUtil;

public class NewsListFragment extends BasePresenterFragment<NewsListView>
    implements IRefreshLoadMore, android.view.View.OnClickListener {

  private NewsListRepository newsListRepository;

  public static NewsListFragment instance() {
    return new NewsListFragment();
  }

  @Override protected Class<NewsListView> getDelegateViewClass() {
    return NewsListView.class;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    newsListRepository = NewsListRepository.instance();
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    viewDelegate.showLoadingPage();
    //第一次,从本地加载
    loadNewsList(false, true);
  }

  public void loadNewsList(final boolean fromRemote, final boolean refresh) {

    //设置是否从远端拉取数据
    newsListRepository.refreshNewsList(fromRemote);

    newsListRepository.loadNewsList("", refresh, new INewsListDataSource.LoadNewsListCallback() {
      @Override public void onSuccess(Daily daily) {
        if (daily == null) {
          return;
        }

        if (refresh) {
          viewDelegate.showLoadingIndicator(false);
        }

        if (fromRemote) {
          //从远端返回的数据
          if (CommonUtil.isEmpty(daily.stories)) {
            if (viewDelegate.isDataEmpty()) {
              viewDelegate.showEmptyPage();
            } else {
              viewDelegate.showErrorInfo("拉取数据为空");
            }
          } else {
            viewDelegate.showNews(daily, refresh);
          }

          viewDelegate.bindFooter(daily.stories, false);
        } else {
          //本地的数据
          if (refresh) {
            if (CommonUtil.isEmpty(daily.stories)) {
              //本地都没有缓存
              viewDelegate.showLoadingIndicator(false);
              onRefresh();
            } else {
              //本地有,展示数据
              viewDelegate.showNews(daily, true);
              viewDelegate.bindFooter(daily.stories, true);

              UIUtil.postDelayed(new Runnable() {
                @Override public void run() {
                  viewDelegate.showLoadingIndicator(true);
                  onRefresh();
                }
              }, 1000);
            }
          } else {
            viewDelegate.showNews(daily, false);
            viewDelegate.bindFooter(daily.stories, true);
          }
        }
      }

      @Override public void onFailure() {
        viewDelegate.showLoadingIndicator(false);
        viewDelegate.showErrorInfo("网络错误");

        if (viewDelegate.isDataEmpty()) {
          //历史数据都没有
          viewDelegate.showErrorPage();
        } else {
          viewDelegate.bindFooter(null, false);
        }
      }
    });
  }

  @Override public void loadMore() {
    if (HttpNetUtil.isConnected()) {
      loadNewsList(true, false);
    } else {
      loadNewsList(false, false);
    }
  }

  @Override public void onRefresh() {
    if (HttpNetUtil.isConnected()) {
      //刷新数据时从网络拉取
      loadNewsList(true, true);
    } else {
      viewDelegate.showLoadingIndicator(false);
      viewDelegate.showErrorInfo("网络未连接");

      if (viewDelegate.isDataEmpty()) {
        viewDelegate.showErrorPage();
      }
    }
  }

  @Override public void onClick(View v) {
    long id = 0;
    switch (v.getId()) {
      case R.id.item_news:
        NewsViewHolder holder = (NewsViewHolder) v.getTag();

        holder.mStroy.isRead = true;
        id = holder.mStroy.id;

        //已读
        viewDelegate.showNewsReaded(holder.getAdapterPosition(), true);
        startActivity(NewsDetailActivity.instance(mContext, holder.mStroy));

        break;
      case R.id.item_topic_news:
        id = (long) v.getTag(R.id.item_topic_news);
        startActivity(NewsDetailActivity.instance(mContext, id));
        break;
    }

    if (id != 0) {
      ReadedListUtil.saveToReadedList(NewsListRepository.READ_LIST, String.valueOf(id));
    }
  }
}
