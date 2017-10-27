package com.baiiu.common.base.list.activity;

import com.baiiu.common.base.mvp.MvpView;
import java.util.List;

/**
 * author: baiiu
 * date: on 17/1/18 15:19
 * description:
 */
public interface IListPageView<C extends List> extends MvpView {

    void showEmptyPage(String emptyInfo);

    void showErrorPage(String errorInfo);

    void showLoadingPage(String loadingInfo);

    void showContent(C list);

}
