package com.resumematch.controller.admin;

import com.resumematch.common.R;
import com.resumematch.service.ResumeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/resume")
@RequiredArgsConstructor
public class AdminResumeController {

    private final ResumeService resumeService;

    @GetMapping("/list")
    public R<?> listAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return resumeService.listAllResumes(page, size);
    }

    @GetMapping("/export")
    public void exportResumes(HttpServletResponse response) {
        resumeService.exportResumes(response);
    }
}
