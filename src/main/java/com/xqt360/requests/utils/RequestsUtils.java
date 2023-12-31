package com.xqt360.requests.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xqt360.requests.config.ProxyConfig;
import com.xqt360.requests.config.RequestConfig;
import com.xqt360.requests.config.RetryConfig;
import com.xqt360.requests.exception.RetryException;
import com.xqt360.requests.exception.UnsupportedTypeException;
import com.xqt360.requests.retry.*;
import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.client.methods.*;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestsUtils {
    private static boolean isJsonData(Object data) {
        return data instanceof JSONObject || data instanceof JSONArray || isValidObject(data);
    }

    private static boolean isFormUrlEncoded(String data) {
        try {
            String decodedRequestBody = URLDecoder.decode(data);
            Pattern pattern = Pattern.compile("^[^=&]+=[^=&]+(&[^=&]+=[^=&]+)*$");
            Matcher matcher = pattern.matcher(decodedRequestBody);
            return matcher.matches() && ParseUtils.qsParse(data).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }


    private static boolean isValidObject(Object obj) {
        try {
            JSON.parseObject(obj.toString());
            return true;
        } catch (Exception ignored) {
//            ignored.printStackTrace();
        }
        try {
            JSON.parseArray(obj.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isExceptionIncluded(Throwable exception, Class<? extends Throwable>[] includeException) {
        return includeException != null && Arrays.stream(includeException).anyMatch(ex -> ex.isInstance(exception));
    }


    @SneakyThrows
    public static <D> HttpUriRequest createRequest(RequestConfig<D> config, Connection.Method method, CloseableHttpClient defaultHttpClient) {
        RequestBuilder requestBuilder = RequestBuilder.create(method.name()).setUri(RequestsUtils.getUrl(config));


        org.apache.http.client.config.RequestConfig build = org.apache.http.client.config.RequestConfig.custom()
                .setConnectTimeout(config.getTimeout())
                .setSocketTimeout(config.getTimeout())
                .setConnectionRequestTimeout(config.getTimeout())
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setRedirectsEnabled(config.isFollowRedirects())
                .build();

        requestBuilder.setConfig(build);
        if (config.getUserAgent() != null) {
            requestBuilder.setHeader(HttpHeaders.USER_AGENT, config.getUserAgent());
        } else if (config.getHeaders().containsKey(HttpHeaders.USER_AGENT)) {
            requestBuilder.setHeader(HttpHeaders.USER_AGENT, config.getHeaders().get(HttpHeaders.USER_AGENT));
        }
        if (method == Connection.Method.POST && isJsonData(config.getData())) {
            requestBuilder.setEntity(new StringEntity(config.getData().toString(), StandardCharsets.UTF_8));
            requestBuilder.addHeader("Content-Type", ContentType.APPLICATION_FORM_URLENCODED_UTF8_VALUE);
        } else if (method == Connection.Method.POST && config.getData() instanceof Map) {
            requestBuilder.addHeader("Content-Type", ContentType.APPLICATION_FORM_URLENCODED_UTF8_VALUE);
            requestBuilder.setEntity(new StringEntity(ParseUtils.qsStringify((Map) config.getData()), StandardCharsets.UTF_8));
        } else if (method == Connection.Method.POST && config.getData() instanceof String) {
            requestBuilder.setEntity(new StringEntity(config.getData().toString(), StandardCharsets.UTF_8));
            requestBuilder.addHeader("Content-Type", isFormUrlEncoded(config.getData().toString()) ? ContentType.APPLICATION_FORM_URLENCODED_UTF8_VALUE : ContentType.TEXT_PLAIN_VALUE);
        } else if (method == Connection.Method.POST && config.getData() instanceof List) {//有些网站的键值对会重复
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            requestBuilder.addHeader("Content-Type", ContentType.APPLICATION_FORM_URLENCODED_UTF8_VALUE);
            for (Object datum : (List) config.getData()) {
                if (datum instanceof String) {
                    String[] split = datum.toString().split("=");
                    nameValuePairs.add(new BasicNameValuePair(split[0], split[1]));
                } else if (datum instanceof NameValuePair) {
                    NameValuePair basicNameValuePair = (NameValuePair) datum;
                    nameValuePairs.add(basicNameValuePair);
                }
            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs);
            requestBuilder.setEntity(urlEncodedFormEntity);
        }
        Optional.ofNullable(config.getHeaders()).ifPresent(headers -> headers.forEach(requestBuilder::addHeader));
        return requestBuilder.build();
    }

    public static <D> void setDefaultHeadersAndBody(RequestConfig<D> config, Connection.Method method, Connection connection) {


        if (config.getUserAgent() != null) {
            connection.userAgent(config.getUserAgent());
        } else if (config.getHeaders().containsKey(HttpHeaders.USER_AGENT)) {
            connection.userAgent(config.getHeaders().get(HttpHeaders.USER_AGENT));
        }


        Optional.ofNullable(config.getHeaders()).ifPresent(connection::headers);//加上配置类的请求头

        if (method == Connection.Method.POST && isJsonData(config.getData())) {
            connection.header("Content-Type", ContentType.APPLICATION_JSON_UTF8_VALUE);
            connection.requestBody(config.getData().toString());

        } else if (config.getData() instanceof List && method == Connection.Method.POST) {//有些网站的键值对会重复
            connection.header("Content-Type", ContentType.APPLICATION_FORM_URLENCODED_UTF8_VALUE);
            for (Object datum : (List) config.getData()) {
                if (datum instanceof String) {
                    String[] split = datum.toString().split("=");
                    connection.data(split[0], split[1]);
                } else if (datum instanceof NameValuePair) {
                    NameValuePair basicNameValuePair = (NameValuePair) datum;
                    connection.data(basicNameValuePair.getName(), basicNameValuePair.getValue());
                }
            }

        } else if (config.getData() instanceof Map && method == Connection.Method.POST) {
            connection.header("Content-Type", ContentType.APPLICATION_FORM_URLENCODED_UTF8_VALUE);
            connection.data((Map) config.getData());
        } else if (config.getData() instanceof String) {
            connection.requestBody(config.getData().toString());
            connection.header("Content-Type", isFormUrlEncoded(config.getData().toString()) ? ContentType.APPLICATION_FORM_URLENCODED_UTF8_VALUE : ContentType.TEXT_PLAIN_VALUE);
        }
    }

    public static <D> String getUrl(RequestConfig<D> config) {
        return config.getUrl() + ((config.getQueryString() == null || config.getQueryString().isEmpty()) ? "" : "?" + config.getQueryString());
    }

    public static <D> void setProxy(Connection connection, RequestConfig<D> config, Proxy defaultProxy) {
        //正确的设置了IP字符串的情况下
        if (config.getProxyString() != null && config.getProxyString().length() > 0) {
            String[] split = config.getProxyString().split("@");
            if (split.length == 1) {
                String[] hostPort = split[0].split(":");
                connection.proxy(hostPort[0], Integer.parseInt(hostPort[1]));//设置代理IP；
                return;
            } else if (split.length == 2) {
                String[] hostPort = split[0].split(":");
                String[] rootPassword = split[1].split(":");
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(rootPassword[0], rootPassword[1].toCharArray());
                    }
                });
                connection.proxy(hostPort[0], Integer.parseInt(hostPort[1]));//设置代理IP；
                return;
            }
        }
        if (config.getProxy() != null) {
            connection.proxy(config.getProxy());
            return;
        }
        connection.proxy(defaultProxy);
    }

    public static boolean needRetry(RetryConfig retryConfig, Connection.Response response, int retryCount, int maxRetryCount) {
        if (retryConfig == null || retryConfig.getRetryCondition() == null) {
            return false;
        }

        if (retryCount >= Math.min(maxRetryCount, retryConfig.getMaxAttempts())) {
            throw new RetryException("异常重试次数达到最大值" + retryCount);
        }

        RetryCondition retryCondition = retryConfig.getRetryCondition();
        if (retryCondition instanceof RetryByStatus) {
            return ((RetryByStatus) retryCondition).shouldRetry(response.statusCode());
        } else if (retryCondition instanceof RetryByBody) {
            return ((RetryByBody) retryCondition).shouldRetry(response.body());
        } else if (retryCondition instanceof RetryByJsoupResponse) {
            return ((RetryByJsoupResponse) retryCondition).shouldRetry(response);
        } else {
            throw new UnsupportedTypeException(retryCondition);
        }
    }


    public static boolean needRetry(RetryConfig retryConfig, CloseableHttpResponse response, int retryCount, int MAX_RETRY_COUNT) {
        if (retryConfig == null || retryConfig.getRetryCondition() == null) {
            return false;
        }

        //防止程序员设置错了重试次数导致递归问题出现，默认最大重试20次
        if (retryCount >= Math.min(MAX_RETRY_COUNT, retryConfig.getMaxAttempts())) {
            throw new RetryException("异常重试次数达到最大值" + retryCount);
        }

        if (retryConfig.getRetryCondition() instanceof RetryByStatus) {
            RetryByStatus retryCondition = (RetryByStatus) retryConfig.getRetryCondition();
            return retryCondition.shouldRetry(response.getStatusLine().getStatusCode());
        } else if (retryConfig.getRetryCondition() instanceof RetryByBody) {
            RetryByBody retryCondition = (RetryByBody) retryConfig.getRetryCondition();
            try {
                return retryCondition.shouldRetry(EntityUtils.toString(response.getEntity()));
            } catch (IOException e) {
                return true;
            }

        } else if (retryConfig.getRetryCondition() instanceof RetryByHttpResponse) {
            RetryByHttpResponse retryCondition = (RetryByHttpResponse) retryConfig.getRetryCondition();
            return retryCondition.shouldRetry(response);
        } else {
            //抛出不支持该类型的异常
            throw new UnsupportedTypeException(retryConfig.getRetryCondition());
        }
    }

    public static void assertRetry(RetryConfig retryConfig, Exception e, int retryCount, int MAX_RETRY_COUNT) {
        if (retryConfig == null) {
            return;
        }

        //防止程序员设置错了重试次数导致递归问题出现，默认最大重试20次
        if (retryCount >= Math.min(MAX_RETRY_COUNT, retryConfig.getMaxAttempts())) {
            e.printStackTrace();
            throw new RetryException(e.getMessage() + "异常重试次数达到最大值" + retryCount);
        }


        //没有设置包含什么异常的情况下，默认所有异常重试
        if ((retryConfig.getIncludeException() == null || retryConfig.getIncludeException().length == 0) && (retryConfig.getExcludeException() == null || retryConfig.getExcludeException().length == 0)) {
            throw new RetryException(e.getMessage() + "---默认异常不重试，如果需要异常重试，请指定IncludeException或ExcludeException");
        }

        //获取包含的异常
        boolean includeException = RequestsUtils.isExceptionIncluded(e, retryConfig.getIncludeException());

        boolean excludeException = RequestsUtils.isExceptionIncluded(e, retryConfig.getExcludeException());

        if (excludeException || !includeException) {
            e.printStackTrace();
            throw new RetryException(e.getMessage() + "异常不在您的重试列表中或您已排除该异常");
        }
    }



    public static DefaultHttpClient getHttpsClient(ProxyConfig proxyConfig) {

        DefaultHttpClient client = new DefaultHttpClient(new ThreadSafeClientConnManager());
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");//TLS SSL

            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = client.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));

            LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
            client.setRedirectStrategy(redirectStrategy);

            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);

            client.getParams().setParameter("http.protocol.max-redirects", 2);
            client.getParams().setParameter(CoreConnectionPNames.SO_KEEPALIVE, true);
            client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);

            if (proxyConfig == null) {
                return client;
            }

            HttpHost proxy = new HttpHost(proxyConfig.getHost(), proxyConfig.getPort(), "http");
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            AuthScope auth = new AuthScope(proxyConfig.getHost(), proxyConfig.getPort() );
            Credentials credentials = new org.apache.http.auth.NTCredentials(proxyConfig.getUsername(), proxyConfig.getPassword(), "", "");
            client.getCredentialsProvider().setCredentials(auth, credentials);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }
}
