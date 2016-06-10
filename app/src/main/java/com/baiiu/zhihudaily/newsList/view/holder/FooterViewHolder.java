package com.baiiu.zhihudaily.newsList.view.holder;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindView;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.util.UIUtil;
import com.baiiu.zhihudaily.view.base.BaseViewHolder;

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

    @BindView(R.id.loading) LinearLayout loadingView;
    @BindView(R.id.error) LinearLayout errorView;
    @BindView(R.id.nomore) LinearLayout noMoreView;

    public FooterViewHolder(Context context) {
        super(UIUtil.inflate(context, R.layout.holder_footer));
    }

    @Override public synchronized void bind(Integer data) {
        if (mCurrentState == data) {
            return;
        }

        mCurrentState = data;

        loadingView.setVisibility(data == HAS_MORE ? View.VISIBLE : View.INVISIBLE);
        noMoreView.setVisibility(data == NO_MORE ? View.VISIBLE : View.INVISIBLE);
        errorView.setVisibility(data == ERROR ? View.VISIBLE : View.INVISIBLE);
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

    public void setNoMoreView(View view, LinearLayout.LayoutParams params) {
        noMoreView.removeAllViews();
        noMoreView.addView(view, params);
    }
}
