package com.xqt360.requests.interceptors;


import org.jsoup.Connection;

@FunctionalInterface
public interface ResponseInterceptor {

    <T> T use(T response);

}
