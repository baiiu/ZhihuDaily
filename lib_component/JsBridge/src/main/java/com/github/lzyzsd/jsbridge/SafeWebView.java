package com.github.lzyzsd.jsbridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebView;
import com.github.lzyzsd.library.BuildConfig;

/**
 * safeWebView
 *
 * add the ${@link SafeWebInterface} on the JavaScriptInterface class,not on the function
 * 5.8版本不用
 */
public class SafeWebView extends WebView {
    private static final String[] SAFE_JS_CLASS_PREFIX;
    private static final String[] UNSAFE_JS_INTERFACE;

    static {
        UNSAFE_JS_INTERFACE = new String[] { "searchBoxJavaBridge_", "accessibility", "accessibilityTraversal" };
        SAFE_JS_CLASS_PREFIX = new String[] { "android." };
    }

    public SafeWebView(Context context) {
        super(context);
        init();
    }

    public SafeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SafeWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @SuppressLint({ "NewApi" })
    public SafeWebView(Context context, AttributeSet attrs, int defStyle, boolean privateBrowsing) {
        super(context, attrs, defStyle, privateBrowsing);
        init();
    }

    @SuppressLint({ "NewApi" }) private void init() {
        if (VERSION.SDK_INT >= 11 && VERSION.SDK_INT <= 16) {
            for (String unsafe : UNSAFE_JS_INTERFACE) {
                super.removeJavascriptInterface(unsafe);
            }
        }
    }

    @SuppressLint({ "JavascriptInterface" }) public void addJavascriptInterface(Object obj, String interfaceName) {
        if (BuildConfig.DEBUG || VERSION.SDK_INT <= 16) {
            Class<?> cls = obj.getClass();
            if (!(cls.isAnnotationPresent(SafeWebInterface.class) || isSafeJSClass(cls))) {
                throw new SecurityException(
                        "specified object " + obj + " is not declared safe with SafeWebInterface annotation.");
            }
        }
        super.addJavascriptInterface(obj, interfaceName);
    }

    @SuppressLint({ "NewApi" }) public void removeJavascriptInterface(String interfaceName) {
        super.removeJavascriptInterface(interfaceName);
    }

    private static boolean isSafeJSClass(Class<?> cls) {
        String name = cls.getName();
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        for (String prefix : SAFE_JS_CLASS_PREFIX) {
            if (name.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
