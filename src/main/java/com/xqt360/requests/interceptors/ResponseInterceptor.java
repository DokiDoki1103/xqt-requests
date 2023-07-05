package com.xqt360.requests.interceptors;

@FunctionalInterface
public interface ResponseInterceptor {
    <T> T use(T response);
}
