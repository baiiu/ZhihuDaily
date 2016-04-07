package com.baiiu.zhihudaily.pojo;

/**
 * author: baiiu
 * date: on 16/4/5 15:24
 * description:
 */
public class TopStory {
  public String image;
  public int type;
  public long id;
  public String ga_prefix;
  public String title;

  public TopStory() {
  }

  public TopStory(long id, String image, String title) {
    this.image = image;
    this.title = title;
    this.id = id;
  }
}
