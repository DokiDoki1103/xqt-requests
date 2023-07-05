import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.xqt360.requests.config.RequestConfig;
import com.xqt360.requests.config.RetryConfig;
import com.xqt360.requests.http.Requests;
import com.xqt360.requests.http.impl.HttpClientRequests;
import com.xqt360.requests.retry.RetryByStatus;
import org.jsoup.Connection;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.util.List;


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
//      requests.setProxyIp("127.0.0.1:8866");

//        requests.setRequestInterceptor(new RequestInterceptor() {
//            @Override
//            public void use(RequestConfig requestConfig) {
//                if (requestConfig.getUrl().equals("https://passport2-api.chaoxing.com/v11/loginregister?cx_xxt_passport=json")){
//                    requestConfig.setData("uname=17531309659&code=8221314933z");
//                }
//            }
//        });

        String login = requests.post(
                "https://passport2-api.chaoxing.com/v11/loginregister?cx_xxt_passport=json",
                "uname=17531309659&code=8221314933z");

        System.out.println("发送字符串表单请求示例1" + login);

        JSONObject jsonObject = requests.get("https://mooc1-api.chaoxing.com/mycourse/backclazzdata", JSONObject.class);
        System.out.println(jsonObject);
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
//        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(new BasicNameValuePair("uname","17531309659"));
//        nameValuePairs.add(new BasicNameValuePair("code","8221314933z"));
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
        RetryByStatus retryByStatus = statusCode -> statusCode == 405;//状态码重试条件
//
//        RetryByBody retryByBody = body -> body.contains("机器人验证");//返回的内容重试条件
//
//        RetryByHttpResponse retryByHttpResponse = new RetryByHttpResponse() {//对HttpClient的返回进行判断然后重试
//            @Override
//            public boolean shouldRetry(HttpResponse response) {
//                int statusCode = response.getStatusLine().getStatusCode();
//                return false;
//            }
//        };
//
//        RetryByJsoupResponse retryByJsoupResponse = new RetryByJsoupResponse() {//对Jsoup的返回进行判断然后重试
//            @Override
//            public boolean shouldRetry(Connection.Response response) {
//                int i = response.statusCode();
//                return false;
//            }
//        };
//
//        //重试配置类
        RetryConfig retryConfig = RetryConfig.builder().retryCondition(retryByStatus).build();
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
