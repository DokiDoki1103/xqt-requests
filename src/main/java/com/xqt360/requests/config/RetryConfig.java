package com.xqt360.requests.config;

import com.xqt360.requests.retry.RetryCondition;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor

public class RetryConfig {
    //只有能正常请求通过对方才会走此重试条件包括 503、403 状态码均视为请求通过
    private RetryCondition retryCondition;//重试条件；默认null即没有发生异常的情况下不重试
    private int maxAttempts = 5;//最大重试次数,包括第一次请求。设置多少最终会发送多少次请求；默认5
    private int delay = 1000;//重试间隔；默认100
    private float multiplier = 1;//幂，默认1
    //没有请求通过，抛出java的异常
    private Class<? extends Throwable>[] includeException;//包含这些异常才会重试；默认所有异常都重试
    private Class<? extends Throwable>[] excludeException;//排除某些异常；默认不排除任何异常

    public RetryConfig(RetryCondition retryCondition, int maxAttempts, int delay, float multiplier, Class<? extends Throwable>[] includeException, Class<? extends Throwable>[] excludeException) {
        this.retryCondition = retryCondition;
        this.maxAttempts = maxAttempts <= 0 ? this.maxAttempts : maxAttempts;
        this.delay = delay <= 0 ? this.delay : delay;
        this.multiplier = multiplier <=0 ? this.multiplier : multiplier;
        this.includeException = includeException;
        this.excludeException = excludeException;
    }
}
