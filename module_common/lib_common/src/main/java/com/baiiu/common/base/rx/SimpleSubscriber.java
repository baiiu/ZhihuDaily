package com.baiiu.common.base.rx;


import com.baiiu.common.base.mvp.MvpView;
import com.baiiu.common.base.rx.exception.NotResponseException;
import com.baiiu.common.net.http.HttpNetUtil;
import com.baiiu.library.LogUtil;
import rx.Subscriber;

/**
 * author: baiiu
 * date: on 16/8/24 17:06
 * description:
 */
public abstract class SimpleSubscriber<T> extends Subscriber<T> {
    private MvpView mMvpView;

    public SimpleSubscriber(MvpView showReLoginDialog) {
        this.mMvpView = showReLoginDialog;
    }

    @Override public void onNext(T t) {
        if (mMvpView != null && mMvpView.isAlive()) {
            handleOnNext(t);
        }
    }


    @Override public void onCompleted() {
    }

    @Override public void onError(Throwable e) {
        if (mMvpView == null || !mMvpView.isAlive()) {
            return;
        }

        LogUtil.e(e.toString());

        boolean netAvailable = true;


        if (HttpNetUtil.isConnected()) {
            //网络可用
            if (e instanceof NotResponseException) {
                //ToastUtil.showMessage(e.getMessage());
            } else {
                //ToastUtil.showMessage(ApiConstants.NET_ERROR_INFO);
            }
        } else {
            netAvailable = false;
            //无网
            //ToastUtil.showMessage(ApiConstants.NET_ERROR_INFO);
        }

        handleError(e, netAvailable);
    }

    protected abstract void handleOnNext(T t);

    protected void handleError(Throwable e, boolean netAvailable) {
    }

}
