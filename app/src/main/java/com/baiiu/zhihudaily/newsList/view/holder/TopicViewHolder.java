package com.baiiu.zhihudaily.newsList.view.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.newsList.model.TopStory;
import com.baiiu.zhihudaily.newsList.view.TopicAdapter;
import com.baiiu.zhihudaily.view.base.BaseViewHolder;
import com.baiiu.zhihudaily.view.widget.indicator._interface.IPageIndicator;
import com.baiiu.zhihudaily.view.widget.loopvp.LoopViewPager;
import java.util.List;

/**
 * author: baiiu
 * date: on 16/4/5 15:53
 * description:
 */
public class TopicViewHolder extends BaseViewHolder<List<TopStory>> {

    @BindView(R.id.loopViewPager) LoopViewPager viewPager;
    @BindView(R.id.indicator) IPageIndicator indicator;
    private TopicAdapter topicAdapter;

    public TopicViewHolder(Context context, ViewGroup parent, View.OnClickListener listener) {
        super(context, R.layout.holder_topic, parent, listener, false);
    }

    @Override public void bind(List<TopStory> data) {
        if (topicAdapter == null) {
            topicAdapter = new TopicAdapter(mContext, data, mListener);
            viewPager.setAdapter(topicAdapter);
            viewPager.startAutoScroll();
            indicator.setViewPager(viewPager);
        }
    }

    public void stop() {
        if (viewPager != null) {
            viewPager.stopAutoScroll();
        }
    }

    public void start() {
        if (viewPager != null) {
            viewPager.startAutoScroll();
        }
    }
}
