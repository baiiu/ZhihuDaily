package com.baiiu.zhihudaily.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.baiiu.zhihudaily.R;

/**
 * author: baiiu
 * date: on 16/4/12 10:36
 * description:
 */
public class EmptyLayout extends FrameLayout {

  public static final int TYPE_LOADING = 0;
  public static final int TYPE_EMPTY = 1;
  public static final int TYPE_ERROR = 2;

  private View container_empty;
  private View empty;
  private View container_error;
  private View error;
  private View container_loading;
  private View loading;

  public EmptyLayout(Context context) {
    this(context, null);
  }

  public EmptyLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public EmptyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    inflate(context, R.layout.layout_empty_error_loading, this);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    container_empty = ButterKnife.findById(this, R.id.container_empty);
    empty = ButterKnife.findById(this, R.id.empty);

    container_error = ButterKnife.findById(this, R.id.container_error);
    error = ButterKnife.findById(this, R.id.error);

    container_loading = ButterKnife.findById(this, R.id.container_loading);
    loading = ButterKnife.findById(this, R.id.loading);
  }

  public void setState(int type) {
    container_loading.setVisibility(type == TYPE_LOADING ? VISIBLE : INVISIBLE);
    container_empty.setVisibility(type == TYPE_EMPTY ? VISIBLE : INVISIBLE);
    container_error.setVisibility(type == TYPE_ERROR ? VISIBLE : INVISIBLE);
  }
}
