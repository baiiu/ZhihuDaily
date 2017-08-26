package com.baiiu.zhihudaily.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.baiiu.zhihudaily.R;
import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by baiiu on 15/11/16.
 * Base
 */
public abstract class BaseActivity extends RxAppCompatActivity implements SwipeBackLayout.SwipeBackListener {

    @Nullable @BindView(R.id.toolbar) public Toolbar mToolbar;
    protected ActionBar actionBar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideLayoutId());

        ButterKnife.bind(this);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);

            actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(canSwipeBack);
                initActionBar(actionBar);
            }
        }

        initOnCreate(savedInstanceState);
    }

    public abstract int provideLayoutId();

    protected void initActionBar(ActionBar actionBar) {
    }

    protected abstract void initOnCreate(Bundle savedInstanceState);

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
    }

    //===============================swipeBack,在ProvideLayoutId前调用==============================================
    private SwipeBackLayout swipeBackLayout;
    private boolean canSwipeBack = false;

    public void setCanSwipeBack(boolean canSwipeBack) {
        this.canSwipeBack = canSwipeBack;
    }

    @Override public void setContentView(int layoutResID) {
        if (canSwipeBack) {
            super.setContentView(getContainer());
            View view = LayoutInflater.from(this)
                    .inflate(layoutResID, null);
            swipeBackLayout.addView(view);
        } else {
            super.setContentView(layoutResID);
        }
    }

    private View getContainer() {
        getSwipeBackLayout().setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        getSwipeBackLayout().setOnSwipeBackListener(this);
        return getSwipeBackLayout();
    }

    public void setDragEdge(SwipeBackLayout.DragEdge dragEdge) {
        getSwipeBackLayout().setDragEdge(dragEdge);
    }

    public void setSwipeBackEnabled(boolean enabled) {
        getSwipeBackLayout().setEnabled(enabled);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        if (swipeBackLayout == null) {
            swipeBackLayout = new SwipeBackLayout(this);
        }
        return swipeBackLayout;
    }

    @Override public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
    }
}
