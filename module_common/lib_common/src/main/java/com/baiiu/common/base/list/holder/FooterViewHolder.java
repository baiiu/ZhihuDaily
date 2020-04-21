package com.baiiu.common.base.list.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.baiiu.common.base.BaseViewHolder;
import com.baiiu.common.util.UIUtil;
import com.baiiu.lib_common.R;

/**
 * Created by baiiu on 2015/11/21.
 * ListView的脚布局，提供三种状态的设置
 */
public class FooterViewHolder extends BaseViewHolder<Integer> {
    public static final int HAS_MORE = 0;
    public static final int NO_MORE = 1;
    public static final int ERROR = 2;
    public static final int GONE = 3;

    private int mCurrentState = -1;

    private ViewGroup loadingView;
    private ViewGroup errorView;
    private ViewGroup noMoreView;
    private FrameLayout mRootView;


    public FooterViewHolder(Context context) {
        super(UIUtil.inflate(context, R.layout.holder_footer));
        loadingView = itemView.findViewById(R.id.loading);
        errorView = itemView.findViewById(R.id.error);
        noMoreView = itemView.findViewById(R.id.nomore);
        mRootView = itemView.findViewById(R.id.container);
    }

    @Override public void bind(Integer data) {
        mRootView.setVisibility(data == View.GONE ? View.GONE : View.VISIBLE);

        if (mCurrentState == data) {
            return;
        }

        mCurrentState = data;

        loadingView.setVisibility(data == HAS_MORE ? View.VISIBLE : View.INVISIBLE);
        noMoreView.setVisibility(data == NO_MORE ? View.VISIBLE : View.INVISIBLE);
        errorView.setVisibility(data == ERROR ? View.VISIBLE : View.INVISIBLE);
    }

    public void setOnErrorClickListener(View.OnClickListener listener) {
        errorView.setOnClickListener(listener);
    }

    public void setInvisible() {
        mRootView.setVisibility(View.INVISIBLE);
    }

    public void setGone() {
        mRootView.setVisibility(View.GONE);
    }


    public FrameLayout getRootView() {
        return mRootView;
    }

    public boolean isNoMore() {
        return mCurrentState == NO_MORE;
    }

    public boolean isError() {
        return mCurrentState == ERROR;
    }

    public boolean isHasLoadMore() {
        return mCurrentState == HAS_MORE;
    }
}

