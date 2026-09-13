package com.resumematch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.resumematch.common.R;
import com.resumematch.entity.OperationLog;
import com.resumematch.mapper.OperationLogMapper;
import com.resumematch.service.LogService;
import com.resumematch.util.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final OperationLogMapper operationLogMapper;
    private final ExcelUtil excelUtil;

    @Override
    public R logOperation(Long userId, String username, String operation, String method,
                          String params, String ip, Long costTime) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setOperation(operation);
        log.setMethod(method);
        log.setParams(params);
        log.setIp(ip);
        log.setCostTime(costTime);
        operationLogMapper.insert(log);
        return R.ok();
    }

    @Override
    public R listLogs(Integer page, Integer size, String keyword) {
        Page<OperationLog> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like(OperationLog::getUsername, keyword)
                    .or().like(OperationLog::getOperation, keyword)
                    .or().like(OperationLog::getMethod, keyword));
        }
        wrapper.orderByDesc(OperationLog::getCreateTime);
        operationLogMapper.selectPage(pageObj, wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("records", pageObj.getRecords());
        result.put("total", pageObj.getTotal());
        return R.ok(result);
    }

    @Override
    public R exportLogs(HttpServletResponse response) {
        List<OperationLog> logs = operationLogMapper.selectList(null);
        excelUtil.export(response, "操作日志", "操作日志", OperationLog.class, logs);
        return R.ok();
    }
}
