package com.baiiu.autoloopviewpager.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import com.baiiu.autoloopviewpager.R;
import com.baiiu.autoloopviewpager.interfaces.ILoopViewPager;
import com.baiiu.autoloopviewpager.interfaces.IPageIndicator;

/**
 * auther: baiiu
 * time: 16/3/27 27 17:42
 * description:
 */
public class SimpleCircleIndicator extends View implements IPageIndicator {

    /**
     * 点之间的距离
     */
    private int mDotInterval = 30;

    /**
     * 点的半径
     */
    private int mSelectedRadius;
    private int mUnSelectedRadius;
    private int mMaxRadius;

    /**
     * 是否是空心圆
     */
    private boolean selectedIsStroke;
    private boolean unselectedIsStroke;

    /**
     * 画笔线条宽度
     */
    private int mSelectedStrokeWidth;
    private int mUnSelectedStrokeWidth;
    private int mMaxStrokeWidth;

    private int mSelectedColor;
    private Paint mSelectedPaint;

    private int mUnSelectedColor;
    private Paint mUnSelectedPaint;

    private int mSelectedPosition;
    private ViewPager mViewPager;

    public SimpleCircleIndicator(Context context) {
        this(context, null);
    }

    public SimpleCircleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SimpleCircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleCircleIndicator);
            mDotInterval = typedArray.getDimensionPixelSize(R.styleable.SimpleCircleIndicator_dot_interval, 40);
            mSelectedRadius = typedArray.getDimensionPixelSize(R.styleable.SimpleCircleIndicator_selected_radius, 10);
            mUnSelectedRadius =
                    typedArray.getDimensionPixelSize(R.styleable.SimpleCircleIndicator_unselected_radius, 10);
            mSelectedColor = typedArray.getColor(R.styleable.SimpleCircleIndicator_selected_color, Color.RED);
            mUnSelectedColor = typedArray.getColor(R.styleable.SimpleCircleIndicator_unselected_color, Color.WHITE);

            mSelectedStrokeWidth =
                    typedArray.getDimensionPixelSize(R.styleable.SimpleCircleIndicator_selected_strokeWidth, 1);
            mUnSelectedStrokeWidth =
                    typedArray.getDimensionPixelSize(R.styleable.SimpleCircleIndicator_unselected_strokeWidth, 1);

            selectedIsStroke = typedArray.getBoolean(R.styleable.SimpleCircleIndicator_selectedStroke, false);
            unselectedIsStroke = typedArray.getBoolean(R.styleable.SimpleCircleIndicator_unselectedStroke, false);

            typedArray.recycle();
        }

        mMaxRadius = Math.max(mSelectedRadius, mUnSelectedRadius);
        mMaxStrokeWidth = Math.max(mSelectedStrokeWidth, mUnSelectedStrokeWidth);

        mSelectedPaint = new Paint();
        mSelectedPaint.setStyle(selectedIsStroke ? Paint.Style.STROKE : Paint.Style.FILL);
        mSelectedPaint.setStrokeWidth(mSelectedStrokeWidth);
        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setColor(mSelectedColor);

        mUnSelectedPaint = new Paint();
        mUnSelectedPaint.setStyle(unselectedIsStroke ? Paint.Style.STROKE : Paint.Style.FILL);
        mUnSelectedPaint.setStrokeWidth(mUnSelectedStrokeWidth);
        mUnSelectedPaint.setStyle(Paint.Style.FILL);
        mUnSelectedPaint.setAntiAlias(true);
        mUnSelectedPaint.setColor(mUnSelectedColor);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width_mode = MeasureSpec.getMode(widthMeasureSpec);
        int width_size = MeasureSpec.getSize(widthMeasureSpec);

        int height_mode = MeasureSpec.getMode(heightMeasureSpec);
        int height_size = MeasureSpec.getSize(heightMeasureSpec);

        int width_result;
        int height_result;

        if (width_mode == MeasureSpec.EXACTLY) {
            width_result = width_size;

            if (height_mode == MeasureSpec.EXACTLY) {
                height_result = height_size;
            } else {
                height_result = mMaxRadius * 2 + mMaxStrokeWidth;
            }
        } else {
            int mCount = getRealCount();

            width_result = mSelectedStrokeWidth + (mCount - 1) * (mUnSelectedStrokeWidth
                    + mDotInterval
                    + mUnSelectedRadius * 2) + mSelectedRadius * 2;

            if (height_mode == MeasureSpec.EXACTLY) {
                height_result = height_size;
            } else {
                height_result = mMaxRadius * 2 + mMaxStrokeWidth;
            }
        }

        setMeasuredDimension(width_result, height_result);
    }

    @Override protected void onDraw(Canvas canvas) {
        int measuredWidth = getWidth();

        int mCount = getRealCount();
        if (mCount <= 1) {
            return;
        }


        int mDotTotalWidth = mSelectedStrokeWidth
                + (mCount - 1) * (mUnSelectedStrokeWidth + mDotInterval + mUnSelectedRadius * 2)
                + mSelectedRadius * 2;

        //不支持padding,所以总是从居中开始画
        int mFirstDotXCoordinate;
        if (mSelectedPosition == 0) {
            mFirstDotXCoordinate =
                    (int) ((measuredWidth - mDotTotalWidth) / 2F + mSelectedRadius + mSelectedStrokeWidth / 2 + 0.5);
        } else {
            mFirstDotXCoordinate = (int) ((measuredWidth - mDotTotalWidth) / 2F
                    + mUnSelectedRadius
                    + mUnSelectedStrokeWidth / 2
                    + 0.5);
        }

        int measuredHeight = getHeight();
        int mDotYCoordinate = (int) (measuredHeight / 2F + 0.5);

        int x = mFirstDotXCoordinate;

        for (int i = 0; i < mCount; ) {
            if (i == mSelectedPosition) {
                //画选中点
                canvas.drawCircle(x, mDotYCoordinate, mSelectedRadius, mSelectedPaint);

                //下一个未选中点的圆心
                x += mDotInterval
                        + mUnSelectedRadius
                        + mSelectedRadius
                        + (mUnSelectedStrokeWidth + mSelectedStrokeWidth) / 2;

                ++i;
            } else {
                //画 未选重点
                canvas.drawCircle(x, mDotYCoordinate, mUnSelectedRadius, mUnSelectedPaint);

                //                int j = i;
                //下一个点
                if (++i == mSelectedPosition) {
                    //可能选中
                    x += mDotInterval
                            + mUnSelectedRadius
                            + mSelectedRadius
                            + (mUnSelectedStrokeWidth + mSelectedStrokeWidth) / 2;
                } else {
                    //也可能未选中
                    x += mDotInterval + mUnSelectedRadius * 2 + mUnSelectedStrokeWidth;
                }
            }
        }
    }

    @Override public void setViewPager(ViewPager viewPager) {
        if (viewPager == null || viewPager.getAdapter() == null) {
            throw new IllegalStateException("you must initial the viewpager with adapter");
        }

        int initialPosition = 0;

        if (viewPager instanceof ILoopViewPager) {
            ILoopViewPager loopViewPage = (ILoopViewPager) viewPager;
            loopViewPage.addOnIndicatorPageChangeListener(this);
            initialPosition = loopViewPage.getRealCurrentItem();
        } else {
            viewPager.removeOnPageChangeListener(this);
            viewPager.addOnPageChangeListener(this);
        }

        this.mViewPager = viewPager;

        setCurrentItem(initialPosition);
    }

    @Override public void setCurrentItem(int item) {
        onPageSelected(item);
    }

    @Override public void notifyDataSetChanged() {
        requestLayout();
        invalidate();
    }

    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override public void onPageSelected(int position) {
        this.mSelectedPosition = position % getRealCount();
        invalidate();
    }

    @Override public void onPageScrollStateChanged(int state) {
    }

    private int getRealCount() {
        try {
            if (mViewPager instanceof ILoopViewPager) {
                return ((ILoopViewPager) mViewPager).getRealCount();
            }

            return mViewPager.getAdapter()
                    .getCount();
        } catch (Exception e) {
            //Log.e(getClass().getSimpleName(), e.toString());
            return 0;
        }

    }
}
