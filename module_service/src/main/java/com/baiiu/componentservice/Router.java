package com.baiiu.componentservice;

import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;

/**
 * 组件初始化、各个组件注册服务。各组件提供的服务：如提供的View等，可以包括路由服务
 *
 * 主要解决的是数据传递的问题
 */
public enum Router {

    INSTANCE;

    private HashMap<String, Object> services = new HashMap<>();
    //注册的组件的集合
    private static HashMap<String, ApplicationDelegate> components = new HashMap<>();

    public synchronized void addService(String serviceName, Object serviceImpl) {
        if (serviceName == null || serviceImpl == null) {
            return;
        }
        services.put(serviceName, serviceImpl);
    }

    public synchronized Object getService(String serviceName) {
        if (serviceName == null) {
            return null;
        }
        return services.get(serviceName);
    }

    public synchronized void removeService(String serviceName) {
        if (serviceName == null) {
            return;
        }
        services.remove(serviceName);
    }

    /**
     * 注册组件
     *
     * @param classname 组件名
     */
    public static void registerComponent(@Nullable String classname) {
        if (TextUtils.isEmpty(classname)) {
            return;
        }
        if (components.keySet()
                .contains(classname)) {
            return;
        }

        try {
            Class clazz = Class.forName(classname);
            ApplicationDelegate applicationLike = (ApplicationDelegate) clazz.newInstance();
            applicationLike.onCreate();
            components.put(classname, applicationLike);
        } catch (Exception e) {
            //LogUtil.e(e.toString());
            Log.e("mLogUtil", e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 反注册组件
     *
     * @param classname 组件名
     */
    public static void unregisterComponent(@Nullable String classname) {
        if (TextUtils.isEmpty(classname)) {
            return;
        }
        if (components.keySet()
                .contains(classname)) {
            components.get(classname)
                    .onStop();
            components.remove(classname);
            return;
        }
        try {
            Class clazz = Class.forName(classname);
            ApplicationDelegate applicationLike = (ApplicationDelegate) clazz.newInstance();
            applicationLike.onStop();
            components.remove(classname);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
