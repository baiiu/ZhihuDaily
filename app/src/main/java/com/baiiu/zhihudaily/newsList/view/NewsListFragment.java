package com.baiiu.zhihudaily.newsList.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.baiiu.tsnackbar.Prompt;
import com.baiiu.tsnackbar.TSnackbar;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.newsDetail.view.NewsDetailActivity;
import com.baiiu.zhihudaily.newsList.NewsListContract;
import com.baiiu.zhihudaily.newsList.model.Daily;
import com.baiiu.zhihudaily.newsList.model.Story;
import com.baiiu.zhihudaily.newsList.presenter.NewsListPresenter;
import com.baiiu.zhihudaily.newsList.view.holder.NewsViewHolder;
import com.baiiu.zhihudaily.view.base.BaseFragment;
import java.util.List;

public class NewsListFragment extends BaseFragment implements View.OnClickListener, NewsListContract.IView {

    public static NewsListFragment instance() {
        return new NewsListFragment();
    }

    private NewsListContract.IPresenter mNewsListPresenter;
    private DailyNewsAdapter mDailyNewsAdapter;

    @BindView(R.id.refreshLayout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override public int provideLayoutId() {
        return R.layout.fragment_main;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mNewsListPresenter = new NewsListPresenter();
        mNewsListPresenter.attachView(this);

        mRefreshLayout.setOnRefreshListener(mNewsListPresenter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mDailyNewsAdapter = new DailyNewsAdapter(mContext, this, mNewsListPresenter);
        mRecyclerView.setAdapter(mDailyNewsAdapter);

        getActivity().findViewById(R.id.fab)
                .setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        mRecyclerView.smoothScrollToPosition(0);
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

    @Override public void showSuccessInfo(String info) {
        TSnackbar.make(mRefreshLayout, info, Prompt.SUCCESS)
                .show();
    }

    @Override public void showErrorInfo(String info) {
        TSnackbar.make(mRefreshLayout, info, Prompt.ERROR)
                .show();
    }

    @Override public void showLoadingPage() {
        mDailyNewsAdapter.setLoading(true);
    }

    @Override public void showLoadingIndicator(boolean show) {
        mRefreshLayout.setRefreshing(show);
    }

    @Override public void showNews(Daily daily, boolean update) {
        mDailyNewsAdapter.setLoading(false);
        mDailyNewsAdapter.setEmpty(false);
        mDailyNewsAdapter.setError(false);
        mDailyNewsAdapter.setDaily(daily, update);
    }

    @Override public void showEmptyPage() {
        mDailyNewsAdapter.setEmpty(true);
        mDailyNewsAdapter.notifyDataSetChanged();
    }

    @Override public void showErrorPage() {
        mDailyNewsAdapter.setError(true);
        mDailyNewsAdapter.notifyDataSetChanged();
    }

    @Override public boolean isDataEmpty() {
        return mDailyNewsAdapter.isDataEmpty();
    }

    @Override public void bindFooter(List<Story> list, boolean fromLocal) {
        mDailyNewsAdapter.bindFooter(list, fromLocal);
    }

    @Override public void showNewsDetail(Story story) {
        startActivity(NewsDetailActivity.instance(mContext, story));
    }

    @Override public void showNewsReaded(int position, boolean isRead) {
        mDailyNewsAdapter.notifyItemChanged(position, isRead);
    }
}
