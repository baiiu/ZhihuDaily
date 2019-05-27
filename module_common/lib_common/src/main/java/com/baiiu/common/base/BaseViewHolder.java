package com.baiiu.common.base;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.baiiu.common.util.UIUtil;

/**
 * Created by baiiu on 15/11/18.
 * BaseViewHolder
 */
public abstract class BaseViewHolder<E> extends RecyclerView.ViewHolder {
    public OnClickListener mListener;
    public Context mContext;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
    }

    public BaseViewHolder(Context context, @LayoutRes int layoutId, ViewGroup parent) {
        this(context, layoutId, parent, null);
    }

    public BaseViewHolder(Context context, @LayoutRes int layoutId, ViewGroup parent, OnClickListener listener) {
        this(context, layoutId, parent, listener, true);
    }

    public BaseViewHolder(Context context, @LayoutRes int layoutId, ViewGroup parent, OnClickListener listener,
            boolean canItemClick) {
        super(UIUtil.inflate(context, layoutId, parent, false));
        this.mContext = context;

        if (listener != null) {
            this.mListener = listener;

            if (canItemClick && itemView != null) {
                itemView.setOnClickListener(listener);
            }
        }
    }

    public abstract void bind(E data);
}
