package com.baiiu.common.base.list.holder;

import android.content.Context;
import android.view.ViewGroup;
import com.baiiu.common.base.BaseViewHolder;
import com.baiiu.lib_common.R;

/**
 * author: baiiu
 * date: on 16/4/11 11:16
 * description:
 */
public class EmptyViewHolder extends BaseViewHolder<String> {

    public EmptyViewHolder(Context mContext, ViewGroup parent) {
        super(mContext, R.layout.layout_empty, parent);
    }

    @Override public void bind(String data) {

    }
}
