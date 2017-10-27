package com.baiiu.autoloopviewpager;

import android.content.Context;
import android.util.AttributeSet;
import com.baiiu.autoloopviewpager.loopvp.LoopingViewPager;


/**
 * auther: baiiu
 * time: 16/3/26 26 21:48
 * description: 对LoopingViewPager进行包装
 * <p>
 * 1. 添加自定义属性,可以控制宽高.该属性使用RatioFrameLayout替代
 * 2. to be continued
 */
public class AutoLoopViewPager extends LoopingViewPager {

    public AutoLoopViewPager(Context context) {
        this(context, null);
    }

    public AutoLoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
