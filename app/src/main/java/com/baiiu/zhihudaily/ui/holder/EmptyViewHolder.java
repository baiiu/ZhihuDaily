package com.baiiu.zhihudaily.ui.holder;

import android.content.Context;
import android.view.ViewGroup;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.base.BaseViewHolder;

/**
 * author: baiiu
 * date: on 16/4/11 11:16
 * description:
 */
public class EmptyViewHolder extends BaseViewHolder<String> {

  public EmptyViewHolder(Context mContext, ViewGroup parent) {
    super(mContext, R.layout.holder_empty, parent);
  }

  @Override public void bind(String data) {

  }
}
