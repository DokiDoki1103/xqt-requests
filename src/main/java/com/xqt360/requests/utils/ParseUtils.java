package com.xqt360.requests.utils;

import com.alibaba.fastjson.JSONObject;
import com.xqt360.requests.exception.UnsupportedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ParseUtils {

    /**
     * 自动解析Jsoup返回
     */
    public static <T> T parseResponse(Connection.Response response, Class<T> cls) {
        // 在这里解析响应，并返回指定类型的对象
        if (cls == Connection.Response.class) {
            return (T) response;
        } else if (cls == JSONObject.class) {
            return (T) JSONObject.parseObject(response.body());
        } else if (cls == String.class) {
            return (T) response.body();
        } else if (cls == Document.class) {
            try {
                return (T) response.parse();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if ("byte[]".equals(cls.getSimpleName())) {
            return (T) response.bodyAsBytes();
        } else {
            throw new UnsupportedTypeException(cls);
        }
        return null;
    }

    /**
     * 自动解析Jsoup返回
     */
    public static <T> T parseResponse(CloseableHttpResponse response, Class<T> cls) {
        try {
            // 在这里解析响应，并返回指定类型的对象
            if (cls == CloseableHttpResponse.class) {
                return (T) response;
            } else if (cls == JSONObject.class) {
                String body = EntityUtils.toString(response.getEntity());
                return (T) JSONObject.parseObject(body);

            } else if (cls == String.class) {
                return (T) EntityUtils.toString(response.getEntity());
            } else if (cls == Document.class) {
                try {
                    String string = EntityUtils.toString(response.getEntity());
                    return (T) Jsoup.parse(string);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if ("byte[]".equals(cls.getSimpleName())) {
                return (T)IOUtils.toByteArray(response.getEntity().getContent());
            } else {
                throw new UnsupportedTypeException(cls);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String qsStringify(Map<String, String> params) {
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            try {
                key = URLEncoder.encode(key, "UTF-8");
                value = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (queryString.length() > 0) {
                queryString.append("&");
            }
            queryString.append(key).append("=").append(value);
        }
        return queryString.toString();
    }

    public static Map<String, String> qsParse(String queryString) {

        Map<String, String> params = new HashMap<>();
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                try {
                    key = URLDecoder.decode(key, "UTF-8");
                    value = URLDecoder.decode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                params.put(key, value);
            }
        }
        return params;
    }
}

