package com.baiiu.zhihudaily.newsList.presenter;

import com.baiiu.zhihudaily.newsList.NewsListContract;

/**
 * auther: baiiu
 * time: 16/5/10 10 22:42
 * description:
 */
public class NewsListPresenter implements NewsListContract.Presenter {
  private NewsListContract.View mNewsListView;

  /*
   * 很明显,使用构造函数注入依赖
   */
  public NewsListPresenter(NewsListContract.View newsListView) {

    //Presenter绑定View
    this.mNewsListView = newsListView;

    //View绑定Presenter
    newsListView.setPresenter(this);
  }

  @Override public void start() {

  }
}
