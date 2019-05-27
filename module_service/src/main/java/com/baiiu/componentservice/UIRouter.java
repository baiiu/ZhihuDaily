package com.baiiu.componentservice;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

/**
 * author: baiiu
 * date: on 17/10/31 17:37
 * description:
 *
 * 解决的是路由跳转问题
 */
public final class UIRouter {

    private static final Map<String, Class<? extends AppCompatActivity>> mRouterMap = new HashMap<>();

    public static void put(String url, Class<? extends AppCompatActivity> clazz) {
        mRouterMap.put(url, clazz);
    }

    public static void remove(String url, Class<? extends AppCompatActivity> clazz) {
        mRouterMap.remove(url);
    }

    public static void router(Context context, String url) {
        Class<? extends AppCompatActivity> clazz = mRouterMap.get(url);
        context.startActivity(new Intent(context, clazz));
    }

}
