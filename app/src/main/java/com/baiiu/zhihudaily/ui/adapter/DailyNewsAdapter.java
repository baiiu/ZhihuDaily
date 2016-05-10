package com.baiiu.zhihudaily.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.baiiu.zhihudaily.base.BaseViewHolder;
import com.baiiu.zhihudaily.pojo.Daily;
import com.baiiu.zhihudaily.pojo.Story;
import com.baiiu.zhihudaily.pojo.TopStory;
import com.baiiu.zhihudaily.ui.activity.MainActivity;
import com.baiiu.zhihudaily.ui.holder.DateViewHolder;
import com.baiiu.zhihudaily.ui.holder.EmptyViewHolder;
import com.baiiu.zhihudaily.ui.holder.ErrorViewHolder;
import com.baiiu.zhihudaily.ui.holder.FooterViewHolder;
import com.baiiu.zhihudaily.ui.holder.LoadingViewHolder;
import com.baiiu.zhihudaily.ui.holder.NewsViewHolder;
import com.baiiu.zhihudaily.ui.holder.TopicViewHolder;
import com.baiiu.zhihudaily.util.CommonUtil;
import java.util.List;

/**
 * author: baiiu
 * date: on 16/4/5 14:44
 * description: 第一次set时默认展示Loading页面
 */
public class DailyNewsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

  public static final int TYPE_TOPIC = 0;
  public static final int TYPE_NEWS = 1;
  public static final int TYPE_DATE = 2;
  public static final int TYPE_FOOTER = 3;

  /**
   * 网络错误,并且本地没有存储数据.
   */
  private boolean isError = false;
  public static final int TYPE_ERROR = -1;

  /**
   * 网络良好,没有数据时
   */
  private boolean isEmpty = false;
  public static final int TYPE_EMPTY = -2;

  /**
   * 正在加载...<br />
   * <b>默认展示</b>
   */
  private boolean isLoading = true;
  public static final int TYPE_LOADING = -3;

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

  public void setError(boolean error) {
    this.isError = error;
    this.isLoading = false;
  }

  public void setEmpty(boolean empty) {
    this.isEmpty = empty;
    this.isLoading = false;
  }

  public void setLoading(boolean loading) {
    this.isLoading = loading;
  }

  public void setDaily(Daily daily, boolean latest) {
    if (daily == null) {
      return;
    }

    List<Story> hereStories = daily.stories;

    if (latest) {
      this.stories = hereStories;
      this.topStories = daily.top_stories;

      //// TODO: 16/4/12 可以删掉,添加顶部轮播图
      this.topStories = null;

      notifyDataSetChanged();
    } else {
      //添加分割线Date
      Story story = new Story();
      story.mType = DailyNewsAdapter.TYPE_DATE;
      story.title = daily.date;
      this.stories.add(story);

      this.stories.addAll(hereStories);

      int startIndex = this.stories.size() - hereStories.size();

      notifyItemRangeInserted(topStories == null ? --startIndex : startIndex,
          hereStories.size() + 1);
    }
  }

  public List<Story> getStories() {
    return stories;
  }

  @Override public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    BaseViewHolder viewHolder = null;
    switch (viewType) {
      case TYPE_TOPIC:
        if (topicViewHolder == null) {
          //缓存起来
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
      case TYPE_LOADING:
        viewHolder = new LoadingViewHolder(mContext, parent);
        break;
      case TYPE_EMPTY:
        viewHolder = new EmptyViewHolder(mContext, parent);
        break;
      case TYPE_ERROR:
        viewHolder = new ErrorViewHolder(mContext, parent);
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
      case TYPE_ERROR:
        break;
      case TYPE_EMPTY:
        break;
    }
  }

  private void dispatchLoadMore() {
    ((MainActivity) mContext).loadMore();
  }

  @Override public void onViewAttachedToWindow(BaseViewHolder holder) {
    super.onViewAttachedToWindow(holder);
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

  @Override public int getItemViewType(int position) {
    if (isLoading) {
      return TYPE_LOADING;
    }

    if (isError) {
      return TYPE_ERROR;
    }

    if (isEmpty) {
      return TYPE_EMPTY;
    }

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
    if (isLoading || isError || isEmpty) {
      return 1;
    }

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
    bindFooter(list, false);
  }

  public void bindFooter(List<Story> list, boolean fromLocal) {
    if (CommonUtil.isEmpty(list)) {
      getFooterHolder().bind(fromLocal ? FooterViewHolder.NO_MORE : FooterViewHolder.ERROR);
    } else {
      getFooterHolder().bind(FooterViewHolder.HAS_MORE);
    }
  }
}
