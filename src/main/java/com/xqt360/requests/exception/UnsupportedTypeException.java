package com.xqt360.requests.exception;

import com.xqt360.requests.retry.RetryCondition;

public class UnsupportedTypeException extends IllegalArgumentException {

    public <T> UnsupportedTypeException(Class<T> cls) {
        super("HTTP返回时:不支持您设置的类型:"+cls.getName());
    }

    public <T> UnsupportedTypeException(RetryCondition retryCondition) {
        super("异常重试条件:无法确定子类类型，无法重试;提示：new时请不要直接使用RetryCondition类应该使用其子类");
    }
}
