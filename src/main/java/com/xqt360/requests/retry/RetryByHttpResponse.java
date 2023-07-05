package com.xqt360.requests.retry;

import org.apache.http.HttpResponse;

@FunctionalInterface
public interface RetryByHttpResponse extends RetryCondition<HttpResponse> {
    boolean shouldRetry(HttpResponse response);

}
