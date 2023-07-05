package com.xqt360.requests.http;

import com.xqt360.requests.config.RequestConfig;
import com.xqt360.requests.interceptors.RequestInterceptor;
import com.xqt360.requests.interceptors.ResponseInterceptor;

public interface Requests {

    String get(String url);

    <T> T get(String url, Class<T> cls);

    <D, T> T get(RequestConfig<D> config, Class<T> cls);

    <D> String get(RequestConfig<D> config);

    <D, T> T post(RequestConfig<D> config, Class<T> cls);

    <T> T post(String url, Class<T> cls);

    String post(String url);

    <D, T> T post(String url, D data, Class<T> cls);

    <D> String post(String url, D data);

    <D> String execute(RequestConfig<D> config);

    <D, T> T execute(RequestConfig<D> config, Class<T> cls);

    void download(String url,String path);

    void setRequestInterceptor(RequestInterceptor requestInterceptor);

    void setResponseInterceptor(ResponseInterceptor responseInterceptor);

    void setProxyIp(String proxyIp);

    void setProxyIp(String proxyIp, String username, String password);

}
