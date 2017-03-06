package com.baiiu.tsnackbar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * 提供两种方法：
 * 1. 将布局扩展到状态栏
 * 2. 设置状态栏颜色。如不使用代码设置，则需要在styles中配置
 *
 * 提供setters、getters方法是为了属性动画。使用后要调用clear方法
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LUtils {

    private static final int FAKE_STATUS_BAR_ID = R.id.statusBarView;

    private Activity mActivity;

    private LUtils(Activity activity) {
        mActivity = activity;
    }

    public static LUtils instance(Activity activity) {
        return new LUtils(activity);
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    public int getStatusBarColor() {
        return getStatusBarColor(mActivity);
    }

    public void setStatusBarColor(int color) {
        setStatusBarColor(mActivity, color);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////


    public static int getStatusBarColor(Activity activity) {
        if (Version.belowKitKat()) {
            // On pre-kitKat devices, you can have any status bar color so long as it's black.
            return Color.BLACK;
        }

        if (Version.hasL()) {
            return activity.getWindow()
                    .getStatusBarColor();
        }

        if (Version.hasKitKat()) {
            ViewGroup decorView = (ViewGroup) activity.getWindow()
                    .getDecorView();
            View statusBarView = decorView.findViewById(FAKE_STATUS_BAR_ID);
            if (statusBarView != null) {
                Drawable drawable = statusBarView.getBackground();
                if (drawable != null) {
                    return ((ColorDrawable) drawable).getColor();
                }
            }
        }

        return -1;
    }

    public static void setStatusBarColor(Activity activity, @ColorInt int color) {
        if (Version.belowKitKat() || activity == null) {
            return;
        }

        if (Version.hasL() && !isTranslucentStatus(activity)) {
            activity.getWindow()
                    .setStatusBarColor(color);
            return;
        }

        if (Version.hasKitKat()) {
            ViewGroup decorView = (ViewGroup) activity.getWindow()
                    .getDecorView();
            View fakeStatusBarView = decorView.findViewById(FAKE_STATUS_BAR_ID);
            if (fakeStatusBarView != null) {
                if (fakeStatusBarView.getVisibility() == View.GONE) {
                    fakeStatusBarView.setVisibility(View.VISIBLE);
                }
                fakeStatusBarView.setBackgroundColor(color);
            } else {
                decorView.addView(createStatusBarView(activity, color));
            }

            setRootView(activity);
        }
    }

    private static View createStatusBarView(Activity activity, int color) {
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                         ScreenUtil.getStatusHeight((activity)));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        statusBarView.setId(FAKE_STATUS_BAR_ID);
        return statusBarView;
    }

    private static void setRootView(Activity activity) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    private static final int[] THEME_ATTRS = {
            android.R.attr.colorPrimaryDark, android.R.attr.windowTranslucentStatus
    };

    static int getDefaultStatusBarBackground(Context context) {

        final TypedArray a = context.obtainStyledAttributes(THEME_ATTRS);
        try {
            return a.getColor(0, Color.TRANSPARENT);
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            a.recycle();
        }

        return Color.TRANSPARENT;
    }

    //=====================================TranslucentStatusBar====================================================

    /**
     * 将布局扩展到状态栏
     */
    public static void translucentStatusBar(Activity activity) {
        if (activity == null || Version.belowKitKat()) {
            return;
        }

        if (Version.hasKitKatAndUnderL()) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                              WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            return;
        }

        if (Version.hasL()) {
            activity.getWindow()
                    .clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow()
                    .setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

    }

    public static boolean isTranslucentStatus(Context context) {
        if (Version.belowKitKat()) {
            return false;
        }

        if (context instanceof Activity) {
            if (Version.hasL()) {
                return (((Activity) context).getWindow()
                        .getDecorView()
                        .getSystemUiVisibility() & (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)) != 0;
            } else {
                return hasTranslucentStatusFlag((Activity) context);
            }
        }

        final TypedArray a = context.obtainStyledAttributes(THEME_ATTRS);

        try {
            //noinspection ResourceType
            return a.getBoolean(1, false);
        } finally {
            a.recycle();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT) private static boolean hasTranslucentStatusFlag(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return (activity.getWindow()
                    .getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0;
        }
        return false;
    }

    public static void paddingContainer(Context context, View container) {
        if (context == null || container == null) return;

        if (context instanceof Activity) {
            if (Version.hasKitKat() && isTranslucentStatus(context)) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) container.getLayoutParams();
                params.height = ScreenUtil.getStatusHeight(context) + params.height;
                container.setLayoutParams(params);
                container.setPadding(0, ScreenUtil.getStatusHeight(context), 0, 0);
            }
        } else {
            if (Version.hasKitKatAndUnderL() && isTranslucentStatus(context)) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) container.getLayoutParams();
                params.height = ScreenUtil.getStatusHeight(context) + params.height;
                container.setLayoutParams(params);
                container.setPadding(0, ScreenUtil.getStatusHeight(context), 0, 0);
            }
        }

    }

}
