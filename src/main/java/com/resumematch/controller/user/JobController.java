package com.resumematch.controller.user;

import com.resumematch.common.R;
import com.resumematch.service.JobService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping("/list")
    public R<?> listJobs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location) {
        return jobService.listJobs(page, size, keyword, category, location);
    }

    @GetMapping("/{id}")
    public R<?> getJobDetail(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        return jobService.getJobDetail(id, userId);
    }

    @GetMapping("/all")
    public R<?> getAllJobs() {
        return jobService.getAllJobs();
    }
}
