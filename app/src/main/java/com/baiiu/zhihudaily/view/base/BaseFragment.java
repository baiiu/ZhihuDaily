package com.baiiu.zhihudaily.view.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;

/**
 * Created by baiiu on 2015/11/16.
 * Base
 */
public abstract class BaseFragment extends Fragment {
  public final String volleyTag = getClass().getSimpleName();

  /**
   * 绑定到当前的attach的activity上.可强转
   */
  public Context mContext;
  protected View view;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (view == null) {
      view = inflater.inflate(provideLayoutId(), container, false);
    }
    ButterKnife.bind(this, view);
    initOnCreateView();
    return view;
  }

  public abstract int provideLayoutId();

  protected void initOnCreateView() {
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    //        ButterKnife.unbind(this);不要回收,会造成空指针异常
  }

  @Override public void onDetach() {
    super.onDetach();
  }

  @Override public void onStop() {
    super.onStop();
  }
}
