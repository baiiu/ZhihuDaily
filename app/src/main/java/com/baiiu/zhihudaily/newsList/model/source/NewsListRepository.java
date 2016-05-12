package com.baiiu.zhihudaily.newsList.model.source;

import android.text.TextUtils;
import com.baiiu.zhihudaily.newsList.model.Daily;
import com.baiiu.zhihudaily.newsList.model.Story;
import com.baiiu.zhihudaily.newsList.model.source.local.NewsListLocalSource;
import com.baiiu.zhihudaily.newsList.model.source.remote.NewsListRemoteSource;
import com.baiiu.zhihudaily.util.CommonUtil;
import com.baiiu.zhihudaily.util.Constant;
import com.baiiu.zhihudaily.util.PreferenceUtil;
import com.baiiu.zhihudaily.util.ReadedListUtil;
import com.baiiu.zhihudaily.util.net.util.HttpNetUtil;
import java.util.List;
import java.util.Map;

/**
 * author: baiiu
 * date: on 16/5/11 10:43
 * description: NewsList数据管理类,控制从哪里取数据,并对数据进行处理.
 */
public class NewsListRepository implements INewsListDataSource {

  public static final String READ_LIST = "read_list";

  private NewsListLocalSource mNewsListLocalSource;
  private NewsListRemoteSource mNewsListRemoteSource;

  /**
   * 从网络加载数据
   */
  private boolean mFromRemote = true;

  private String mCurrentDate;

  private NewsListRepository() {
    //硬编码注入
    mNewsListLocalSource = new NewsListLocalSource();
    mNewsListRemoteSource = new NewsListRemoteSource();
  }

  public static NewsListRepository instance() {
    return new NewsListRepository();
  }

  @Override public void loadNewsList(String date, boolean loadMore, LoadNewsListCallback callback) {
    if (!loadMore) {
      //下拉刷新时,reset
      mCurrentDate = null;
    }

    if (TextUtils.isEmpty(mCurrentDate)) {
      mCurrentDate = PreferenceUtil.instance().get(Constant.LATEST_DATE, "");
    }

    loadList(mCurrentDate, loadMore, callback);
  }

  private void loadList(String date, final boolean loadMore, final LoadNewsListCallback callback) {

    if (mFromRemote) {

      mNewsListRemoteSource.loadNewsList(date, loadMore, new LoadNewsListCallback() {
        @Override public void onSuccess(Daily daily) {
          if (!loadMore) {
            PreferenceUtil.instance().put(Constant.LATEST_DATE, daily.date).commit();
          }

          mCurrentDate = daily.date;
          markRead(daily.stories);
          callback.onSuccess(daily);
        }

        @Override public void onFailure() {
          callback.onFailure();
        }
      });
    } else {

      mNewsListLocalSource.loadNewsList(date, loadMore, new LoadNewsListCallback() {
        @Override public void onSuccess(Daily daily) {
          mCurrentDate = daily.date;
          markRead(daily.stories);

          callback.onSuccess(daily);
        }

        @Override public void onFailure() {
          callback.onFailure();
        }
      });
    }
  }

  /**
   * 标记已读
   */
  private void markRead(List<Story> list) {
    Map<String, String> readedMap = ReadedListUtil.getReadedMap(READ_LIST);

    if (!CommonUtil.isEmpty(list)) {
      for (Story story : list) {
        story.isRead = readedMap.get(String.valueOf(story.id)) != null;
      }
    }
  }

  /**
   * 控制是否从远端拉去数据
   */
  public void refreshNewsList(boolean fromRemote) {
    this.mFromRemote = fromRemote && HttpNetUtil.isConnected();
  }
}
