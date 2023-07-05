package com.xqt360.requests.retry;

import org.jsoup.Connection;

@FunctionalInterface
public interface RetryByJsoupResponse extends RetryCondition<Connection.Response>{
    @Override
    boolean shouldRetry(Connection.Response response);
}
