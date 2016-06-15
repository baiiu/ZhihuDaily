package com.baiiu.zhihudaily.newsDetail.model;

import com.baiiu.zhihudaily.util.net.http.HttpNetUtil;
import com.baiiu.zhihudaily.util.db.DBManager;
import com.baiiu.zhihudaily.util.net.ApiFactory;
import rx.Observable;

/**
 * author: baiiu
 * date: on 16/5/12 15:47
 * description: 该逻辑稍微简单些,就写在一块了
 */
public class NewsDetailRepository implements INewsDetailRepository {
    @Override public Observable<DailyDetail> loadNewsDetail(long id) {

        if (HttpNetUtil.isConnected()) {
            //加载并存入缓存
            return loadFromRemote(id);
        } else {
            //从缓存中取
            return loadFromLocal(id);
        }
    }

    private Observable<DailyDetail> loadFromRemote(long id) {
        return ApiFactory.getDailyAPI()
                .newsDetail(id)
                .doOnNext(this::saveData);
    }

    private void saveData(DailyDetail response) {
        DBManager.instance()
                .saveDetailStory(response);
    }

    private Observable<DailyDetail> loadFromLocal(final long id) {
        return Observable.just(id)
                .flatMap(aLong -> Observable.just(DBManager.instance()
                        .getDetialStory(id)));
    }

}
