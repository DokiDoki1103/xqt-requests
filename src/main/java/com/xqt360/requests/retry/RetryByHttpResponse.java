package com.xqt360.requests.retry;

import org.apache.http.client.methods.CloseableHttpResponse;

@FunctionalInterface
public interface RetryByHttpResponse extends RetryCondition<CloseableHttpResponse> {
    @Override
    boolean shouldRetry(CloseableHttpResponse response);

}
