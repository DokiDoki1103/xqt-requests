package com.xqt360.requests.utils;

import com.xqt360.requests.config.ProxyConfig;

public class ProxyUtils {
    public static ProxyConfig formatProxyString(String proxyIpString) {
        if (proxyIpString == null || proxyIpString.isEmpty()){
            return null;
        }
        try {
            String[] split = proxyIpString.split("@");
            if (split.length == 1) {
                String[] hostPort = split[0].split(":");
                return new ProxyConfig(hostPort[0], Integer.parseInt(hostPort[1]), null, null);
            } else if (split.length == 2) {

                String[] hostPort = split[0].split(":");
                String[] usernamePassword = split[1].split(":");
                return new ProxyConfig(hostPort[0], Integer.parseInt(hostPort[1]), usernamePassword[0], usernamePassword[1]);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("代理字符串格式不正确，应该为：host:port@username:password或者host:port");
        }

        return null;
    }
}
