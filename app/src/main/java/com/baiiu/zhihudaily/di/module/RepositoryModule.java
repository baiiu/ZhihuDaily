package com.baiiu.zhihudaily.di.module;


import com.baiiu.zhihudaily.data.repository.NewsRepository;
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


    @Provides @PerApp public NewsRepository provideNewsRepository() {
        return new NewsRepository();
    }

}
