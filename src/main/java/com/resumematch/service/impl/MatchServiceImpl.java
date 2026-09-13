package com.resumematch.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.resumematch.common.R;
import com.resumematch.dto.MatchRequestDTO;
import com.resumematch.entity.Job;
import com.resumematch.entity.MatchResult;
import com.resumematch.entity.Resume;
import com.resumematch.ai.JobMatcher;
import com.resumematch.mapper.JobMapper;
import com.resumematch.mapper.MatchResultMapper;
import com.resumematch.mapper.ResumeMapper;
import com.resumematch.service.MatchService;
import com.resumematch.vo.MatchResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final ResumeMapper resumeMapper;
    private final JobMapper jobMapper;
    private final MatchResultMapper matchResultMapper;
    private final JobMatcher jobMatcher;

    @Override
    @Transactional
    public R matchResumeToJob(MatchRequestDTO matchRequestDTO) {
        Resume resume = resumeMapper.selectById(matchRequestDTO.getResumeId());
        if (resume == null) {
            return R.fail("简历不存在");
        }

        Job job = jobMapper.selectById(matchRequestDTO.getJobId());
        if (job == null) {
            return R.fail("岗位不存在");
        }

        // AI匹配
        JSONObject matchJson = jobMatcher.match(resume.getContent(), job.getRequirements());
        int score = matchJson.getInteger("score") != null ? matchJson.getInteger("score") : 0;
        String detail = matchJson.toJSONString();

        // 保存匹配结果
        MatchResult matchResult = new MatchResult();
        matchResult.setResumeId(resume.getId());
        matchResult.setJobId(job.getId());
        matchResult.setUserId(resume.getUserId());
        matchResult.setScore(score);
        matchResult.setDetail(detail);
        matchResult.setSuggestion(matchJson.getString("suggestion"));
        matchResultMapper.insert(matchResult);

        // 构建VO
        MatchResultVO vo = new MatchResultVO();
        vo.setId(matchResult.getId());
        vo.setResumeId(resume.getId());
        vo.setJobId(job.getId());
        vo.setJobTitle(job.getTitle());
        vo.setJobName(job.getTitle());
        vo.setCompany(job.getCompany());
        vo.setCompanyName(job.getCompany());
        vo.setLocation(job.getLocation());
        vo.setSalary(job.getSalary());
        vo.setSalaryDisplay(job.getSalary());
        vo.setScore(score);
        vo.setMatchScore(score);
        vo.setDetail(detail);
        vo.setSuggestion(matchResult.getSuggestion());
        vo.setCreateTime(matchResult.getCreateTime());

        return R.ok(vo);
    }

    @Override
    @Transactional
    public R batchMatch(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        if (resume == null) {
            return R.fail("简历不存在");
        }

        // 获取所有在招岗位
        LambdaQueryWrapper<Job> jobWrapper = new LambdaQueryWrapper<>();
        jobWrapper.eq(Job::getStatus, 1);
        List<Job> activeJobs = jobMapper.selectList(jobWrapper);

        if (activeJobs.isEmpty()) {
            return R.fail("暂无在招岗位");
        }

        // 删除旧的批量匹配结果
        LambdaQueryWrapper<MatchResult> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(MatchResult::getResumeId, resumeId);
        matchResultMapper.delete(deleteWrapper);

        List<MatchResultVO> results = new ArrayList<>();
        for (Job job : activeJobs) {
            JSONObject matchJson = jobMatcher.match(resume.getContent(), job.getRequirements());
            int score = matchJson.getInteger("score") != null ? matchJson.getInteger("score") : 0;
            String detail = matchJson.toJSONString();

            MatchResult matchResult = new MatchResult();
            matchResult.setResumeId(resume.getId());
            matchResult.setJobId(job.getId());
            matchResult.setUserId(resume.getUserId());
            matchResult.setScore(score);
            matchResult.setDetail(detail);
            matchResult.setSuggestion(matchJson.getString("suggestion"));
            matchResultMapper.insert(matchResult);

            MatchResultVO vo = new MatchResultVO();
            vo.setId(matchResult.getId());
            vo.setResumeId(resume.getId());
            vo.setJobId(job.getId());
            vo.setJobTitle(job.getTitle());
            vo.setJobName(job.getTitle());
            vo.setCompany(job.getCompany());
            vo.setCompanyName(job.getCompany());
            vo.setLocation(job.getLocation());
            vo.setSalary(job.getSalary());
            vo.setSalaryDisplay(job.getSalary());
            vo.setScore(score);
            vo.setMatchScore(score);
            vo.setDetail(detail);
            vo.setSuggestion(matchResult.getSuggestion());
            vo.setCreateTime(matchResult.getCreateTime());
            results.add(vo);
        }

        // 按分数降序排序
        results.sort((a, b) -> b.getScore().compareTo(a.getScore()));
        return R.ok(results);
    }

    @Override
    public R getMatchHistory(Long userId, Integer page, Integer size) {
        Page<MatchResult> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<MatchResult> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MatchResult::getUserId, userId);
        wrapper.orderByDesc(MatchResult::getCreateTime);
        matchResultMapper.selectPage(pageObj, wrapper);

        List<MatchResultVO> voList = pageObj.getRecords().stream().map(mr -> {
            MatchResultVO vo = new MatchResultVO();
            vo.setId(mr.getId());
            vo.setResumeId(mr.getResumeId());
            vo.setJobId(mr.getJobId());
            vo.setScore(mr.getScore());
            vo.setMatchScore(mr.getScore());
            vo.setDetail(mr.getDetail());
            vo.setSuggestion(mr.getSuggestion());
            vo.setCreateTime(mr.getCreateTime());

            Job job = jobMapper.selectById(mr.getJobId());
            if (job != null) {
                vo.setJobTitle(job.getTitle());
                vo.setJobName(job.getTitle());
                vo.setCompany(job.getCompany());
                vo.setCompanyName(job.getCompany());
                vo.setLocation(job.getLocation());
                vo.setSalary(job.getSalary());
                vo.setSalaryDisplay(job.getSalary());
            }
            return vo;
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("records", voList);
        result.put("total", pageObj.getTotal());
        return R.ok(result);
    }

    @Override
    public R getMatchDetail(Long matchId) {
        MatchResult matchResult = matchResultMapper.selectById(matchId);
        if (matchResult == null) {
            return R.fail("匹配记录不存在");
        }

        MatchResultVO vo = new MatchResultVO();
        vo.setId(matchResult.getId());
        vo.setResumeId(matchResult.getResumeId());
        vo.setJobId(matchResult.getJobId());
        vo.setScore(matchResult.getScore());
        vo.setMatchScore(matchResult.getScore());
        vo.setDetail(matchResult.getDetail());
        vo.setSuggestion(matchResult.getSuggestion());
        vo.setCreateTime(matchResult.getCreateTime());

        Job job = jobMapper.selectById(matchResult.getJobId());
        if (job != null) {
            vo.setJobTitle(job.getTitle());
            vo.setJobName(job.getTitle());
            vo.setCompany(job.getCompany());
            vo.setCompanyName(job.getCompany());
            vo.setLocation(job.getLocation());
            vo.setSalary(job.getSalary());
            vo.setSalaryDisplay(job.getSalary());
        }

        return R.ok(vo);
    }
}
