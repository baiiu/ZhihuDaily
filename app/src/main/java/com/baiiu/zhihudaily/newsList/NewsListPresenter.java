package com.baiiu.zhihudaily.newsList;

import com.baiiu.library.LogUtil;
import com.baiiu.zhihudaily.data.bean.Daily;
import com.baiiu.zhihudaily.data.net.ApiConstants;
import com.baiiu.zhihudaily.data.net.http.HttpNetUtil;
import com.baiiu.zhihudaily.data.repository.NewsRepository;
import com.baiiu.zhihudaily.data.util.CommonUtil;
import com.baiiu.zhihudaily.data.util.ReadedListUtil;
import com.baiiu.zhihudaily.newsList.holder.NewsViewHolder;
import com.baiiu.zhihudaily.util.UIUtil;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * auther: baiiu
 * time: 16/5/10 10 22:42
 * description:
 */

public class NewsListPresenter extends NewsListContract.IPresenter {
    private NewsRepository mNewsListRepository;

    //很明显,使用构造函数注入依赖
    @Inject public NewsListPresenter(NewsRepository newsListRepository) {
        //持有 NewsList数据操作类,硬编码注入
        this.mNewsListRepository = newsListRepository;
        LogUtil.d(mNewsListRepository.toString());
    }

    @Override public NewsListContract.IView getMvpView() {
        return (NewsListContract.IView) super.getMvpView();
    }

    @Override public void start() {
        //第一次,从本地加载
        loadNewsList(false, true);
    }

    @Override public void loadNewsList(final boolean fromRemote, final boolean refresh) {
        //设置是否从远端拉取数据
        mNewsListRepository.refreshNewsList(fromRemote);

        addSubscription(

                mNewsListRepository.loadNewsList("", refresh)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(daily -> daily != null)
                        .doOnError(Throwable::printStackTrace)
                        .subscribe(daily -> {
                            if (daily == null) {
                                return;
                            }

                            dealDaily(fromRemote, refresh, daily);

                        }, e -> {
                            LogUtil.e(e.toString());

                            getMvpView().showLoadingIndicator(false);
                            getMvpView().showErrorInfo("网络错误");

                            if (getMvpView().isDataEmpty()) {
                                //历史数据都没有
                                getMvpView().showErrorPage(ApiConstants.NET_ERROR_INFO);
                            }
                        }, () -> LogUtil.d("onComplete"))

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
                    getMvpView().showEmptyPage(ApiConstants.EMPTY_INFO);
                } else {
                    getMvpView().showErrorInfo("拉取数据为空");
                }
            } else {
                getMvpView().showContent(daily.stories, refresh);
            }

        } else {
            //本地的数据
            if (refresh) {
                if (CommonUtil.isEmpty(daily.stories)) {
                    //本地都没有缓存
                    getMvpView().showLoadingIndicator(false);
                    onRefresh();
                } else {
                    //本地有,展示数据
                    getMvpView().showContent(daily.stories, true);

                    UIUtil.postDelayed(() -> {
                        getMvpView().showLoadingIndicator(true);
                        onRefresh();
                    }, 1000);
                }
            } else {
                getMvpView().showContent(daily.stories, false);
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

    @Override public void onLoadingMore() {
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
                getMvpView().showErrorPage(ApiConstants.NET_ERROR_INFO);
            }
        }
    }

}