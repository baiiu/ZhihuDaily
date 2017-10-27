package com.baiiu.common.util;

import android.widget.TextView;
import com.baiiu.lib_common.R;
import java.util.Map;

import static com.baiiu.common.util.PreferenceUtil.instance;


/**
 * author: baiiu
 * date: on 16/2/19 14:45
 * description:
 */
// @formatter:off
public class ReadedListUtil {
public static final String UI_MODE = "Ui_mode";

  public static void saveToReadedList(String prefName, String id) {
    PreferenceUtil preferenceUtil = PreferenceUtil.instance(prefName);

    if (preferenceUtil.getSettings().getAll().size() > 100) {
      preferenceUtil.getSettings().edit().clear();
    }

    preferenceUtil.put(id, "T").commit();
  }

  public static Map<String, String> getReadedMap(String prefName) {
    return (Map<String, String>) PreferenceUtil.instance(prefName).getSettings().getAll();
  }

  public static void setTextColor(TextView textView, boolean isRead) {
    if (PreferenceUtil.instance().get(UI_MODE, true)) {
      textView.setTextColor(isRead ? UIUtil.getColor(R.color.news_read_Day)
          : UIUtil.getColor(R.color.news_unread_Day));
    } else {
      textView.setTextColor(isRead ? UIUtil.getColor(R.color.news_read_Night)
          : UIUtil.getColor(R.color.news_unread_Night));
    }
  }
}
