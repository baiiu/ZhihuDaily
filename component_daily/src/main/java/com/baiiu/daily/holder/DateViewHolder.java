package com.baiiu.daily.holder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import com.baiiu.common.base.BaseViewHolder;
import com.baiiu.daily.data.bean.Story;
import com.baiiu.daily.R;
import com.baiiu.daily.R2;

/**
 * author: baiiu
 * date: on 16/4/5 19:31
 * description:
 */
public class DateViewHolder extends BaseViewHolder<Story> {

    @BindView(R2.id.textView) TextView textView;

    public DateViewHolder(Context mContext, ViewGroup parent) {
        super(mContext, R.layout.daily_holder_date, parent);
    }

    @Override public void bind(Story data) {
        textView.setText(data.date);
    }
}
