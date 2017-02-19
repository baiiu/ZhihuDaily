package com.baiiu.zhihudaily.newsList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.baiiu.zhihudaily.base.BaseViewHolder;
import com.baiiu.zhihudaily.base.list.fragment.BaseRefreshLoadMoreAdapter;
import com.baiiu.zhihudaily.data.bean.Story;
import com.baiiu.zhihudaily.newsList.holder.DateViewHolder;
import com.baiiu.zhihudaily.newsList.holder.NewsViewHolder;
import com.baiiu.zhihudaily.newsList.holder.TopicViewHolder;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

/**
 * auther: baiiu
 * time: 17/2/19 19 14:50
 * description:
 */

public class DailyNewsAdapter extends BaseRefreshLoadMoreAdapter<Story> implements StickyRecyclerHeadersAdapter<DateViewHolder> {
    public static final int TYPE_TOPIC = 0;
    public static final int TYPE_NEWS = 1;

    private TopicViewHolder topicViewHolder;
    private View.OnClickListener mOnClickListener;

    public DailyNewsAdapter(Context context, View.OnClickListener listener) {
        super(context,true);
        this.mOnClickListener = listener;
    }

    @Override protected BaseViewHolder onCreateViewHolderInner(ViewGroup parent, int viewType) {
        return new NewsViewHolder(mContext, parent, mOnClickListener);
    }

    @Override public long getHeaderId(int position) {
        try {
            Story story = mList.get(position);
            return Long.parseLong(story.date);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override public DateViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new DateViewHolder(mContext, parent);
    }

    @Override public void onBindHeaderViewHolder(DateViewHolder holder, int position) {
        try {
            Story story = mList.get(position);
            holder.bind(story);
            //LogUtil.d(position +", " + story.toString());
        } catch (Exception e) {
            Story story = new Story();
            story.title = "title";
            holder.bind(story);
        }
    }

}
