package com.baiiu.zhihudaily.mvpbase.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.baiiu.zhihudaily.mvpbase.view.IActivityViewDelegate;

/**
 * author: baiiu
 * date: on 16/5/13 10:39
 * description:
 */
public abstract class BasePresenterActivity<V extends IActivityViewDelegate> extends AppCompatActivity {
  protected V viewDelegate;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    try {
      viewDelegate = getDelegateViewClass().newInstance();
      viewDelegate.onCreateView(getLayoutInflater(), null);
      setContentView(viewDelegate.getRootView());

      initToolbar();
      viewDelegate.initView();
      initOnCreate();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private void initToolbar() {
    Toolbar toolbar = viewDelegate.getToolBar();
    if (toolbar != null) {
      setSupportActionBar(toolbar);
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        viewDelegate.initActionBar(actionBar);
      }
    }
  }

  protected void initOnCreate() {

  }

  protected abstract Class<V> getDelegateViewClass();
}
