package com.resumematch.service.impl;

import com.resumematch.common.R;
import com.resumematch.service.OssService;
import com.resumematch.util.OssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class OssServiceImpl implements OssService {

    private final OssUtil ossUtil;

    @Override
    public R uploadFile(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            return R.fail("请选择文件");
        }
        try {
            String fileUrl = ossUtil.uploadFile(file, folder != null ? folder : "default");
            return R.ok(fileUrl);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return R.fail("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public R deleteFile(String fileUrl) {
        try {
            ossUtil.deleteFile(fileUrl);
            return R.ok("文件已删除");
        } catch (Exception e) {
            log.error("文件删除失败", e);
            return R.fail("文件删除失败");
        }
    }
}
