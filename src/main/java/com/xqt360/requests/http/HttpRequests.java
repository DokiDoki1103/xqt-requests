package com.xqt360.requests.http;

import com.xqt360.requests.config.ProxyConfig;
import com.xqt360.requests.config.RequestConfig;
import com.xqt360.requests.config.RetryConfig;
import com.xqt360.requests.interceptors.RequestInterceptor;
import com.xqt360.requests.interceptors.ResponseInterceptor;
import com.xqt360.requests.utils.FileUtils;
import com.xqt360.requests.utils.RequestsUtils;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;

@Data
@Slf4j
@NoArgsConstructor
public abstract class HttpRequests implements Requests {
    protected final int MAX_RETRY_COUNT = 20;// 防止递归或者其他意外错误，最大重试次数
    protected ProxyConfig proxyConfig;

    /**
     * 默认的请求拦截器。拦截RequestConfig
     */
    protected RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public <D> void use(RequestConfig<D> requestConfig) {

        }
    };
    /**
     * 默认的响应，拦截你返回的请求
     */
    protected ResponseInterceptor responseInterceptor = new ResponseInterceptor() {
        @Override
        public <T> T use(T response) {
            return response;
        }
    };

    protected abstract <D, T> T execute(Connection.Method method, RequestConfig<D> config, Class<T> cls, int retryCount);

    @Override
    public String get(String url) {
        return execute(Connection.Method.GET, RequestConfig.builder().url(url).build(), String.class, 0);
    }

    @Override
    public <T> T get(String url, Class<T> cls) {
        return execute(Connection.Method.GET, RequestConfig.builder().url(url).build(), cls, 0);
    }

    @Override
    public <D, T> T get(RequestConfig<D> config, Class<T> cls) {
        return execute(Connection.Method.GET, config, cls, 0);
    }

    @Override
    public <D> String get(RequestConfig<D> config) {
        return execute(Connection.Method.GET, config, String.class, 0);
    }

    @Override
    public <D, T> T post(RequestConfig<D> config, Class<T> cls) {
        return execute(Connection.Method.POST, config, cls, 0);
    }

    @Override
    public <T> T post(String url, Class<T> cls) {
        return execute(Connection.Method.POST, RequestConfig.builder().url(url).build(), cls, 0);
    }

    @Override
    public String post(String url) {
        return execute(Connection.Method.POST, RequestConfig.builder().url(url).build(), String.class, 0);
    }

    @Override
    public <D, T> T post(String url, D data, Class<T> cls) {
        return execute(Connection.Method.POST, RequestConfig.<D>builder().url(url).data(data).build(), cls, 0);
    }

    @Override
    public <D> String post(String url, D data) {
        return execute(Connection.Method.POST, RequestConfig.<D>builder().url(url).data(data).build(), String.class, 0);
    }

    @Override
    public <D> String execute(RequestConfig<D> config) {
        return execute(config.getMethod(), config, String.class, 0);
    }

    @Override
    public <D, T> T execute(RequestConfig<D> config, Class<T> cls) {
        return execute(config.getMethod(), config, cls, 0);
    }

    @Override
    public void  download(String url,String path) {
        byte[] bytes = get(url, byte[].class);
        FileUtils.saveFile(bytes,path);
    }

    @Override
    public void setRequestInterceptor(RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
    }

    @Override
    public void setResponseInterceptor(ResponseInterceptor responseInterceptor) {
        this.responseInterceptor = responseInterceptor;
    }

    @Override
    public abstract void setProxyIp(ProxyConfig proxyConfig);//格式：127.0.0.1:8888@root:password


    /**
     * 恢复默认代理IP，因为每个请求会更改可能会代理ip
     */
    protected void restoreDefaultProxyIp(ProxyConfig proxyConfig) {
        this.setProxyIp(proxyConfig);
    }

    protected <D, T> T defaultExceptionExecute(Connection.Method method, RequestConfig<D> config, Class<T> cls, int retryCount, Exception e) {
        //可能会抛出重试异常，如果抛出程序终止
        RetryConfig retryConfig = config.getRetryConfig();
        RequestsUtils.assertRetry(retryConfig, e, retryCount, MAX_RETRY_COUNT);
        log.error("发生{}异常,正在重试第{}次", e.getClass().getName(), retryCount + 1);
        try {
            Thread.sleep((long) retryConfig.getDelay() * (retryCount + 1));
        } catch (InterruptedException ignored) {
        }
        return this.execute(method, config, cls, retryCount + 1);
    }
}
