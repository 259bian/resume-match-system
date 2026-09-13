package com.resumematch.controller.user;

import com.resumematch.common.R;
import com.resumematch.dto.ApplicationDTO;
import com.resumematch.service.ApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public R<?> apply(HttpServletRequest request, @RequestBody ApplicationDTO applicationDTO) {
        Long userId = (Long) request.getAttribute("userId");
        return applicationService.apply(userId, applicationDTO);
    }

    @GetMapping("/my")
    public R<?> listMyApplications(HttpServletRequest request,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) Integer status) {
        Long userId = (Long) request.getAttribute("userId");
        return applicationService.listMyApplications(userId, page, size, status);
    }

    @GetMapping("/{id}")
    public R<?> getApplicationDetail(@PathVariable Long id) {
        return applicationService.getApplicationDetail(id);
    }

    @DeleteMapping("/{id}")
    public R<?> withdrawApplication(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        return applicationService.withdrawApplication(id, userId);
    }
}
