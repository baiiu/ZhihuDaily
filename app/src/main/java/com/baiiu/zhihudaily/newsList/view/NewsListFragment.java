package com.baiiu.zhihudaily.newsList.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.baiiu.tsnackbar.Prompt;
import com.baiiu.tsnackbar.TSnackbar;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.data.entity.Daily;
import com.baiiu.zhihudaily.data.entity.Story;
import com.baiiu.zhihudaily.data.repository.DaggerNewsComponent;
import com.baiiu.zhihudaily.data.util.PreferenceUtil;
import com.baiiu.zhihudaily.newsList.NewsListContract;
import com.baiiu.zhihudaily.newsList.presenter.NewsListPresenter;
import com.baiiu.zhihudaily.newsList.view.holder.NewsViewHolder;
import com.baiiu.zhihudaily.util.Constant;
import com.baiiu.zhihudaily.util.DoubleClickListener;
import com.baiiu.zhihudaily.util.UIUtil;
import com.baiiu.zhihudaily.util.router.Navigator;
import com.baiiu.zhihudaily.view.base.BaseFragment;
import com.baiiu.zhihudaily.view.fastscroll.FastScrollLinearLayoutManager;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import java.util.List;
import javax.inject.Inject;

public class NewsListFragment extends BaseFragment implements View.OnClickListener, NewsListContract.IView {

    public static NewsListFragment instance() {
        return new NewsListFragment();
    }

    @Inject NewsListPresenter mNewsListPresenter;

    private DailyNewsAdapter mDailyNewsAdapter;

    @BindView(R.id.refreshLayout) SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override public int provideLayoutId() {
        return R.layout.fragment_main;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DaggerNewsComponent.builder()
                .appComponent(UIUtil.getAppComponent())
                .build()
                .inject(this);


        mNewsListPresenter.attachView(this);

        mRefreshLayout.setOnRefreshListener(mNewsListPresenter);

        if (PreferenceUtil.instance()
                .get(Constant.UI_MODE, true)) {
            mRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.colorPrimary_Day));
        } else {
            mRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.colorPrimary_Night));
        }

        mRecyclerView.setLayoutManager(new FastScrollLinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mDailyNewsAdapter = new DailyNewsAdapter(mContext, this, mNewsListPresenter);
        mRecyclerView.setAdapter(mDailyNewsAdapter);

        StickyRecyclerHeadersDecoration headersDecor =
                new StickyRecyclerHeadersDecoration(mDailyNewsAdapter);
        mRecyclerView.addItemDecoration(headersDecor);

        getActivity().findViewById(R.id.fab)
                .setOnClickListener(v -> mRecyclerView.smoothScrollToPosition(0));

        //双击返回
        View view = ((NewsListActivity) getActivity()).mTtoolbar;
        if (view != null) {
            view.setOnClickListener(new DoubleClickListener() {
                @Override public void onDoubleClick(View v) {
                    mRecyclerView.smoothScrollToPosition(0);
                }
            });
        }

        //放在这里执行,只执行一次,在onResume时可见时会加载页面,不需要这样
        mNewsListPresenter.start();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mNewsListPresenter.detachView();
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
                Navigator.INSTANCE.navigatorToDetail(mContext, id);
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
        Navigator.INSTANCE.navigatorToDetail(mContext, story);
    }

    @Override public void showNewsReaded(int position, boolean isRead) {
        mDailyNewsAdapter.notifyItemChanged(position, isRead);
    }
}
