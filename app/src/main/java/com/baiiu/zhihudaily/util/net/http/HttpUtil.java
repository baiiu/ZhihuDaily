package com.baiiu.zhihudaily.util.net.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baiiu.zhihudaily.util.net.bean.HttpCall;
import com.baiiu.zhihudaily.util.net.util.HttpNetUtil;
import com.baiiu.zhihudaily.util.net.util.OkHttpStack;
import com.baiiu.zhihudaily.util.UIUtil;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by baiiu on 15/11/10.
 * <p/>
 * 封装所有的网络方式
 */
public class HttpUtil implements HttpConstant {
    private static final HttpUtil httpUtil = new HttpUtil();

    /**
     * statusCode < 200 || statusCode > 299 抛出error,响应errorListener
     */
    private RequestQueue mRequestQueue;

    public static HttpUtil get() {
        return httpUtil;
    }

    private HttpUtil() {
        mRequestQueue = getRequestQueue();
    }

    /**
     * 获取请求队列
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            OkHttpStack okHttpStack = new OkHttpStack();
            mRequestQueue = Volley.newRequestQueue(UIUtil.getContext(), okHttpStack);
        }

        return mRequestQueue;
    }

    private void addRequest(Request request) {
        addRequest(request, null);
    }

    private void addRequest(Request request, Object tag) {
        if (isNetConnected(request)) {
            if (tag != null) {
                request.setTag(tag);
            }
            getRequestQueue().add(request);
        }
    }

    private boolean isNetConnected(Request request) {
        if (HttpNetUtil.isConnected()) {
            return true;
        }

        request.deliverError(getError());
        return false;
    }

    public void cancelAll(Object object) {
        getRequestQueue().cancelAll(object);
    }

    public VolleyError getError() {
        String s = STRING_NO_HTTP;
        byte[] bytes;
        try {
            bytes = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            bytes = s.getBytes();
        }
        NetworkResponse networkResponse = new NetworkResponse(NO_HTTP, bytes, null, false);
        return new VolleyError(networkResponse);
    }


    //------------------------------------------------------------------


    /**
     * GET
     */
    public void callGet(HttpCall httpCall, RequestCallBack callBack) {
        addRequest(new SimpleStringRequest(httpCall.getUrl(), callBack));
    }

    /**
     * GET+tag
     */
    public void callGet(HttpCall httpCall, RequestCallBack callBack, String tag) {
        addRequest(new SimpleStringRequest(httpCall.getUrl(), callBack), tag);
    }

    /**
     * 自己设定
     */
    public void call(HttpCall httpCall, RequestCallBack callBack) {
        addRequest(new SimpleStringRequest(httpCall.method, httpCall.getUrl(), httpCall.params, callBack));
    }

    /**
     * call + tag
     */
    public void call(HttpCall httpCall, RequestCallBack callBack, String tag) {
        int method = httpCall.method;
        if (Method.GET == method) {
            callGet(httpCall, callBack, tag);
        } else {
            addRequest(new SimpleStringRequest(httpCall.method, httpCall.getUrl(), httpCall.params, callBack), tag);
        }
    }


    /**
     * Get
     */
    public void httpGet(String url, RequestCallBack callBack) {
        addRequest(new SimpleStringRequest(url, callBack));
    }

    /**
     * Get + Tag
     */
    public void httpGet(String url, RequestCallBack callBack, Object tag) {
        addRequest(new SimpleStringRequest(url, callBack), tag);
    }

    /**
     * POST + Params
     */
    public void httpPost(String url, Map<String, String> params, RequestCallBack callBack) {
        addRequest(new SimpleStringRequest(Method.POST, url, params, callBack));
    }

    public void httpPut(String url, Map<String, String> params, RequestCallBack callBack, Object tag) {
        addRequest(new SimpleStringRequest(Method.PUT, url, params, callBack), tag);
    }

    /**
     * POST + Params + Tag
     */
    public void httpPost(String url, Map<String, String> params, RequestCallBack callBack, Object tag) {
        addRequest(new SimpleStringRequest(Method.POST, url, params, callBack), tag);
    }

    /**
     * Delete + Params + Tag
     */
    public void httpDelete(String url, Map<String, String> params, RequestCallBack callBack, Object tag) {
        addRequest(new SimpleStringRequest(Method.DELETE, url, params, callBack), tag);
    }

    /**
     * delete + tag
     */
    public void httpDelete(String url, RequestCallBack callBack, Object tag) {
        addRequest(new SimpleStringRequest(Method.DELETE, url, null, callBack), tag);
    }


    /**
     * StringRequest Get
     */
    public void httpStringRequestGet(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        addRequest(new StringRequest(url, listener, errorListener));
    }


    public void callStringRequest(final HttpCall httpCall, String tag, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        if (httpCall == null) {
            return;
        }

        int method = httpCall.method;

        if (method == Method.GET) {
            addRequest(new StringRequest(method, httpCall.getUrl(), listener, errorListener), tag);
        } else if (method == Method.POST) {
            addRequest(new StringRequest(method, httpCall.getPath(), listener, errorListener) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return httpCall.params;
                }
            }, tag);
        }

    }

}
