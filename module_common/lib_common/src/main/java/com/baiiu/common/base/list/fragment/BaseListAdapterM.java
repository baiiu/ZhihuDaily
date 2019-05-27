package com.baiiu.common.base.list.fragment;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.baiiu.common.base.BaseViewHolder;
import com.baiiu.common.base.list.holder.EmptyViewHolder;
import com.baiiu.common.base.list.holder.ErrorViewHolder;
import com.baiiu.common.base.list.holder.LoadingViewHolder;

/**
 * author: baiiu
 * date: on 16/5/28 09:31
 * description:
 */
public abstract class BaseListAdapterM extends RecyclerView.Adapter<BaseViewHolder> {
    public static final int TYPE_EMPTY = -1;
    public static final int TYPE_ERROR = -2;
    public static final int TYPE_LOADING = -3;

    protected Context mContext;
    protected boolean isError = false;
    protected boolean isEmpty = false;
    protected String mEmptyOrErrorString = null;
    private View.OnClickListener mRetryListener;
    /**
     * 默认打开时显示Loading页面
     */
    protected boolean isLoading = true;

    public BaseListAdapterM(Context context, boolean needLoading) {
        this.mContext = context;
        this.isLoading = needLoading;
    }

    public void setErrorRetryListener(View.OnClickListener listener) {
        mRetryListener = listener;
    }

    @Override public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_EMPTY:
                return new EmptyViewHolder(mContext, parent);
            case TYPE_ERROR:
                return new ErrorViewHolder(mContext, parent);
            case TYPE_LOADING:
                return new LoadingViewHolder(mContext, parent);
        }

        return onCreateViewHolderInner(parent, viewType);
    }


    @Override public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder instanceof EmptyViewHolder || holder instanceof ErrorViewHolder) {
            holder.bind(mEmptyOrErrorString);

            if (holder instanceof ErrorViewHolder) {
                ((ErrorViewHolder) holder).setRetryListener(mRetryListener);
            }

            return;
        }

        onBindViewHolderInner(holder, position);
    }

    @Override public int getItemViewType(int position) {
        if (isLoading) {
            return TYPE_LOADING;
        }

        if (isEmpty) {
            return TYPE_EMPTY;
        }

        if (isError) {
            return TYPE_ERROR;
        }

        return getItemViewTypeInner(position);
    }


    @Override public int getItemCount() {
        if (isEmpty || isError || isLoading) {
            return 1;
        }

        return getItemCountInner();
    }


    protected abstract int getItemCountInner();

    protected abstract int getItemViewTypeInner(int position);

    protected abstract BaseViewHolder onCreateViewHolderInner(ViewGroup parent, int viewType);

    protected abstract void onBindViewHolderInner(BaseViewHolder holder, int position);

    public void setError(boolean error, String errorString) {
        this.isError = error;
        this.isLoading = false;
        this.mEmptyOrErrorString = errorString;
    }

    public void setEmpty(boolean empty, String emptyString) {
        this.isEmpty = empty;
        this.isLoading = false;
        this.mEmptyOrErrorString = emptyString;
    }

}
