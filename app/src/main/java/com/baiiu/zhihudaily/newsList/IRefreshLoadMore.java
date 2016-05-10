package com.baiiu.zhihudaily.newsList;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * author: baiiu
 * date: on 16/5/10 17:19
 * description:
 */
public interface IRefreshLoadMore extends SwipeRefreshLayout.OnRefreshListener {
  void loadMore();

}
