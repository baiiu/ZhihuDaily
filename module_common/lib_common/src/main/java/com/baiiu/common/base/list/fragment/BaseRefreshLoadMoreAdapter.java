package com.baiiu.common.base.list.fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.baiiu.common.base.BaseViewHolder;
import com.baiiu.common.base.list.holder.EmptyViewHolder;
import com.baiiu.common.base.list.holder.ErrorViewHolder;
import com.baiiu.common.base.list.holder.FooterViewHolder;
import com.baiiu.common.base.list.holder.LoadingViewHolder;
import com.baiiu.common.util.CommonUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * author: baiiu
 * date: on 16/7/4 10:02
 * description:
 * 基础上拉加载更多Adapter，可以通过复写getItemViewTypeInner、getItemCountInner来调整列表显示
 * <p>
 * 10个以内不显示FooterViewHolder
 */
public abstract class BaseRefreshLoadMoreAdapter<E> extends RecyclerView.Adapter<BaseViewHolder> {
    protected static final int TYPE_FOOTER = -1;
    protected static final int TYPE_EMPTY = -2;// 空页面
    protected static final int TYPE_ERROR = -3;// 错误
    protected static final int TYPE_LOADING = -4;// 加载

    //public static final int MIN_COUNT_SHOW_FOOTER = 2;
    public static final int MIN_COUNT_SHOW_FOOTER = 10;

    protected Context mContext;
    protected List<E> mList;

    // FIXME private -> protected: isEmpty & isError & isLoading
    protected boolean isEmpty;
    protected boolean isError;
    private FooterViewHolder mFooterViewHolder;
    protected String mEmptyOrErrorString = null;
    protected View.OnClickListener mRetryListener;

    /**
     * 默认展示Loading页面
     */
    protected boolean isLoading = true;

    public BaseRefreshLoadMoreAdapter(Context context) {
        this(context, null, false);
    }

    public BaseRefreshLoadMoreAdapter(Context context, boolean needLoading) {
        this(context, null, needLoading);
    }

    public BaseRefreshLoadMoreAdapter(Context context, List<E> list, boolean needLoading) {
        this.mContext = context;
        isEmpty = false;
        isError = false;
        this.isLoading = needLoading;

        this.mList = list;
        notifyDataSetChanged();
    }

    public void setList(List<E> list) {
        if (list == null) {
            list = new ArrayList<>();
        }

        this.mList = list;
        isEmpty = false;
        isError = false;
        isLoading = false;
        notifyDataSetChanged();
    }

    public void addToLast(List<E> list) {
        if (CommonUtil.isEmpty(list) || mList == null) {
            return;
        }

        notifyItemRangeInserted(mList.size(), list.size());
        mList.addAll(list);
    }

    public void clear() {
        if (mList == null) {
            return;
        }

        mList.clear();
    }

    @Nullable public List<E> getList() {
        return mList;
    }

    @Override public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_EMPTY:
                return new EmptyViewHolder(mContext, parent);
            case TYPE_ERROR:
                return new ErrorViewHolder(mContext, parent);
            case TYPE_LOADING:
                return new LoadingViewHolder(mContext, parent);
            case TYPE_FOOTER:
                return getFooterHolder();
        }

        return onCreateViewHolderInner(parent, viewType);
    }

    protected abstract BaseViewHolder onCreateViewHolderInner(ViewGroup parent, int viewType);

    @Override public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder instanceof EmptyViewHolder || holder instanceof ErrorViewHolder) {
            holder.bind(mEmptyOrErrorString);

            if (holder instanceof ErrorViewHolder) {
                ((ErrorViewHolder) holder).setRetryListener(mRetryListener);
            }

            return;
        }

        if (holder instanceof LoadingViewHolder) {
            return;
        }


        if (holder instanceof FooterViewHolder) {
            return;
        }

        onBindViewHolderInner(holder, position);
    }

    protected void onBindViewHolderInner(BaseViewHolder holder, int position) {
        if (mList == null || mList.size() == 0) return;

        E data = mList.get(position);
        if (data == null) {
            return;
        }

        bindData(holder, data, position);
    }

    public void bindData(BaseViewHolder holder, E data, int position) {
        holder.bind(data);
    }


    public FooterViewHolder getFooterHolder() {
        if (mFooterViewHolder == null) {
            mFooterViewHolder = new FooterViewHolder(mContext);
            ViewGroup footerView = mFooterViewHolder.getRootView();
            footerView.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
            //footerView.setBackgroundColor(UIUtil.getColor(R.color.color_f2f4f5));
            initFooterView(footerView);
        }

        return mFooterViewHolder;
    }

    public void initFooterView(ViewGroup footerView) {

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

    //自然数
    protected int getItemViewTypeInner(int position) {
        if (mList != null && mList.size() > MIN_COUNT_SHOW_FOOTER && position == mList.size()) {
            return TYPE_FOOTER;
        }

        return 0;
    }

    @Override public int getItemCount() {
        if (isEmpty || isError || isLoading) {
            return 1;
        }

        return getItemCountInner();
    }

    protected int getItemCountInner() {
        if (CommonUtil.isEmpty(mList)) {
            return 0;
        }

        int size = mList.size();

        if (size > MIN_COUNT_SHOW_FOOTER) {
            return size + 1;//添加FooterViewHolder
        }

        return size;
    }

    // 所有对Footer的控制都需要再请求一次网络
    //public void bindFooter(List<E> list) {
    //    bindFooter(list, ApiConstants.PAGE_SIZE);
    //}
    //
    //public void bindFooter(List<E> list, int pageSize) {
    //    if (list == null) {
    //        getFooterHolder().bind(FooterViewHolder.ERROR);
    //    } else if (list.size() < pageSize) {
    //        getFooterHolder().bind(FooterViewHolder.NO_MORE);
    //    } else {
    //        getFooterHolder().bind(FooterViewHolder.HAS_MORE);
    //    }
    //}


    public void bindFooter(int state) {
        getFooterHolder().bind(state);
    }

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

    public void setLoading(boolean loading, String loadingString) {
        this.isEmpty = false;
        this.isError = false;
        this.isLoading = true;
    }

    public void setErrorRetryListener(View.OnClickListener listener) {
        mRetryListener = listener;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
