package com.baiiu.zhihudaily.newsDetail.model;

import com.baiiu.zhihudaily.util.UIUtil;
import com.baiiu.zhihudaily.util.async.TinyTaskManager;
import com.baiiu.zhihudaily.util.db.DBManager;
import com.baiiu.zhihudaily.util.net.DailyClient;
import com.baiiu.zhihudaily.util.net.http.RequestCallBack;
import com.baiiu.zhihudaily.util.net.util.HttpNetUtil;

/**
 * author: baiiu
 * date: on 16/5/12 15:47
 * description: 该逻辑稍微简单些,就写在一块了
 */
public class NewsDetailRepository implements INewsDetailRepository {

  @Override public void loadNewsDetail(long id, LoadNewsDetailCallback callback) {
    if (HttpNetUtil.isConnected()) {
      //加载并存入缓存
      loadFromRemote(id, callback);
    } else {
      //从缓存中取
      loadFromLocal(id, callback);
    }
  }

  private void loadFromRemote(long id, final LoadNewsDetailCallback callback) {
    DailyClient.getNewsDetail(id, new RequestCallBack<DailyDetail>() {
      @Override public void onSuccess(DailyDetail response) {
        callback.onSuccess(response);
        saveData(response);
      }

      @Override public void onFailure(int statusCode, String errorString) {
        callback.onFailure();
      }
    });
  }

  private void saveData(final DailyDetail response) {
    TinyTaskManager.instance().post(new Runnable() {
      @Override public void run() {
        DBManager.instance().saveDetailStory(response);
      }
    });
  }

  private void loadFromLocal(final long id, final LoadNewsDetailCallback callback) {
    TinyTaskManager.instance().postAtFrontOfQueue(new Runnable() {
      @Override public void run() {
        final DailyDetail dailyDetail = DBManager.instance().getDetialStory(id);

        UIUtil.runInMainThread(new Runnable() {
          @Override public void run() {
            if (dailyDetail == null) {
              callback.onFailure();
            } else {
              callback.onSuccess(dailyDetail);
            }
          }
        });
      }
    });
  }
}
