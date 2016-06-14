package com.baiiu.zhihudaily.newsList.di;

import com.baiiu.zhihudaily.di.scope.PerFragment;
import com.baiiu.zhihudaily.newsList.view.NewsListFragment;
import dagger.Subcomponent;

/**
 * author: baiiu
 * date: on 16/6/14 15:39
 * description:
 */
@PerFragment
@Subcomponent(
        modules = NewsListFragmentModule.class

)
public interface NewsListFragmentComponent {
    void inject(NewsListFragment newsListFragment);
}
