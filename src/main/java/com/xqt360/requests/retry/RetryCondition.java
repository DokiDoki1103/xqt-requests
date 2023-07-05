package com.xqt360.requests.retry;

public interface RetryCondition<T> {
    boolean shouldRetry(T statusCode);
}
