package com.baiiu.zhihudaily.newsList.model.source.remote;

import com.baiiu.zhihudaily.newsList.model.Daily;
import com.baiiu.zhihudaily.newsList.model.Story;
import com.baiiu.zhihudaily.newsList.model.TopStory;
import com.baiiu.zhihudaily.newsList.model.source.INewsListDataSource;
import com.baiiu.zhihudaily.util.CommonUtil;
import com.baiiu.zhihudaily.util.async.MappingConvertUtil;
import com.baiiu.zhihudaily.util.async.TinyTaskManager;
import com.baiiu.zhihudaily.util.db.DBManager;
import com.baiiu.zhihudaily.util.net.DailyClient;
import com.baiiu.zhihudaily.util.net.http.RequestCallBack;
import java.util.List;

/**
 * author: baiiu
 * date: on 16/5/11 10:51
 * description:
 */
public class NewsListRemoteSource implements INewsListDataSource {

  @Override
  public void loadNewsList(String date, boolean loadMore, final LoadNewsListCallback callback) {
    if (loadMore) {
      DailyClient.getBeforeNews(date, new RequestCallBack<Daily>() {
        @Override public void onSuccess(Daily response) {
          callback.onSuccess(response);
          if (response != null) {
            saveStories(response.stories, response.date);
            saveTopStories(response.top_stories);
          }
        }

        @Override public void onFailure(int statusCode, String errorString) {
          callback.onFailure();
        }
      });
    } else {
      DailyClient.getLatestNews(new RequestCallBack<Daily>() {

        @Override public void onSuccess(Daily response) {
          callback.onSuccess(response);

          if (response != null) {
            saveStories(response.stories, response.date);
            saveTopStories(response.top_stories);
          }
        }

        @Override public void onFailure(int statusCode, String errorString) {
          callback.onFailure();
        }
      });
    }
  }

  private void saveTopStories(final List<TopStory> top_stories) {
    if (CommonUtil.isEmpty(top_stories)) {
      return;
    }

    TinyTaskManager.instance().post(new Runnable() {
      @Override public void run() {
        DBManager.instance().saveTopStoryList(top_stories);
      }
    });
  }

  private void saveStories(final List<Story> stories, final String date) {
    if (CommonUtil.isEmpty(stories)) {
      return;
    }

    TinyTaskManager.instance().post(new Runnable() {
      @Override public void run() {
        DBManager.instance().saveStoryList(MappingConvertUtil.toSavedStory(stories, date));
      }
    });
  }
}
