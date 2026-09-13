package com.resumematch.ai;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.resumematch.util.PdfUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobMatcher {

    private final DeepSeekClient deepSeekClient;
    private final PdfUtil pdfUtil;

    private static final String SYSTEM_PROMPT = """
            你是一位专业的招聘匹配分析师。请根据简历内容和岗位要求，计算匹配度分数(0-100)，
            并按以下JSON格式返回分析结果：
            {
              "score": 85,
              "skillMatch": {
                "matched": ["Java", "Spring Boot"],
                "missing": ["Docker", "Kubernetes"]
              },
              "analysis": "详细的匹配分析说明",
              "suggestion": "针对不匹配部分的改进建议"
            }
            只返回JSON，不要包含其他内容。""";

    public JSONObject match(String resumeContent, String jobRequirements) {
        if (resumeContent == null || jobRequirements == null) {
            return localMatch(resumeContent, jobRequirements);
        }

        String userMessage = String.format(
                "简历内容：\n%s\n\n岗位要求：\n%s\n\n请分析匹配度。", resumeContent, jobRequirements);

        log.info("开始调用AI进行简历-岗位匹配");
        String result = deepSeekClient.chat(SYSTEM_PROMPT, userMessage);

        if (result != null) {
            try {
                String jsonStr = result.trim();
                if (jsonStr.startsWith("```json")) {
                    jsonStr = jsonStr.substring(7);
                }
                if (jsonStr.endsWith("```")) {
                    jsonStr = jsonStr.substring(0, jsonStr.length() - 3);
                }
                return JSON.parseObject(jsonStr.trim());
            } catch (Exception e) {
                log.error("AI匹配结果解析失败，使用本地匹配", e);
            }
        }
        return localMatch(resumeContent, jobRequirements);
    }

    private JSONObject localMatch(String resumeContent, String jobRequirements) {
        List<String> resumeSkills = pdfUtil.extractKeywords(resumeContent != null ? resumeContent : "");
        List<String> jobSkills = pdfUtil.extractKeywords(jobRequirements != null ? jobRequirements : "");

        int matchCount = 0;
        for (String skill : resumeSkills) {
            if (jobSkills.contains(skill)) {
                matchCount++;
            }
        }

        int totalJobSkills = jobSkills.isEmpty() ? 1 : jobSkills.size();
        int score = Math.min(95, (int) ((double) matchCount / totalJobSkills * 100));

        JSONObject result = new JSONObject();
        result.put("score", score);

        JSONObject skillMatch = new JSONObject();
        skillMatch.put("matched", resumeSkills.stream()
                .filter(jobSkills::contains).toList());
        skillMatch.put("missing", jobSkills.stream()
                .filter(s -> !resumeSkills.contains(s)).toList());
        result.put("skillMatch", skillMatch);

        result.put("analysis", score >= 70 ? "匹配度较高，建议投递该岗位" :
                score >= 40 ? "匹配度一般，部分技能需补充" : "匹配度较低，建议提升相关技能后再投递");

        result.put("suggestion", "建议补充缺失的技能项，并突出已有匹配技能在简历中的描述。");

        return result;
    }
}
