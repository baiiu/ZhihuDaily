/*
 * Copyright (C) 2012 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baiiu.autoloopviewpager.indicator;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.baiiu.autoloopviewpager.R;
import com.baiiu.autoloopviewpager.interfaces.ILoopViewPager;
import com.baiiu.autoloopviewpager.interfaces.IPageIndicator;

/**
 * Draws a line for each page. The current page line is colored differently
 * than the unselected page lines.
 */
public class LinePageIndicator extends View implements IPageIndicator {
    private static final int INVALID_POINTER = -1;

    private final Paint mPaintUnselected = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPaintSelected = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ViewPager mViewPager;
    private int mCurrentPage;
    private boolean mCentered;
    private float mLineWidth;
    private float mGapWidth;

    private float mLastMotionX = -1;
    private int mActivePointerId = INVALID_POINTER;
    private boolean mIsDragging;

    public LinePageIndicator(Context context) {
        this(context, null);
    }

    public LinePageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LinePageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LinePageIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (isInEditMode()) return;

        final Resources res = getResources();

        //Load defaults from resources
        final int defaultSelectedColor = Color.parseColor("#FF33B5E5");
        final int defaultUnselectedColor = Color.parseColor("#FFBBBBBB");
        final float defaultLineWidth = 36;
        final float defaultGapWidth = 12;
        final float defaultStrokeWidth = 3;
        final boolean defaultCentered = true;

        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinePageIndicator);

        mCentered = a.getBoolean(R.styleable.LinePageIndicator_centered, defaultCentered);
        mLineWidth = a.getDimension(R.styleable.LinePageIndicator_lineWidth, defaultLineWidth);
        mGapWidth = a.getDimension(R.styleable.LinePageIndicator_gapWidth, defaultGapWidth);
        setStrokeWidth(a.getDimension(R.styleable.LinePageIndicator_strokeWidth, defaultStrokeWidth));
        mPaintUnselected.setColor(a.getColor(R.styleable.LinePageIndicator_unselectedColor, defaultUnselectedColor));
        mPaintSelected.setColor(a.getColor(R.styleable.LinePageIndicator_selectedColor, defaultSelectedColor));

        Drawable background = a.getDrawable(R.styleable.LinePageIndicator_android_background);
        if (background != null) {
            setBackgroundDrawable(background);
        }

        a.recycle();
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        float result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null)) {
            //We were told how big to be
            result = specSize;
        } else {
            //Calculate the width according the views count
            final int count = mViewPager.getAdapter()
                    .getCount();
            result = getPaddingLeft() + getPaddingRight() + (count * mLineWidth) + ((count - 1) * mGapWidth);
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return (int) Math.ceil(result);
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        float result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            //We were told how big to be
            result = specSize;
        } else {
            //Measure the height
            result = mPaintSelected.getStrokeWidth() + getPaddingTop() + getPaddingBottom();
            //Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return (int) Math.ceil(result);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mViewPager == null) {
            return;
        }
        final int count = getRealCount();
        if (count == 0) {
            return;
        }

        if (mCurrentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }

        final float lineWidthAndGap = mLineWidth + mGapWidth;
        final float indicatorWidth = (count * lineWidthAndGap) - mGapWidth;
        final float paddingTop = getPaddingTop();
        final float paddingLeft = getPaddingLeft();
        final float paddingRight = getPaddingRight();

        float verticalOffset = paddingTop + ((getHeight() - paddingTop - getPaddingBottom()) / 2.0f);
        float horizontalOffset = paddingLeft;
        if (mCentered) {
            horizontalOffset += ((getWidth() - paddingLeft - paddingRight) / 2.0f) - (indicatorWidth / 2.0f);
        }

        //Draw stroked circles
        for (int i = 0; i < count; i++) {
            float dx1 = horizontalOffset + (i * lineWidthAndGap);
            float dx2 = dx1 + mLineWidth;
            canvas.drawLine(dx1, verticalOffset, dx2, verticalOffset,
                            (i == mCurrentPage) ? mPaintSelected : mPaintUnselected);
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

    @Override public void onPageScrollStateChanged(int state) {
    }

    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override public void onPageSelected(int position) {
        mCurrentPage = position % getRealCount();
        invalidate();
    }

    private int getRealCount() {
        if (mViewPager == null) {
            return 0;
        }

        try {
            if (mViewPager instanceof ILoopViewPager) {
                return ((ILoopViewPager) mViewPager).getRealCount();
            } else {
                return mViewPager.getAdapter()
                        .getCount();
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.toString());
            return 0;
        }

    }

    @Override public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPage = savedState.currentPage;
        requestLayout();
    }

    @Override public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = mCurrentPage;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPage;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPage);
        }

        @SuppressWarnings("UnusedDeclaration") public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    @Override public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @Override public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    public void setCentered(boolean centered) {
        mCentered = centered;
        invalidate();
    }

    public boolean isCentered() {
        return mCentered;
    }

    public void setUnselectedColor(int unselectedColor) {
        mPaintUnselected.setColor(unselectedColor);
        invalidate();
    }

    public int getUnselectedColor() {
        return mPaintUnselected.getColor();
    }

    public void setSelectedColor(int selectedColor) {
        mPaintSelected.setColor(selectedColor);
        invalidate();
    }

    public int getSelectedColor() {
        return mPaintSelected.getColor();
    }

    public void setLineWidth(float lineWidth) {
        mLineWidth = lineWidth;
        invalidate();
    }

    public float getLineWidth() {
        return mLineWidth;
    }

    public void setStrokeWidth(float lineHeight) {
        mPaintSelected.setStrokeWidth(lineHeight);
        mPaintUnselected.setStrokeWidth(lineHeight);
        invalidate();
    }

    public float getStrokeWidth() {
        return mPaintSelected.getStrokeWidth();
    }

    public void setGapWidth(float gapWidth) {
        mGapWidth = gapWidth;
        invalidate();
    }

    public float getGapWidth() {
        return mGapWidth;
    }
}