package com.baiiu.zhihudaily.util.net.http;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by baiiu on 15/11/16.
 * 继承listener,并进行转换
 */
public class SimpleStringRequest extends Request<String> {

    private RequestCallBack mCallBack;
    private Map<String, String> mParams;
    DefaultRetryPolicy defaultRetryPolicy = new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    /**
     * Get调用,不用传参数
     */
    public SimpleStringRequest(String url, RequestCallBack callBack) {
        super(Method.GET, url, callBack);
        setRetryPolicy(defaultRetryPolicy);
        mCallBack = callBack;
    }

    /**
     * 非GET调用,必须传入参数
     */
    public SimpleStringRequest(int method, String url, Map<String, String> params, RequestCallBack callBack) {
        super(method, url, callBack);
        setRetryPolicy(defaultRetryPolicy);
        mCallBack = callBack;
        mParams = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams == null ? super.getParams() : mParams;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }


    @Override
    protected void deliverResponse(String response) {
        if (mCallBack != null) {
            mCallBack.onResponse(response);
        }
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public void cancel() {
        super.cancel();
        mParams = null;
        mCallBack = null;
    }

}
