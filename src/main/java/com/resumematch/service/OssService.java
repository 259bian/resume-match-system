package com.resumematch.service;

import com.resumematch.common.R;
import org.springframework.web.multipart.MultipartFile;

public interface OssService {

    R uploadFile(MultipartFile file, String folder);

    R deleteFile(String fileUrl);
}
