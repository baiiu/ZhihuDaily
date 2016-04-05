package com.baiiu.zhihudaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.baiiu.zhihudaily.base.BaseViewHolder;
import com.baiiu.zhihudaily.pojo.Daily;
import com.baiiu.zhihudaily.pojo.Story;
import com.baiiu.zhihudaily.pojo.TopStory;
import com.baiiu.zhihudaily.ui.holder.NewsViewHolder;
import com.baiiu.zhihudaily.ui.holder.TopicViewHolder;
import com.baiiu.zhihudaily.util.CommonUtil;

import java.util.List;

/**
 * author: baiiu
 * date: on 16/4/5 14:44
 * description:
 */
public class DailyNewsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final int TYPE_TOPIC = 0;
    private static final int TYPE_NEWS = 1;

    private Context context;
    private List<Story> stories;
    private List<TopStory> topStories;

    public DailyNewsAdapter(Context context) {
        this.context = context;
    }

    public void setDaily(Daily daily) {
        this.stories = daily.stories;
        this.topStories = daily.top_stories;

        notifyItemRangeChanged(0, getItemCount());
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_TOPIC:
                viewHolder = new TopicViewHolder(context, parent);
                break;
            case TYPE_NEWS:
                viewHolder = new NewsViewHolder(context, parent);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_TOPIC:
                holder.bind(topStories);
                break;
            case TYPE_NEWS:
                holder.bind(stories.get(CommonUtil.isEmpty(topStories) ? position : --position));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (CommonUtil.isEmpty(topStories)) {
            return TYPE_NEWS;
        }

//        return TYPE_NEWS;
        return position == 0 ? TYPE_TOPIC : TYPE_NEWS;

    }

    @Override
    public int getItemCount() {
        int storiesCount = stories == null ? 0 : stories.size();
        return CommonUtil.isEmpty(topStories) ? storiesCount : storiesCount + 1;
    }

}
