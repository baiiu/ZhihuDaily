package com.baiiu.zhihudaily.pojo;

import com.baiiu.zhihudaily.adapter.DailyNewsAdapter;

import java.util.List;

/**
 * author: baiiu
 * date: on 16/4/5 15:22
 * description:
 */
public class Story {
  public List<String> images;
  private int type;
  public String id;
  public String ga_prefix;
  public String title;
  public boolean isRead;

  public int mType = DailyNewsAdapter.TYPE_NEWS;
}
