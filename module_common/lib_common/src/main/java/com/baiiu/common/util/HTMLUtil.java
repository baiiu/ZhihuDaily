package com.baiiu.common.util;

/**
 * author: baiiu
 * date: on 16/4/6 12:57
 * description:
 */
public class HTMLUtil {


    public static StringBuffer handleHtml(String body, boolean isNight) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"css/detail.css\" >");
        stringBuffer.append(
                "<meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, width=device-width”></head>");
        stringBuffer.append(isNight ? "<body class=\"night\">" : "<body>");
        stringBuffer.append(body);
        stringBuffer.append("</body></html>");
        return stringBuffer;
    }
}
