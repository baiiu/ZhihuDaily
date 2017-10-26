package com.baiiu.autoloopviewpager.autoscroll;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * CustomDurationScroller
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-3-2
 */
public class CustomDurationScroller extends Scroller {

    /**
     * 影响滑动速度的因子
     */
    private double scrollFactor = 1;

    public CustomDurationScroller(Context context) {
        super(context);
    }

    public CustomDurationScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        this.scrollFactor = scrollFactor;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        //修改duration来控制速度.一定的路程内,时间越长,滑的越慢
        super.startScroll(startX, startY, dx, dy, (int) (duration * scrollFactor));
    }
}
