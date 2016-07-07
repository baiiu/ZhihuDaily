package com.baiiu.zhihudaily.util;

import android.view.View;

/**
 * auther: baiiu
 * time: 16/7/7 07 22:28
 * description:
 */
public abstract class DoubleClickListener implements View.OnClickListener {

    private long lastClickTime;

    @Override public void onClick(View v) {
        long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis - lastClickTime > 500) {
            lastClickTime = currentTimeMillis;
            return;
        }

        onDoubleClick(v);

    }

    public abstract void onDoubleClick(View v);

}
