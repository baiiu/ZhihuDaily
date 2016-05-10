package com.baiiu.zhihudaily.view.widget.loopvp;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.baiiu.zhihudaily.R;


/**
 * auther: baiiu
 * time: 16/3/26 26 21:48
 * description: 对LoopingViewPager进行包装
 * <p>
 * 1. 添加自定义属性,可以控制宽高
 * 2. to be continued
 */
public class LoopViewPager extends LoopingViewPager {

    /**
     * 默认的宽高比,用于宽高都是wrap_content时
     */
    private static final float DEFAULT_SCALE = 0.5F;

    private float mScale = DEFAULT_SCALE;

    public LoopViewPager(Context context) {
        this(context, null);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoopViewPager);
        mScale = typedArray.getFloat(R.styleable.LoopViewPager_scale, DEFAULT_SCALE);
        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width_mode = View.MeasureSpec.getMode(widthMeasureSpec);
        int width_size = View.MeasureSpec.getSize(widthMeasureSpec);

        int height_mode = View.MeasureSpec.getMode(heightMeasureSpec);
        int height_size = View.MeasureSpec.getSize(heightMeasureSpec);


        int width_result = width_size;
        int height_result = height_size;

        width_result = width_size;//宽度wrap_content时,size由父控件决定.总是等于parent_size,即屏幕宽度.

        if (height_mode == View.MeasureSpec.EXACTLY) {
            height_result = height_size;
        } else {
            height_result = (int) (width_result * mScale + 0.5);
        }

        int measureSpecWidth = View.MeasureSpec.makeMeasureSpec(width_result, View.MeasureSpec.EXACTLY);
        int measureSpecHeight = View.MeasureSpec.makeMeasureSpec(height_result, View.MeasureSpec.EXACTLY);

        super.onMeasure(measureSpecWidth, measureSpecHeight);
    }

}
