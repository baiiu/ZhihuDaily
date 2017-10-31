package com.baiiu.daily.newsDetail;

import android.os.Bundle;
import com.baiiu.common.base.mvp.IToastInfoView;
import com.baiiu.common.base.mvp.MVPPresenter;
import com.baiiu.common.base.mvp.MvpView;
import com.baiiu.daily.data.bean.DailyDetail;

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
