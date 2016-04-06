package com.baiiu.zhihudaily.util;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baiiu.zhihudaily.app.DailyApplication;


/**
 * UI 工具包
 **/
public class UIUtil {

    /**
     * 获取ApplicationContext
     **/
    public static Context getContext() {
        return DailyApplication.getContext();
    }

    /**
     * 获取包名
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }


    /**
     * 根据图片名获取图片id
     */
    public static int getDrawableId(Context context, String name) {
        return context.getResources().getIdentifier(name, "mipmap", context.getPackageName());
    }

    /**
     * 传入dp，即输出dp值
     */
    public static int dp(int dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics()) + 0.5F);
    }

    /**
     * 获取布局
     **/
    public static View inflate(Context context, @LayoutRes int layoutId) {
        return inflate(context, layoutId, null);
    }

    public static View inflate(Context context, @LayoutRes int layoutId, ViewGroup parent) {
        return inflate(context, layoutId, parent, false);
    }

    public static View inflate(Context context, @LayoutRes int layoutId, ViewGroup parent, boolean attatch) {
        return getInflater(context).inflate(layoutId, parent, attatch);
    }

    /**
     * 获取布局天重启LayoutInflater.建议获取该context相关填充器.
     */
    public static LayoutInflater getInflater(Context context) {
        return LayoutInflater.from(context);
    }

    /**
     * 获取颜色
     */
    public static int getColor(int resId) {
        return getContext().getResources().getColor(resId);
    }


}
