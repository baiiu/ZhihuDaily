package com.baiiu.zhihudaily.newsList.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.Bind;
import com.baiiu.tsnackbar.Prompt;
import com.baiiu.tsnackbar.TSnackbar;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.mvpbase.view.BaseFragmentViewDelegate;
import com.baiiu.zhihudaily.newsList.NewsListContract;
import com.baiiu.zhihudaily.newsList.model.Daily;
import com.baiiu.zhihudaily.newsList.model.Story;
import java.util.List;

/**
 * author: baiiu
 * date: on 16/5/13 14:55
 * description:
 */
public class NewsListView extends BaseFragmentViewDelegate {

  private NewsListContract.Presenter mNewsListPresenter;

  @Bind(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
  @Bind(R.id.recyclerView) RecyclerView recyclerView;

  private DailyNewsAdapter dailyNewsAdapter;

  @Override public int provideLayoutId() {
    return R.layout.fragment_main;
  }

  @Override public void initView() {
    refreshLayout.setOnRefreshListener(mNewsListPresenter);
    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    dailyNewsAdapter = new DailyNewsAdapter(mContext, mNewsListPresenter, mNewsListPresenter);
    recyclerView.setAdapter(dailyNewsAdapter);

    //mRootView.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
    //  @Override public void onClick(View v) {
    //    recyclerView.smoothScrollToPosition(0);
    //  }
    //});

    mNewsListPresenter.start();
  }

  public void setPresenter(NewsListContract.Presenter presenter) {
    this.mNewsListPresenter = presenter;
  }

  public void showSuccessInfo(String info) {
    TSnackbar.make(refreshLayout, info, Prompt.SUCCESS).show();
  }

  public void showErrorInfo(String info) {
    TSnackbar.make(refreshLayout, info, Prompt.ERROR).show();
  }

  public void showLoadingPage() {
    dailyNewsAdapter.setLoading(true);
  }

  public void showLoadingIndicator(boolean show) {
    refreshLayout.setRefreshing(show);
  }

  public void showNews(Daily daily, boolean update) {
    dailyNewsAdapter.setLoading(false);
    dailyNewsAdapter.setEmpty(false);
    dailyNewsAdapter.setError(false);
    dailyNewsAdapter.setDaily(daily, update);
  }

  public void showEmptyPage() {
    dailyNewsAdapter.setEmpty(true);
    dailyNewsAdapter.notifyDataSetChanged();
  }

  public void showErrorPage() {
    dailyNewsAdapter.setError(true);
    dailyNewsAdapter.notifyDataSetChanged();
  }

  public boolean isDataEmpty() {
    return dailyNewsAdapter.isDataEmpty();
  }

  public void bindFooter(List<Story> list, boolean fromLocal) {
    dailyNewsAdapter.bindFooter(list, fromLocal);
  }

  public void showNewsReaded(int position, boolean isRead) {
    dailyNewsAdapter.notifyItemChanged(position, isRead);
  }
}
