package com.baiiu.componentservice;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.baiiu.library.LogUtil;
import java.util.HashMap;

/**
 * 组件初始化、各个组件注册服务
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
            LogUtil.e(e.toString());
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
