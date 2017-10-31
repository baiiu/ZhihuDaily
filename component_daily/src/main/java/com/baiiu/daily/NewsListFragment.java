package com.baiiu.daily;

import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;
import com.baiiu.common.base.BaseActivity;
import com.baiiu.common.base.list.fragment.BaseListFragment;
import com.baiiu.common.base.list.fragment.BaseRefreshLoadMoreAdapter;
import com.baiiu.common.util.CommonUtil;
import com.baiiu.common.util.DoubleClickListener;
import com.baiiu.common.view.fastscroll.FastScrollLinearLayoutManager;
import com.baiiu.daily.data.bean.Story;
import com.baiiu.daily.holder.NewsViewHolder;
import com.baiiu.daily.newsDetail.NewsDetailActivity;
import com.baiiu.daily.R;
import com.baiiu.tsnackbar.Prompt;
import com.baiiu.tsnackbar.TSnackbar;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

/**
 * auther: baiiu
 * time: 17/2/19 19 14:38
 * description:
 */

public class NewsListFragment extends BaseListFragment<Story, NewsListPresenter> implements NewsListContract.IView, View.OnClickListener {

    public static NewsListFragment instance() {
        return new NewsListFragment();
    }

    @Override protected void initOnCreate() {
    }

    @Override protected void initRecyclerView() {
        //if (PreferenceUtil.instance()
        //        .get(Constant.UI_MODE, true)) {
        //mRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.colorPrimary_Day));
        //} else {
        //mRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.colorPrimary_Night));
        //}

        mRecyclerView.setLayoutManager(new FastScrollLinearLayoutManager(mContext));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration((DailyNewsAdapter) mAdapter);
        mRecyclerView.addItemDecoration(headersDecor);

        View fab = getActivity().findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(v -> mRecyclerView.smoothScrollToPosition(0));
        }

        View view = ((BaseActivity) getActivity()).mToolbar;
        if (view != null) {
            view.setOnClickListener(new DoubleClickListener() {
                @Override public void onDoubleClick(View v) {
                    mRecyclerView.smoothScrollToPosition(0);
                }
            });
        }
    }

    @Override protected NewsListPresenter providePresenter() {
        return new NewsListPresenter();
    }


    @Override protected BaseRefreshLoadMoreAdapter<Story> provideAdapter() {
        return new DailyNewsAdapter(mContext, this);
    }

    @Override public void onClick(View v) {
        long id = 0;
        switch (v.getId()) {
            case R2.id.item_news:
                NewsViewHolder holder = (NewsViewHolder) v.getTag();
                mPresenter.openNewsDetail(holder);
                break;
            case R2.id.item_topic_news:
                id = (long) v.getTag(R.id.item_topic_news);
                mContext.startActivity(NewsDetailActivity.instance(mContext, id));
                break;
        }
    }

    @Override public boolean isDataEmpty() {
        return CommonUtil.isEmpty(mAdapter.getList());
    }

    @Override public void showSuccessInfo(String info) {
        TSnackbar.make(mPtr, info, Prompt.SUCCESS)
                .show();
    }

    @Override public void showErrorInfo(String info) {
        TSnackbar.make(mPtr, info, Prompt.ERROR)
                .show();
    }

    @Override public void showNewsDetail(Story story) {
        mContext.startActivity(NewsDetailActivity.instance(mContext, story));
    }

    @Override public void showNewsReaded(int position, boolean isRead) {
        mAdapter.notifyItemChanged(position, isRead);
    }

}
