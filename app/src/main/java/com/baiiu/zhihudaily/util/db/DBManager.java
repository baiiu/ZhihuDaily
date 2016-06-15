package com.baiiu.zhihudaily.util.db;

import android.database.sqlite.SQLiteDatabase;
import baiiu.greendao.gen.DaoMaster;
import baiiu.greendao.gen.DaoSession;
import baiiu.greendao.gen.SavedDailyDetail;
import baiiu.greendao.gen.SavedDailyDetailDao;
import baiiu.greendao.gen.SavedStory;
import baiiu.greendao.gen.SavedStoryDao;
import baiiu.greendao.gen.SavedTopStory;
import baiiu.greendao.gen.SavedTopStoryDao;
import com.baiiu.zhihudaily.newsDetail.model.DailyDetail;
import com.baiiu.zhihudaily.newsList.model.Story;
import com.baiiu.zhihudaily.newsList.model.TopStory;
import com.baiiu.zhihudaily.util.CommonUtil;
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
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(UIUtil.getContext(), DB_NAME, null);
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

    //=======================Story================================
    private SavedStoryDao getSavedStoryDao() {
        return daoSession.getSavedStoryDao();
    }

    public void saveStoryList(List<SavedStory> list) {
        if (CommonUtil.isEmpty(list)) {
            return;
        }

        getSavedStoryDao().insertOrReplaceInTx(list);
    }

    public List<Story> getStoryList(String date) {
        List<SavedStory> savedStories = getSavedStoryDao()
                .queryBuilder()
                .where(SavedStoryDao.Properties.Date.eq(date))
                .orderAsc(SavedStoryDao.Properties.Position)
                .list();
        return MappingConvertUtil.toStory(savedStories);
    }

    //========================TopStory=====================================
    private SavedTopStoryDao getSavedTopStoryDao() {
        return daoSession.getSavedTopStoryDao();
    }

    public void saveTopStoryList(List<TopStory> list) {
        if (CommonUtil.isEmpty(list)) {
            return;
        }

        getSavedTopStoryDao().deleteAll();
        getSavedTopStoryDao().insertOrReplaceInTx(MappingConvertUtil.toSavedTopStory(list));
    }

    public List<TopStory> getTopStoryList() {
        List<SavedTopStory> savedTopStories = getSavedTopStoryDao().loadAll();
        return MappingConvertUtil.toTopStory(savedTopStories);
    }

    //========================DailyDetail=====================================
    private SavedDailyDetailDao getDailyDetailDao() {
        return daoSession.getSavedDailyDetailDao();
    }

    public void saveDetailStory(DailyDetail dailyDetail) {
        getDailyDetailDao().insertOrReplace(MappingConvertUtil.toSavedDailyDetail(dailyDetail));
    }

    public DailyDetail getDetialStory(long id) {
        SavedDailyDetail unique = getDailyDetailDao()
                .queryBuilder()
                .where(SavedDailyDetailDao.Properties.Id.eq(id))
                .unique();

        return unique == null ? null : MappingConvertUtil.toDailyDetail(unique);
    }
}
