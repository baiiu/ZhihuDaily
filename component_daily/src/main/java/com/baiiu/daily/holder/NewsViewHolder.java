package com.baiiu.daily.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import com.baiiu.common.base.BaseViewHolder;
import com.baiiu.common.util.CommonUtil;
import com.baiiu.common.util.ReadedListUtil;
import com.baiiu.daily.data.bean.Story;
import com.baiiu.daily.R;
import com.baiiu.daily.R2;
import com.bumptech.glide.Glide;

/**
 * author: baiiu
 * date: on 16/4/5 16:23
 * description:
 */
public class NewsViewHolder extends BaseViewHolder<Story> {

    @BindView(R2.id.imageView) ImageView imageView;
    public @BindView(R2.id.textView) TextView textView;

    public Story mStroy;

    public NewsViewHolder(Context context, ViewGroup parent, View.OnClickListener listener) {
        super(context, R.layout.daily_holder_news, parent, listener);
        itemView.setId(R.id.daily_item_news);
    }

    @Override public void bind(Story data) {
        itemView.setTag(this);
        mStroy = data;

        String url = null;
        if (!CommonUtil.isEmpty(data.images)) {
            url = data.images.get(0);
        }

        Glide.with(mContext)
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(imageView);

        textView.setText(data.title);
        ReadedListUtil.setTextColor(textView, data.isRead);
    }
}
