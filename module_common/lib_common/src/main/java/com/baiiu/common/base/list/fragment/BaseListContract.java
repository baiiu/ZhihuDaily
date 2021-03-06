package com.baiiu.common.base.list.fragment;


import com.baiiu.common.base.mvp.MVPPresenter;
import com.baiiu.common.base.mvp.MvpView;
import com.baiiu.common.base.rx.interface_.IShowLoadingIndicator;
import java.util.List;

/**
 * author: baiiu
 * date: on 17/1/4 16:12
 * description:
 */

public interface BaseListContract {

    interface IListMvpView<C extends List> extends MvpView, IShowLoadingIndicator, LoadingMoreScrollListenerM.OnVisibleCountEqualsTotalCountListener {
        void showEmptyPage(String emptyInfo);

        void showErrorPage(String errorInfo);

        void showContent(C data, boolean refresh);

        void showFooter(int state);
    }

    abstract class IRefreshPresenter<C extends List> extends MVPPresenter<IListMvpView<C>> implements IRefreshLoadMore {


    }

}
