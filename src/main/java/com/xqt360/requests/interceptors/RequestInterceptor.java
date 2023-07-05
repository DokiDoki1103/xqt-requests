package com.xqt360.requests.interceptors;

import com.xqt360.requests.config.RequestConfig;

@FunctionalInterface
public interface RequestInterceptor {
    <D> void use(RequestConfig<D> requestConfig);
}
