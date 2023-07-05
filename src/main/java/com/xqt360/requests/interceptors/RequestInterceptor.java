package com.xqt360.requests.interceptors;

import com.xqt360.requests.config.RequestConfig;

/**
 * 请求前置拦截器
 */
@FunctionalInterface
public interface RequestInterceptor {
    <D> void use(RequestConfig<D> requestConfig);
}
