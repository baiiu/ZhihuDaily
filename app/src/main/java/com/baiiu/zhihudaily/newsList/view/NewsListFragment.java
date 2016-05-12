package com.baiiu.zhihudaily.newsList.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Bind;
import com.baiiu.tsnackbar.Prompt;
import com.baiiu.tsnackbar.TSnackbar;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.newsList.model.Daily;
import com.baiiu.zhihudaily.newsList.model.Story;
import com.baiiu.zhihudaily.newsDetail.view.NewsDetailActivity;
import com.baiiu.zhihudaily.newsList.NewsListContract;
import com.baiiu.zhihudaily.newsList.view.holder.NewsViewHolder;
import com.baiiu.zhihudaily.view.base.BaseFragment;
import java.util.List;

public class NewsListFragment extends BaseFragment
    implements View.OnClickListener, NewsListContract.View {

  public static NewsListFragment instance() {
    return new NewsListFragment();
  }

  private NewsListContract.Presenter mNewsListPresenter;
  private DailyNewsAdapter dailyNewsAdapter;

  @Bind(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
  @Bind(R.id.recyclerView) RecyclerView recyclerView;

  @Override public int provideLayoutId() {
    return R.layout.fragment_main;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    refreshLayout.setOnRefreshListener(mNewsListPresenter);
    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    dailyNewsAdapter = new DailyNewsAdapter(mContext, this, mNewsListPresenter);
    recyclerView.setAdapter(dailyNewsAdapter);

    getActivity().findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        recyclerView.smoothScrollToPosition(0);
      }
    });

    //放在这里执行,只执行一次,在onResume时可见时会加载页面,不需要这样
    mNewsListPresenter.start();
  }

  @Override public void onClick(View v) {
    long id = 0;
    switch (v.getId()) {
      case R.id.item_news:
        NewsViewHolder holder = (NewsViewHolder) v.getTag();
        mNewsListPresenter.openNewsDetail(holder);
        break;
      case R.id.item_topic_news:
        id = (long) v.getTag(R.id.item_topic_news);
        startActivity(NewsDetailActivity.instance(mContext, id));
        break;
    }
  }

  @Override public void setPresenter(NewsListContract.Presenter newsListPresenter) {
    this.mNewsListPresenter = newsListPresenter;
  }

  @Override public void showSuccessInfo(String info) {
    TSnackbar.make(refreshLayout, info, Prompt.SUCCESS).show();
  }

  @Override public void showErrorInfo(String info) {
    TSnackbar.make(refreshLayout, info, Prompt.ERROR).show();
  }

  @Override public void showLoadingPage() {
    dailyNewsAdapter.setLoading(true);
  }

  @Override public void showLoadingIndicator(boolean show) {
    refreshLayout.setRefreshing(show);
  }

  @Override public void showNews(Daily daily, boolean update) {
    dailyNewsAdapter.setLoading(false);
    dailyNewsAdapter.setEmpty(false);
    dailyNewsAdapter.setError(false);
    dailyNewsAdapter.setDaily(daily, update);
  }

  @Override public void showEmptyPage() {
    dailyNewsAdapter.setEmpty(true);
    dailyNewsAdapter.notifyDataSetChanged();
  }

  @Override public void showErrorPage() {
    dailyNewsAdapter.setError(true);
    dailyNewsAdapter.notifyDataSetChanged();
  }

  @Override public boolean isDataEmpty() {
    return dailyNewsAdapter.isDataEmpty();
  }

  @Override public void bindFooter(List<Story> list, boolean fromLocal) {
    dailyNewsAdapter.bindFooter(list, fromLocal);
  }

  @Override public void showNewsDetail(Story story) {
    startActivity(NewsDetailActivity.instance(mContext, story));
  }

  @Override public void showNewsReaded(int position, boolean isRead) {
    dailyNewsAdapter.notifyItemChanged(position, isRead);
  }
}
