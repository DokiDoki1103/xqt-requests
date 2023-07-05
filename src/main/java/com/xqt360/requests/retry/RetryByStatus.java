package com.xqt360.requests.retry;

@FunctionalInterface
public interface RetryByStatus extends RetryCondition<Integer>{
    boolean shouldRetry(Integer statusCode);
}
