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
  public long id;
  public String ga_prefix;
  public String title;
  public boolean isRead;

  public int mType = DailyNewsAdapter.TYPE_NEWS;

  public Story() {
  }

  public Story(long id, List<String> images, String title) {
    this.images = images;
    this.id = id;
    this.title = title;
  }
}
