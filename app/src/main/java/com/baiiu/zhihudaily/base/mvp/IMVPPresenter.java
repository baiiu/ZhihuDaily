package com.baiiu.zhihudaily.base.mvp;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the MvpView type that wants to be attached with.
 */
public interface IMVPPresenter<V extends MvpView> {

    void start();

    void attachView(V mvpView);

    void detachView();
}

