package com.baiiu.zhihudaily.base.rx;


import com.baiiu.zhihudaily.base.rx.interface_.IShowLoadingDialog;
import com.baiiu.zhihudaily.base.rx.interface_.IShowLoadingIndicator;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author: baiiu
 * date: on 16/8/17 11:15
 * description: 自定义Transformer
 */
public class Transformers {
    public static <T> Observable.Transformer<T, T> switchSchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static <T> Observable.Transformer<T, T> shouldShowLoadingIndicator(boolean refresh,
            IShowLoadingIndicator view) {
        if (view == null) {
            return tObservable -> tObservable;
        }

        if (refresh) {
            return showAndDismissLoadingIndicator(view);
        } else {
            return dismissLoadingIndicator(view);
        }
    }

    public static <T> Observable.Transformer<T, T> dismissLoadingIndicator(IShowLoadingIndicator view) {
        if (view == null) {
            return tObservable -> tObservable;
        }

        return tObservable -> tObservable.doOnCompleted(() -> view.showLoadingIndicator(false))
                .doOnError(throwable -> view.showLoadingIndicator(false));
    }

    public static <T> Observable.Transformer<T, T> showAndDismissLoadingIndicator(IShowLoadingIndicator view) {
        return tObservable -> tObservable.doOnSubscribe(() -> view.showLoadingIndicator(true))
                .doOnCompleted(() -> view.showLoadingIndicator(false))
                .doOnError(throwable -> view.showLoadingIndicator(false));
    }


    public static <T> Observable.Transformer<T, T> showAndDismissLoadingDialog(IShowLoadingDialog view) {
        return tObservable -> tObservable.doOnSubscribe(() -> view.showLoadingDialog(true))
                .doOnCompleted(() -> view.showLoadingDialog(false))
                .doOnError(throwable -> view.showLoadingDialog(false));
    }

    public static <T> Observable.Transformer<T, T> catchExceptionToNull() {
        return observable -> observable.onErrorResumeNext(throwable -> {
            return Observable.just(null);
        });
    }

}
