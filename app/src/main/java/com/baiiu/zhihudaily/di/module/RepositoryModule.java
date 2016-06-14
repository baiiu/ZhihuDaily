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
        return new NewsListRemoteSource();
    }


    @Provides @PerApp public NewsDetailRepository provideNewsDetailRepository() {
        return new NewsDetailRepository();
    }

}
