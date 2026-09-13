package com.resumematch.util;

import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class ExcelUtil {

    public <T> void export(HttpServletResponse response, String fileName, String sheetName,
                           Class<T> clazz, List<T> data) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String encodedName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                    .replace("\\+", "%20");
            response.setHeader("Content-Disposition",
                    "attachment;filename*=UTF-8''" + encodedName + ".xlsx");

            EasyExcel.write(response.getOutputStream(), clazz)
                    .sheet(sheetName)
                    .doWrite(data);
        } catch (IOException e) {
            log.error("Excel导出失败", e);
            throw new RuntimeException("Excel导出失败");
        }
    }
}
