package com.baiiu.zhihudaily.newsDetail.presenter;

import android.os.Bundle;
import com.baiiu.zhihudaily.newsDetail.NewsDetailContract;
import com.baiiu.zhihudaily.newsDetail.model.NewsDetailRepository;
import com.baiiu.zhihudaily.newsDetail.view.NewsDetailFragment;
import com.baiiu.zhihudaily.util.LogUtil;
import com.orhanobut.logger.Logger;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author: baiiu
 * date: on 16/5/12 15:05
 * description:
 */
public class NewsDetailPresenter implements NewsDetailContract.Presenter {

    private NewsDetailContract.View mNewsDetailView;
    private final NewsDetailRepository mNewsDetailRepository;
    private long id;

    public NewsDetailPresenter(NewsDetailContract.View newsDetailView) {
        //构造函数注入
        this.mNewsDetailView = newsDetailView;

        //双向绑定
        newsDetailView.setPresenter(this);

        //硬编码注入
        mNewsDetailRepository = new NewsDetailRepository();
    }

    @Override public void processArguments(Bundle arguments) {
        if (arguments != null) {
            id = arguments.getLong(NewsDetailFragment.NEWS_ID);
        }
    }

    @Override public void start() {
        if (id == 0) {
            mNewsDetailView.showErrorPage();
        } else {
            mNewsDetailView.showLoadingPage();

            mNewsDetailRepository.loadNewsDetail(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(dailyDetail -> {
                        mNewsDetailView.showNewsDetail(dailyDetail);
                    }, e -> {
                        Logger.e(e.toString());
                        mNewsDetailView.showErrorPage();
                        mNewsDetailView.showErrorInfo("some error");
                    }, () -> LogUtil.d("onComplete"));
        }
    }
}
