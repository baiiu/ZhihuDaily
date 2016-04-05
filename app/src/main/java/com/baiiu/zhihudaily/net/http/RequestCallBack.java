package com.baiiu.zhihudaily.net.http;

import com.android.volley.NetworkResponse;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baiiu.zhihudaily.net.ApiResponse;
import com.baiiu.zhihudaily.net.util.GsonUtil;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by baiiu on 15/11/15.
 * 网络回调
 */
public abstract class RequestCallBack<T extends ApiResponse> implements Listener<String>, ErrorListener {

    private Type mType;

    public abstract void onSuccess(T response);

    public abstract void onFailure(int statusCode, String errorString);

    public void onCustomFailuer(int customCode, T t) {
    }

    public RequestCallBack() {
    }


    public RequestCallBack(Type type) {
        this.mType = type;
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse == null) {
            onFailure(HttpConstant.ERROR_NOT_KNOWN, "error not known");
            return;
        }

        int statusCode = networkResponse.statusCode;
        String errorString;

        try {
            errorString = new String(networkResponse.data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            errorString = new String(networkResponse.data);
        }

        onFailure(statusCode, errorString);
    }

    @Override
    public void onResponse(String response) {
        if (mType != null) {
            T t = GsonUtil.parseJson(response, mType);
            onSuccess(t);
            return;
        }

        Type type = null;
        if (getClass().getGenericSuperclass() instanceof ParameterizedType) {
            type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }

        T t = null;
        if (type != null) {
            t = GsonUtil.parseJson(response, type);
        }

        onSuccess(t);
    }

}
