package com.resumematch.controller.admin;

import com.resumematch.common.R;
import com.resumematch.dto.JobDTO;
import com.resumematch.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/job")
@RequiredArgsConstructor
public class AdminJobController {

    private final JobService jobService;

    @PostMapping
    public R<?> createJob(@RequestBody JobDTO jobDTO) {
        return jobService.createJob(jobDTO);
    }

    @PutMapping("/{id}")
    public R<?> updateJob(@PathVariable Long id, @RequestBody JobDTO jobDTO) {
        return jobService.updateJob(id, jobDTO);
    }

    @DeleteMapping("/{id}")
    public R<?> deleteJob(@PathVariable Long id) {
        return jobService.deleteJob(id);
    }

    @GetMapping("/all")
    public R<?> getAllJobs() {
        return jobService.getAllJobs();
    }
}
