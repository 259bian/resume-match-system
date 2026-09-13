package com.resumematch.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Component
public class PdfUtil {

    private static final List<String> COMMON_SKILLS = Arrays.asList(
            "Java", "Python", "JavaScript", "Go", "C++", "C#", "Rust", "TypeScript",
            "Spring", "SpringBoot", "MyBatis", "Hibernate", "Django", "Flask", "React",
            "Vue", "Angular", "Node.js", "Express", "MySQL", "PostgreSQL", "MongoDB",
            "Redis", "Elasticsearch", "Docker", "Kubernetes", "Jenkins", "Git", "Linux",
            "AWS", "阿里云", "腾讯云", "机器学习", "深度学习", "NLP", "数据分析",
            "项目管理", "团队管理", "产品设计", "UI设计", "测试", "运维"
    );

    public List<String> extractKeywords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> foundSkills = new LinkedHashSet<>();
        for (String skill : COMMON_SKILLS) {
            if (text.contains(skill)) {
                foundSkills.add(skill);
            }
        }

        // 提取英文单词作为补充
        String[] words = text.split("[\\s,，。.；;！!？?、]+");
        for (String word : words) {
            String trimmed = word.trim();
            if (trimmed.length() >= 2 && Pattern.matches("[A-Za-z+#]+", trimmed)) {
                foundSkills.add(trimmed);
            }
        }

        return new ArrayList<>(foundSkills);
    }

    public double calculateSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null || text1.isEmpty() || text2.isEmpty()) {
            return 0.0;
        }

        List<String> words1 = extractKeywords(text1);
        List<String> words2 = extractKeywords(text2);

        if (words1.isEmpty() && words2.isEmpty()) {
            return 0.0;
        }

        Set<String> allWords = new HashSet<>();
        allWords.addAll(words1);
        allWords.addAll(words2);

        int intersection = 0;
        for (String word : words1) {
            if (words2.contains(word)) {
                intersection++;
            }
        }

        int union = allWords.size();
        return union == 0 ? 0.0 : (double) intersection / union;
    }
}
