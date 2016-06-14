package com.baiiu.zhihudaily.newsList.di;

import com.baiiu.zhihudaily.di.component.AppComponent;
import com.baiiu.zhihudaily.di.scope.PerActivity;
import com.baiiu.zhihudaily.newsList.view.NewsListActivity;
import dagger.Component;

/**
 * author: baiiu
 * date: on 16/6/14 15:32
 * description:
 */
@PerActivity
@Component(
        dependencies = AppComponent.class,
        modules = NewsListModule.class

)
public interface NewsListComponent {

    void inject(NewsListActivity newsListActivity);

    NewsListFragmentComponent newsListFragmentComponent();
}
