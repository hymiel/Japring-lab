package com.common.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

public class ServletUtils {
    public static String getClientIpAddress(HttpServletRequest request) {
        return Arrays.stream(new String[]{
                        "X-Forwarded-For",
                        "Proxy-Client-IP",
                        "WL-Proxy-Client-IP",
                        "HTTP_CLIENT_IP",
                        "HTTP_X_FORWARDED_FOR"
                })
                .map(request::getHeader)
                .filter(ip -> ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip))
                .map(ip -> ip.split(",")[0].trim())
                .findFirst()
                .orElse(null);
    }
}
