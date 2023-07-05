package com.xqt360.requests.http.impl;

import com.xqt360.requests.config.RequestConfig;
import com.xqt360.requests.config.RetryConfig;
import com.xqt360.requests.http.HttpRequests;
import com.xqt360.requests.http.Requests;
import com.xqt360.requests.utils.ParseUtils;
import com.xqt360.requests.utils.RequestsUtils;
import com.xqt360.requests.utils.SslUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.methods.*;

import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.*;

import org.jsoup.Connection;

import java.io.IOException;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class HttpClientRequests extends HttpRequests implements Requests {
    private DefaultHttpClient httpClient;

    public HttpClientRequests() {
        this.httpClient = RequestsUtils.getHttpsClient("");
    }

    public HttpClientRequests(boolean isHttps, String proxyIp) {
        this.httpClient = isHttps ? RequestsUtils.getHttpsClient(proxyIp) : RequestsUtils.getHttpClient(proxyIp);
    }

    public HttpClientRequests(boolean isHttps, String proxyIp, String username, String password) {
        this.httpClient = isHttps ? RequestsUtils.getHttpsClient(proxyIp, username, password) : RequestsUtils.getHttpClient(proxyIp, username, password);
    }


    @Override
    protected <D, T> T execute(Connection.Method method, RequestConfig<D> config, Class<T> cls, int retryCount) {
        SslUtil.ignoreSsl();
        HttpUriRequest  httpUriRequest = RequestsUtils.createRequest(config, method);

        try (CloseableHttpResponse response = httpClient.execute(httpUriRequest)) {
            log.info("{} {} 请求 {}", method.name(), response.getStatusLine().getStatusCode(), config.getUrl());
            RetryConfig retryConfig = config.getRetryConfig();
            if (RequestsUtils.needRetry(retryConfig, response,retryCount,super.MAX_RETRY_COUNT)) {
                log.error("满足条件重试条件,正在重试第{}次", retryCount + 1);
                Thread.sleep((long) retryConfig.getDelay() * (retryCount + 1));
                closeResponse(response);//在这里其实并没有调用finally，因为方法还没有结束，必须手动关闭资源。否则会报错
                return execute(method, config, cls, retryCount + 1);
            }
            T t = ParseUtils.parseResponse(response, cls);

            return super.responseInterceptor.use(t);

        } catch (Exception e) {
            return super.defaultExceptionExecute(method, config, cls, retryCount, e);
        }finally {
            super.restoreDefaultProxyIP();//恢复为默认的代理IP
        }
    }

    @Override
    public void setProxyIp(String proxyIp) {
        setProxyIp(proxyIp, "", "");
    }

    @Override
    public void setProxyIp(String proxyIp, String username, String password) {
        String[] split = proxyIp.split(":");
        String proxyHost = split[0];
        int proxyPort = Integer.parseInt(split[1]);

        HttpHost proxy = new HttpHost(proxyHost, proxyPort, "http");
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        AuthScope auth = new AuthScope(proxyHost, proxyPort);
        Credentials credentials = new org.apache.http.auth.NTCredentials(username, password, "", "");
        httpClient.getCredentialsProvider().setCredentials(auth, credentials);
    }

    // 关闭response
    private void closeResponse(CloseableHttpResponse response) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
