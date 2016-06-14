package com.baiiu.zhihudaily.di.module;

import com.baiiu.zhihudaily.di.scope.PerApp;
import com.baiiu.zhihudaily.newsDetail.model.NewsDetailRepository;
import com.baiiu.zhihudaily.newsList.model.source.NewsListRepository;
import com.baiiu.zhihudaily.newsList.model.source.local.NewsListLocalSource;
import com.baiiu.zhihudaily.newsList.model.source.remote.NewsListRemoteSource;
import dagger.Module;
import dagger.Provides;

/**
 * author: baiiu
 * date: on 16/6/14 17:29
 * description:
 */
@Module
public class RepositoryModule {


    @Provides @PerApp public NewsListRepository provideNewsListRepository(NewsListLocalSource localSource,
            NewsListRemoteSource remoteSource) {
        return new NewsListRepository(localSource, remoteSource);
    }

    @Provides @PerApp public NewsListLocalSource provideLocalSource() {
        return new NewsListLocalSource();
    }

    @Provides @PerApp public NewsListRemoteSource provideRemoteSource() {
        /*
            在此处可以把retrofit API创建出来,通过构造函数注入,但不想那么做,现在封装的觉得不错.
            之后在做单元测试时若有不妥,可通过Dagger2提供依赖.
         */
        return new NewsListRemoteSource();
    }


    @Provides @PerApp public NewsDetailRepository provideNewsDetailRepository() {
        /*

         */
        return new NewsDetailRepository();
    }

}
