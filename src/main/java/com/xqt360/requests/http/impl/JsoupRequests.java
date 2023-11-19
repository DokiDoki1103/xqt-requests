package com.xqt360.requests.http.impl;

import com.xqt360.requests.config.ProxyConfig;
import com.xqt360.requests.config.RequestConfig;
import com.xqt360.requests.config.RetryConfig;
import com.xqt360.requests.http.HttpRequests;
import com.xqt360.requests.utils.ParseUtils;
import com.xqt360.requests.utils.ProxyUtils;
import com.xqt360.requests.utils.RequestsUtils;
import com.xqt360.requests.utils.SslUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.Optional;


@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@NoArgsConstructor
public class JsoupRequests extends HttpRequests {

    private final Connection session = Jsoup.newSession()
            .maxBodySize(1024 * 1024 * 1024)
            .ignoreContentType(true)
            .ignoreHttpErrors(true);
    //标准来说应该区分域名来设置cookies，这里为了方便，直接写一起，后期有需要再改动
    //是否设置代理IP
    private Proxy proxy = null;

    public JsoupRequests(String proxyIpString) {
        ProxyConfig proxyConfig = ProxyUtils.formatProxyString(proxyIpString);
        super.setProxyConfig(proxyConfig);
        this.setProxyIp(proxyConfig);
    }


    static {
        SslUtil.ignoreSsl();
    }

    @Override
    protected <D, T> T execute(Connection.Method method, RequestConfig<D> config, Class<T> cls, int retryCount) {
        try {
            super.requestInterceptor.use(config);

            RetryConfig retryConfig = Optional.ofNullable(config.getRetryConfig()).orElseGet(RetryConfig::new);

            Connection connection = session.newRequest()
                    .url(RequestsUtils.getUrl(config))
                    .method(method)
                    .followRedirects(config.isFollowRedirects())
                    .timeout(config.getTimeout());
            if (config.getCookies() != null) {
                config.getCookies().forEach(connection::cookie);
            }

            RequestsUtils.setProxy(connection, config, this.proxy);//设置代理，优先设置请求配置类的代理
            RequestsUtils.setDefaultHeadersAndBody(config, method, connection);//设置默认请求头和发送的数据

            Connection.Response response = connection.execute();
            log.info("{} {} 请求 {}", method.name(), response.statusCode(), config.getUrl());
            if (RequestsUtils.needRetry(retryConfig, response, retryCount, super.MAX_RETRY_COUNT)) {
                log.error("满足条件重试条件,正在重试第{}次", retryCount + 1);
                Thread.sleep((long) retryConfig.getDelay() * (retryCount + 1));
                return execute(method, config, cls, retryCount + 1);
            }
            return super.responseInterceptor.use(ParseUtils.parseResponse(response, cls));
        } catch (Exception e) {
            e.printStackTrace();
            return super.defaultExceptionExecute(method, config, cls, retryCount, e);
        } finally {
            super.restoreDefaultProxyIp(super.proxyConfig);//恢复为默认的代理IP
        }
    }

    @Override
    public void setProxyIp(ProxyConfig proxyConfig) {
        if (proxyConfig == null) {
            return;
        }
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyConfig.getHost(), proxyConfig.getPort()));

        if (proxyConfig.getUsername() != null && !proxyConfig.getUsername().isEmpty()) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(proxyConfig.getUsername(), proxyConfig.getPassword().toCharArray());
                }
            });
        }
    }
}
