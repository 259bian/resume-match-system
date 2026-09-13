package com.resumematch.ai;

import com.resumematch.util.PdfUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeywordExtractor {

    private final PdfUtil pdfUtil;

    /**
     * 从简历内容提取关键词并排序
     */
    public List<String> extractKeywords(String content) {
        if (content == null || content.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return pdfUtil.extractKeywords(content);
    }

    /**
     * 检查简历相似度
     */
    public Map<String, Object> checkSimilarity(String content, List<String> otherContents) {
        Map<String, Object> result = new HashMap<>();
        double maxSimilarity = 0;
        String mostSimilar = null;

        for (String other : otherContents) {
            double sim = pdfUtil.calculateSimilarity(content, other);
            if (sim > maxSimilarity) {
                maxSimilarity = sim;
                mostSimilar = other;
            }
        }

        result.put("maxSimilarity", String.format("%.1f%%", maxSimilarity * 100));
        result.put("hasSimilar", maxSimilarity > 0.6);

        if (mostSimilar != null && maxSimilarity > 0.6) {
            String preview = mostSimilar.length() > 100 ?
                    mostSimilar.substring(0, 100) + "..." : mostSimilar;
            result.put("similarContent", preview);
        }

        return result;
    }

    /**
     * 提取简历中的技能标签并返回分类
     */
    public Map<String, List<String>> extractCategorizedSkills(String content) {
        List<String> allKeywords = extractKeywords(content);
        Map<String, List<String>> categorized = new LinkedHashMap<>();
        categorized.put("编程语言", new ArrayList<>());
        categorized.put("框架工具", new ArrayList<>());
        categorized.put("数据库", new ArrayList<>());
        categorized.put("其他技能", new ArrayList<>());

        Set<String> languages = Set.of("Java", "Python", "JavaScript", "Go", "C++", "C#",
                "Rust", "TypeScript", "PHP", "Ruby", "Swift", "Kotlin");
        Set<String> frameworks = Set.of("Spring", "SpringBoot", "MyBatis", "Hibernate",
                "Django", "Flask", "React", "Vue", "Angular", "Node.js", "Express",
                "Docker", "Kubernetes", "Jenkins", "Git");
        Set<String> databases = Set.of("MySQL", "PostgreSQL", "MongoDB", "Redis",
                "Elasticsearch", "Oracle", "SQLite");

        for (String kw : allKeywords) {
            if (languages.contains(kw)) {
                categorized.get("编程语言").add(kw);
            } else if (frameworks.contains(kw)) {
                categorized.get("框架工具").add(kw);
            } else if (databases.contains(kw)) {
                categorized.get("数据库").add(kw);
            } else {
                categorized.get("其他技能").add(kw);
            }
        }

        categorized.values().removeIf(List::isEmpty);
        return categorized;
    }
}
