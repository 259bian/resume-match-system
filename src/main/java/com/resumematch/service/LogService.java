package com.resumematch.service;

import com.resumematch.common.R;
import jakarta.servlet.http.HttpServletResponse;

public interface LogService {

    R logOperation(Long userId, String username, String operation, String method, String params, String ip, Long costTime);

    R listLogs(Integer page, Integer size, String keyword);

    R exportLogs(HttpServletResponse response);
}
