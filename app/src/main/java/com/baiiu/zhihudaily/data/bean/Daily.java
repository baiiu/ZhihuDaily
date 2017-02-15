package com.baiiu.zhihudaily.data.bean;

import java.util.List;

/**
 * author: baiiu
 * date: on 16/4/5 15:21
 * description:
 */
public class Daily {
    public String date;
    public List<Story> stories;
    public List<TopStory> top_stories;

    /**
     * 根据业务需求,第一次访问本地数据,之后有网的话访问网络数据.
     *
     * 根据concat的顺序,第一次走本地,所以默认为true.在之后置为false.
     */
    private static boolean isAvailable = true;

    public static boolean isAvailable() {
        return isAvailable;
    }

    public static void setAvailable(boolean isAvailable) {
        Daily.isAvailable = isAvailable;
    }
}
