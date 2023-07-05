package com.xqt360.requests.http;

import com.xqt360.requests.config.RequestConfig;
import com.xqt360.requests.interceptors.RequestInterceptor;
import com.xqt360.requests.interceptors.ResponseInterceptor;
import org.jsoup.Connection;

/**
 * 目的：大大简化爬虫实现，减少冗余代码，让程序员更注重业务逻辑
 * 优势如下：
 * 1:方便get post请求
 * 2:自动管理Cookie(当对方网站返回set-cookie时下次请求自动携带)
 * 3:自定义重试次数，自定义重试间隔，自定义排除某些异常，包含某些异常，
 * 自定义对403或502等状态码的重试，自定义对网站返回内容的重试，如果不满足需求可以自定义重试策略(根据Connection.Response的返回)
 * 4:每个请求可配置独立代理IP，也可以在对象创建时指定代理IP，支持String和java.net.Proxy格式
 * 5:data传参支持String类型：json类型{"a":"xxx"} 或者 表单类型a=xxx&b==xxx 自动设置其对应的请求头ContentType
 * 6:data传参支持Map类型：自动序列化为a=xxx&b==xxx格式 并自动设置请求头Content-Type为 application/x-www-form-urlencoded
 * 7:data传参支持JSONObject类型：自动序列化为{"a":"xxx"} 并自动设置请求头Content-Type为application/json
 * <p>
 * 使用技巧
 * 1:当form表单特别长并且有些参数为空都不需要值的情况下 例如 String s = "a=1&b=2&c=3&d=4&e=&f="
 * 调用ParseUtils.qsParse(s)自动转为Map对象 想要设置什么参数直接map.put("a","新的值")
 * <p>
 * 2:如果表单很有序，并且几乎每个值都被用到了，自己想要规范化
 * Map map = new HashMap() 然后map.put("username","xxx");map.put("password","xx")
 * 最后调用ParseUtils.qsStringify(map) 将其转换为username=xxx&password==xxx 直接发送请求即可
 * <p>
 * 3:对表单格式可以这么做，其实对JSONObject同理。
 * List<String> eval = (List<String>)JSONPath.eval(jsonObject, "$.channelList[1,2,3].content.name");
 * 推荐使用JSONPath更加方便
 * <p>
 * 4:对于某些请求我们需要单独设置代理IP，或者增加header或者cookies
 * RequestConfig<String> config = RequestConfig.<String>builder()
 * .url("http://baidu.com")
 * .build();
 * config.setProxy();
 * config.addCookie();
 * config.addHeader()
 * <p>
 * 4:对于我们需要移除Cookie中的某些key直接调用 requests.removeCookie("key")
 */
public interface Requests {

    String get(String url);

    <T> T get(String url, Class<T> cls);

    <D, T> T get(RequestConfig<D> config, Class<T> cls);

    <D> String get(RequestConfig<D> config);

    <D, T> T post(RequestConfig<D> config, Class<T> cls);

    <T> T post(String url, Class<T> cls);

    String post(String url);

    <D, T> T post(String url, D data, Class<T> cls);

    <D> String post(String url, D data);

    <D> String execute(RequestConfig<D> config);

    <D, T> T execute(RequestConfig<D> config, Class<T> cls);

    void setRequestInterceptor(RequestInterceptor requestInterceptor);

    void setResponseInterceptor(ResponseInterceptor responseInterceptor);

    void setProxyIp(String proxyIp);

    void setProxyIp(String proxyIp, String username, String password);

}
