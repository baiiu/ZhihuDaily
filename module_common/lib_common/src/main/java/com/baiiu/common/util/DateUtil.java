package com.baiiu.common.util;

import android.text.TextUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * author: baiiu
 * date: on 16/4/7 19:37
 * description:将date前置一天
 */
public class DateUtil {

    /**
     * @param date 20160331
     */
    public static String getYesterdayDate(String date) {
        if (TextUtils.isEmpty(date)) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        try {
            Date parseDate = simpleDateFormat.parse(date);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parseDate);

            int day = calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE, day - 1);

            return simpleDateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
