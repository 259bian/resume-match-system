package com.resumematch.controller.user;

import com.resumematch.common.R;
import com.resumematch.dto.ResumeDTO;
import com.resumematch.service.ResumeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
@Slf4j
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/upload")
    public R<?> uploadResume(HttpServletRequest request,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam(required = false, defaultValue = "未命名简历") String title) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("用户 {} 上传简历，文件名：{}，标题：{}", userId, file.getOriginalFilename(), title);
        return resumeService.uploadResume(userId, file, title);
    }

    @GetMapping("/my")
    public R<?> listMyResumes(HttpServletRequest request,
                              @RequestParam(defaultValue = "1") Integer page,
                              @RequestParam(defaultValue = "10") Integer size) {
        Long userId = (Long) request.getAttribute("userId");
        return resumeService.listMyResumes(userId, page, size);
    }

    @GetMapping("/{id}")
    public R<?> getResumeDetail(@PathVariable Long id) {
        return resumeService.getResumeDetail(id);
    }

    @PutMapping("/{id}")
    public R<?> updateResume(@PathVariable Long id, @RequestBody ResumeDTO resumeDTO) {
        resumeDTO.setId(id);
        return resumeService.updateResume(resumeDTO);
    }

    @DeleteMapping("/{id}")
    public R<?> deleteResume(@PathVariable Long id) {
        return resumeService.deleteResume(id);
    }

    @PostMapping("/{id}/version")
    public R<?> uploadNewVersion(@PathVariable Long id,
                                 @RequestParam("file") MultipartFile file) {
        return resumeService.uploadNewVersion(id, file);
    }

    @PostMapping("/{id}/version/text")
    public R<?> saveOptimizedVersion(@PathVariable Long id,
                                     @RequestBody Map<String, String> body) {
        String content = body.get("content");
        return resumeService.saveOptimizedVersion(id, content);
    }

    @GetMapping("/{id}/versions")
    public R<?> getVersions(@PathVariable Long id) {
        return resumeService.getVersions(id);
    }

    @PutMapping("/{id}/version/{version}")
    public R<?> switchVersion(@PathVariable Long id, @PathVariable Integer version) {
        return resumeService.switchVersion(id, version);
    }

    @GetMapping("/{id}/similarity")
    public R<?> checkSimilarity(@PathVariable Long id) {
        return resumeService.checkSimilarity(id);
    }
}
