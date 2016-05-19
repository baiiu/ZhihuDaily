package com.baiiu.zhihudaily.newsList.model.source.remote;

import com.baiiu.zhihudaily.newsList.model.Daily;
import com.baiiu.zhihudaily.newsList.model.Story;
import com.baiiu.zhihudaily.newsList.model.TopStory;
import com.baiiu.zhihudaily.newsList.model.source.INewsListDataSource;
import com.baiiu.zhihudaily.util.CommonUtil;
import com.baiiu.zhihudaily.util.async.MappingConvertUtil;
import com.baiiu.zhihudaily.util.async.TinyTaskManager;
import com.baiiu.zhihudaily.util.db.DBManager;
import com.baiiu.zhihudaily.util.net.ApiFactory;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * author: baiiu
 * date: on 16/5/11 10:51
 * description:
 */
public class NewsListRemoteSource implements INewsListDataSource {

  @Override
  public void loadNewsList(String date, boolean refresh, final LoadNewsListCallback callback) {
    if (refresh) {
      ApiFactory.getDailyAPI().newsLatest().enqueue(new Callback<Daily>() {
        @Override public void onResponse(Call<Daily> call, Response<Daily> response) {
          Daily body = response.body();
          callback.onSuccess(body);

          if (body != null) {
            saveStories(body.stories, body.date);
            saveTopStories(body.top_stories);
          }
        }

        @Override public void onFailure(Call<Daily> call, Throwable t) {
          callback.onFailure();
        }
      });
    } else {

      ApiFactory.getDailyAPI().newsBefore(date).enqueue(new Callback<Daily>() {
        @Override public void onResponse(Call<Daily> call, Response<Daily> response) {
          Daily body = response.body();
          callback.onSuccess(body);
          if (body != null) {
            saveStories(body.stories, body.date);
            saveTopStories(body.top_stories);
          }
        }

        @Override public void onFailure(Call<Daily> call, Throwable t) {
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
