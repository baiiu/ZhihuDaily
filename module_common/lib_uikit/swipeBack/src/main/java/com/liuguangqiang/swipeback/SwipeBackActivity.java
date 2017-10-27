package com.liuguangqiang.swipeback;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Eric on 15/3/3.
 */
public class SwipeBackActivity extends AppCompatActivity implements SwipeBackLayout.SwipeBackListener {

    private static final SwipeBackLayout.DragEdge DEFAULT_DRAG_EDGE = SwipeBackLayout.DragEdge.LEFT;

    private SwipeBackLayout swipeBackLayout;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(getContainer());
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        getSwipeBackLayout().addView(view);
    }

    private View getContainer() {
        getSwipeBackLayout().setDragEdge(DEFAULT_DRAG_EDGE);
        getSwipeBackLayout().setOnSwipeBackListener(this);
        return getSwipeBackLayout();
    }

    public void setDragEdge(SwipeBackLayout.DragEdge dragEdge) {
        getSwipeBackLayout().setDragEdge(dragEdge);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        if (swipeBackLayout == null) {
            swipeBackLayout = new SwipeBackLayout(this);
        }
        return swipeBackLayout;
    }

    @Override
    public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
    }

}
