package com.baiiu.zhihudaily.base.list.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.base.BaseViewHolder;

/**
 * author: baiiu
 * date: on 16/4/11 11:21
 * description:
 */
public class ErrorViewHolder extends BaseViewHolder<String> {
    @BindView(R.id.retry) View retry;

    public ErrorViewHolder(Context mContext, ViewGroup parent) {
        super(mContext, R.layout.holder_error, parent);
    }

    @Override public void bind(String data) {

    }

    public void setRetryListener(View.OnClickListener listener) {
        if (listener == null) {
            return;
        }

        retry.setOnClickListener(listener);
    }
}
