package com.resumematch.controller.admin;

import com.resumematch.common.R;
import com.resumematch.service.LogService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/log")
@RequiredArgsConstructor
public class AdminLogController {

    private final LogService logService;

    @GetMapping("/list")
    public R<?> listLogs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return logService.listLogs(page, size, keyword);
    }

    @GetMapping("/export")
    public void exportLogs(HttpServletResponse response) {
        logService.exportLogs(response);
    }
}
