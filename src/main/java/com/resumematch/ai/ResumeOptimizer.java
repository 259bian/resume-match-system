package com.resumematch.ai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResumeOptimizer {

    private final DeepSeekClient deepSeekClient;

    private static final String SYSTEM_PROMPT = """
            你是一位资深的HR和职业规划专家，擅长优化简历。请根据用户提供的简历内容，
            从以下几个方面进行优化：
            1. 语言表达：使用更专业、更有力的动词和描述
            2. 结构排版：建议更清晰的内容组织结构
            3. 关键词优化：添加行业关键词，提高ATS筛选通过率
            4. 亮点突出：突出候选人的核心竞争力
            5. 量化成果：将模糊描述转化为可量化的成果

            请返回优化后的完整简历内容，并在最后附上【优化建议】部分，
            列出3-5条具体改进建议。""";

    public String optimize(String resumeContent) {
        if (resumeContent == null || resumeContent.trim().isEmpty()) {
            return "简历内容为空，无法进行优化。请先上传并完善您的简历。";
        }

        String userMessage = "请帮我优化以下简历内容：\n\n" + resumeContent;
        log.info("开始调用AI优化简历");
        String result = deepSeekClient.chat(SYSTEM_PROMPT, userMessage);

        if (result == null) {
            // AI调用失败时，返回本地优化结果
            return generateLocalOptimization(resumeContent);
        }

        // ✅ 核心修复：清理所有 Markdown 标记，去掉 **、#、--- 等
        return cleanMarkdown(result);
    }

    public String polish(String content) {
        String systemPrompt = """
                你是一位专业的简历润色专家。请对用户提供的文字进行润色，
                使其更加专业、简洁、有吸引力。保持原意不变，优化语言表达。
                直接返回润色后的文字，不要添加额外解释。""";

        String result = deepSeekClient.chat(systemPrompt, content);
        if (result != null) {
            result = cleanMarkdown(result);
        }
        return result != null ? result : content;
    }

    // ==============================================
    // ✅ 清理 Markdown 标记的方法
    // ==============================================
    private String cleanMarkdown(String text) {
        if (text == null) return null;

        // 1. 去掉加粗标记 **
        text = text.replaceAll("\\*\\*", "");

        // 2. 去掉标题标记 #
        text = text.replaceAll("### ", "");
        text = text.replaceAll("## ", "");
        text = text.replaceAll("# ", "");

        // 3. 去掉分割线 ---
        text = text.replaceAll("---", "");

        // 4. 去掉斜体标记 *（如果有的话）
        text = text.replaceAll("(?<!\\*)\\*(?!\\*)", "");

        return text;
    }

    private String generateLocalOptimization(String original) {
        StringBuilder sb = new StringBuilder();
        sb.append("【优化后的简历内容】\n\n");
        sb.append(original).append("\n\n");
        sb.append("---\n\n");
        sb.append("【优化建议】\n");
        sb.append("1. 建议使用更多量化的成果描述（如'提升了XX%'、'管理了XX人团队'）\n");
        sb.append("2. 每个工作经历建议包含：背景-行动-结果的STAR法则描述\n");
        sb.append("3. 技能部分建议按照熟练程度分类（精通/熟练/了解）\n");
        sb.append("4. 建议添加与目标岗位相关的行业关键词\n");
        sb.append("5. 控制简历长度在1-2页，突出近5年相关经验\n");
        sb.append("\n（注：当前为本地模板优化，配置DeepSeek API后可获得AI定制化优化）");
        return cleanMarkdown(sb.toString());
    }
}