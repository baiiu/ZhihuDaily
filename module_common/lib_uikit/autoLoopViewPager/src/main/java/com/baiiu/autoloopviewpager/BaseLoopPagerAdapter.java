package com.baiiu.autoloopviewpager;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.baiiu.autoloopviewpager.interfaces.ILoopWrapperAdapter;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseLoopPagerAdapter<T> extends PagerAdapter implements ILoopWrapperAdapter {

    protected boolean mCopyTwo = false;
    public List<T> mList = new ArrayList<>();
    public int size;
    public Context mContext;

    public BaseLoopPagerAdapter(Context context, List<T> list) {
        super();
        this.mContext = context;
        setList(list);
    }

    public void setList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        mCopyTwo = false;

        if (list.size() == 2) {
            if (mList != null) {
                mList.clear();
                mCopyTwo = true;
                mList.addAll(list);
                mList.addAll(list);
            }
        } else {
            this.mList = list;
        }

        notifyDataSetChanged();
    }

    @Override public int getRealCount() {
        return mCopyTwo ? 2 : getCount();
    }

    @Override public int getCount() {
        return size = mList.size();
    }


    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        View view = onCreateView(position);
        container.addView(view);
        return view;
    }

    /**
     * 初始化
     */
    public abstract View onCreateView(int position);

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 必须要复写
     */
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
