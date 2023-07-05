import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xqt360.requests.http.Requests;
import com.xqt360.requests.http.impl.HttpClientRequests;
import com.xqt360.requests.http.impl.JsoupRequests;
import com.xqt360.requests.utils.FileUtils;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Test {

    @SneakyThrows
    public static void main(String[] args) {


        Requests requests = new JsoupRequests();



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
