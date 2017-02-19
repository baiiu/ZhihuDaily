package com.baiiu.zhihudaily.newsDetail;

import android.os.Bundle;
import com.baiiu.zhihudaily.data.bean.DailyDetail;
import com.baiiu.zhihudaily.base.mvp.MVPPresenter;
import com.baiiu.zhihudaily.base.mvp.MvpView;
import com.baiiu.zhihudaily.base.mvp.IToastInfoView;

/**
 * author: baiiu
 * date: on 16/5/11 10:38
 * description:
 */
public interface NewsDetailContract {

    interface IView extends IToastInfoView, MvpView {
        void showErrorPage();

        void showLoadingPage();

        void showNewsDetail(DailyDetail dailyDetail);
    }

    abstract class IPresenter extends MVPPresenter<IView> {
        public abstract void processArguments(Bundle arguments);
    }
}
