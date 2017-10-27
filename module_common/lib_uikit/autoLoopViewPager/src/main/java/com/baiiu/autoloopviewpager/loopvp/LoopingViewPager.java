/*
 * Copyright (C) 2013 Leszek Mzyk
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

package com.baiiu.autoloopviewpager.loopvp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import com.baiiu.autoloopviewpager.autoscroll.AutoScrollViewPager;
import com.baiiu.autoloopviewpager.interfaces.ILoopViewPager;
import com.baiiu.autoloopviewpager.interfaces.ILoopWrapperAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * A ViewPager subclass enabling infinte scrolling of the viewPager elements
 * <p>
 * When used for paginating views (in opposite to fragments), no code changes
 * should be needed only change xml's from <android.support.v4.view.ViewPager>
 * to <com.imbryk.viewPager.LoopViewPager>
 * <p>
 * If "blinking" can be seen when paginating to first or last view, simply call
 * seBoundaryCaching( true ), or change DEFAULT_BOUNDARY_CASHING to true
 * <p>
 * When using a FragmentPagerAdapter or FragmentStatePagerAdapter,
 * additional changes in the adapter must be done.
 * The adapter must be prepared to create 2 extra items e.g.:
 * <p>
 * The original adapter creates 4 items: [0,1,2,3]
 * The modified adapter will have to create 6 items [0,1,2,3,4,5]
 * with mapping realPosition=(position-1)%count
 * [0->3, 1->0, 2->1, 3->2, 4->3, 5->0]
 * <p>
 * 主要处理无限轮播.
 */
public class LoopingViewPager extends AutoScrollViewPager implements ILoopViewPager {

    private static final boolean DEFAULT_BOUNDARY_CASHING = false;

    private List<ViewPager.OnPageChangeListener> mIndicatorPageChangeListeners;
    private LoopingAdapterWrapper mAdapter;
    private boolean mBoundaryCaching = DEFAULT_BOUNDARY_CASHING;

    public LoopingViewPager(Context context) {
        this(context, null);
    }

    public LoopingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(onPageChangeListener);
    }

    @Override public int getRealCount() {
        if (mAdapter.getRealAdapter() instanceof ILoopWrapperAdapter) {
            return ((ILoopWrapperAdapter) mAdapter.getRealAdapter()).getRealCount();
        }

        return mAdapter != null ? mAdapter.getRealCount() : 0;
    }

    @Override public int getRealCurrentItem() {
        return getCurrentItem();
    }

    @Override public int getFakeCurrentItem() {
        return super.getCurrentItem();
    }

    @Override public void setFakeCurrentItem(int item) {
        setCurrentItem(item);
    }

    @Override public void setAdapter(PagerAdapter adapter) {
        mAdapter = new LoopingAdapterWrapper(adapter);
        mAdapter.setBoundaryCaching(mBoundaryCaching);
        super.setAdapter(mAdapter);
        //        setOffscreenPageLimit(mAdapter.getCount());//添加上会导致缓存(viewPager.items)不能清理,notifyDataSetChanged不能正确显示.
        setCurrentItem(0, false);
    }

    @Override public PagerAdapter getAdapter() {
        return mAdapter;//适配AutoScrollViewPager
        //        return mAdapter != null ? mAdapter.getRealAdapter() : mAdapter;
    }

    @Override public int getCurrentItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }

    @Override public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = mAdapter.toInnerPosition(item);
        super.setCurrentItem(realItem, smoothScroll);
    }

    @Override public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    @Override public void addOnPageChangeListener(OnPageChangeListener listener) {
        super.addOnPageChangeListener(listener);
    }

    @Override public void addOnIndicatorPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (listener == null) {
            return;
        }

        if (mIndicatorPageChangeListeners == null) {
            mIndicatorPageChangeListeners = new ArrayList<>();
        }

        mIndicatorPageChangeListeners.add(listener);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        private float mPreviousOffset = -1;
        private float mPreviousPosition = -1;

        @Override public void onPageSelected(int position) {

            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                if (mIndicatorPageChangeListeners != null) {
                    for (ViewPager.OnPageChangeListener listener : mIndicatorPageChangeListeners) {
                        if (listener != null) {
                            listener.onPageSelected(realPosition);
                        }
                    }
                }
            }
        }

        @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = position;
            if (mAdapter != null) {
                realPosition = mAdapter.toRealPosition(position);

                if (positionOffset == 0 && mPreviousOffset == 0 && (position == 0
                        || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }

            mPreviousOffset = positionOffset;

            if (mIndicatorPageChangeListeners != null) {
                for (ViewPager.OnPageChangeListener listener : mIndicatorPageChangeListeners) {
                    if (listener != null) {
                        if (realPosition != mAdapter.getRealCount() - 1) {
                            listener.onPageScrolled(realPosition, positionOffset, positionOffsetPixels);
                        } else {
                            if (positionOffset > .5) {
                                listener.onPageScrolled(0, 0, 0);
                            } else {
                                listener.onPageScrolled(realPosition, 0, 0);
                            }
                        }
                    }
                }
            }
        }

        @Override public void onPageScrollStateChanged(int state) {
            if (mAdapter != null) {
                int position = LoopingViewPager.super.getCurrentItem();
                int realPosition = mAdapter.toRealPosition(position);
                if (state == ViewPager.SCROLL_STATE_IDLE && (position == 0 || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }
            if (mIndicatorPageChangeListeners != null) {
                for (ViewPager.OnPageChangeListener listener : mIndicatorPageChangeListeners) {
                    if (listener != null) {
                        listener.onPageScrollStateChanged(state);
                    }
                }

            }
        }
    };


    /**
     * helper function which may be used when implementing FragmentPagerAdapter
     *
     * @return (position-1)%count
     */
    public static int toRealPosition(int position, int count) {
        position = position - 1;
        if (position < 0) {
            position += count;
        } else {
            position = position % count;
        }
        return position;
    }

    /**
     * If set to true, the boundary views (i.e. first and last) will never be destroyed
     * This may help to prevent "blinking" of some views
     */
    public void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
        if (mAdapter != null) {
            mAdapter.setBoundaryCaching(flag);
        }
    }

}
