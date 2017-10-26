package com.baiiu.autoloopviewpager.interfaces;

import android.support.v4.view.ViewPager;

/**
 * author: baiiu
 * date: on 16/3/28 10:09
 * description: 无限循环ViewPager接口,之类可实现不同的Loop方式.
 * <p>
 * 目前提供两种:
 * 1.LoopingViewPager方式
 * 2.Integer.MaxValue方式
 */
public interface ILoopViewPager {

    int getRealCount();

    int getRealCurrentItem();

    int getFakeCurrentItem();

    void setFakeCurrentItem(int item);

    /**
     * 由ILoopViewPage进行转接,直接返回真正的数据.
     */
    public void addOnIndicatorPageChangeListener(ViewPager.OnPageChangeListener listener);

}
