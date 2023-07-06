import com.xqt360.requests.config.RequestConfig;
import com.xqt360.requests.http.Requests;
import com.xqt360.requests.http.impl.JsoupRequests;
import com.xqt360.requests.interceptors.RequestInterceptor;
import lombok.SneakyThrows;


public class Test {

    @SneakyThrows
    public static void main(String[] args) {
        Requests requests = new JsoupRequests("127.0.0.1:7890");
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public <D> void use(RequestConfig<D> requestConfig) {
                requestConfig.addHeader("Origin", "https://ta.allegiantair.com");
                requestConfig.addHeader("Referer", requestConfig.getUrl());
                requestConfig.addHeader("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"");
                requestConfig.addHeader("Sec-Ch-Ua-Mobile", "?0");
                requestConfig.addHeader("Sec-Ch-Ua-Platform", "\"macOS\"");
                requestConfig.addHeader("Sec-Fetch-Dest", "document");
                requestConfig.addHeader("Sec-Fetch-Mode", "navigate");
                requestConfig.addHeader("Sec-Fetch-Site", "same-origin");
                requestConfig.addHeader("Sec-Fetch-User", "?1");;
                requestConfig.addHeader("Upgrade-Insecure-Requests", "1");
                requestConfig.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
                requestConfig.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
                requestConfig.addHeader("Accept-Encoding", "gzip, deflate, br");
                requestConfig.addHeader("Accept-Language", "zh-CN,zh;q=0.9");
                requestConfig.addHeader("Cache-Control", "no-cache");
                requestConfig.addCookie("BIGipServer~API~vswprdg4varnish-https_pool=!9OZNPOazGzpz1eqGKZHqN5erFE0+65MiNWm0WU2MBk0im0BY/xqFYMcSacvWPnY65inBSYCn9+Fkwp4=; ALGTSESSID=172.69.33.251_29264_1688613302; mmapi.p.bid=%22prodphxcgus03%22; mmapi.p.srv=%22prodphxcgus03%22; has_js=1; g4_nache=5ge8wlnnud7wuedu04x7pmkt1; session_manifest=ovm2wkdzgv7rxnwvdp2d7euhnoedp7jbok2suhmch55; _gcl_au=1.1.1220389030.1688613306; __cf_bm=g6caG3OCyiEiLagMn7rWa4FNQPNFAKZH7CFLROKPIC0-1688613306-0-AT/ZXydVT52tZxqR03WOdFHbDTsgTyfGDVi6Op2D6ui3+RusCCGwfbdKQOLdI1OuM4CycklF0V7rwmVaWvYNn/WnTU2UIpEzlHh09hJXaGwcS7TgNLb19CUv5hn1m3VQnQ==; sd_client_id=a9a875a8-c949-454d-a173-f89bda72162a; _ga=GA1.2.33526108.1688613308; _gid=GA1.2.572034613.1688613308; _gat_UA-33024747-2=1; _ga=GA1.3.33526108.1688613308; _gid=GA1.3.572034613.1688613308; __attentive_id=b0ea50f0bbd3499f9523fd0f0349e602; _attn_=eyJ1Ijoie1wiY29cIjoxNjg4NjEzMzA4MjAyLFwidW9cIjoxNjg4NjEzMzA4MjAyLFwibWFcIjoyMTkwMCxcImluXCI6ZmFsc2UsXCJ2YWxcIjpcImIwZWE1MGYwYmJkMzQ5OWY5NTIzZmQwZjAzNDllNjAyXCJ9In0=; __attentive_cco=1688613308203; __attentive_ss_referrer=https://ta.allegiantair.com/?__cf_chl_tk=y3vNBMDZZ34WEhnjS0w5bn9YFutXGHfgybwpNDssHcM-1688613291-0-gaNycGzNDSU; _fbp=fb.1.1688613308542.1241442994; __attentive_dv=1; cf_clearance=2LKr0bIP_hFxxDgIJxAd05bIp0pPICqxuExFqucLYWs-1688613333-0-250; g4_nache=5ge8wlnnud7wuedu04x7pmkt1; js_auth_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0YUlhdGFObyI6IjA4MzMzMTM1IiwiaWQiOiIxMTI3NiIsInVzZXJUeXBlIjoidGEiLCJhZ2VudE5hbWUiOiJDSExPRVdVIiwic3ViIjoiIiwicGVybWlzc2lvbnMiOlsiQVBQTFlfVk9VQ0hFUlMiLCJCT09LSU5HX0FERF9CQUdTIiwiQk9PS0lOR19BRERfUFJJT1JJVFlfQk9BUkRJTkciLCJCT09LSU5HX0FERF9TRUFUUyIsIkJPT0tJTkdfQUREX1NTUiIsIkJPT0tfRkxJR0hUIiwiQk9PS0lOR19GTElHSFRfSE9URUwiLCJCT09LSU5HX0ZMSUdIVF9IT1RFTF9WRUhJQ0xFIiwiQk9PS0lOR19GTElHSFRfVkVISUNMRSIsIkJPT0tJTkdfUEFSVElBTF9IT1RFTF9TVEFZIiwiQVBQTFlfUFJPTU9TIl19.Zg72JLohRpEkzIs0AOjovqC8wbPgIha25hc4ws60zGs; CCH=N; SESSbe6c55f23793e7c8b8c6946c1f6197ae=SnCKSdUZtvc8Ji9CeP4NbMcHUOfqLJWqC82wVuddH1U; SSESSbe6c55f23793e7c8b8c6946c1f6197ae=LUWmOu2h3xPQtt5_FZs5MYxGOAHuOH1lVz36ryEgUNg; authenticated=1; mmapi.p.pd=%22XMMZ2RmeT95ztfqdvF-VjzUpcZowVxsPAN1Jkd_JINI%3D%7CAgAAAApDH4sIAAAAAAAEAGNhyI5UCe4TX_OMgbkgo4JRiIHRiYGrQ-gJI8P6g2u9ztfe9hBdNtcQRDMAwX8oYBAuSdRLzMlJTc9MzCtJzCzSS87PZeoTZwQpAgOYShDNwMDI8MabkWECUIbRFQD1lEHKcwAAAA%3D%3D%22; _uetsid=4e9864601bab11ee9dcdab2b1976e1f1; _uetvid=4e9863b01bab11ee8c92df1d384b3b5b; __attentive_pv=2; xyz_cr_986_et_111==NaN&cr=986&wegc=&et=111&ap=");
            }
        };
        requests.setRequestInterceptor(requestInterceptor);

//        System.out.println(post.cookies());

//        URI uri = URI.create("https://passport2-api.chaoxing.com/v11/loginregister?cx_xxt_passport=json");
//        System.out.println(uri.getHost());
//        uri.g
//        System.out.println(uri.get);
//        CookieManager cookieManager = new CookieManager();
//        cookieManager.get()

//        CookieStore cookieStore = cookieManager.getCookieStore();
//        cookieStore.add(URI.create("https://lyck6.cn/sadas/dadasd"),new HttpCookie("a","b"));
//        cookieStore.add(URI.create("https://app.cn/sadas/dadasd"),new HttpCookie("aaa","bbb"));
//
//        List<HttpCookie> httpCookies = cookieStore.get(URI.create("http://app.cn/abc/dada/daasdas2"));
//        System.out.println(JSON.toJSONString(httpCookies));


//        //创建 HttpClient，使用HttpClient发送请求
//        Requests requests1 = new HttpClientRequests();
//        Requests requests2 = new HttpClientRequests(true, "127.0.0.1:8888");
//        Requests requests3 = new HttpClientRequests(false, "127.0.0.1:8888", "username", "password");
//
//        //创建 JsoupRequests，使用Jsoup发送请求
//        Requests requests4 = new JsoupRequests();
//        Requests requests5 = new JsoupRequests("127.0.0.1:8888");
//        Requests requests6 = new JsoupRequests("127.0.0.1:8888", "username", "password");
//
//        String data1 = requests.get("https://baidu.com");
//        JSONObject jsonObject1 = requests.get("https://baidu.com", JSONObject.class);
//        Document document = requests.get("https://baidu.com", Document.class);
//
//        Connection.Response response = requests.get("https://baidu.com", Connection.Response.class);
//        HttpResponse httpResponse = requests.get("https://baidu.com", HttpResponse.class);
//
//        String data2 = requests.post("https://baidu.com", "a=1&b=2");
//        String data6 = requests.post("https://baidu.com", "{\"username\": \"zhang\"}");
//
//        Map<String, String> map = new HashMap<>();
//        map.put("a", "1");
//        map.put("b", "2");
//        String data3 = requests.post("https://baidu.com", map);
//
//
//        JSONObject jsonObject3 = new JSONObject();
//        jsonObject3.put("username", "zhang");
//        String data4 = requests.post("https://baidu.com", jsonObject3);
//
//
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.add(jsonObject3);
//        String data5 = requests.post("https://baidu.com", jsonArray);
//
//
//        List<NameValuePair> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(new BasicNameValuePair("username", "root"));
//        nameValuePairs.add(new BasicNameValuePair("password", "123456"));
//
//        String data7 = requests.post("https://baidu.com", nameValuePairs);
//
//        List<String> strings = new ArrayList<>();
//        strings.add("username=root");
//        strings.add("password=123456");
//        String data8 = requests.post("https://baidu.com", strings);
//
//
//        JSONObject jsonObject = requests.get("https://baidu.com?a=1&b=2", JSONObject.class);
//
//
//

    }
}
