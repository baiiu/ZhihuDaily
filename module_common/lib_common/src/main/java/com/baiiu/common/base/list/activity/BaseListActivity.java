package com.baiiu.common.base.list.activity;

import android.os.Bundle;
import android.text.TextUtils;
import com.baiiu.common.base.BaseActivity;
import com.baiiu.common.base.list.fragment.BaseRefreshLoadMoreAdapter;
import com.baiiu.common.net.ApiConstants;
import java.util.List;

/**
 * author: baiiu
 * date: on 17/1/19 14:37
 * description: 用Adapter控制的ListActivity
 */
public abstract class BaseListActivity<E> extends BaseActivity implements IListPageView<List<E>> {
    protected BaseRefreshLoadMoreAdapter<E> mAdapter;

    @Override protected void initOnCreate(Bundle savedInstanceState) {
        mAdapter = provideAdapter();
    }

    /** 发生在initOnCreate()中 */
    protected abstract BaseRefreshLoadMoreAdapter<E> provideAdapter();

    @Override public void showContent(List<E> data) {
        if (mAdapter == null) {
            return;
        }
        mAdapter.setList(data);
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

    @Override public void showLoadingPage(String loadingInfo) {
        if (mAdapter == null) {
            return;
        }

        mAdapter.clear();

        mAdapter.setEmpty(false, loadingInfo);
        mAdapter.setError(false, loadingInfo);
        mAdapter.setLoading(true, loadingInfo);
        mAdapter.notifyDataSetChanged();
    }

}
