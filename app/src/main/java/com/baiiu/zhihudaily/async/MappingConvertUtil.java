package com.baiiu.zhihudaily.async;

import android.text.TextUtils;
import baiiu.greendao.gen.SavedStory;
import baiiu.greendao.gen.SavedTopStory;
import com.baiiu.zhihudaily.pojo.Story;
import com.baiiu.zhihudaily.pojo.TopStory;
import com.baiiu.zhihudaily.util.CommonUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * author: baiiu
 * date: on 16/4/7 15:34
 * description:
 */
public class MappingConvertUtil {

  public static List<SavedTopStory> toSavedTopStory(List<TopStory> topStories) {

    List<SavedTopStory> savedTopStories = new ArrayList<>();
    for (TopStory topStory : topStories) {
      savedTopStories.add(new SavedTopStory(topStory.id, topStory.image, topStory.title));
    }
    return savedTopStories;
  }

  public static List<TopStory> toTopStory(List<SavedTopStory> savedTopStories) {
    List<TopStory> topStories = new ArrayList<>();

    for (SavedTopStory story : savedTopStories) {
      topStories.add(new TopStory(story.getId(), story.getImage(), story.getTitle()));
    }

    return topStories;
  }

  public static List<SavedStory> toSavedStory(List<Story> stories, String date) {
    List<SavedStory> savedStories = new ArrayList<>();
    for (Story story : stories) {
      List<String> images = story.images;

      savedStories.add(
          new SavedStory(story.id, CommonUtil.isEmpty(images) ? "" : story.images.get(0),
              story.title, date));
    }

    return savedStories;
  }

  public static List<Story> toStory(List<SavedStory> savedStories) {
    List<Story> list = new ArrayList<>();

    for (SavedStory story : savedStories) {

      String image = story.getImage();

      List<String> images = null;
      if (!TextUtils.isEmpty(image)) {
        images = new ArrayList<>();
        images.add(image);
      }

      list.add(new Story(story.getId(), images, story.getTitle()));
    }

    return list;
  }
}
