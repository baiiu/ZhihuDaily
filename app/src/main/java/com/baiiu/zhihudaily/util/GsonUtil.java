package com.baiiu.zhihudaily.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;

/**
 * Created by baiiu on 15/11/10.
 * json解析类
 */
public class GsonUtil {
  public final static Gson gson = new GsonBuilder().serializeNulls().create();

  public static <T> T parseJson(String json, Class<T> clazz) {
    T t = null;

    try {
      t = gson.fromJson(json, clazz);
    } catch (Exception e) {
      LogUtil.e(e.toString());
    }

    return t;
  }

  public static <T> T parseJson(String json, Type typeOfT) {
    T t = null;

    try {
      t = gson.fromJson(json, typeOfT);
    } catch (Exception e) {
      LogUtil.e(e.toString());
    }

    return t;
  }

  public static String toJson(Object obj) {
    return obj == null ? null : gson.toJson(obj);
  }
}
