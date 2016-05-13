package com.baiiu.zhihudaily.mvpbase.view;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baiiu.zhihudaily.BaseView;

/**
 * author: baiiu
 * date: on 16/5/13 10:35
 * description:
 */
public interface IActivityView extends BaseView {
  void onCreateView(LayoutInflater inflater, ViewGroup container);

  View getRootView();

  Toolbar getToolBar();

  void initActionBar(ActionBar actionBar);

  void setCanSwipeBack(boolean canSwipeBack);

  boolean isCanSwipeBack();

  void initView();
}
