package com.baiiu.zhihudaily.newsList.view.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.view.base.BaseViewHolder;
import com.baiiu.zhihudaily.data.newsListData.Story;

import butterknife.Bind;

/**
 * author: baiiu
 * date: on 16/4/5 19:31
 * description:
 */
public class DateViewHolder extends BaseViewHolder<Story> {

  @Bind(R.id.textView) TextView textView;

  public DateViewHolder(Context mContext, ViewGroup parent) {
    super(mContext, R.layout.holder_date, parent);
  }

  @Override public void bind(Story data) {
    textView.setText(data.title);
  }
}
