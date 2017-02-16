package com.baiiu.zhihudaily.data.repository;

import android.text.TextUtils;
import com.baiiu.library.LogUtil;
import com.baiiu.zhihudaily.data.entity.Daily;
import com.baiiu.zhihudaily.data.entity.DailyDetail;
import com.baiiu.zhihudaily.data.entity.Story;
import com.baiiu.zhihudaily.data.net.retrofit.RetrofitClient;
import com.baiiu.zhihudaily.data.repository.api.NewsAPI;
import com.baiiu.zhihudaily.data.repository.api.NewsProviders;
import com.baiiu.zhihudaily.data.util.CommonUtil;
import com.baiiu.zhihudaily.data.util.PreferenceUtil;
import com.baiiu.zhihudaily.data.util.ReadedListUtil;
import com.baiiu.zhihudaily.data.util.StorageUtil;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import io.rx_cache.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import java.io.File;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.functions.Func1;

import static com.baiiu.zhihudaily.data.net.ApiConstants.CACHE_NAME;

/**
 * author: baiiu
 * date: on 17/2/16 14:51
 * description:
 */
public class NewsRepository implements INewsDataSource {
    public static final String LATEST_DATE = "Latest_Date";
    public static final String READ_LIST = "read_list";

    private final NewsProviders mNewsProviders;
    private final NewsAPI mNewsAPI;

    public NewsRepository() {
        File cacheDirectory = StorageUtil.getUnderCacheDirectory(CACHE_NAME, false);
        mNewsProviders = new RxCache.Builder().persistence(cacheDirectory, new GsonSpeaker())
                .using(NewsProviders.class);

        mNewsAPI = RetrofitClient.INSTANCE.create(NewsAPI.class);
    }

    private String mCurrentDate;

    /**
     * 控制是否从远端拉去数据
     */
    public void refreshNewsList(boolean fromRemote) {
        //this.mFromRemote = fromRemote && HttpNetUtil.isConnected();
    }

    @Override public Observable<Daily> loadNewsList(String date, boolean refresh) {
        if (refresh) {
            //下拉刷新时,reset
            mCurrentDate = "latest";
        }

        if (TextUtils.isEmpty(mCurrentDate)) {
            mCurrentDate = PreferenceUtil.instance()
                    .get(LATEST_DATE, "");
        }


        return mNewsProviders.newsList(refresh ? mNewsAPI.newsLatest() : mNewsAPI.newsBefore(mCurrentDate),
                                       new DynamicKey(mCurrentDate), new EvictDynamicKey(false))
                .map(extractReply())
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


    @Override public Observable<DailyDetail> loadNewsDetail(long id) {
        return mNewsProviders.newsDetail(mNewsAPI.newsDetail(id), new DynamicKey(id))
                .map(extractReply());
    }


    private static <T> Func1<Reply<T>, T> extractReply() {
        return tReply -> {
            LogUtil.d(tReply.toString());
            return tReply.getData();
        };
    }

}
