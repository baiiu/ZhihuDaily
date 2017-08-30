package com.baiiu.zhihudaily.base.list.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import butterknife.BindView;
import com.baiiu.library.LogUtil;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.base.BaseFragment;
import com.baiiu.zhihudaily.base.list.WrapContentLinearLayoutManager;
import com.baiiu.zhihudaily.base.list.holder.FooterViewHolder;
import com.baiiu.zhihudaily.data.net.ApiConstants;
import com.baiiu.zhihudaily.data.net.http.HttpNetUtil;
import com.baiiu.zhihudaily.data.util.CommonUtil;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import java.util.List;
import javax.inject.Inject;

/**
 * author: baiiu
 * date: on 16/7/1 17:45
 * description: ListFragment，带下拉刷新
 */
public abstract class BaseListFragment<E, P extends BaseListContract.IRefreshPresenter<List<E>>> extends BaseFragment implements BaseListContract.IListMvpView<List<E>>, PtrHandler {

    @BindView(R.id.ptr) protected PtrClassicFrameLayout mPtr;
    @BindView(R.id.recyclerView) protected RecyclerView mRecyclerView;

    @Inject protected P mPresenter;
    protected BaseRefreshLoadMoreAdapter<E> mAdapter;
    protected LoadingMoreScrollListenerM mLoadingMoreScrollListener;

    @Override public int provideLayoutId() {
        return R.layout.fragment_list_refresh;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOnCreate();
        providePresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        //initPresenter();
    }

    protected void initOnCreate() {
    }

    //protected void initPresenter() {
    //}

    @Override public void onDestroyView() {
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(null);
            mPtr.setPtrHandler(null);
        }

        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override protected void initOnCreateView() {
        mAdapter = provideAdapter();
        mAdapter.setErrorRetryListener(v -> {
            showLoadingIndicator(true);
            mPresenter.start();
        });
        initRecyclerView();
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mContext));
        mLoadingMoreScrollListener = new LoadingMoreScrollListenerM(mPresenter, this);
        mRecyclerView.addOnScrollListener(mLoadingMoreScrollListener);
        initLoadingMoreScrollListener();
        mRecyclerView.setAdapter(mAdapter);
        //mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mPtr.setPtrHandler(this);
        mPtr.setDurationToCloseHeader(1000);
        mPtr.setDurationToClose(200);
        mPtr.setDurationToCloseHeaderAfterComplete(500);
        mPtr.setLoadingMinTime(1000);
        mPtr.setEnabledNextPtrAtOnce(true);
        //mPtr.setPinContent(true);

        mPtr.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= 16) {
                            mPtr.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            mPtr.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }

                        mPresenter.start();
                    }
                });
    }


    protected void initLoadingMoreScrollListener() {
    }


    protected void initRecyclerView() {
    }


    @Override public void onVisibleCountEqualsTotalCount() {
        if (mAdapter == null || mPresenter == null) return;

        /*
            RecyclerView同时进行下拉刷新、上拉加载数据刷新list时会报错。
            Release包中对LinearLayout进行捕获，可以不加。
         */
        if (mPtr.isRefreshing()) {
            mLoadingMoreScrollListener.setLoading(false);
            mAdapter.getFooterHolder()
                    .bind(FooterViewHolder.NO_MORE);
            return;
        }

        if (mAdapter.getFooterHolder()
                .isHasLoadMore()) {
            mPresenter.onLoadingMore();
        }
    }

    @Override public void onVisibleCountLessThanTotalCount() {
    }

    protected void convertBackgroundToTransparent() {
        if (mRecyclerView.getBackground() != null) {
            int color = ((ColorDrawable) mRecyclerView.getBackground()).getColor();
            if (color == Color.TRANSPARENT) {
                return;
            }
        }

        //LogUtil.d("执行LessThan: ");
        mRecyclerView.setBackgroundColor(Color.TRANSPARENT);
    }

    protected void providePresenter() {
    }

    protected abstract BaseRefreshLoadMoreAdapter<E> provideAdapter();

    @Override public void showLoadingIndicator(boolean show) {
        if (show) {
            mPtr.showLoadingIndicator();
        } else {
            mPtr.refreshComplete();
        }
    }

    @Override public void showEmptyPage(String emptyInfo) {
        if (mAdapter == null) {
            return;
        }

        if (TextUtils.isEmpty(emptyInfo)) {
            emptyInfo = ApiConstants.EMPTY_INFO;
        }

        mAdapter.clear();

        mAdapter.setEmpty(true, emptyInfo);
        mAdapter.setError(false, emptyInfo);
        mAdapter.notifyDataSetChanged();
    }

    @Override public void showErrorPage(String errorInfo) {
        if (mAdapter == null) {
            return;
        }

        if (TextUtils.isEmpty(errorInfo)) {
            errorInfo = ApiConstants.NET_ERROR_INFO;
        }

        mAdapter.clear();

        mAdapter.setEmpty(false, errorInfo);
        mAdapter.setError(true, errorInfo);
        mAdapter.notifyDataSetChanged();
    }

    @Override public void showContent(List<E> data, boolean refresh) {
        if (refresh) {
            mAdapter.setList(data);
        } else {
            mAdapter.addToLast(data);
        }

        showFooter(data);
    }

    protected void showFooter(List<E> list) {
        /*
            默认的是以pageSize比较,所有服务器返回的list，并不准确，再请求一次确定终止
            mAdapter.bindFooter(list);
            mLoadingMoreScrollListener.setLoading(mAdapter.getFooterHolder().isHasLoadMore());
         */

        int state;

        //// TODO: 17/1/4 使用HttpNetUtil
        if (HttpNetUtil.isConnected()) {
            if (list == null) {
                state = FooterViewHolder.ERROR;
            } else {
                state = CommonUtil.isEmpty(list) ? FooterViewHolder.NO_MORE : FooterViewHolder.HAS_MORE;
            }
        } else {
            state = FooterViewHolder.ERROR;
        }

        mAdapter.getFooterHolder()
                .bind(state);
        mLoadingMoreScrollListener.setLoading(mAdapter.getFooterHolder()
                                                      .isHasLoadMore());
        //LogUtil.d("state: " + state);

        if (state == FooterViewHolder.HAS_MORE) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

            int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            int itemCount = linearLayoutManager.getItemCount();

            int mVisibleCount = lastVisibleItemPosition - linearLayoutManager.findFirstVisibleItemPosition() + 1;
            if (mVisibleCount >= itemCount) {
                // 不满一页
                onVisibleCountEqualsTotalCount();
                return;
            }

            if (lastVisibleItemPosition + 1 == itemCount) {
                //最后一个可见，即loading圈可见，去主动加载下一页
                onVisibleCountEqualsTotalCount();
            }
        }

    }

    //从Presenter调用，一定要在showContent()后，有list时会自己触发，所有当无list时才手动调用
    @Override public void showFooter(int state) {
        mAdapter.bindFooter(state);
        mLoadingMoreScrollListener.setLoading(state == FooterViewHolder.HAS_MORE);
    }


    public void smoothScrollToTop() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Override public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        boolean b = PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
        LogUtil.d("checkCanDoRefresh: " + b);
        return b;
    }

    @Override public void onRefreshBegin(PtrFrameLayout frame) {
        mPresenter.onRefresh();
    }
}
