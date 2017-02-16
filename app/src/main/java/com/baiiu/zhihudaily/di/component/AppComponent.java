package com.baiiu.zhihudaily.di.component;

import com.baiiu.zhihudaily.DailyApplication;
import com.baiiu.zhihudaily.data.repository.NewsRepository;
import com.baiiu.zhihudaily.di.module.AppModule;
import com.baiiu.zhihudaily.di.module.RepositoryModule;
import com.baiiu.zhihudaily.di.scope.PerApp;
import dagger.Component;

/**
 * author: baiiu
 * date: on 16/6/12 16:04
 * description:
 */
@PerApp
@Component(
        modules = {
                AppModule.class, RepositoryModule.class
        }

)
public interface AppComponent {
    DailyApplication getApplication();

    NewsRepository getNewsRepository();

    void inject(DailyApplication myApplication);
}
