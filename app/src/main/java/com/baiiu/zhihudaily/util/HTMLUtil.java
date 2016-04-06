package com.baiiu.zhihudaily.util;

/**
 * author: baiiu
 * date: on 16/4/6 12:57
 * description:
 */
public class HTMLUtil {


    public static StringBuffer handleHtml(String body,boolean isNight) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"css/detail.css\" ></head>");
        stringBuffer.append(isNight ? "<body class=\"night\">" : "<body>");
        stringBuffer.append(body);
        stringBuffer.append("</body></html>");
        return stringBuffer;
    }
}
