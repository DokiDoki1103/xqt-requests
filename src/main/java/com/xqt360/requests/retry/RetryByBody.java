package com.xqt360.requests.retry;

@FunctionalInterface
public interface RetryByBody extends RetryCondition<String> {
    @Override
    boolean shouldRetry(String body);
}
