package com.baiiu.zhihudaily.mvpbase.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baiiu.zhihudaily.BaseView;

/**
 * author: baiiu
 * date: on 16/5/13 14:53
 * description:
 */
public interface IFragmentView<T> extends BaseView<T> {

  void onCreateView(LayoutInflater inflater, ViewGroup container);

  View getRootView();

  void initView();
}
