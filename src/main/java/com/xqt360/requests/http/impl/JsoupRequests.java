package com.xqt360.requests.http.impl;

import com.xqt360.requests.config.RequestConfig;
import com.xqt360.requests.config.RetryConfig;
import com.xqt360.requests.http.HttpRequests;
import com.xqt360.requests.utils.ParseUtils;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@NoArgsConstructor
public class JsoupRequests extends HttpRequests {
    private final Map<String, String> cookiesStore = new HashMap<>();
    //是否设置代理IP
    private Proxy proxy = null;


    public JsoupRequests(String proxyIp) {
        this.setProxyIp(proxyIp);
    }

    public JsoupRequests(String proxyIp, String username, String password) {
        this.setProxyIp(proxyIp, username, password);
    }

    @Override
    protected <D, T> T execute(Connection.Method method, RequestConfig<D> config, Class<T> cls, int retryCount) {
        try {
            super.requestInterceptor.use(config);

            SslUtil.ignoreSsl();//忽略SSL错误

            RetryConfig retryConfig = Optional.ofNullable(config.getRetryConfig()).orElseGet(RetryConfig::new);
            Connection.Response response;

            Optional.ofNullable(config.getCookies()).ifPresent(cookiesStore::putAll);

            Connection connection = Jsoup.connect(RequestsUtils.getUrl(config))
                    .maxBodySize(1024 * 1024 * 1024)
                    .method(method)
                    .followRedirects(config.isFollowRedirects())
                    .cookies(cookiesStore)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .timeout(config.getTimeout());


            RequestsUtils.setProxy(connection, config, this.proxy);//设置代理，优先设置请求配置类的代理
            RequestsUtils.setDefaultHeadersAndBody(config, method, connection);//设置默认请求头和发送的数据

            response = connection.execute();
            log.info("{} {} 请求 {}", method.name(), response.statusCode(), config.getUrl());
            if (RequestsUtils.needRetry(retryConfig, response, retryCount, super.MAX_RETRY_COUNT)) {
                log.error("满足条件重试条件,正在重试第{}次", retryCount + 1);
                Thread.sleep((long) retryConfig.getDelay() * (retryCount + 1));
                return execute(method, config, cls, retryCount + 1);
            }
            // 自动携带此次请求官方返回的Cookie
            cookiesStore.putAll(response.cookies());
            // 对返回内容的解析为用户指定的格式，如果不指定，那么将会返回默认的String格式，
            // 如果需要拿到http响应头信息，请指定为 Connection.Response 类型
            T t = ParseUtils.parseResponse(response, cls);

            return super.responseInterceptor.use(t);
        } catch (Exception e) {
            return super.defaultExceptionExecute(method, config, cls, retryCount, e);
        } finally {
            super.restoreDefaultProxyIP();//恢复为默认的代理IP
        }
    }

    @Override
    public void setProxyIp(String proxyIp) {
        super.setProxyIpString(proxyIp);
        String[] split = proxyIp.split(":");
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(split[0], Integer.parseInt(split[1])));
    }

    @Override
    public void setProxyIp(String proxyIp, String username, String password) {
        super.setProxyIpString(proxyIp + ":" + username + ":" + password);
        Authenticator.setDefault(new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });
        setProxyIp(proxyIp);
    }
}
