package com.baiiu.zhihudaily.mvpbase.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baiiu.zhihudaily.mvpbase.view.IFragmentView;

/**
 * author: baiiu
 * date: on 16/5/13 14:40
 * description:
 */
public abstract class BasePresenterFragment<V extends IFragmentView> extends Fragment {

  protected V viewDelegate;
  protected Context mContext;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    try {
      //Presenter绑定View
      if (viewDelegate == null) {
        viewDelegate = getDelegateViewClass().newInstance();
        viewDelegate.onCreateView(inflater, container);
      }

      //View绑定Presenter
      viewDelegate.setPresenter(this);

      initOnCreateView();

      return viewDelegate.getRootView();
    } catch (java.lang.InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    return null;
  }

  protected void initOnCreateView() {

  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    viewDelegate.initView();
  }

  protected abstract Class<V> getDelegateViewClass();
}
