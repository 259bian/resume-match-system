package com.resumematch.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
public class FileUtil {

    public String extractText(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return "";
        }
        String lowerName = originalFilename.toLowerCase();

        try {
            if (lowerName.endsWith(".pdf")) {
                return extractPdfText(file);
            } else if (lowerName.endsWith(".docx")) {
                return extractDocxText(file);
            } else if (lowerName.endsWith(".txt") || lowerName.endsWith(".md")) {
                return new String(file.getBytes());
            } else {
                log.warn("不支持的文件格式: {}", lowerName);
                return "";
            }
        } catch (Exception e) {
            log.error("文件解析失败", e);
            return "";
        }
    }

    // PDFBox 2.0.27
    private String extractPdfText(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    // DOCX
    private String extractDocxText(MultipartFile file) throws IOException {
        try (XWPFDocument document = new XWPFDocument(file.getInputStream());
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }

    public String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}