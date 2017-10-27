/*
 * Copyright 2015 Eric Liu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liuguangqiang.swipeback;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import java.util.Stack;

/**
 * Swipe or Pull to finish a Activity.
 * <p/>
 * This layout must be a root layout and contains only one direct child view.
 * <p/>
 * The activity must use a theme that with translucent style.
 * <style name="Theme.Swipe.Back" parent="AppTheme">
 * <item name="android:windowIsTranslucent">true</item>
 * <item name="android:windowBackground">@android:color/transparent</item>
 * </style>
 * <p/>
 * Created by Eric on 15/1/8.
 */
public class SwipeBackLayout extends ViewGroup {

    private static final String TAG = "SwipeBackLayout";
    private float mFractionScreen;

    public enum DragEdge {
        LEFT,

        TOP,

        RIGHT,

        BOTTOM
    }

    private DragEdge dragEdge = DragEdge.TOP;

    public void setDragEdge(DragEdge dragEdge) {
        this.dragEdge = dragEdge;
    }

    private static final double AUTO_FINISHED_SPEED_LIMIT = 2000.0;
    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;

    private final ViewDragHelper viewDragHelper;

    private View target;

    private View scrollChild;

    private int verticalDragRange = 0;

    private int horizontalDragRange = 0;

    private int draggingState = 0;

    private int draggingOffset;

    private boolean mInLayout = true;

    private int mContentLeft;
    private int mContentTop;
    private int mScrimColor = DEFAULT_SCRIM_COLOR;

    /**
     * Whether allow to pull this layout.
     */
    private boolean enablePullToBack = true;

    private static final float BACK_FACTOR = 0.3f;

    /**
     * the anchor of calling finish.
     */
    private float finishAnchor = 0;

    /**
     * Set the anchor of calling finish.
     */
    public void setFinishAnchor(float offset) {
        finishAnchor = offset;
    }

    private boolean enableFlingBack = true;

    /**
     * Whether allow to finish activity by fling the layout.
     */
    public void setEnableFlingBack(boolean b) {
        enableFlingBack = b;
    }

    private SwipeBackListener swipeBackListener;

    public void setOnSwipeBackListener(SwipeBackListener listener) {
        swipeBackListener = listener;
    }

    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelperCallBack());
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = 400 * density;
        viewDragHelper.setMinVelocity(minVel);
    }

    private void ensureTarget() {
        if (target == null) {
            if (getChildCount() > 1) {
                throw new IllegalStateException("SwipeBackLayout must contains only one direct child");
            }
            target = getChildAt(0);

            //if (scrollChild == null && target != null) {
            //    if (target instanceof ViewGroup) {
            //        findScrollView((ViewGroup) target);
            //    } else {
            //        scrollChild = target;
            //    }
            //}
        }
    }

    /**
     * Find out the scrollable child view from a ViewGroup.
     * <p>
     * 树的遍历
     */
    private static View findScrollView(ViewGroup viewGroup, int downX, int downY, DragEdge dragEdge) {
        if (viewGroup == null) return null;

        Stack<View> stack = new Stack<>();
        stack.push(viewGroup);

        while (!stack.empty()) {
            View child = stack.pop();

            if (!isUnderView(child, downX, downY)) {
                continue;
            }

            if (dragEdge == DragEdge.LEFT || dragEdge == DragEdge.RIGHT) {
                if (child instanceof HorizontalScrollView || child instanceof ViewPager || child instanceof WebView) {
                    return child;
                }

                if (child instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) child;
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                            return child;
                        }
                    }
                }
            }

            if (dragEdge == DragEdge.TOP || dragEdge == DragEdge.BOTTOM) {
                if (child instanceof ScrollView || child instanceof AbsListView) {
                    return child;
                }

                if (child instanceof RecyclerView) {
                    RecyclerView recyclerView = (RecyclerView) child;
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL) {
                            return child;
                        }
                    }
                }
            }

            if (child instanceof ViewGroup) {
                viewGroup = (ViewGroup) child;

                for (int i = 0, count = viewGroup.getChildCount(); i < count; ++i) {
                    stack.push(viewGroup.getChildAt(i));
                }
            }
        }

        return viewGroup;
    }

    private static boolean isUnderView(View view, int x, int y) {
        if (view == null) return false;
        int[] location = new int[2];
        view.getLocationInWindow(location);

        return x >= location[0]
                && x <= location[0] + view.getMeasuredWidth()
                && y >= location[1]
                && y <= location[1] + view.getMeasuredHeight();
    }


    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 1) {
            throw new IllegalStateException("SwipeBackLayout must contains only one direct child.");
        }

        if (getChildCount() > 0) {
            int measureWidth = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                                                           MeasureSpec.EXACTLY);
            int measureHeight = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(),
                                                            MeasureSpec.EXACTLY);
            getChildAt(0).measure(measureWidth, measureHeight);
        }
    }

    @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mInLayout = true;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (getChildCount() == 0) return;

        View child = getChildAt(0);

        int childWidth = width - getPaddingLeft() - getPaddingRight();
        int childHeight = height - getPaddingTop() - getPaddingBottom();
        int childLeft = getPaddingLeft() + mContentLeft;
        int childTop = getPaddingTop() + mContentTop;
        int childRight = childLeft + childWidth;
        int childBottom = childTop + childHeight;
        child.layout(childLeft, childTop, childRight, childBottom);

        mInLayout = false;
    }

    @Override public void requestLayout() {
        if (mInLayout) {
            return;
        }
        super.requestLayout();
    }

    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        verticalDragRange = h;
        horizontalDragRange = w;

        switch (dragEdge) {
            case TOP:
            case BOTTOM:
                finishAnchor = finishAnchor > 0 ? finishAnchor : verticalDragRange * BACK_FACTOR;
                break;
            case LEFT:
            case RIGHT:
                finishAnchor = finishAnchor > 0 ? finishAnchor : horizontalDragRange * BACK_FACTOR;
                break;
        }
    }

    private int getDragRange() {
        switch (dragEdge) {
            case TOP:
            case BOTTOM:
                return verticalDragRange;
            case LEFT:
            case RIGHT:
                return horizontalDragRange;
            default:
                return verticalDragRange;
        }
    }

    @Override protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final boolean drawContent = child == target;

        boolean ret = super.drawChild(canvas, child, drawingTime);
        if (mFractionScreen <= 1 && drawContent && viewDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            //            drawShadow(canvas, child);
            drawScrim(canvas, child);
        }
        return ret;
    }

    private void drawScrim(Canvas canvas, View child) {
        final int baseAlpha = (mScrimColor & 0xff000000) >>> 24;
        final int alpha = (int) (baseAlpha * (1 - mFractionScreen));
        final int color = alpha << 24 | (mScrimColor & 0xffffff);

        if (dragEdge == DragEdge.LEFT) {
            canvas.clipRect(0, 0, child.getLeft(), getHeight());
        } else if (dragEdge == DragEdge.RIGHT) {
            canvas.clipRect(child.getRight(), 0, getRight(), getHeight());
        } else if (dragEdge == DragEdge.BOTTOM) {
            canvas.clipRect(child.getLeft(), child.getBottom(), getRight(), getHeight());
        } else if (dragEdge == DragEdge.TOP) {
            canvas.clipRect(child.getLeft(), 0, getRight(), child.getTop());
        }

        canvas.drawColor(color);
    }

    private int mInitialMotionX;
    private int mInitialMotionY;

    @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP || !isEnabled()) {
            viewDragHelper.cancel();
            return false;
        }

        ensureTarget();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                enablePullToBack = false;
                mInitialMotionX = (int) ev.getX();
                mInitialMotionY = (int) ev.getY();
                scrollChild = findScrollView((ViewGroup) target, mInitialMotionX, mInitialMotionY, dragEdge);
                Log.d("mLogU", scrollChild.toString());
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = ev.getX();
                final float y = ev.getY();
                final float adx = Math.abs(x - mInitialMotionX);
                final float ady = Math.abs(y - mInitialMotionY);
                final int slop = viewDragHelper.getTouchSlop();

                if (dragEdge == DragEdge.LEFT || dragEdge == DragEdge.RIGHT) {
                    if (adx > slop && adx * 0.5 > ady) {
                        enablePullToBack = true;
                        break;
                    }

                    if (ady > slop && ady > adx) {
                        viewDragHelper.cancel();
                        enablePullToBack = false;
                        return false;
                    }
                }

                if (dragEdge == DragEdge.TOP || dragEdge == DragEdge.BOTTOM) {
                    if (ady > slop && ady * 0.5 > adx) {
                        enablePullToBack = true;
                        break;
                    }

                    if (adx > slop && adx > ady) {
                        viewDragHelper.cancel();
                        enablePullToBack = false;
                        return false;
                    }
                }
                break;
        }

        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override public boolean onTouchEvent(MotionEvent ev) {
        viewDragHelper.processTouchEvent(ev);

        if (enablePullToBack) {
            return true;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                enablePullToBack = false;
                mInitialMotionX = (int) ev.getX();
                mInitialMotionY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = ev.getX();
                final float y = ev.getY();
                final float adx = Math.abs(x - mInitialMotionX);
                final float ady = Math.abs(y - mInitialMotionY);
                final int slop = viewDragHelper.getTouchSlop();

                if (dragEdge == DragEdge.LEFT || dragEdge == DragEdge.RIGHT) {
                    if (adx > slop && adx * 0.5 > ady) {
                        enablePullToBack = true;
                        break;
                    }

                    if (ady > slop && ady > adx) {
                        viewDragHelper.cancel();
                        enablePullToBack = false;
                        break;
                    }
                }

                if (dragEdge == DragEdge.TOP || dragEdge == DragEdge.BOTTOM) {
                    if (ady > slop && ady * 0.5 > adx) {
                        enablePullToBack = true;
                        break;
                    }

                    if (adx > slop && adx > ady) {
                        viewDragHelper.cancel();
                        enablePullToBack = false;
                        break;
                    }
                }
                break;
        }

        return true;
    }

    @Override public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public boolean canChildScrollUp() {
        return ViewCompat.canScrollVertically(scrollChild, -1);
    }

    public boolean canChildScrollDown() {
        return ViewCompat.canScrollVertically(scrollChild, 1);
    }

    private boolean canChildScrollRight() {
        return ViewCompat.canScrollHorizontally(scrollChild, 1);
    }

    private boolean canChildScrollLeft() {
        return ViewCompat.canScrollHorizontally(scrollChild, -1);
    }

    private void finish() {
        Activity act = (Activity) getContext();
        act.finish();
        act.overridePendingTransition(0, android.R.anim.fade_out);
    }

    private class ViewDragHelperCallBack extends ViewDragHelper.Callback {

        @Override public boolean tryCaptureView(View child, int pointerId) {
            return child == target && enablePullToBack;
        }

        @Override public int getViewVerticalDragRange(View child) {
            return (dragEdge == DragEdge.BOTTOM || dragEdge == DragEdge.TOP) ? ViewDragHelper.EDGE_TOP
                    | ViewDragHelper.EDGE_BOTTOM : 0;
        }

        @Override public int getViewHorizontalDragRange(View child) {
            return (dragEdge == DragEdge.LEFT || dragEdge == DragEdge.RIGHT) ? ViewDragHelper.EDGE_LEFT
                    | ViewDragHelper.EDGE_RIGHT : 0;
        }

        @Override public int clampViewPositionVertical(View child, int top, int dy) {

            int result = 0;

            if (dragEdge == DragEdge.TOP && !canChildScrollUp() && top > 0) {
                final int topBound = getPaddingTop();
                final int bottomBound = verticalDragRange;
                result = Math.min(Math.max(top, topBound), bottomBound);
            } else if (dragEdge == DragEdge.BOTTOM && !canChildScrollDown() && top < 0) {
                final int topBound = -verticalDragRange;
                final int bottomBound = getPaddingTop();
                result = Math.min(Math.max(top, topBound), bottomBound);
            }

            return result;
        }

        @Override public int clampViewPositionHorizontal(View child, int left, int dx) {
            int result = 0;

            if (dragEdge == DragEdge.LEFT && !canChildScrollLeft() && left > 0) {
                final int leftBound = getPaddingLeft();
                final int rightBound = horizontalDragRange;
                result = Math.min(Math.max(left, leftBound), rightBound);
            } else if (dragEdge == DragEdge.RIGHT && !canChildScrollRight() && left < 0) {
                final int leftBound = -horizontalDragRange;
                final int rightBound = getPaddingLeft();
                result = Math.min(Math.max(left, leftBound), rightBound);
            }

            return result;
        }

        @Override public void onViewDragStateChanged(int state) {
            if (state == draggingState) return;

            if ((draggingState == ViewDragHelper.STATE_DRAGGING || draggingState == ViewDragHelper.STATE_SETTLING)
                    && state == ViewDragHelper.STATE_IDLE) {
                // the view stopped from moving.
                if (draggingOffset == getDragRange()) {
                    finish();
                }
            }

            draggingState = state;
        }

        @Override public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            switch (dragEdge) {
                case TOP:
                case BOTTOM:
                    draggingOffset = Math.abs(top);
                    break;
                case LEFT:
                case RIGHT:
                    draggingOffset = Math.abs(left);
                    break;
                default:
                    break;
            }

            mContentLeft = left;
            mContentTop = top;

            //The proportion of the sliding.
            float fractionAnchor = (float) draggingOffset / finishAnchor;
            if (fractionAnchor >= 1) fractionAnchor = 1;

            mFractionScreen = (float) draggingOffset / (float) getDragRange();
            if (mFractionScreen >= 1) mFractionScreen = 1;
            invalidate();

            if (swipeBackListener != null) {
                swipeBackListener.onViewPositionChanged(fractionAnchor, mFractionScreen);
            }
        }

        @Override public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (draggingOffset == 0) return;

            if (draggingOffset == getDragRange()) return;

            boolean isBack = false;

            if (enableFlingBack && backBySpeed(xvel, yvel)) {
                // 通过速度来滑回
                isBack = true;
            } else if (draggingOffset >= finishAnchor) {
                isBack = true;
            } else if (draggingOffset < finishAnchor) {
                isBack = false;
            }

            int finalLeft;
            int finalTop;
            switch (dragEdge) {
                case LEFT:
                    finalLeft = xvel >= 0 && isBack ? horizontalDragRange : 0;
                    smoothScrollToX(finalLeft);
                    break;
                case RIGHT:
                    finalLeft = xvel <= 0 && isBack ? -horizontalDragRange : 0;
                    smoothScrollToX(finalLeft);
                    break;
                case TOP:
                    finalTop = yvel >= 0 && isBack ? verticalDragRange : 0;
                    smoothScrollToY(finalTop);
                    break;
                case BOTTOM:
                    finalTop = yvel <= 0 && isBack ? -verticalDragRange : 0;
                    smoothScrollToY(finalTop);
                    break;
            }

        }
    }

    private boolean backBySpeed(float xvel, float yvel) {
        switch (dragEdge) {
            case TOP:
            case BOTTOM:
                if (Math.abs(yvel) > Math.abs(xvel) && Math.abs(yvel) > AUTO_FINISHED_SPEED_LIMIT) {
                    return dragEdge == DragEdge.TOP ? !canChildScrollUp() : !canChildScrollDown();
                }
                break;
            case LEFT:
            case RIGHT:
                if (Math.abs(xvel) > Math.abs(yvel) && Math.abs(xvel) > AUTO_FINISHED_SPEED_LIMIT) {
                    return dragEdge == DragEdge.LEFT ? !canChildScrollLeft() : !canChildScrollRight();
                }
                break;
        }
        return false;
    }

    private void smoothScrollToX(int finalLeft) {
        if (viewDragHelper.settleCapturedViewAt(finalLeft, 0)) {
            ViewCompat.postInvalidateOnAnimation(SwipeBackLayout.this);
        }
    }

    private void smoothScrollToY(int finalTop) {
        if (viewDragHelper.settleCapturedViewAt(0, finalTop)) {
            ViewCompat.postInvalidateOnAnimation(SwipeBackLayout.this);
        }
    }

    public interface SwipeBackListener {

        /**
         * Return scrolled fraction of the layout.
         *
         * @param fractionAnchor relative to the anchor.
         * @param fractionScreen relative to the screen.
         */
        void onViewPositionChanged(float fractionAnchor, float fractionScreen);

    }

}
