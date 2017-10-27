package com.baiiu.common.base.list.holder;

import android.content.Context;
import android.view.ViewGroup;
import com.baiiu.common.base.BaseViewHolder;
import com.baiiu.lib_common.R;

/**
 * author: baiiu
 * date: on 16/4/11 15:05
 * description:
 */
public class LoadingViewHolder extends BaseViewHolder<String> {
    public LoadingViewHolder(Context mContext, ViewGroup parent) {
        super(mContext, R.layout.layout_loading, parent);
    }

    @Override public void bind(String data) {

    }
}
