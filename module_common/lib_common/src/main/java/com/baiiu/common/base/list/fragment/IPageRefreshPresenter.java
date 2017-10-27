package com.baiiu.common.base.list.fragment;

import com.baiiu.common.base.ApiResponse;
import com.baiiu.common.base.list.holder.FooterViewHolder;
import com.baiiu.common.base.rx.Functions;
import com.baiiu.common.base.rx.SimpleSubscriber;
import com.baiiu.common.base.rx.Transformers;
import com.baiiu.common.net.ApiConstants;
import com.baiiu.common.util.CommonUtil;
import java.util.List;
import rx.Observable;

/**
 * author: baiiu
 * date: on 16/7/1 18:58
 * description:
 *
 * D为ApiResponse<T>
 * E为List<E>
 */
public abstract class IPageRefreshPresenter<T, E> extends BaseListContract.IRefreshPresenter<List<E>> implements IRefreshLoadMore {
    @Override public void start() {
        getMvpView().showLoadingIndicator(true);
        onRefresh();
    }

    @Override public void onRefresh() {
        loadData(true);
    }

    @Override public void onLoadingMore() {
        loadData(false);
    }

    protected void loadData(final boolean refresh) {

        addSubscription(

                provideObservable(refresh)

                        .map(Functions.extractResponse())
                        .compose(Transformers.switchSchedulers())
                        .compose(Transformers.dismissLoadingIndicator(getMvpView()))
                        .subscribe(new SimpleSubscriber<T>(getMvpView()) {
                            @Override public void handleOnNext(T t) {
                                if (dealSelf(t, refresh)) {
                                    return;
                                }

                                List<E> list = provideList(t);

                                if (CommonUtil.isEmpty(list)) {
                                    if (refresh) {
                                        getMvpView().showEmptyPage(ApiConstants.EMPTY_INFO);
                                    } else {
                                        getMvpView().showFooter(FooterViewHolder.NO_MORE);
                                    }
                                } else {
                                    getMvpView().showContent(list, refresh);
                                }
                            }

                            @Override protected void handleError(Throwable e, boolean netAvailable) {
                                getMvpView().showLoadingIndicator(false);//在OnNext中发生异常时不会回调doOnError

                                if (refresh) {
                                    getMvpView().showErrorPage(ApiConstants.NET_ERROR_RETRY);
                                } else {
                                    getMvpView().showFooter(FooterViewHolder.ERROR);
                                }

                                IPageRefreshPresenter.this.handleError(e, netAvailable);
                            }
                        })

        );

    }


    protected abstract Observable<ApiResponse<T>> provideObservable(boolean refresh);

    protected abstract List<E> provideList(T data);

    protected boolean dealSelf(T t, boolean refresh) {
        return false;
    }

    protected void handleError(Throwable e, boolean netAvailable) {
    }

}
