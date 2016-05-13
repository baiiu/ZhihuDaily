package com.baiiu.zhihudaily.mvpbase.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.util.UIUtil;
import com.baiiu.zhihudaily.view.SwipeBackLayout;

/**
 * author: baiiu
 * date: on 16/5/13 14:12
 * description:
 */
public abstract class BaseActivityView implements IActivityView, SwipeBackLayout.SwipeBackListener {
  protected View mRootView;
  protected Context mContext;

  @Nullable @Bind(R.id.toolbar) public Toolbar toolbar;

  @Override public void onCreateView(LayoutInflater inflater, ViewGroup container) {
    mRootView = inflater.inflate(provideLayoutId(), container, false);
    ButterKnife.bind(mRootView);
    mContext = mRootView.getContext();
  }

  @Override public View getRootView() {
    if (canSwipeBack) {
      swipeBackLayout.addView(mRootView);
      return getContainer();
    } else {
      return mRootView;
    }
  }

  @Override public Toolbar getToolBar() {
    return toolbar;
  }

  @Override public void initActionBar(ActionBar actionBar) {
    actionBar.setDisplayHomeAsUpEnabled(isCanSwipeBack());
  }

  public abstract int provideLayoutId();

  //====================侧滑==============================

  private ImageView ivShadow;
  private SwipeBackLayout swipeBackLayout;
  private boolean canSwipeBack = false;

  public void setCanSwipeBack(boolean canSwipeBack) {
    this.canSwipeBack = canSwipeBack;
  }

  public boolean isCanSwipeBack() {
    return canSwipeBack;
  }

  private View getContainer() {
    RelativeLayout container = new RelativeLayout(mContext);
    swipeBackLayout = new SwipeBackLayout(mContext);
    swipeBackLayout.setOnSwipeBackListener(this);
    ivShadow = new ImageView(mContext);
    ivShadow.setBackgroundColor(UIUtil.getColor(R.color.black_p50));
    RelativeLayout.LayoutParams params =
        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT);
    container.addView(ivShadow, params);
    container.addView(swipeBackLayout);
    return container;
  }

  @Override public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
    ivShadow.setAlpha(1 - fractionScreen);
  }
}
