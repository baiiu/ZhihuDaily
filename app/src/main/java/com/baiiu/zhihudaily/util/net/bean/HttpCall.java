package com.baiiu.zhihudaily.util.net.bean;

import com.android.volley.Request.Method;
import com.baiiu.zhihudaily.util.net.util.MapParamsUtil;

import java.util.Map;

/**
 * Created by baiiu on 15/11/16.
 * 封装请求
 */
public class HttpCall {
  /**
   * 用于下拉刷新和上拉加载，标示是否第一次刷新
   */
  private boolean isFirst = true;
  /**
   * 请求方式 {@link Method}
   */
  public int method = Method.GET;

  /**
   * GET方式时: ? 前路径参数
   */
  private String path;

  /**
   * GET方式时: ? 后参数
   */
  public Map<String, String> params;

  public HttpCall() {
  }

  public HttpCall(String path) {
    this.method = Method.GET;
    this.path = path;
  }

  public HttpCall(String path, Map<String, String> params) {
    this.method = Method.GET;
    this.path = path;
    this.params = params;
  }

  public HttpCall(int method, String path) {
    this.method = method;
    this.path = path;
  }

  public HttpCall(int method, String path, Map<String, String> params) {
    this.method = method;
    this.path = path;
    this.params = params;
  }

  public void setMethod(int method) {
    if (method < 0 || method > 7) {
      //默认为POST请求
      method = 1;
      return;
    }

    this.method = method;
  }

  public String getUrl() {
    if (method == Method.GET) {
      return MapParamsUtil.toParams(path, params);
    }

    return path;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Map<String, String> getParams() {
    return params;
  }

  public void setParams(Map<String, String> params) {
    this.params = params;
  }

  public int getMethod() {
    return method;
  }

  public boolean isFirst() {
    return isFirst;
  }

  public void setIsFirst(boolean isFirst) {
    if (this.isFirst != isFirst) {
      this.isFirst = isFirst;
    }
  }
}
