package com.baiiu.zhihudaily.newsDetail;

import android.os.Bundle;
import com.baiiu.zhihudaily.BasePresenter;
import com.baiiu.zhihudaily.BaseView;
import com.baiiu.zhihudaily.newsDetail.model.DailyDetail;

/**
 * author: baiiu
 * date: on 16/5/11 10:38
 * description:
 */
public class NewsDetailContract {

    public interface View extends BaseView<Presenter> {
        void showErrorPage();

        void showLoadingPage();

        void showNewsDetail(DailyDetail dailyDetail);
    }

    public interface Presenter extends BasePresenter {
        void processArguments(Bundle arguments);
    }
}
