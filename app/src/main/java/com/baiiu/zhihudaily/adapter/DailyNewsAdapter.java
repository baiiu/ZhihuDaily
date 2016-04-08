package com.baiiu.zhihudaily.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.baiiu.zhihudaily.async.MappingConvertUtil;
import com.baiiu.zhihudaily.async.TinyTaskManager;
import com.baiiu.zhihudaily.base.BaseViewHolder;
import com.baiiu.zhihudaily.db.DBManager;
import com.baiiu.zhihudaily.pojo.Daily;
import com.baiiu.zhihudaily.pojo.Story;
import com.baiiu.zhihudaily.pojo.TopStory;
import com.baiiu.zhihudaily.ui.activity.MainActivity;
import com.baiiu.zhihudaily.ui.holder.DateViewHolder;
import com.baiiu.zhihudaily.ui.holder.FooterViewHolder;
import com.baiiu.zhihudaily.ui.holder.NewsViewHolder;
import com.baiiu.zhihudaily.ui.holder.TopicViewHolder;
import com.baiiu.zhihudaily.util.CommonUtil;
import com.baiiu.zhihudaily.util.ReadedListUtil;
import java.util.List;
import java.util.Map;

/**
 * author: baiiu
 * date: on 16/4/5 14:44
 * description:
 */
public class DailyNewsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

  public static final int TYPE_TOPIC = 0;
  public static final int TYPE_NEWS = 1;
  public static final int TYPE_DATE = 2;
  public static final int TYPE_FOOTER = 3;
  private final View.OnClickListener mOnClickListener;

  private Context mContext;
  private FooterViewHolder footerViewHolder;
  private TopicViewHolder topicViewHolder;

  private List<Story> stories;
  private List<TopStory> topStories;

  public DailyNewsAdapter(Context context, View.OnClickListener onClickListener) {
    this.mContext = context;
    this.mOnClickListener = onClickListener;
  }

  public void setDaily(Daily daily, boolean lastest) {
    setDaily(daily, lastest, true);
  }

  public void setDaily(Daily daily, boolean lastest, boolean needSave) {
    List<Story> hereStories = daily.stories;
    bindFooter(hereStories);

    //标记已读
    Map<String, String> readedMap =
        ReadedListUtil.getReadedMap(((MainActivity) mContext).volleyTag);

    if (!CommonUtil.isEmpty(hereStories)) {
      for (Story story : hereStories) {
        story.isRead = readedMap.get(String.valueOf(story.id)) != null;
      }
    }

    //存储当前的story
    if (needSave) {
      saveStories(hereStories, daily.date);
    }

    if (CommonUtil.isEmpty(hereStories)) {
      return;
    }

    if (lastest) {
      this.topStories = daily.top_stories;
      this.stories = hereStories;
      notifyDataSetChanged();

      if (needSave) {
        saveTopStories(this.topStories);
      }
    } else {
      //添加Date分割线Story
      Story story = new Story();
      story.mType = TYPE_DATE;
      story.title = daily.date;
      this.stories.add(story);

      this.stories.addAll(hereStories);

      int startIndex = stories.size() - hereStories.size();
      notifyItemRangeInserted(topStories == null ? --startIndex : startIndex,
          hereStories.size() + 1);
    }
  }

  @Override public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    BaseViewHolder viewHolder = null;
    switch (viewType) {
      case TYPE_TOPIC:
        if (topicViewHolder == null) {
          topicViewHolder = new TopicViewHolder(mContext, parent, mOnClickListener);
        }
        viewHolder = topicViewHolder;
        break;
      case TYPE_NEWS:
        viewHolder = new NewsViewHolder(mContext, parent, mOnClickListener);
        break;
      case TYPE_DATE:
        viewHolder = new DateViewHolder(mContext, parent);
        break;
      case TYPE_FOOTER:
        viewHolder = getFooterHolder();
        break;
    }

    return viewHolder;
  }

  @Override public void onBindViewHolder(BaseViewHolder holder, int position) {
    int type = getItemViewType(position);
    switch (type) {
      case TYPE_TOPIC:
        holder.bind(topStories);
        break;
      case TYPE_NEWS:
      case TYPE_DATE:
        holder.bind(stories.get(CommonUtil.isEmpty(topStories) ? position : --position));
        break;
      case TYPE_FOOTER:
        if (!CommonUtil.isEmpty(stories)) {
          if (getFooterHolder().isHasLoadMore()) {
            dispatchLoadMore();
          }
        }
        break;
    }
  }

  @Override public void onViewAttachedToWindow(BaseViewHolder holder) {
    super.onViewAttachedToWindow(holder);
    super.onViewDetachedFromWindow(holder);
    if (holder instanceof TopicViewHolder) {
      ((TopicViewHolder) holder).start();
    }
  }

  @Override public void onViewDetachedFromWindow(BaseViewHolder holder) {
    super.onViewDetachedFromWindow(holder);
    if (holder instanceof TopicViewHolder) {
      ((TopicViewHolder) holder).stop();
    }
  }

  private void dispatchLoadMore() {
    ((MainActivity) mContext).loadMore();
  }

  @Override public int getItemViewType(int position) {
    if (position == getItemCount() - 1) {
      return TYPE_FOOTER;
    }

    if (CommonUtil.isEmpty(topStories)) {
      return stories.get(position).mType;
    }

    if (position == 0) {
      return TYPE_TOPIC;
    }

    return stories.get(--position).mType;
  }

  @Override public int getItemCount() {
    if (CommonUtil.isEmpty(stories)) {
      //未赋值时,不用添加footer
      return 0;
    }

    return 1 + stories.size() + (CommonUtil.isEmpty(topStories) ? 0 : 1);//添加footer
  }

  public FooterViewHolder getFooterHolder() {
    if (footerViewHolder == null) {
      footerViewHolder = new FooterViewHolder(mContext);
    }
    return footerViewHolder;
  }

  public void bindFooter(List<Story> list) {
    if (CommonUtil.isEmpty(list)) {
      getFooterHolder().bind(FooterViewHolder.ERROR);
    } else {
      getFooterHolder().bind(FooterViewHolder.HAS_MORE);
    }
  }

  //=============存储==================
  private void saveTopStories(final List<TopStory> top_stories) {
    TinyTaskManager.instance().post(new Runnable() {
      @Override public void run() {
        DBManager.instance().saveTopStoryList(top_stories);
      }
    });
  }

  private void saveStories(final List<Story> stories, final String date) {
    TinyTaskManager.instance().post(new Runnable() {
      @Override public void run() {
        DBManager.instance().saveStoryList(MappingConvertUtil.toSavedStory(stories, date));
      }
    });
  }
}
