package com.baiiu.zhihudaily.data.net.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.baiiu.zhihudaily.util.UIUtil;


public class HttpNetUtil {
    private static boolean isConnected = true;

    /**
     * 获取是否连接，在应用启动时先调用下 @{@link {@link #setConnected()}}进行初始化
     */
    public static boolean isConnected() {
        return isConnected;
    }

    private static void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * 判断网络连接是否存在
     */
    public static boolean setConnected() {
        ConnectivityManager manager = (ConnectivityManager) UIUtil.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            setConnected(false);
            return false;
        }

        NetworkInfo info = manager.getActiveNetworkInfo();

        boolean connected = info != null && info.isConnected();
        setConnected(connected);

        return connected;
    }


}
