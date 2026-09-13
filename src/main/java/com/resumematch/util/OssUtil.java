package com.resumematch.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.resumematch.config.OssConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


@Slf4j
@Component
@RequiredArgsConstructor
public class OssUtil {

    private final OssConfig ossConfig;


    public String uploadFile(MultipartFile file, String folder) {
        String originalFilename = file.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String objectName = folder + "/" + UUID.randomUUID().toString().replace("-", "") + suffix;

        OSS ossClient = null;
        try (InputStream inputStream = file.getInputStream()) {
            ossClient = new OSSClientBuilder().build(
                    ossConfig.getEndpoint(),
                    ossConfig.getAccessKeyId(),
                    ossConfig.getAccessKeySecret()
            );
            ossClient.putObject(new PutObjectRequest(ossConfig.getBucketName(), objectName, inputStream));
            log.info("文件上传成功: {}", objectName);
            return ossConfig.getBaseUrl() + "/" + objectName;
        } catch (IOException e) {
            log.error("OSS文件上传失败", e);
            throw new RuntimeException("文件上传失败");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
    // ========================
// 新增：支持 byte[] 上传，解决流只能读一次问题
// ========================
    public String uploadFileBytes(byte[] fileBytes, String originalFilename, String folder) {
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String objectName = folder + "/" + UUID.randomUUID().toString().replace("-", "") + suffix;

        OSS ossClient = null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
            ossClient = new OSSClientBuilder().build(
                    ossConfig.getEndpoint(),
                    ossConfig.getAccessKeyId(),
                    ossConfig.getAccessKeySecret()
            );
            ossClient.putObject(new PutObjectRequest(ossConfig.getBucketName(), objectName, inputStream));
            log.info("文件上传成功: {}", objectName);
            return ossConfig.getBaseUrl() + "/" + objectName;
        } catch (Exception e) {
            log.error("OSS文件上传失败", e);
            throw new RuntimeException("文件上传失败");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl == null || !fileUrl.contains(ossConfig.getBaseUrl())) {
            return;
        }
        String objectName = fileUrl.substring(ossConfig.getBaseUrl().length() + 1);
        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(
                    ossConfig.getEndpoint(),
                    ossConfig.getAccessKeyId(),
                    ossConfig.getAccessKeySecret()
            );
            ossClient.deleteObject(ossConfig.getBucketName(), objectName);
            log.info("文件删除成功: {}", objectName);
        } catch (Exception e) {
            log.error("OSS文件删除失败", e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
