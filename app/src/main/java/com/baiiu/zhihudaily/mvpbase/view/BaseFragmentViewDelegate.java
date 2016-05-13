package com.baiiu.zhihudaily.mvpbase.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

/**
 * author: baiiu
 * date: on 16/5/13 14:57
 * description:
 */
public abstract class BaseFragmentViewDelegate<T> implements IFragmentViewDelegate<T> {

  protected T mPresenter;

  protected View mRootView;
  protected Context mContext;

  @Override public void setPresenter(T presenter) {
    this.mPresenter = presenter;
  }

  @Override public void onCreateView(LayoutInflater inflater, ViewGroup container) {
    mRootView = inflater.inflate(provideLayoutId(), container, false);
    ButterKnife.bind(this, mRootView);
    mContext = mRootView.getContext();
  }

  public abstract int provideLayoutId();

  @Override public View getRootView() {
    return mRootView;
  }
}
