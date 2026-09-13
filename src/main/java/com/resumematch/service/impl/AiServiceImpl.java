package com.resumematch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.resumematch.common.R;
import com.resumematch.entity.Resume;
import com.resumematch.entity.ResumeSkill;
import com.resumematch.ai.KeywordExtractor;
import com.resumematch.ai.ResumeOptimizer;
import com.resumematch.mapper.ResumeMapper;
import com.resumematch.mapper.ResumeSkillMapper;
import com.resumematch.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final ResumeMapper resumeMapper;
    private final ResumeSkillMapper resumeSkillMapper;
    private final ResumeOptimizer resumeOptimizer;
    private final KeywordExtractor keywordExtractor;

    @Override
    public R optimizeResume(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return R.fail("简历不存在");
        }
        if (resume.getContent() == null || resume.getContent().trim().isEmpty()) {
            return R.fail("简历内容为空，无法优化");
        }
        String optimized = resumeOptimizer.optimize(resume.getContent());
        return R.ok(optimized);
    }

    @Override
    public R polishText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return R.fail("文本内容为空");
        }
        String polished = resumeOptimizer.polish(text);
        return R.ok(polished);
    }

    @Override
    @Transactional
    public R extractKeywords(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return R.fail("简历不存在");
        }
        if (resume.getContent() == null || resume.getContent().trim().isEmpty()) {
            return R.fail("简历内容为空");
        }

        List<String> keywords = keywordExtractor.extractKeywords(resume.getContent());

        // 清除旧技能
        LambdaQueryWrapper<ResumeSkill> skillWrapper = new LambdaQueryWrapper<>();
        skillWrapper.eq(ResumeSkill::getResumeId, resumeId);
        resumeSkillMapper.delete(skillWrapper);

        // 保存新技能
        for (String keyword : keywords) {
            ResumeSkill skill = new ResumeSkill();
            skill.setResumeId(resumeId);
            skill.setSkillName(keyword);
            resumeSkillMapper.insert(skill);
        }

        return R.ok(keywords);
    }

    @Override
    public R extractCategorizedSkills(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return R.fail("简历不存在");
        }
        if (resume.getContent() == null || resume.getContent().trim().isEmpty()) {
            return R.fail("简历内容为空");
        }

        Map<String, List<String>> categorizedSkills = keywordExtractor.extractCategorizedSkills(resume.getContent());
        return R.ok(categorizedSkills);
    }

    @Override
    public R checkSimilarity(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return R.fail("简历不存在");
        }
        if (resume.getContent() == null || resume.getContent().trim().isEmpty()) {
            return R.fail("简历内容为空");
        }

        // 获取所有其他简历的内容
        LambdaQueryWrapper<Resume> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(Resume::getId, resumeId);
        wrapper.isNotNull(Resume::getContent);
        List<Resume> otherResumes = resumeMapper.selectList(wrapper);

        List<String> otherContents = otherResumes.stream()
                .map(Resume::getContent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Map<String, Object> similarityResult = keywordExtractor.checkSimilarity(resume.getContent(), otherContents);
        return R.ok(similarityResult);
    }
}
