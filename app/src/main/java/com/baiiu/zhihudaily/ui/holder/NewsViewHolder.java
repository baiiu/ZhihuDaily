package com.baiiu.zhihudaily.ui.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.base.BaseViewHolder;
import com.baiiu.zhihudaily.pojo.Story;
import com.bumptech.glide.Glide;

import butterknife.Bind;

/**
 * author: baiiu
 * date: on 16/4/5 16:23
 * description:
 */
public class NewsViewHolder extends BaseViewHolder<Story> {

    @Bind(R.id.imageView)
    ImageView imageView;

    @Bind(R.id.textView)
    TextView textView;


    public NewsViewHolder(Context context, ViewGroup parent, View.OnClickListener listener) {
        super(context, R.layout.holder_news, parent, listener);
        itemView.setId(R.id.item_news);
    }

    @Override
    public void bind(Story data) {
        itemView.setTag(data.id);

        Glide.with(mContext)
                .load(data.images.get(0))
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(imageView);

        textView.setText(data.title);
    }
}
