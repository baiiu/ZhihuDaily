package com.baiiu.zhihudaily.base.list.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.baiiu.zhihudaily.base.list.WrapContentLinearLayoutManager;

/**
 * author: baiiu
 * date: on 16/7/1 20:06
 * description:
 */
public class LoadingMoreScrollListenerM extends RecyclerView.OnScrollListener {
    private static final int LAST_VISIBLE_COUNT_TO_LOADING = 2;

    private boolean isLoading = true;
    private OnLoadingMoreListenerM mListener;
    private OnVisibleCountEqualsTotalCountListener mOnVisibleCountEqualsTotalCountListener;

    public LoadingMoreScrollListenerM(OnLoadingMoreListenerM mListener,
            OnVisibleCountEqualsTotalCountListener listener) {
        this.mListener = mListener;
        this.mOnVisibleCountEqualsTotalCountListener = listener;
    }

    @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        //LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int itemCount = -1;
        int lastVisibleItemPosition = -1;
        int mVisibleCount = -1;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (WrapContentLinearLayoutManager) recyclerView.getLayoutManager();
            itemCount = layoutManager.getItemCount();
            lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            mVisibleCount = lastVisibleItemPosition - layoutManager.findFirstVisibleItemPosition() + 1;
        }

        if (mOnVisibleCountEqualsTotalCountListener != null) {
            if (mVisibleCount >= itemCount) {
                mOnVisibleCountEqualsTotalCountListener.onVisibleCountEqualsTotalCount();
            } else {
                mOnVisibleCountEqualsTotalCountListener.onVisibleCountLessThanTotalCount();
            }
        }

        //最后一个可见时加载数据
        if (isLoading && dy >= 0 && (lastVisibleItemPosition + LAST_VISIBLE_COUNT_TO_LOADING >= itemCount)) {
            isLoading = false;
            if (mListener != null) {
                mListener.onLoadingMore();
            }
        }
    }


    @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        //if (newState == RecyclerView.SCROLL_STATE_IDLE) {
        //    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //
        //    int mVisibleCount =
        //            layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition() + 1;
        //
        //    if (mOnVisibleCountEqualsTotalCountListener != null) {
        //        if (mVisibleCount >= layoutManager.getItemCount()) {
        //            mOnVisibleCountEqualsTotalCountListener.onVisibleCountEqualsTotalCount();
        //        } else {
        //            mOnVisibleCountEqualsTotalCountListener.onVisibleCountLessThanTotalCount();
        //        }
        //    }
        //}

    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public interface OnLoadingMoreListenerM {
        void onLoadingMore();
    }

    public interface OnVisibleCountEqualsTotalCountListener {
        void onVisibleCountEqualsTotalCount();//不满一屏,全部可见

        void onVisibleCountLessThanTotalCount();//满于一屏
    }


    /**
     * 数据加载更多完成后一定要设置为true
     */
    public void onLoadMoreComplete() {
        setLoading(true);
    }

    //    public void setOnLoadingMoreListener(OnLoadingMoreListener loadingMoreListener) {
    //        this.mListener = loadingMoreListener;
    //    }
}