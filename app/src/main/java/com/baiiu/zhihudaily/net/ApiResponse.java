package com.baiiu.zhihudaily.net;

/**
 * Created by baiiu on 15/10/29.
 * Model类基础
 */
public class ApiResponse<T> {
    public int code;
    public T data;
    public String msg;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return code + ", " + msg + ", " + data.toString();
    }
}
