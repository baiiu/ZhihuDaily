package com.baiiu.zhihudaily.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.baiiu.zhihudaily.base.BasePagerAdapter;
import com.baiiu.zhihudaily.pojo.TopStory;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * author: baiiu
 * date: on 16/4/5 16:09
 * description:
 */
public class TopicAdapter extends BasePagerAdapter<TopStory> {

    public TopicAdapter(Context context, List<TopStory> list) {
        super(context, list);
    }

    @Override
    public View onCreateView(int position) {
        ImageView imageView = new ImageView(mContext);
        Glide.with(mContext).load(list.get(position).image).centerCrop().into(imageView);
        return imageView;
    }
}
