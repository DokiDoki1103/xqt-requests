package com.xqt360.requests.config;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Connection;

import java.net.Proxy;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.commons.codec.CharEncoding.UTF_8;

@Data
@Builder
@NoArgsConstructor
public class RequestConfig<D> {

    /**
     * 请求参数相关
     */
    private Connection.Method method;
    private String url;//完整的请求地址
    private String queryString = "";//url 查询字符串，拼接在url后面
    private D data;
    private Map<String, String> headers;
    private Map<String, String> cookies;


    /**
     * 全局参数 可以针对某个接口设置超时时间以及UA
     */
    private int timeout = 8000;//单位ms
    private String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36";

    private Proxy proxy;
    private String proxyString;//格式：127.0.0.1:8888:root:password
    private boolean followRedirects = true;//默认支持重定向
    private RetryConfig retryConfig = new RetryConfig();//异常重试配置类

    public void addHeader(String key, String val) {
        this.headers = this.addMap(this.headers, key, val);
    }

    public void addCookie(String key, String val) {
        this.cookies = this.addMap(this.cookies, key, val);
    }


    public void addCookie(String cookieString){
        String[] split = cookieString.split(";\\s?");
        for (String s : split) {
            String[] split1 = s.split("=");
            try{
                addCookie(URLDecoder.decode(split1[1],UTF_8), URLDecoder.decode(split1[1],UTF_8));
            }catch (Exception ignored){

            }
        }

    }

    private <K, V> Map<K, V> addMap(Map<K, V> map, K key, V val) {
        map = Optional.ofNullable(map).orElseGet(HashMap::new);
        map.put(key, val);
        return map;
    }

    private <K, V> void removeMapKey(Map<K, V> map, K key) {
        Optional.ofNullable(map).ifPresent(m -> m.remove(key));
    }

    public RequestConfig(Connection.Method method, String url, String queryString, D data, Map<String, String> headers, Map<String, String> cookies, int timeout, String userAgent, Proxy proxy, String proxyString,boolean followRedirects, RetryConfig retryConfig) {
        this.method = method;
        this.url = url;
        this.queryString = queryString == null ? this.queryString : queryString;
        this.data = data;
        this.headers = headers;
        this.cookies = cookies;
        this.timeout = timeout;
        this.userAgent = userAgent == null ? this.userAgent : userAgent;
        this.proxy = proxy;
        this.followRedirects = followRedirects;
        this.retryConfig = retryConfig == null ? new RetryConfig() : retryConfig;
        this.proxyString = proxyString;
    }
}

