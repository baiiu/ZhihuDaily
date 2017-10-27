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

import android.os.Parcelable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.baiiu.autoloopviewpager.interfaces.ILoopWrapperAdapter;

/**
 * A PagerAdapter wrapper responsible for providing a proper page to
 * LoopViewPager
 * <p>
 * This class shouldn't be used directly
 */
public class LoopingAdapterWrapper extends PagerAdapter implements ILoopWrapperAdapter {

    private PagerAdapter mRealAdapter;

    private SparseArray<ToDestroy> mToDestroy = new SparseArray<ToDestroy>();

    private boolean mBoundaryCaching;

    void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
    }

    LoopingAdapterWrapper(PagerAdapter adapter) {
        this.mRealAdapter = adapter;
    }

    @Override public void notifyDataSetChanged() {
        mToDestroy.clear();
        super.notifyDataSetChanged();
    }

    int toRealPosition(int position) {
        //获取真实的count
        int realCount = getRealCount();
        if (realCount == 0) return 0;

        int realPosition = (position - 1) % realCount;
        if (realPosition < 0) realPosition += realCount;

        return realPosition;
    }

    public int toInnerPosition(int realPosition) {
        return ++realPosition;
    }

    private int getRealFirstPosition() {
        return 1;
    }

    private int getRealLastPosition() {
        return getRealFirstPosition() + getRealCount() - 1;
    }

    @Override public int getCount() {
        //复写getCount时加了两个
        return mRealAdapter.getCount() + 2;
    }

    @Override public int getRealCount() {
        return mRealAdapter != null ? mRealAdapter.getCount() : 0;
    }

    public PagerAdapter getRealAdapter() {
        return mRealAdapter;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        int realPosition =
                (mRealAdapter instanceof FragmentPagerAdapter || mRealAdapter instanceof FragmentStatePagerAdapter)
                        ? position : toRealPosition(position);

        if (mBoundaryCaching) {
            ToDestroy toDestroy = mToDestroy.get(position);
            if (toDestroy != null) {
                mToDestroy.remove(position);
                return toDestroy.object;
            }
        }

        return mRealAdapter.instantiateItem(container, realPosition);
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        int realFirst = getRealFirstPosition();
        int realLast = getRealLastPosition();
        int realPosition =
                (mRealAdapter instanceof FragmentPagerAdapter || mRealAdapter instanceof FragmentStatePagerAdapter)
                        ? position : toRealPosition(position);

        if (mBoundaryCaching && (position == realFirst || position == realLast)) {
            mToDestroy.put(position, new ToDestroy(container, realPosition, object));
        } else {
            mRealAdapter.destroyItem(container, realPosition, object);
        }
    }

    /*
     * Delegate rest of methods directly to the inner adapter.
     */

    @Override public void finishUpdate(ViewGroup container) {
        mRealAdapter.finishUpdate(container);
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return mRealAdapter.isViewFromObject(view, object);
    }

    @Override public void restoreState(Parcelable bundle, ClassLoader classLoader) {
        mRealAdapter.restoreState(bundle, classLoader);
    }

    @Override public Parcelable saveState() {
        return mRealAdapter.saveState();
    }

    @Override public void startUpdate(ViewGroup container) {
        mRealAdapter.startUpdate(container);
    }

    @Override public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mRealAdapter.setPrimaryItem(container, position, object);
    }

    /*
     * End delegation
     */

    /**
     * Container class for caching the boundary views
     */
    private static class ToDestroy {
        ViewGroup container;
        int position;
        Object object;

        ToDestroy(ViewGroup container, int position, Object object) {
            this.container = container;
            this.position = position;
            this.object = object;
        }
    }

}