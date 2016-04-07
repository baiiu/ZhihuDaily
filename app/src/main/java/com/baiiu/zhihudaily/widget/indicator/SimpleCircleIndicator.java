package com.baiiu.zhihudaily.widget.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.util.LogUtil;
import com.baiiu.zhihudaily.widget.indicator._interface.IPageIndicator;
import com.baiiu.zhihudaily.widget.loopvp._interface.ILoopViewPager;


/**
 * auther: baiiu
 * time: 16/3/27 27 17:42
 * description:
 */
public class SimpleCircleIndicator extends View implements IPageIndicator {

    /**
     * 点之间的距离
     */
    private int mDotInterval = 10;

    /**
     * 点的半径
     */
    private int mDotRadius = 10;

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
            mDotInterval = (int) typedArray.getDimension(R.styleable.SimpleCircleIndicator_dot_interval, 40);
            mDotRadius = (int) typedArray.getDimension(R.styleable.SimpleCircleIndicator_dot_radius, 10);
            mSelectedColor = typedArray.getColor(R.styleable.SimpleCircleIndicator_selected_color, Color.RED);
            mUnSelectedColor = typedArray.getColor(R.styleable.SimpleCircleIndicator_unselected_color, Color.WHITE);
            typedArray.recycle();
        }

        mSelectedPaint = new Paint();
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setColor(mSelectedColor);

        mUnSelectedPaint = new Paint();
        mUnSelectedPaint.setStyle(Paint.Style.FILL);
        mUnSelectedPaint.setAntiAlias(true);
        mUnSelectedPaint.setColor(mUnSelectedColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
                height_result = mDotRadius * 2;
            }
        } else {
            int mCount = getRealCount();
            width_result = (mCount - 1) * mDotInterval + mCount * mDotRadius * 2;

            if (height_mode == MeasureSpec.EXACTLY) {
                height_result = height_size;
            } else {
                height_result = mDotRadius * 2;
            }

        }

        setMeasuredDimension(width_result, height_result);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int measuredWidth = getWidth();

        int mCount = getRealCount();
        int mDotTotalWidth = (mCount - 1) * mDotInterval + mCount * mDotRadius * 2;
        int mFirstDotXCoordinate = (int) ((measuredWidth - mDotTotalWidth) / 2F + 0.5) + mDotRadius;

        int measuredHeight = getHeight();
        int mDotYCoordinate = (int) ((measuredHeight - mDotRadius * 2) / 2F + 0.5) + mDotRadius;


        int x = mFirstDotXCoordinate;

        for (int i = 0; i < mCount; ++i) {
            if (i == mSelectedPosition) {
                canvas.drawCircle(x, mDotYCoordinate, mDotRadius, mSelectedPaint);
            } else {
                canvas.drawCircle(x, mDotYCoordinate, mDotRadius, mUnSelectedPaint);
            }
            x += mDotInterval + mDotRadius * 2;
        }
    }

    @Override
    public void setViewPager(ViewPager viewPager) {
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

    @Override
    public void setCurrentItem(int item) {
        onPageSelected(item);
    }

    @Override
    public void notifyDataSetChanged() {
        requestLayout();
        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.mSelectedPosition = position;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    private int getRealCount() {
        if (mViewPager == null) {
            return 0;
        }

        try {
            if (mViewPager instanceof ILoopViewPager) {
                return ((ILoopViewPager) mViewPager).getRealCount();
            } else {
                return mViewPager.getAdapter().getCount();
            }

        } catch (Exception e) {
            LogUtil.e(e.toString());
            return 0;
        }
    }
}
