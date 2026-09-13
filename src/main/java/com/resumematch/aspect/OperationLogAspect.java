package com.resumematch.aspect;

import com.resumematch.service.LogService;
import com.resumematch.util.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final LogService logService;

    // 拦截所有控制器
    @Pointcut("execution(* com.resumematch.controller..*.*(..))")
    public void logPointcut() {}

    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long costTime = System.currentTimeMillis() - start;

        try {
            // 获取请求信息
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = IpUtil.getIpAddr(request);
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String username = getCurrentUsername();
            Long userId = getCurrentUserId();

            // 调用已有的写日志方法
            logService.logOperation(
                    userId,
                    username,
                    uri,
                    method,
                    "",
                    ip,
                    costTime
            );
        } catch (Exception e) {
            // 不影响业务
        }

        return result;
    }

    // 这里你可以改成你项目真实获取用户的方式
    private String getCurrentUsername() {
        return "admin";
    }

    private Long getCurrentUserId() {
        return 1L;
    }
}