package com.baiiu.zhihudaily.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import butterknife.Bind;
import com.baiiu.tsnackbar.Prompt;
import com.baiiu.tsnackbar.TSnackbar;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily._interface.IRefreshLoadMore;
import com.baiiu.zhihudaily.async.MappingConvertUtil;
import com.baiiu.zhihudaily.async.TinyTaskManager;
import com.baiiu.zhihudaily.base.BaseFragment;
import com.baiiu.zhihudaily.db.DBManager;
import com.baiiu.zhihudaily.net.DailyClient;
import com.baiiu.zhihudaily.net.http.RequestCallBack;
import com.baiiu.zhihudaily.net.util.HttpNetUtil;
import com.baiiu.zhihudaily.pojo.Daily;
import com.baiiu.zhihudaily.pojo.Story;
import com.baiiu.zhihudaily.pojo.TopStory;
import com.baiiu.zhihudaily.ui.activity.MainActivity;
import com.baiiu.zhihudaily.ui.activity.NewsDetailActivity;
import com.baiiu.zhihudaily.ui.adapter.DailyNewsAdapter;
import com.baiiu.zhihudaily.ui.holder.NewsViewHolder;
import com.baiiu.zhihudaily.util.CommonUtil;
import com.baiiu.zhihudaily.util.Constant;
import com.baiiu.zhihudaily.util.DateUtil;
import com.baiiu.zhihudaily.util.PreferenceUtil;
import com.baiiu.zhihudaily.util.ReadedListUtil;
import java.util.List;
import java.util.Map;

public class MainFragment extends BaseFragment implements IRefreshLoadMore, View.OnClickListener {

  public static MainFragment instance() {
    return new MainFragment();
  }

  @Bind(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
  @Bind(R.id.recyclerView) RecyclerView recyclerView;

  private DailyNewsAdapter dailyNewsAdapter;
  private String mCurrentDate;

  @Override public int provideLayoutId() {
    return R.layout.fragment_main;
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    refreshLayout.setOnRefreshListener(this);
    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    dailyNewsAdapter = new DailyNewsAdapter(mContext, this,this);
    recyclerView.setAdapter(dailyNewsAdapter);

    getActivity().findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        recyclerView.smoothScrollToPosition(0);
      }
    });

    loadFromLocal(true);
  }

  @Override public void onRefresh() {
    mCurrentDate = "";

    if (HttpNetUtil.isConnected()) {
      loadData();
    } else {
      refreshLayout.setRefreshing(false);
      TSnackbar.make(refreshLayout, "网络错误", Prompt.ERROR).show();

      if (CommonUtil.isEmpty(dailyNewsAdapter.getStories())) {
        //历史数据都没有,展示错误页面
        dailyNewsAdapter.setError(true);
        dailyNewsAdapter.notifyDataSetChanged();
      }
    }
  }

  private void loadData() {
    DailyClient.getLatestNews(volleyTag, dailyRequest);
  }

  @Override public void loadMore() {
    if (HttpNetUtil.isConnected()) {
      DailyClient.getBeforeNews(volleyTag, mCurrentDate, dailyRequest);
    } else {
      loadFromLocal(false);
    }
  }

  private void loadFromLocal(final boolean latest) {
    TinyTaskManager.instance().postAtFrontOfQueue(new Runnable() {
      @Override public void run() {
        final Daily daily = new Daily();

        List<Story> storyList;

        String date = PreferenceUtil.instance().get(Constant.LATEST_DATE, "");

        if (latest) {
          daily.top_stories = DBManager.instance().getTopStoryList();
          daily.date = date;
          storyList = DBManager.instance().getStoryList(date);
        } else {
          if (TextUtils.isEmpty(mCurrentDate)) {
            mCurrentDate = date;
          }

          String yesterDayDate = DateUtil.getYesterDayDate(mCurrentDate);
          daily.date = yesterDayDate;
          storyList = DBManager.instance().getStoryList(yesterDayDate);
        }

        daily.stories = storyList;

        ((MainActivity) mContext).runOnUiThread(new Runnable() {
          @Override public void run() {
            recyclerView.postDelayed(new Runnable() {
              @Override public void run() {
                setDaily(daily, latest, true);

                if (latest) {
                  if (CommonUtil.isEmpty(daily.stories)) {
                    //缓存数据为空
                    onRefresh();
                  } else {
                    recyclerView.postDelayed(new Runnable() {
                      @Override public void run() {
                        refreshLayout.setRefreshing(true);
                        onRefresh();
                      }
                    }, 1000);
                  }
                }
              }
            }, 500);
          }
        });
      }
    });
  }

  private RequestCallBack<Daily> dailyRequest = new RequestCallBack<Daily>() {

    @Override public void onSuccess(Daily response) {
      if (TextUtils.isEmpty(mCurrentDate)) {
        refreshLayout.setRefreshing(false);

        setDaily(response, true);

        //存储最新日期
        PreferenceUtil.instance().put(Constant.LATEST_DATE, response.date).commit();
      } else {
        setDaily(response, false);
      }

      mCurrentDate = response.date;
    }

    @Override public void onFailure(int statusCode, String errorString) {
      if (TextUtils.isEmpty(mCurrentDate)) {
        refreshLayout.setRefreshing(false);

        /*
         * 第一次,若是网络错误,展示错误页面
         */

        if (dailyNewsAdapter.getItemCount() == 0) {
          //历史数据也没有
          dailyNewsAdapter.setError(true);
        } else {
          TSnackbar.make(refreshLayout, "网络错误", Prompt.ERROR).show();
        }
      } else {
        dailyNewsAdapter.bindFooter(null);
      }
    }
  };

  @Override public void onClick(View v) {
    long id = 0;
    switch (v.getId()) {
      case R.id.item_news:
        NewsViewHolder holder = (NewsViewHolder) v.getTag();
        id = holder.mStroy.id;
        startActivity(NewsDetailActivity.instance(mContext, holder.mStroy.id));

        holder.mStroy.isRead = true;
        dailyNewsAdapter.notifyItemChanged(holder.getAdapterPosition());
        break;
      case R.id.item_topic_news:
        id = (long) v.getTag(R.id.item_topic_news);
        startActivity(NewsDetailActivity.instance(mContext, id));
        break;
    }

    if (id != 0) {
      ReadedListUtil.saveToReadedList(volleyTag, String.valueOf(id));
    }
  }

  //==================== 操作数据 ====================================
  public void setDaily(Daily daily, boolean lastest) {
    setDaily(daily, lastest, false);
  }

  /**
   * @param lastest 来自下拉刷新的数据
   * @param fromLocal 来自于数据库的数据不需要存储
   */
  public void setDaily(Daily daily, boolean lastest, boolean fromLocal) {
    List<Story> hereStories = daily.stories;
    dailyNewsAdapter.bindFooter(hereStories, fromLocal);

    /*
     * 标记已读
     */
    Map<String, String> readedMap = ReadedListUtil.getReadedMap(volleyTag);

    if (!CommonUtil.isEmpty(hereStories)) {
      for (Story story : hereStories) {
        story.isRead = readedMap.get(String.valueOf(story.id)) != null;
      }
    }

    /*
     * 存储当前的story
     */
    mCurrentDate = daily.date;
    if (!fromLocal) {
      saveStories(hereStories, daily.date);
    }

    if (lastest) {
      /*
       *下拉刷新
       */
      dailyNewsAdapter.setError(false);
      dailyNewsAdapter.setEmpty(false);

      if (CommonUtil.isEmpty(hereStories)) {
        /*
         * 数据为空处理
         */
        if (fromLocal) {
          //缓存数据为空,即第一次加载的时候,就是点进来的时候.
          //上面已经处理.此处不必在处理.可以删掉此段代码.
          return;
        }

        TSnackbar.make(refreshLayout, "没有新数据", Prompt.SUCCESS).show();

        if (CommonUtil.isEmpty(dailyNewsAdapter.getStories())) {
          //历史数据都没有,展现空页面
          dailyNewsAdapter.setEmpty(true);
          dailyNewsAdapter.notifyDataSetChanged();
        }
      } else {
        /*
         * 数据不为空,直接添加到当前
         */
        dailyNewsAdapter.setLoading(false);
        dailyNewsAdapter.setDaily(daily, true);
      }

      if (!fromLocal) {
        saveTopStories(daily.top_stories);
      }
    } else {
      /*
       *上拉加载
       */
      if (CommonUtil.isEmpty(daily.stories)) {
        return;
      }

      dailyNewsAdapter.setDaily(daily, false);
    }
  }

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
