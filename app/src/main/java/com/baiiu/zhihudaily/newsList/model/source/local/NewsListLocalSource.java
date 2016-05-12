package com.baiiu.zhihudaily.newsList.model.source.local;

import com.baiiu.zhihudaily.newsList.model.Daily;
import com.baiiu.zhihudaily.newsList.model.Story;
import com.baiiu.zhihudaily.newsList.model.source.INewsListDataSource;
import com.baiiu.zhihudaily.util.DateUtil;
import com.baiiu.zhihudaily.util.UIUtil;
import com.baiiu.zhihudaily.util.async.TinyTaskManager;
import com.baiiu.zhihudaily.util.db.DBManager;
import java.util.List;

/**
 * author: baiiu
 * date: on 16/5/11 10:50
 * description:
 */
public class NewsListLocalSource implements INewsListDataSource {

  @Override public void loadNewsList(final String date, final boolean loadMore,
      final LoadNewsListCallback callback) {
    TinyTaskManager.instance().postAtFrontOfQueue(new Runnable() {
      @Override public void run() {

        String cursorDate = date;
        if (loadMore) {
          cursorDate = DateUtil.getYesterDayDate(cursorDate);
        }

        final Daily daily = new Daily();

        final List<Story> storyList;

        daily.top_stories = DBManager.instance().getTopStoryList();
        daily.date = cursorDate;

        storyList = DBManager.instance().getStoryList(cursorDate);
        daily.stories = storyList;

        UIUtil.runInMainThread(new Runnable() {
          @Override public void run() {
            callback.onSuccess(daily);
          }
        });
      }
    });
  }
}
