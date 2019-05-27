package com.baiiu.common.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baiiu.lib_common.R;
import com.liuguangqiang.swipeback.SwipeBackLayout;

/**
 * Created by baiiu on 15/11/16.
 * Base
 */
public abstract class BaseActivity extends AppCompatActivity implements SwipeBackLayout.SwipeBackListener {

    // TODO: 17/10/30 ButterKnife的module中和application中toolbarId不一致
    public Toolbar mToolbar;
    protected ActionBar actionBar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideLayoutId());

        initView();
        mToolbar = findViewById(R.id.toolbar);

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

    protected void initView() {
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
