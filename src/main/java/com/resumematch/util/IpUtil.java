package com.resumematch.util;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtil {
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return ip == null ? "127.0.0.1" : ip;
    }
}