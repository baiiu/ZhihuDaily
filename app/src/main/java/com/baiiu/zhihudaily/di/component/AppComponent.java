package com.baiiu.zhihudaily.di.component;

import com.baiiu.zhihudaily.DailyApplication;
import com.baiiu.zhihudaily.di.module.AppModule;
import com.baiiu.zhihudaily.di.module.RepositoryModule;
import com.baiiu.zhihudaily.di.scope.PerApp;
import com.baiiu.zhihudaily.newsDetail.model.NewsDetailRepository;
import com.baiiu.zhihudaily.newsList.model.source.NewsListRepository;
import dagger.Component;

/**
 * author: baiiu
 * date: on 16/6/12 16:04
 * description:
 */
@PerApp //标明该Component中有Module使用了@PerApp
@Component(
        modules = {
                AppModule.class, RepositoryModule.class

        }

)
public interface AppComponent {
    DailyApplication getApplication();

    NewsListRepository getNewsListRepository();

    NewsDetailRepository getNewsDetailRepository();


    void inject(DailyApplication myApplication);
}
