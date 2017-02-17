package com.baiiu.zhihudaily.base.list.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.BindView;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.base.BaseViewHolder;
import com.baiiu.zhihudaily.util.UIUtil;

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

    @BindView(R.id.loading) ViewGroup loadingView;
    @BindView(R.id.error) ViewGroup errorView;
    @BindView(R.id.nomore) ViewGroup noMoreView;
    @BindView(R.id.container) FrameLayout mRootView;


    public FooterViewHolder(Context context) {
        super(UIUtil.inflate(context, R.layout.holder_footer));
    }

    public FooterViewHolder(Context context, ViewGroup parent) {
        super(UIUtil.inflate(context, R.layout.holder_footer, parent));
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

