package com.baiiu.zhihudaily.util.net.util;

import android.text.TextUtils;

import java.util.Map;

/**
 * Created by baiiu on 15/11/16.
 * 对于Get请求,
 */
public class MapParamsUtil {

    public static String toParams(String path, Map<String, String> map) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        if (map == null || map.size() == 0) {
            return path;
        }


        StringBuilder sb = new StringBuilder(path);//封装path

        if (!path.contains("?")) {
            sb.append("?");
        }

        int i = 0;
        int size = map.size();

        for (String key : map.keySet()) {
            String value = map.get(key);
            ++i;
            if (TextUtils.isEmpty(value)) {
                continue;
            }

            sb.append(key);
            sb.append("=");
            sb.append(value);

            if (i == size) {
                break;
            }

            sb.append("&");
        }

        return sb.toString();
    }

}
