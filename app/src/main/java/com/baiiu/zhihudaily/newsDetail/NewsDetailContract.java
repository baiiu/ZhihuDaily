package com.baiiu.zhihudaily.newsDetail;

import android.os.Bundle;
import com.baiiu.zhihudaily.mvp.MVPPresenter;
import com.baiiu.zhihudaily.mvp.MvpView;
import com.baiiu.zhihudaily.mvp.ToastInfoView;
import com.baiiu.zhihudaily.newsDetail.model.DailyDetail;

/**
 * author: baiiu
 * date: on 16/5/11 10:38
 * description:
 */
public class NewsDetailContract {

    public interface IView extends ToastInfoView, MvpView {
        void showErrorPage();

        void showLoadingPage();

        void showNewsDetail(DailyDetail dailyDetail);
    }

    public interface IPresenter extends MVPPresenter<IView> {
        void processArguments(Bundle arguments);
    }
}
