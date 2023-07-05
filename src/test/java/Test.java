import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.xqt360.requests.config.RequestConfig;
import com.xqt360.requests.config.RetryConfig;
import com.xqt360.requests.http.Requests;
import com.xqt360.requests.http.impl.HttpClientRequests;
import com.xqt360.requests.http.impl.JsoupRequests;
import com.xqt360.requests.interceptors.RequestInterceptor;
import com.xqt360.requests.retry.RetryByBody;
import com.xqt360.requests.retry.RetryByHttpResponse;
import com.xqt360.requests.retry.RetryByJsoupResponse;
import com.xqt360.requests.retry.RetryByStatus;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Test {
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static float add(float v1, float v2) {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).floatValue();
    }

    public static void main(String[] args) {



        Requests requests = new HttpClientRequests();

        //创建 HttpClient，使用HttpClient发送请求
        Requests requests1 = new HttpClientRequests();
        Requests requests2 = new HttpClientRequests(true, "127.0.0.1:8888");
        Requests requests3 = new HttpClientRequests(false, "127.0.0.1:8888", "username", "password");

        //创建 JsoupRequests，使用Jsoup发送请求
        Requests requests4 = new JsoupRequests();
        Requests requests5 = new JsoupRequests("127.0.0.1:8888");
        Requests requests6 = new JsoupRequests("127.0.0.1:8888", "username", "password");

        String data1 = requests.get("https://baidu.com");
        JSONObject jsonObject1 = requests.get("https://baidu.com", JSONObject.class);
        Document document = requests.get("https://baidu.com", Document.class);

        Connection.Response response = requests.get("https://baidu.com", Connection.Response.class);
        HttpResponse httpResponse = requests.get("https://baidu.com", HttpResponse.class);

        String data2 = requests.post("https://baidu.com", "a=1&b=2");
        String data6 = requests.post("https://baidu.com", "{\"username\": \"zhang\"}");

        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        String data3 = requests.post("https://baidu.com", map);


        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("username", "zhang");
        String data4 = requests.post("https://baidu.com", jsonObject3);


        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject3);
        String data5 = requests.post("https://baidu.com", jsonArray);


        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("username", "root"));
        nameValuePairs.add(new BasicNameValuePair("password", "123456"));

        String data7 = requests.post("https://baidu.com", nameValuePairs);

        List<String> strings = new ArrayList<>();
        strings.add("username=root");
        strings.add("password=123456");
        String data8 = requests.post("https://baidu.com", strings);


        JSONObject jsonObject = requests.get("https://baidu.com?a=1&b=2", JSONObject.class);
//      requests.setProxyIp("127.0.0.1:8866");

//        RequestInterceptor requestInterceptor = new RequestInterceptor() {
//            @Override
//            public void use(RequestConfig requestConfig) {
//                //一些公用的请求头自动帮您设置无需每个请求再次单独设置
//                requestConfig.addHeader("name1","value1");
//                requestConfig.addHeader("name2","value2");
//
//                //某些url需要为其单独的设置代理IP
//                if (requestConfig.getUrl().contains("needProxy")) {
//                    requestConfig.setProxy(new Proxy(Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1",8000)));
//                }
//                //某些url在请求之前还需要再次请求自己的服务器拿到一个加密Cookie
//                if (requestConfig.getUrl().contains("needEncryptCookie")){
//                    requestConfig.addCookie("name","value");
//                }
//
//                //某些url的请求body被加密了，但是无法用java还原，还需要调用第三方接口获取加密结果
//                if (requestConfig.getUrl().contains("needEncryptData")){
//                    //前往第三方接口获取加密结果,直接设置，对代码0侵入，如果后期需要变动直接在此变动
//                    requestConfig.setData(getEncrypt(requestConfig.getData()));
//                }
//                //把某些接口的https改为http
//                if (requestConfig.getUrl().contains("noHttps")){
//                    requestConfig.setUrl(requestConfig.getUrl().replace("https","http"));
//                }
//
//                //某些域名用了第三方服务提供商提供的安全网络 封控的很严格，各种跳验证，
//                // 这时候可以直接朔源找到对方真实IP直接对其IP发送请求，在不改一句代码的情况下，轻松完成此工作
//                if (requestConfig.getUrl().contains("host")){//某些host下
//                    String ipUrl = convertDomainToIP(requestConfig.getUrl(), "127.0.0.1");
//                    requestConfig.setUrl(ipUrl);
//                    requestConfig.addHeader("Host",domain);
//                }
//
//
//                //某些接口需要响应时间长一点，或者各种自定义化需求，等等全部可以拦截requestConfig来修改
//                if (requestConfig.getUrl().contains("xxxx")){
//                    //修改重试配置
//                    requestConfig.setRetryConfig();
//                    //让这个接口的超时时间变长
//                    requestConfig.setTimeout();
//                    //把这个接口的ua改变了
//                    requestConfig.setUserAgent();
//                    //等等更多的自定义配置requestConfig....应有尽有，期待您的发现
//                    requestConfig.setCookies();
//                    requestConfig.setMethod();
//                    requestConfig.setFollowRedirects();
//
//                }
//            }
//        };

        RequestConfig<Object> objectRequestConfig = new RequestConfig<>();
//        objectRequestConfig.setRetryConfig();
//req
        String login = requests.post(
                "https://passport2-api.chaoxing.com/v11/loginregister?cx_xxt_passport=json",
                "uname=17531309659&code=8221314933z");

        System.out.println("发送字符串表单请求示例1" + login);

//        JSONObject jsonObject = requests.get("https://mooc1-api.chaoxing.com/mycourse/backclazzdata", JSONObject.class);
//        System.out.println(jsonObject);
//        JSONPath.paths()


        List<JSONObject> read = JSONPath.read(jsonObject.toJSONString(), "$.channelList", List.class);

//        Map<String, String> map = new HashMap<>();
//        map.put("uname", "17531309659");
//        map.put("code", "8221314933z");
//        JSONObject login2 = requests.post("https://passport2-api.chaoxing.com/v11/loginregister?cx_xxt_passport=json", map, JSONObject.class);
//
//        System.out.println("发送Map表单请求示例2" + login2);
//
//

//
//        Document login3 = requests.post("https://passport2-api.chaoxing.com/v11/loginregister?cx_xxt_passport=json", nameValuePairs, Document.class);
//
//        System.out.println("发送nameValuePairs表单请求示例3" + login3);
//
//
//
//        ArrayList<String> strings = new ArrayList<>();
//        strings.add("uname=17531309659");
//        strings.add("code=8221314933z");
//
//        String login4 = requests.post("https://passport2-api.chaoxing.com/v11/loginregister?cx_xxt_passport=json", strings);
//
//        System.out.println("发送List<String>表单请求示例4" + login4);
//
//
//
//        //发送json字符串的示例
        JSONObject post = requests.post("https://lyck6.cn/scriptService/api/autoAnswer/44DF8CA64A?gpt=3", "{\n" +
                "    \"qid\": null,\n" +
                "    \"question\": \"2. 简述一般常见的汽车维修企业(不含销售汽车的企业)组成部门有哪些\",\n" +
                "    \"options\": [\n" +
                "        \"主要由以下部门组成:管理部门、业务接待(前台)部、技术部、配件部、财务部、客服部、人力资源和行政部等\",\n" +
                "        \"主要由以下部门组成:管理部门、业务接待(前台)部、配件部、财务部、客服部、人力资源和行政部等\",\n" +
                "        \"主要由以下部门组成:业务接待(前台)部、技术部、配件部、财务部、客服部、人力资源和行政部等\",\n" +
                "        \"主要由以下部门组成:管理部门、业务接待(前台)部、技术部、财务部、客服部、人力资源和行政部等\"\n" +
                "    ],\n" +
                "    \"options_id\": [],\n" +
                "    \"type\": 0\n" +
                "}", JSONObject.class);
        System.out.println("发送Post json字符串的示例：" + post);
//
//
//        Document document = requests.get("https://blog.csdn.net/rookie_li/article/details/28906165", Document.class);
//
////        System.out.println("发送普通Get请求的示例：" + document);
//
//        System.out.println("获取<body>示例" + document.title());
//
//
//        /**
//         * 通过构建RequestConfig请求
//         */
        RequestConfig<String> config = new RequestConfig<>();


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
//
//        //重试配置类
        RetryConfig retryConfig = RetryConfig.builder().retryCondition(retryByStatus).build();
//        retryConfig.setRetryCondition();
        System.out.println((retryConfig.getRetryCondition() == null) + "11111");
        //设置排除某些异常
//        retryConfig.setExcludeException(new Class[]{IOException.class, SocketTimeoutException.class});
        //设包含某些异常
        retryConfig.setIncludeException(new Class[]{UnknownHostException.class});
        config.setUrl("https://lyck6.cnn/scriptService/api/autoAnswer/44DF8CA64A?gpt=3");
        config.setRetryConfig(retryConfig);
        config.setMethod(Connection.Method.GET);
//
        String s = requests.execute(config);
        System.out.println(s);


//        DefaultHttpClient httpClient = httpClientRequests.getHttpClient();
//        System.out.println(httpClient);
//        String queryString = RequestConfig.builder().build().getQueryString();
//        System.out.println(queryString);
//        RequestConfig requestConfig = new RequestConfig();
//        System.out.println(requestConfig.getQueryString());
//
//
//        Requests requests = new JsoupHttpRequests();
//
//        //简单的get请求示例
//        String data = requests.get("http://47.52.11.194:4066/orderManage/createSeat.do?getVYCookie");//获取cookies
//        System.out.println(data);
//
////        requests.
//
//        RetryConfig retryConfig = new RetryConfig();
//        retryConfig.setDelay(100);
//        retryConfig.setMaxAttempts(3);
//
//        retryConfig.setExcludeException(new Class[]{SSLHandshakeException.class});
//        RequestConfig<String> build = RequestConfig.<String>builder()
//                .url("https://lyck6.c1n/scriptService/ap")
//                .retryConfig(retryConfig)
//                .build();
//        String s = requests.get(build);

//        System.out.println(s);

//        String s1 = requests.get("https://lyck6.c1n/scriptService/ap");
//
//        Map<String, String> cookies = ParseUtils.qsParse(data.split("&")[0].replace("; ", "&"));
//        String ua = data.split("&")[1];
////
//        RequestConfig<String> searchConfig = RequestConfig.<String>builder()
//                .url("https://tickets.vueling.com/ScheduleSelectNew.aspx")
//                .userAgent(ua)
//                .cookies(cookies)
//                .data("__EVENTTARGET=ControlGroupAgentHomeView%24AgentHomeSearchView%24LinkButtonNewSearch&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwUBMGRkIDAvynEuRHfWRav0hQXNgUeddS0%3D&pageToken=&ControlGroupAgentHomeView%24AgentHomeSearchView%24RadioButtonMarketStructure=RoundTrip&ControlGroupAgentHomeView%24AgentHomeSearchView%24TextBoxMarketOrigin1=Agadir&ControlGroupAgentHomeView%24AgentHomeSearchView%24TextBoxMarketDestination1=Paris+%28Orly%29&date_picker=2023-07-05&ControlGroupAgentHomeView%24AgentHomeSearchView%24DropDownListMarketDay1=05&ControlGroupAgentHomeView%24AgentHomeSearchView%24DropDownListMarketMonth1=2023-07&date_picker=2023-08-23&ControlGroupAgentHomeView%24AgentHomeSearchView%24DropDownListMarketDay2=23&ControlGroupAgentHomeView%24AgentHomeSearchView%24DropDownListMarketMonth2=2023-08&ControlGroupAgentHomeView%24AgentHomeSearchView%24TextBoxMarketOrigin2=Paris+%28Orly%29&ControlGroupAgentHomeView%24AgentHomeSearchView%24TextBoxMarketDestination2=Agadir&ControlGroupAgentHomeView%24AgentHomeSearchView%24DropDownListPassengerType_ADT=1&ControlGroupAgentHomeView%24AgentHomeSearchView%24DropDownListPassengerType_CHD=0&ControlGroupAgentHomeView%24AgentHomeSearchView%24DropDownListPassengerType_INFANT=0&ControlGroupAgentHomeView%24AgentHomeSearchView%24ResidentFamNumSelector=&ControlGroupAgentHomeView%24AgentHomeSearchView%24ExtraSeat=&ControlGroupAgentHomeView%24AgentHomeSearchView%24DropDownListSearchBy=columnView&departureStationCode1=AGA&arrivalStationCode1=ORY&ErroneousWordOrigin1=&SelectedSuggestionOrigin1=&ErroneousWordDestination1=&SelectedSuggestionDestination1=&versionTd=&departureStationCode2=ORY&arrivalStationCode2=AGA&ErroneousWordOrigin2=&SelectedSuggestionOrigin2=&ErroneousWordDestination2=&SelectedSuggestionDestination2=&stvMonetateExperiment=&stvVersion=")
//                .build();
////
//        Document document = requests.post(searchConfig, Document.class);
//        Element viewState = document.getElementById("viewState");
////
//        System.out.println(viewState.val());


    }
}
