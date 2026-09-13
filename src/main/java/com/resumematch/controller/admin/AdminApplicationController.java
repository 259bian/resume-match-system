package com.resumematch.controller.admin;

import com.resumematch.common.R;
import com.resumematch.service.ApplicationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/application")
@RequiredArgsConstructor
public class AdminApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/list")
    public R<?> listAllApplications(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        return applicationService.listAllApplications(page, size, status);
    }

    @PutMapping("/{id}/status")
    public R<?> updateStatus(@PathVariable Long id,
                             @RequestParam Integer status,
                             @RequestParam(required = false) String remark) {
        return applicationService.updateStatus(id, status, remark);
    }

    @GetMapping("/export")
    public void exportApplications(HttpServletResponse response) {
        applicationService.exportApplications(response);
    }
}
