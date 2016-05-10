package com.baiiu.zhihudaily.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.baiiu.zhihudaily.R;
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

  private View.OnClickListener mListener;

  public TopicAdapter(Context context, List<TopStory> list, View.OnClickListener listener) {
    super(context, list);
    this.mListener = listener;
  }

  @Override public View onCreateView(int position) {
    ImageView imageView = new ImageView(mContext);
    TopStory topStory = list.get(position);

    imageView.setId(R.id.item_topic_news);
    imageView.setTag(R.id.item_topic_news, topStory.id);
    imageView.setOnClickListener(mListener);

    Glide.with(mContext).load(topStory.image).centerCrop().into(imageView);
    return imageView;
  }
}
