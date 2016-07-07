package com.baiiu.zhihudaily.view.fastscroll;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import com.baiiu.zhihudaily.util.LogUtil;

/**
 * author: baiiu
 * date: on 16/7/6 15:02
 * description:
 */
public class FastScrollLinearLayoutManager extends LinearLayoutManager {
    public FastScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public FastScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public FastScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {

        LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

            @Override public PointF computeScrollVectorForPosition(int targetPosition) {
                return FastScrollLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }

            //if returned value is 2 ms, it means scrolling 1000 pixels with LinearInterpolation should take 2 seconds.
            @Override protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                /*
                    控制单位速度,  毫秒/像素, 滑动1像素需要多少毫秒.

                    默认为 (25F/densityDpi) 毫秒/像素

                    mdpi上, 1英寸有160个像素点, 25/160,
                    xxhdpi,1英寸有480个像素点, 25/480,
                 */

                //return 10F / displayMetrics.densityDpi;
                return super.calculateSpeedPerPixel(displayMetrics);
            }

            //Calculates the time it should take to scroll the given distance (in pixels)
            @Override protected int calculateTimeForScrolling(int dx) {
                /*
                    控制距离, 然后上面这个方法提供的速度算出时间,

                    默认一次 滚动 TARGET_SEEK_SCROLL_DISTANCE_PX = 10000个像素,

                    在此处可以减少该值来达到减少滚动时间的目的.
                 */

                if (dx > 3000) {
                    dx = 3000;
                }

                int time = super.calculateTimeForScrolling(dx);

                LogUtil.d(time);//打印时间看下

                return time;
            }
        };

        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }

    @Override public PointF computeScrollVectorForPosition(int targetPosition) {
        return super.computeScrollVectorForPosition(targetPosition);
    }
}
