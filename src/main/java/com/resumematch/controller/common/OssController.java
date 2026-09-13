package com.resumematch.controller.common;

import com.resumematch.common.R;
import com.resumematch.service.OssService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss")
@RequiredArgsConstructor
public class OssController {

    private final OssService ossService;

    @PostMapping("/upload")
    public R<?> uploadFile(@RequestParam("file") MultipartFile file,
                           @RequestParam(defaultValue = "common") String folder) {
        return ossService.uploadFile(file, folder);
    }

    @DeleteMapping("/delete")
    public R<?> deleteFile(@RequestParam String fileUrl) {
        return ossService.deleteFile(fileUrl);
    }
}
