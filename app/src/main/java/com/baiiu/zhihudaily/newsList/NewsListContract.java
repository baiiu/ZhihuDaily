package com.baiiu.zhihudaily.newsList;

import com.baiiu.zhihudaily.BaseView;
import com.baiiu.zhihudaily.newsList.view.IRefreshLoadMore;

/**
 * auther: baiiu
 * time: 16/5/10 10 22:36
 * description: 创建NewsList模块的Contract
 *
 *
 * 打开app的所有逻辑是这样的:
 *
 * 1. 显示Loading页面
 * 2. 加载并显示历史数据
 * -- 有则显示
 * -- 无则显示空页面
 *
 * 3. 开始下拉刷新,加载网络数据,显示并缓存.
 */
public interface NewsListContract {

  interface View extends BaseView<Presenter> {
  }

  interface Presenter extends IRefreshLoadMore, android.view.View.OnClickListener {
    //update true时从网络加载数据
    void loadNewsList(boolean fromRemote, boolean loadMore);
  }
}
