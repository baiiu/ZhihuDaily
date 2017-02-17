package com.baiiu.zhihudaily.newsDetail.presenter;

import android.os.Bundle;
import com.baiiu.library.LogUtil;
import com.baiiu.zhihudaily.data.repository.NewsRepository;
import com.baiiu.zhihudaily.newsDetail.NewsDetailContract;
import com.baiiu.zhihudaily.newsDetail.view.NewsDetailFragment;
import com.baiiu.zhihudaily.view.mvp.BasePresenter;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author: baiiu
 * date: on 16/5/12 15:05
 * description:
 */
public class NewsDetailPresenter extends BasePresenter<NewsDetailContract.IView> implements NewsDetailContract.IPresenter {

    private final NewsRepository mNewsRepository;
    private long id;

    @Inject public NewsDetailPresenter(NewsRepository newsDetailRepository) {
        mNewsRepository = newsDetailRepository;
        LogUtil.d(mNewsRepository.toString());//打印看是否为单例
    }

    @Override public void processArguments(Bundle arguments) {
        if (arguments != null) {
            id = arguments.getLong(NewsDetailFragment.NEWS_ID);
        }
    }

    @Override public void start() {
        if (id == 0) {
            getMvpView().showErrorPage();
        } else {
            getMvpView().showLoadingPage();

            mCompositeSubscription.add(

                    mNewsRepository.loadNewsDetail(id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(dailyDetail -> {
                                getMvpView().showNewsDetail(dailyDetail);
                            }, e -> {
                               LogUtil.e(e.toString());
                                getMvpView().showErrorPage();
                                getMvpView().showErrorInfo("some error");
                            }, () -> LogUtil.d("onComplete"))

            );
        }
    }
}
