package com.baiiu.zhihudaily.data.entity.mapper;

import android.text.TextUtils;
import com.baiiu.zhihudaily.data.entity.DailyDetail;
import com.baiiu.zhihudaily.data.entity.SavedDailyDetail;
import com.baiiu.zhihudaily.data.entity.SavedStory;
import com.baiiu.zhihudaily.data.entity.SavedTopStory;
import com.baiiu.zhihudaily.data.entity.Story;
import com.baiiu.zhihudaily.data.entity.TopStory;
import com.baiiu.zhihudaily.data.util.CommonUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * author: baiiu
 * date: on 16/4/7 15:34
 * description:
 */
public class MappingConvertUtil {

    public static List<SavedTopStory> toSavedTopStory(List<TopStory> topStories) {
        if (CommonUtil.isEmpty(topStories)) {
            return null;
        }

        List<SavedTopStory> savedTopStories = new ArrayList<>();
        for (TopStory topStory : topStories) {
            savedTopStories.add(new SavedTopStory(topStory.id, topStory.image, topStory.title));
        }
        return savedTopStories;
    }

    public static List<TopStory> toTopStory(List<SavedTopStory> savedTopStories) {
        if (CommonUtil.isEmpty(savedTopStories)) {
            return null;
        }

        List<TopStory> topStories = new ArrayList<>();

        for (SavedTopStory story : savedTopStories) {
            topStories.add(new TopStory(story.getId(), story.getImage(), story.getTitle()));
        }

        return topStories;
    }

    public static List<SavedStory> toSavedStory(List<Story> stories, String date) {
        if (CommonUtil.isEmpty(stories)) {
            return null;
        }

        List<SavedStory> savedStories = new ArrayList<>();
        int i = 0;
        for (Story story : stories) {
            List<String> images = story.images;

            savedStories.add(
                    new SavedStory(story.id, CommonUtil.isEmpty(images) ? "" : story.images.get(0), story.title, date,
                                   i));

            ++i;
        }

        return savedStories;
    }

    public static List<Story> toStory(List<SavedStory> savedStories) {
        if (CommonUtil.isEmpty(savedStories)) {
            return null;
        }

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

    public static SavedDailyDetail toSavedDailyDetail(DailyDetail dailyDetail) {
        return dailyDetail == null ? null
                : new SavedDailyDetail(dailyDetail.id, dailyDetail.body, dailyDetail.image_source, dailyDetail.image,
                                       dailyDetail.title, dailyDetail.share_url, dailyDetail.js.toString(),
                                       dailyDetail.css.toString());
    }

    public static DailyDetail toDailyDetail(SavedDailyDetail savedDailyDetail) {
        if (savedDailyDetail == null) {
            return null;
        }

        DailyDetail dailyDetail = new DailyDetail();
        dailyDetail.id = savedDailyDetail.getId();
        dailyDetail.body = savedDailyDetail.getBody();
        dailyDetail.image_source = savedDailyDetail.getImage_source();
        dailyDetail.image = savedDailyDetail.getImage();
        dailyDetail.title = savedDailyDetail.getTitle();
        dailyDetail.share_url = savedDailyDetail.getShare_url();
        return dailyDetail;
    }
}
