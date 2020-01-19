package com.baiiu.common.base.list;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import com.baiiu.common.BaseApplication;
import com.baiiu.library.LogUtil;

public class WrapContentLinearLayoutManager extends LinearLayoutManager {
    public WrapContentLinearLayoutManager(Context context) {
        super(context);
    }

    public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (BaseApplication.isDebug) {
            super.onLayoutChildren(recycler, state);
            return;
        }

        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
    }

    @Override
    public void smoothScrollToPosition(final RecyclerView recyclerView, RecyclerView.State state,
            int position) {
        LinearSmoothScroller linearSmoothScroller =
                new LinearSmoothScroller(recyclerView.getContext()) {
                    @Override public PointF computeScrollVectorForPosition(int targetPosition) {
                        return WrapContentLinearLayoutManager.this.computeScrollVectorForPosition(
                                targetPosition);
                    }

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return super.calculateSpeedPerPixel(displayMetrics);
                    }

                    @Override protected int calculateTimeForScrolling(int dx) {
                        return super.calculateTimeForScrolling(dx);
                    }
                };

        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}