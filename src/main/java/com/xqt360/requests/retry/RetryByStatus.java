package com.xqt360.requests.retry;

@FunctionalInterface
public interface RetryByStatus extends RetryCondition<Integer>{
    @Override
    boolean shouldRetry(Integer statusCode);
}
