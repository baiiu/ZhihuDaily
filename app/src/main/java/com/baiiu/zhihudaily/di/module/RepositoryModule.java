package com.baiiu.zhihudaily.di.module;

import com.baiiu.zhihudaily.data.repository.NewsRepository;
import com.baiiu.zhihudaily.data.repository.local.NewsLocalSource;
import com.baiiu.zhihudaily.data.repository.remote.NewsRemoteSource;
import com.baiiu.zhihudaily.di.scope.PerApp;
import dagger.Module;
import dagger.Provides;

/**
 * author: baiiu
 * date: on 16/6/14 17:29
 * description:
 */
@Module
public class RepositoryModule {


    @Provides @PerApp public NewsRepository provideNewsRepository(NewsLocalSource localSource,
            NewsRemoteSource remoteSource) {
        return new NewsRepository(localSource, remoteSource);
    }

    @Provides @PerApp public NewsLocalSource provideLocalSource() {
        return new NewsLocalSource();
    }

    @Provides @PerApp public NewsRemoteSource provideRemoteSource() {
        /*
            在此处可以把retrofit API创建出来,通过构造函数注入,但不想那么做,现在封装的觉得不错.
            之后在做单元测试时若有不妥,可通过Dagger2提供依赖.
         */
        return new NewsRemoteSource();
    }



}
