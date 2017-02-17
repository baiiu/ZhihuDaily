package com.baiiu.zhihudaily.newsList;

import com.baiiu.zhihudaily.data.bean.Daily;
import com.baiiu.zhihudaily.data.bean.Story;
import com.baiiu.zhihudaily.base.mvp.MVPPresenter;
import com.baiiu.zhihudaily.base.mvp.MvpView;
import com.baiiu.zhihudaily.base.mvp.ToastInfoView;
import com.baiiu.zhihudaily.newsList.view.IRefreshLoadMore;
import com.baiiu.zhihudaily.newsList.view.holder.NewsViewHolder;
import java.util.List;

/**
 * auther: baiiu
 * time: 16/5/10 10 22:36
 * description: 创建NewsList模块的Contract
 *
 *
 * 打开app的所有逻辑是这样的:
 *
 * 1. 显示Loading页面
 * 2. 加载并显示历史数据
 * -- 有则显示
 * -- 无则显示空页面
 *
 * 3. 开始下拉刷新,加载网络数据,显示并缓存.
 */
public interface NewsListContract {

    interface IView extends ToastInfoView, MvpView {
        void showLoadingPage();

        void showLoadingIndicator(boolean show);

        void showNews(Daily daily, boolean update);

        void showEmptyPage();

        void showErrorPage();

        boolean isDataEmpty();

        void bindFooter(List<Story> list, boolean fromLocal);

        //开启新页面
        void showNewsDetail(Story story);

        void showNewsReaded(int position, boolean isRead);
    }

    abstract class IPresenter extends MVPPresenter<IView> implements IRefreshLoadMore {
        //update true时从网络加载数据
        public abstract void loadNewsList(boolean fromRemote, boolean loadMore);

        public abstract void openNewsDetail(NewsViewHolder holder);
    }

}
