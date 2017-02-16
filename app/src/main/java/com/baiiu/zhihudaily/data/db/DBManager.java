package com.baiiu.zhihudaily.data.db;

import com.baiiu.zhihudaily.data.bean.DailyDetail;
import com.baiiu.zhihudaily.data.bean.SavedStory;
import com.baiiu.zhihudaily.data.bean.Story;
import com.baiiu.zhihudaily.data.bean.TopStory;
import com.baiiu.zhihudaily.data.util.CommonUtil;
import de.greenrobot.dao.AbstractDao;
import java.util.List;

/**
 * author: baiiu
 * date: on 16/4/7 14:31
 * description:
 * 数据库仅仅是提供基础操作，CRUD，不要做业务操作
 */
public class DBManager {
    private static final String DB_NAME = "daily-db";

    private static DBManager dbManager;

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


    public <T, K> void save(AbstractDao<T, K> dao, List<T> list) {
        dao.insertOrReplaceInTx(list);
    }

    public <T, K> List<T> query(AbstractDao<T, K> dao) {
        return dao.queryBuilder()
                .list();
    }


    //=======================Story================================
    public void saveStoryList(List<SavedStory> list) {
        if (CommonUtil.isEmpty(list)) {
            return;
        }

        //getSavedStoryDao().insertOrReplaceInTx(list);
    }

    public List<Story> getStoryList(String date) {
        return null;
    }

    //========================TopStory=====================================
    public void saveTopStoryList(List<TopStory> list) {
        if (CommonUtil.isEmpty(list)) {
            return;
        }

        //getSavedTopStoryDao().deleteAll();
        //getSavedTopStoryDao().insertOrReplaceInTx(MappingConvertUtil.toSavedTopStory(list));
    }

    public List<TopStory> getTopStoryList() {
        return null;
    }

    //========================DailyDetail=====================================
    public void saveDetailStory(DailyDetail dailyDetail) {

    }

    public DailyDetail getDetailStory(long id) {
        return null;
    }

}
