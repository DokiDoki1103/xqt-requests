package com.xqt360.requests.retry;

@FunctionalInterface
public interface RetryByBody extends RetryCondition<String> {
    boolean shouldRetry(String body);
}
