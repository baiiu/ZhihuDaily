package com.baiiu.autoloopviewpager.interfaces;


import androidx.viewpager.widget.ViewPager;

/**
 * auther: baiiu
 * time: 16/3/27 27 08:36
 * description: indicator接口,可实现该接口写自己的indicator
 */
public interface IPageIndicator extends ViewPager.OnPageChangeListener {

    void setViewPager(ViewPager viewPager);

    void setCurrentItem(int item);

    void notifyDataSetChanged();
}
