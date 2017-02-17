package com.baiiu.zhihudaily.base.rx.exception;

/**
 * author: baiiu
 * date: on 17/1/3 16:22
 * description: 解析网络返回ApiResponse类，抛出文字用于显示
 */
public class NotResponseException extends RuntimeException {

    public NotResponseException(String message) {
        super(message);
    }

}
