<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">小蜻蜓请求库</h1>
<h4 align="center">一个轻量级 Java HTTP请求框架，让请求变得简单、优雅！</h4>


---

## 前言：

- 在开发中自己是否经常写一大堆冗余代码？明明简单的POST、GET请求却需要
    ``` java
    HttpPost httpPost = new HttpPost("");
    new BasicNameValuePair()
    httpPost.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36");
    header.put("sec-ch-ua-mobile", "?0");
    HttpResponse response = client.execute(httpPost);
    String contentss = EntityUtils.toString(response.getEntity());
    ```

  或者看似简单实则一大坨的链式调用
    ``` java 
    Jsoup.connect("")
      .data()
      .header()
      .method()
      .execute()
    ```


- 通过使用本项目将高度重复部分进行封装，让请求变得优雅简单
    ```java
    Requests requests = new HttpClientRequests();
    //发送get请求，返回String
    String data1 = requests.get("https://baidu.com");
    //发送post请求，返回String
    String data2 = requests.post("https://baidu.com","a=1&b=2",String.class);
   //发送get请求，返回JSONObject
    JSONObject jsonObject = requests.get("https://baidu.com?a=1&b=2", JSONObject.class);
    ```

目的：大大简化爬虫实现，减少冗余代码，让程序员更注重业务逻辑
优势如下：
- 1:方便get post请求

- 2:自动管理Cookie(当对方网站返回set-cookie时下次请求自动携带)

- 3:自定义重试次数，自定义重试间隔，自定义排除某些异常，包含某些异常，

- 自定义对403或502等状态码的重试，自定义对网站返回内容的重试，如果不满足需求可以自定义重试策略(根据```Connection.Response```的返回)

- 4:每个请求可配置独立代理IP，也可以在对象创建时指定代理IP，支持String和```java.net.Proxy```格式

- 5:data传参支持String类型：json类型```{"a":"xxx"}``` 或者 表单类型```a=xxx&b==xxx``` 自动设置其对应的请求头ContentType

- 6:data传参支持Map类型：自动序列化为```a=xxx&b==xxx```格式 并自动设置请求头```Content-Type```为 ```application/x-www-form-urlencoded```

- 7:data传参支持JSONObject类型：自动序列化为```{"a":"xxx"}``` 并自动设置请求头```Content-Type```为```application/json```

### 使用技巧
- 1:当form表单特别长并且有些参数为空都不需要值的情况下 例如 ```String s = "a=1&b=2&c=3&d=4&e=&f="```
调用```ParseUtils.qsParse(s)```自动转为Map对象 想要设置什么参数直接```map.put("a","新的值")```


- 2:如果表单很有序，并且几乎每个值都被用到了，自己想要规范化
```Map map = new HashMap()``` 然后```map.put("username","xxx");map.put("password","xx")```
最后调用```ParseUtils.qsStringify(map)``` 将其转换为````username=xxx&password==xxx```` 直接发送请求即可
        

- 3:对表单格式可以这么做，其实对JSONObject同理。
```List<String> eval = (List<String>)JSONPath.eval(jsonObject, "$.channelList[1,2,3].content.name");```
推荐使用JSONPath更加方便



- 4:对于某些请求我们需要单独设置代理IP，或者增加header或者cookies
``` java
RequestConfig<String> config = RequestConfig.<String>builder()
.url("http://baidu.com")
.build();
config.setProxy();
config.addCookie();
config.addHeader()
```

- 4:对于我们需要移除Cookie中的某些key直接调用 ```requests.removeCookie("key")```

- 更多示例请仔细阅读本文档

## 发送请求示例

### 前提条件


```java
//创建 HttpClient，使用HttpClient发送请求
Requests requests1 = new HttpClientRequests();
Requests requests2 = new HttpClientRequests(true,"127.0.0.1:8888");
Requests requests3 = new HttpClientRequests(false,"127.0.0.1:8888","username","password");

//创建 JsoupRequests，使用Jsoup发送请求
Requests requests4 = new JsoupRequests();
Requests requests5 = new JsoupRequests("127.0.0.1:8888");
Requests requests6 = new JsoupRequests("127.0.0.1:8888","username","password");
```
### 发送get请求
Requests接口有如下方法可供调用
```java
String get(String url);

<T> T get(String url, Class<T> cls);

<D, T> T get(RequestConfig<D> config, Class<T> cls);

<D> String get(RequestConfig<D> config);
```
小蜻蜓请求库 旨在以简单、优雅的方式完成HTTP请求，例如发送get请求你只需要：

``` java
// 填写url后直接发送即可，默认返回String类型的body
String data1 = requests.get("https://baidu.com");
```

无需实现任何接口，无需创建任何配置文件，只需要这一句代码的调用，便可以get请求

如果这个请求返回的是json格式的我们还需要手动调用JSON.parseObject，极致繁琐
然而 在 小蜻蜓 请求库 中，大多数功能都可以一行代码解决：
``` java
// 第二个参数写JSONObject.class即可自动序列化为JSON对象
JSONObject jsonObject1 = requests.get("https://baidu.com", JSONObject.class);
// 同理我们需要返回Document类型的可供我们快速解析，
Document document = requests.get("https://baidu.com", Document.class);
// 当然有时候并不能满足我们的需求，我们可能需要获取http状态码,返回的响应头等，我们可以让他返回Response对象
Connection.Response response = requests.get("https://baidu.com", Connection.Response.class);
// 或者我们使用HttpClient时候需要返回HttpResponse对象
HttpResponse httpResponse = requests.get("https://baidu.com", HttpResponse.class);
```
当然您可能传一个我们不支持的类型，我们将会抛出```UnsupportedTypeException```异常，该异常继承自```IllegalArgumentException```并不需要我们显示捕获它，但是我们写代码时应该尽量避免它的出现。

另外我们可以传入一个```RequestConfig```对象然后发送请求，在后文将会讲到。

### 发送post请求
Requests接口有如下方法可供调用
```java
String post(String url);

<T> T post(String url, Class<T> cls);

<D> String post(String url, D data);

<D, T> T post(String url, D data, Class<T> cls);

<D, T> T post(RequestConfig<D> config, Class<T> cls);

```
前两个方法和get请求极其类似，不过多讲解，主要讲解第三个方法。第三个方法```<D> String post(String url, D data);```看到这个D您就应该想到这个方法可玩性巨多。
D是一个不确定的类型，主要是data的类型不能确定，可能有String，Map，JSON，List等

- 例如String的情况

``` java
// 发送post表单请求,会自动设置请求头application/x-www-form-urlencoded
String data2 = requests.post("https://baidu.com","a=1&b=2");
 
//发送post json参数会自动设置请求头application/json
String data6 = requests.post("https://baidu.com","{\"username\": \"zhang\"}");
```
- 例如Map的情况
```java
//发送post请求，data参数为Map的请求，
//如果参数比较多，Map的可读性比较强，如果为了方便也可直接发送字符串
//同理也会自动设置请求头application/x-www-form-urlencoded
Map<String, String> map = new HashMap<>();
map.put("a","1");
map.put("b","2");
String data3 = requests.post("https://baidu.com",map);
```

- 例如JSON的情况
```java
//也会自动设置请求头application/json
//下方代码等价于发送了这样的一个字符串 {"username": "zhang"}
JSONObject jsonObject3 = new JSONObject();
jsonObject3.put("username","zhang");
String data4 = requests.post("https://baidu.com",jsonObject3);

//同理也可以发送数组格式的
//下方代码等价于发送了这样的一个字符串 [{"username": "zhang"}]
JSONArray jsonArray = new JSONArray();
jsonArray.add(jsonObject3);
String data5 = requests.post("https://baidu.com",jsonArray);
```


- 例如List的情况

很多网站的form表单参数key会重复，这时候就必须发送List格式的
```java
//下面代码等价于发送了这样一段字符串
//username=root&password=123456

//可以发送HttpClient库中的BasicNameValuePair格式
List<NameValuePair> nameValuePairs = new ArrayList<>();
nameValuePairs.add(new BasicNameValuePair("username", "root"));
nameValuePairs.add(new BasicNameValuePair("password", "123456"));
String data7 = requests.post("https://baidu.com", nameValuePairs);

//可以发送String格式
List<String> strings = new ArrayList<>();
strings.add("username=root");
strings.add("password=123456");
String data8 = requests.post("https://baidu.com", strings);
//其他的类型还不支持，如果有好的建议可以提出来
```

### 发送其他请求
Requests接口有如下方法可供调用
```java
<D> String execute(RequestConfig<D> config);

<D, T> T execute(RequestConfig<D> config, Class<T> cls);
```
可以自定配置RequestConfig来进行请求

## 主要配置类
主要配置在```com.xqt360.requests.config```包下
### RequestConfig请求配置类

所有参数均可以set或者get拿到，或者通过Builder链式调用设置参数，最后build
```java

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
    this.addMap(this.headers, key, val);
}

public void addCookie(String key, String val) {
    this.addMap(this.cookies, key, val);
}
```
### RetryConfig 配置类
```java
//只有能正常请求通过对方才会走此重试条件包括 503、403 状态码均视为请求通过
private RetryCondition retryCondition;//重试条件；默认null即没有发生异常的情况下不重试
private int maxAttempts = 5;//最大重试次数,包括第一次请求。设置多少最终会发送多少次请求；默认5
private int delay = 1000;//重试间隔；默认100
private float multiplier = 1;//幂，默认1
//没有请求通过，抛出java的异常
private Class<? extends Throwable>[] includeException;//包含这些异常才会重试；默认所有异常都重试
private Class<? extends Throwable>[] excludeException;//排除某些异常；默认不排除任何异常
```
上述代码除了```RetryCondition```之外其余参数应该一目了然，同理也可以set或builder。

### RetryCondition接口
What？这是什么？顾名思义：重试条件
```java
public interface RetryCondition<T> {
    boolean shouldRetry(T statusCode);
}
```
我们已经提供了4个接口```RetryByStatus```、```RetryByBody```、```RetryByStatus```、```RetryByStatus```
```java
//根据http状态码的重试
RetryByStatus retryByStatus = new RetryByStatus() {
    @Override
    public boolean shouldRetry(Integer statusCode) {
        return statusCode == 405;
    }
};
//也可以直接通过lambda表达式简化
RetryByStatus retryByStatus1 = statusCode -> statusCode == 405;//状态码重试条件

RetryByBody retryByBody = new RetryByBody() {
    @Override
    public boolean shouldRetry(String body) {
        return body.contains("机器人验证");
    }
};
//同理直接通过lambda表达式简化
RetryByBody retryByBody1 = body -> body.contains("机器人验证");//返回的内容重试条件
//
//根据HttpClient HttpResponse 类进行重试
RetryByHttpResponse retryByHttpResponse = new RetryByHttpResponse() {//对HttpClient的返回进行判断然后重试
    @Override
    public boolean shouldRetry(HttpResponse response) {
        //根据response的返回处理您的逻辑，如果需要重试返回true
        return false;
    }
};
//根据Jsoup Connection.Response类进行重试
RetryByJsoupResponse retryByJsoupResponse = new RetryByJsoupResponse() {//对Jsoup的返回进行判断然后重试
    @Override
    public boolean shouldRetry(Connection.Response response) {
        //根据response的返回处理您的逻辑，如果需要重试返回true
        return false;
    }
};
```

自行实现上述接口后调用 ```retryConfig.setRetryCondition();```设置您的重试条件



## 进阶用法
### 请求拦截器 RequestInterceptor
Request类有如下接口，运行您在创建对象之后再为其配置单独的拦截器。
```java
void setRequestInterceptor(RequestInterceptor requestInterceptor);

void setResponseInterceptor(ResponseInterceptor responseInterceptor);
```
很多人不知道其存在意义，看完以下示例您将会豁然开朗。
```java
RequestInterceptor requestInterceptor = new RequestInterceptor() {
    @Override
    public void use(RequestConfig requestConfig) {
        //一些公用的请求头自动帮您设置无需每个请求再次单独设置
        requestConfig.addHeader("name1","value1");
        requestConfig.addHeader("name2","value2");

        //某些url需要为其单独的设置代理IP
        if (requestConfig.getUrl().contains("needProxy")) {
            requestConfig.setProxy(new Proxy(Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1",8000)));
        }
        //某些url在请求之前还需要再次请求自己的服务器拿到一个加密Cookie
        if (requestConfig.getUrl().contains("needEncryptCookie")){
            requestConfig.addCookie("name","value");
        }

        //某些url的请求body被加密了，但是无法用java还原，还需要调用第三方接口获取加密结果
        if (requestConfig.getUrl().contains("needEncryptData")){
            //前往第三方接口获取加密结果,直接设置，对代码0侵入，如果后期需要变动直接在此变动
            requestConfig.setData(getEncrypt(requestConfig.getData()));
        }
        //把某些接口的https改为http
        if (requestConfig.getUrl().contains("noHttps")){
            requestConfig.setUrl(requestConfig.getUrl().replace("https","http"));
        }

        //某些域名用了第三方服务提供商提供的安全网络 封控的很严格，各种跳验证，
        // 这时候可以直接朔源找到对方真实IP直接对其IP发送请求，在不改一句代码的情况下，轻松完成此工作
        if (requestConfig.getUrl().contains("host")){//某些host下
            String ipUrl = convertDomainToIP(requestConfig.getUrl(), "127.0.0.1");
            requestConfig.setUrl(ipUrl);
            requestConfig.addHeader("Host",domain);
        }


        //某些接口需要响应时间长一点，或者各种自定义化需求，等等全部可以拦截requestConfig来修改
        if (requestConfig.getUrl().contains("xxxx")){
            //修改重试配置
            requestConfig.setRetryConfig();
            //让这个接口的超时时间变长
            requestConfig.setTimeout();
            //把这个接口的ua改变了
            requestConfig.setUserAgent();
            //等等更多的自定义配置requestConfig....应有尽有，期待您的发现
            requestConfig.setCookies();
            requestConfig.setMethod();
            requestConfig.setFollowRedirects();

        }
        //请求过哪些网站，请求参数什么，可以在此做日志记录
    }
};
```

最后一句代码设置``` requests.setRequestInterceptor(requestInterceptor);```

### 响应拦截器 ResponseInterceptor
原理与请求拦截器相同，不在过多赘述，比如记录日志，或者返回某些内容后发送邮件或者短信提醒管理员。更多具体用法自行发挥


## 小蜻蜓 请求库 功能模块一览

小蜻蜓请求库 目前主要三大功能模块：便捷发送Get Post请求、丰富的配置类、拦截器。

- **便捷请求** —— 一行代码发送get、post请求，而不需要繁琐的步骤
- **丰富的请求配置类** —— 如果已提供的方法不够用，您可以自行创建请求配置类来发送请求
- **丰富的重试配置类** —— 重试配置类可自定配置延迟重试条件等等
- **自定义重试条件** —— 可以对返回码的，返回内容或者返回的对象判断是否需要重试
- **自定义异常重试** —— 针对某些异常或排除某些异常重试
- **拦截器** —— 可以对请求或者响应进行拦截，不侵入代码
