package com.baiiu.common.base.list.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.baiiu.common.base.BaseViewHolder;
import com.baiiu.lib_common.R;
import com.baiiu.lib_common.R2;

/**
 * author: baiiu
 * date: on 16/4/11 11:21
 * description:
 */
public class ErrorViewHolder extends BaseViewHolder<String> {
    @BindView(R2.id.tv_retry) View retry;

    public ErrorViewHolder(Context mContext, ViewGroup parent) {
        super(mContext, R.layout.layout_error, parent);
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
