package com.baiiu.zhihudaily.newsList.presenter;

import com.baiiu.library.LogUtil;
import com.baiiu.zhihudaily.data.entity.Daily;
import com.baiiu.zhihudaily.data.net.network.HttpNetUtil;
import com.baiiu.zhihudaily.data.repository.NewsRepository;
import com.baiiu.zhihudaily.data.util.CommonUtil;
import com.baiiu.zhihudaily.data.util.ReadedListUtil;
import com.baiiu.zhihudaily.newsList.NewsListContract;
import com.baiiu.zhihudaily.newsList.view.holder.NewsViewHolder;
import com.baiiu.zhihudaily.util.UIUtil;
import com.baiiu.zhihudaily.view.mvp.BasePresenter;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * auther: baiiu
 * time: 16/5/10 10 22:42
 * description:
 *
 * 先从本地拿数据，后从网络拿数据，后保存在本地
 */

public class NewsListPresenter extends BasePresenter<NewsListContract.IView> implements NewsListContract.IPresenter {
    NewsRepository mNewsListRepository;

    //很明显,使用构造函数注入依赖
    @Inject public NewsListPresenter(NewsRepository newsListRepository) {
        //持有 NewsList数据操作类,硬编码注入
        //newsListRepository = NewsListRepository.instance();
        this.mNewsListRepository = newsListRepository;
        LogUtil.d(mNewsListRepository.toString());
    }

    @Override public void start() {
        getMvpView().showLoadingPage();
        //第一次,从本地加载
        loadNewsList(false, true);
    }

    @Override public void loadNewsList(final boolean fromRemote, final boolean refresh) {
        //设置是否从远端拉取数据

        addSubscription(

                mNewsListRepository.loadNewsList("", refresh)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(daily -> daily != null)
                        .doOnError(Throwable::printStackTrace)
                        .subscribe(daily -> {
                            getMvpView().showLoadingIndicator(false);

                            if (CommonUtil.isEmpty(daily.stories)) {
                                if (getMvpView().isDataEmpty()) {
                                    getMvpView().showEmptyPage();
                                } else {
                                    getMvpView().showErrorInfo("拉取数据为空");
                                }
                            } else {
                                getMvpView().showNews(daily, refresh);
                            }

                            getMvpView().bindFooter(daily.stories, false);

                        }, e -> {
                            LogUtil.e(e.toString());

                            getMvpView().showLoadingIndicator(false);
                            getMvpView().showErrorInfo("网络错误");

                            if (getMvpView().isDataEmpty()) {
                                //历史数据都没有
                                getMvpView().showErrorPage();
                            } else {
                                getMvpView().bindFooter(null, false);
                            }
                        })
        //mNewsListRepository.loadNewsList("", refresh)
        //        .subscribeOn(Schedulers.io())
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .filter(daily -> daily != null)
        //        .doOnError(Throwable::printStackTrace)
        //        .subscribe(daily -> {
        //            if (daily == null) {
        //                return;
        //            }
        //
        //            dealDaily(fromRemote, refresh, daily);
        //
        //        }, e -> {
        //            LogUtil.e(e.toString());
        //
        //            getMvpView().showLoadingIndicator(false);
        //            getMvpView().showErrorInfo("网络错误");
        //
        //            if (getMvpView().isDataEmpty()) {
        //                //历史数据都没有
        //                getMvpView().showErrorPage();
        //            } else {
        //                getMvpView().bindFooter(null, false);
        //            }
        //        })

        );

    }

    private void dealDaily(boolean fromRemote, boolean refresh, Daily daily) {
        if (refresh) {
            getMvpView().showLoadingIndicator(false);
        }

        if (fromRemote) {
            //从远端返回的数据
            if (CommonUtil.isEmpty(daily.stories)) {
                if (getMvpView().isDataEmpty()) {
                    getMvpView().showEmptyPage();
                } else {
                    getMvpView().showErrorInfo("拉取数据为空");
                }
            } else {
                getMvpView().showNews(daily, refresh);
            }

            getMvpView().bindFooter(daily.stories, false);
        } else {
            //本地的数据
            if (refresh) {
                if (CommonUtil.isEmpty(daily.stories)) {
                    //本地都没有缓存
                    getMvpView().showLoadingIndicator(false);
                    onRefresh();
                } else {
                    //本地有,展示数据
                    getMvpView().showNews(daily, true);
                    getMvpView().bindFooter(daily.stories, true);

                    UIUtil.postDelayed(() -> {
                        getMvpView().showLoadingIndicator(true);
                        onRefresh();
                    }, 1000);
                }
            } else {
                getMvpView().showNews(daily, false);
                getMvpView().bindFooter(daily.stories, true);
            }
        }
    }

    @Override public void openNewsDetail(NewsViewHolder holder) {
        getMvpView().showNewsDetail(holder.mStroy);

        holder.mStroy.isRead = true;
        getMvpView().showNewsReaded(holder.getAdapterPosition(), true);
        //已读
        long id = holder.mStroy.id;
        if (id != 0) {
            ReadedListUtil.saveToReadedList(NewsRepository.READ_LIST, String.valueOf(id));
        }
    }

    @Override public void loadMore() {
        if (HttpNetUtil.isConnected()) {
            loadNewsList(true, false);
        } else {
            loadNewsList(false, false);
        }
    }

    @Override public void onRefresh() {
        if (HttpNetUtil.isConnected()) {
            //刷新数据时从网络拉取
            loadNewsList(true, true);
        } else {
            getMvpView().showLoadingIndicator(false);
            getMvpView().showErrorInfo("网络未连接");

            if (getMvpView().isDataEmpty()) {
                getMvpView().showErrorPage();
            }
        }
    }
}