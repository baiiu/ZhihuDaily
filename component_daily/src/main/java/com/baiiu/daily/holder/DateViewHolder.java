package com.baiiu.daily.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import com.baiiu.common.base.BaseViewHolder;
import com.baiiu.daily.R;
import com.baiiu.daily.data.bean.Story;

/**
 * author: baiiu
 * date: on 16/4/5 19:31
 * description:
 */
public class DateViewHolder extends BaseViewHolder<Story> {

    private TextView textView;

    public DateViewHolder(Context mContext, ViewGroup parent) {
        super(mContext, R.layout.daily_holder_date, parent);
        textView = itemView.findViewById(R.id.textView);
    }

    @Override public void bind(Story data) {
        textView.setText(data.date);
    }
}
