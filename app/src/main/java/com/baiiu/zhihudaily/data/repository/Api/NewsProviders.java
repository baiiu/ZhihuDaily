package com.baiiu.zhihudaily.data.repository.api;

import com.baiiu.zhihudaily.data.entity.Daily;
import com.baiiu.zhihudaily.data.entity.DailyDetail;
import io.rx_cache.DynamicKey;
import io.rx_cache.LifeCache;
import io.rx_cache.Reply;
import java.util.concurrent.TimeUnit;
import rx.Observable;

/**
 * author: baiiu
 * date: on 17/2/16 14:54
 * description:
 */

public interface NewsProviders {

    Observable<Reply<Daily>> newsList(Observable<Daily> daily, DynamicKey date/*, EvictDynamicKey evictDynamicKey*/);

    Observable<Reply<DailyDetail>> newsDetail(Observable<DailyDetail> dailyDetail, DynamicKey id);

}
