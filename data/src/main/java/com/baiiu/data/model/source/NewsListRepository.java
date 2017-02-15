package com.baiiu.data.model.source;

import android.text.TextUtils;
import com.baiiu.data.model.Daily;
import com.baiiu.data.model.Story;
import com.baiiu.data.model.source.local.NewsListLocalSource;
import com.baiiu.data.model.source.remote.NewsListRemoteSource;
import com.baiiu.data.net.http.HttpNetUtil;
import com.baiiu.data.util.CommonUtil;
import com.baiiu.data.util.PreferenceUtil;
import com.baiiu.data.util.ReadedListUtil;
import java.util.List;
import java.util.Map;
import rx.Observable;

/**
 * author: baiiu
 * date: on 16/5/11 10:43
 * description: NewsList数据管理类,控制从哪里取数据,并对数据进行处理.
 */
@PerFragment
public class NewsListRepository implements INewsListDataSource {
    public static final String LATEST_DATE = "Latest_Date";


    public static final String READ_LIST = "read_list";

    private NewsListLocalSource mNewsListLocalSource;
    private NewsListRemoteSource mNewsListRemoteSource;

    /**
     * 从网络加载数据
     */
    private boolean mFromRemote = true;

    private String mCurrentDate;


    private Observable<Daily> mRemoteDaily;
    private Observable<Daily> mLocalDaily;


    @Inject public NewsListRepository(NewsListLocalSource localSource, NewsListRemoteSource remoteSource) {
        mNewsListLocalSource = localSource;
        mNewsListRemoteSource = remoteSource;
    }

    //public static NewsListRepository instance() {
    //    return new NewsListRepository();
    //}

    @Override public Observable<Daily> loadNewsList(String date, boolean refresh) {
        if (refresh) {
            //下拉刷新时,reset
            mCurrentDate = null;
        }

        if (TextUtils.isEmpty(mCurrentDate)) {
            mCurrentDate = PreferenceUtil.instance()
                    .get(LATEST_DATE, "");
        }

        Observable<Daily> observable;

        if (mFromRemote) {
            observable = mNewsListRemoteSource.loadNewsList(mCurrentDate, refresh)
                    .doOnNext(daily -> {
                        if (refresh) {
                            PreferenceUtil.instance()
                                    .put(LATEST_DATE, daily.date)
                                    .commit();
                        }
                    });

        } else {
            observable = mNewsListLocalSource.loadNewsList(mCurrentDate, refresh);
        }


        return observable
                //return Observable.concat(mLocalDaily, mRemoteDaily)
                //        .first(daily -> Daily.isAvailable())
                .filter(daily -> daily != null)
                .doOnNext(daily -> {
                    mCurrentDate = daily.date;
                    markRead(daily.stories);
                });
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
        Daily.setAvailable(!mFromRemote);
    }

}
