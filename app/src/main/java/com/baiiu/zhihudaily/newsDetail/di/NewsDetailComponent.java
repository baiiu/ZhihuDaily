package com.baiiu.zhihudaily.newsDetail.di;

import com.baiiu.zhihudaily.di.component.AppComponent;
import com.baiiu.zhihudaily.di.scope.PerActivity;
import com.baiiu.zhihudaily.newsDetail.view.NewsDetailFragment;
import dagger.Component;

/**
 * author: baiiu
 * date: on 16/6/14 16:54
 * description:
 */
@PerActivity
@Component(
        dependencies = AppComponent.class

)
public interface NewsDetailComponent {

    void inject(NewsDetailFragment newsDetailFragment);

}
