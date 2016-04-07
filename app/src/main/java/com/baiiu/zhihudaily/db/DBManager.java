package com.baiiu.zhihudaily.db;

import android.database.sqlite.SQLiteDatabase;
import baiiu.greendao.gen.DaoMaster;
import baiiu.greendao.gen.DaoSession;
import baiiu.greendao.gen.SavedStory;
import baiiu.greendao.gen.SavedStoryDao;
import baiiu.greendao.gen.SavedTopStory;
import baiiu.greendao.gen.SavedTopStoryDao;
import com.baiiu.zhihudaily.async.MappingConvertUtil;
import com.baiiu.zhihudaily.pojo.Story;
import com.baiiu.zhihudaily.pojo.TopStory;
import com.baiiu.zhihudaily.util.UIUtil;
import java.util.List;

/**
 * author: baiiu
 * date: on 16/4/7 14:31
 * description:
 */
public class DBManager {

  private static final String DB_NAME = "daily-db";

  private static DBManager dbManager;
  private DaoSession daoSession;
  private final SQLiteDatabase db;

  private DBManager() {
    DaoMaster.DevOpenHelper helper =
        new DaoMaster.DevOpenHelper(UIUtil.getContext(), DB_NAME, null);
    db = helper.getWritableDatabase();
    DaoMaster daoMaster = new DaoMaster(db);
    daoSession = daoMaster.newSession();
  }

  public static DBManager instance() {
    if (dbManager == null) {
      synchronized (DBManager.class) {
        if (dbManager == null) {
          dbManager = new DBManager();
        }
      }
    }

    return dbManager;
  }

  private SavedTopStoryDao getSavedTopStoryDao() {
    return daoSession.getSavedTopStoryDao();
  }

  private SavedStoryDao getSavedStoryDao() {
    return daoSession.getSavedStoryDao();
  }

  public void saveTopStoryList(List<SavedTopStory> list) {
    getSavedTopStoryDao().insertOrReplaceInTx(list);
  }

  public List<TopStory> getTopStoryList() {
    List<SavedTopStory> savedTopStories = getSavedTopStoryDao().loadAll();
    return MappingConvertUtil.toTopStory(savedTopStories);
  }

  public void saveStoryList(List<SavedStory> list) {
    getSavedStoryDao().insertOrReplaceInTx(list);
  }

  public List<Story> getStoryList(String date) {
    List<SavedStory> savedStories =
        getSavedStoryDao().queryBuilder().where(SavedStoryDao.Properties.Date.eq(date)).list();
    return MappingConvertUtil.toStory(savedStories);
  }
}
