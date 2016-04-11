package com.baiiu.zhihudaily.ui.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.Bind;
import butterknife.OnClick;
import com.baiiu.tsnackbar.Prompt;
import com.baiiu.tsnackbar.TSnackbar;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.async.MappingConvertUtil;
import com.baiiu.zhihudaily.async.TinyTaskManager;
import com.baiiu.zhihudaily.base.BaseActivity;
import com.baiiu.zhihudaily.db.DBManager;
import com.baiiu.zhihudaily.net.DailyClient;
import com.baiiu.zhihudaily.net.http.NetWorkReceiver;
import com.baiiu.zhihudaily.net.http.RequestCallBack;
import com.baiiu.zhihudaily.net.util.HttpNetUtil;
import com.baiiu.zhihudaily.pojo.Daily;
import com.baiiu.zhihudaily.pojo.Story;
import com.baiiu.zhihudaily.pojo.TopStory;
import com.baiiu.zhihudaily.ui.adapter.DailyNewsAdapter;
import com.baiiu.zhihudaily.ui.holder.NewsViewHolder;
import com.baiiu.zhihudaily.util.CommonUtil;
import com.baiiu.zhihudaily.util.Constant;
import com.baiiu.zhihudaily.util.DateUtil;
import com.baiiu.zhihudaily.util.PreferenceUtil;
import com.baiiu.zhihudaily.util.ReadedListUtil;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity
    implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

  @Bind(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
  @Bind(R.id.recyclerView) RecyclerView recyclerView;

  private DailyNewsAdapter dailyNewsAdapter;
  private String mCurrentDate;

  @Override public int provideLayoutId() {
    return R.layout.activity_main;
  }

  @Override protected void initOnCreate(Bundle savedInstanceState) {
    initBroadCast();

    refreshLayout.setOnRefreshListener(this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    dailyNewsAdapter = new DailyNewsAdapter(this, this);
    recyclerView.setAdapter(dailyNewsAdapter);

    loadFromLocal(true);
  }

  @Override public void onRefresh() {
    mCurrentDate = "";
    loadData();
  }

  private void loadData() {
    DailyClient.getLatestNews(volleyTag, dailyRequest);
  }

  public void loadMore() {
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

        runOnUiThread(new Runnable() {
          @Override public void run() {
            recyclerView.postDelayed(new Runnable() {
              @Override public void run() {
                setDaily(daily, latest, false);

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
        startActivity(NewsDetailActivity.instance(this, holder.mStroy.id));

        holder.mStroy.isRead = true;
        dailyNewsAdapter.notifyItemChanged(holder.getAdapterPosition());
        break;
      case R.id.item_topic_news:
        id = (long) v.getTag(R.id.item_topic_news);
        startActivity(NewsDetailActivity.instance(this, id));
        break;
    }

    if (id != 0) {
      ReadedListUtil.saveToReadedList(volleyTag, String.valueOf(id));
    }
  }

  //=====================Menu===================================

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private NetWorkReceiver netWorkReceiver;

  //=====================网络=====================================
  private void initBroadCast() {
    IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    netWorkReceiver = new NetWorkReceiver();
    registerReceiver(netWorkReceiver, filter);
  }

  @Override protected void onDestroy() {
    unregisterReceiver(netWorkReceiver);
    super.onDestroy();
  }

  @OnClick(R.id.fab) public void onClick() {
    Snackbar.make(refreshLayout, "Replace with your own action", Snackbar.LENGTH_LONG)
        .setAction("Action", null)
        .show();
  }

  //==================== 操作数据 ====================================
  public void setDaily(Daily daily, boolean lastest) {
    setDaily(daily, lastest, true);
  }

  /**
   * @param lastest 来自下拉刷新的数据
   * @param needSave 来自于数据库的数据不需要存储
   */
  public void setDaily(Daily daily, boolean lastest, boolean needSave) {
    List<Story> hereStories = daily.stories;
    dailyNewsAdapter.bindFooter(hereStories);

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
    if (needSave) {
      saveStories(hereStories, daily.date);
    }

    if (lastest) {
      /*
       *下拉刷新
       */
      dailyNewsAdapter.setError(false);

      if (!CommonUtil.isEmpty(hereStories)) {
        dailyNewsAdapter.setLoading(false);
        dailyNewsAdapter.setDaily(daily, true);
      } else {
        if (!needSave) {
          return;
        }
        /*
         * 判断是空页面还是错误页面
         */
        if (HttpNetUtil.isConnected()) {
          dailyNewsAdapter.setEmpty(true);
        } else {
          dailyNewsAdapter.setError(true);
        }

        dailyNewsAdapter.notifyDataSetChanged();
      }

      //if (CommonUtil.isEmpty(hereStories) && dailyNewsAdapter.getItemCount() == 0) {
      //  //数据为空,并且历史数据也为空
      //  dailyNewsAdapter.setEmpty(true);
      //  dailyNewsAdapter.notifyDataSetChanged();
      //} else {
      //  TSnackbar.make(refreshLayout, "刷新数据失败", Prompt.ERROR).show();
      //}

      if (needSave) {
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
