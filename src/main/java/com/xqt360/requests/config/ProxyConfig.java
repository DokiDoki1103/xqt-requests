package com.xqt360.requests.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProxyConfig {

    private String host;
    private int port;
    private String username;
    private String password;
}
