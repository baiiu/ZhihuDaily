package com.baiiu.zhihudaily.newsList.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import com.baiiu.common.base.BaseViewHolder;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.data.bean.Story;

/**
 * author: baiiu
 * date: on 16/4/5 19:31
 * description:
 */
public class DateViewHolder extends BaseViewHolder<Story> {

    @BindView(R.id.textView) TextView textView;

    public DateViewHolder(Context mContext, ViewGroup parent) {
        super(mContext, R.layout.holder_date, parent);
    }

    @Override public void bind(Story data) {
        textView.setText(data.date);
    }
}
