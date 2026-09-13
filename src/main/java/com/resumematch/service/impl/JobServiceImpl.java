package com.resumematch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.resumematch.common.R;
import com.resumematch.dto.JobDTO;
import com.resumematch.entity.Favorite;
import com.resumematch.entity.Job;
import com.resumematch.mapper.FavoriteMapper;
import com.resumematch.mapper.JobMapper;
import com.resumematch.service.JobService;
import com.resumematch.vo.JobVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;
    private final FavoriteMapper favoriteMapper;

    @Override
    public R createJob(JobDTO jobDTO) {
        Job job = new Job();
        job.setTitle(jobDTO.getTitle());
        job.setCompany(jobDTO.getCompany());
        job.setLocation(jobDTO.getLocation());
        job.setSalary(jobDTO.getSalary());
        job.setExperience(jobDTO.getExperience());
        job.setEducation(jobDTO.getEducation());
        job.setDescription(jobDTO.getDescription());
        job.setRequirements(jobDTO.getRequirements());
        job.setSkills(jobDTO.getSkills());
        job.setCategory(jobDTO.getCategory());
        job.setStatus(jobDTO.getStatus() != null ? jobDTO.getStatus() : 1);
        job.setViewCount(0);
        jobMapper.insert(job);
        return R.ok("岗位创建成功");
    }

    @Override
    public R updateJob(Long id, JobDTO jobDTO) {
        Job job = jobMapper.selectById(id);
        if (job == null) {
            return R.fail("岗位不存在");
        }
        if (jobDTO.getTitle() != null) job.setTitle(jobDTO.getTitle());
        if (jobDTO.getCompany() != null) job.setCompany(jobDTO.getCompany());
        if (jobDTO.getLocation() != null) job.setLocation(jobDTO.getLocation());
        if (jobDTO.getSalary() != null) job.setSalary(jobDTO.getSalary());
        if (jobDTO.getExperience() != null) job.setExperience(jobDTO.getExperience());
        if (jobDTO.getEducation() != null) job.setEducation(jobDTO.getEducation());
        if (jobDTO.getDescription() != null) job.setDescription(jobDTO.getDescription());
        if (jobDTO.getRequirements() != null) job.setRequirements(jobDTO.getRequirements());
        if (jobDTO.getSkills() != null) job.setSkills(jobDTO.getSkills());
        if (jobDTO.getCategory() != null) job.setCategory(jobDTO.getCategory());
        if (jobDTO.getStatus() != null) job.setStatus(jobDTO.getStatus());
        jobMapper.updateById(job);
        return R.ok("岗位更新成功");
    }

    @Override
    @Transactional
    public R deleteJob(Long id) {
        Job job = jobMapper.selectById(id);
        if (job == null) {
            return R.fail("岗位不存在");
        }
        jobMapper.deleteById(id);
        return R.ok("岗位已删除");
    }

    @Override
    public R getJobDetail(Long jobId, Long userId) {
        Job job = jobMapper.selectById(jobId);
        if (job == null) {
            return R.fail("岗位不存在");
        }

        job.setViewCount((job.getViewCount() != null ? job.getViewCount() : 0) + 1);
        jobMapper.updateById(job);

        boolean isFavorited = false;
        if (userId != null) {
            LambdaQueryWrapper<Favorite> favWrapper = new LambdaQueryWrapper<>();
            favWrapper.eq(Favorite::getUserId, userId);
            favWrapper.eq(Favorite::getJobId, jobId);
            isFavorited = favoriteMapper.selectCount(favWrapper) > 0;
        }

        JobVO vo = new JobVO();
        vo.setId(job.getId());
        vo.setTitle(job.getTitle());
        vo.setCompany(job.getCompany());
        vo.setLocation(job.getLocation());
        vo.setSalary(job.getSalary());
        vo.setExperience(job.getExperience());
        vo.setEducation(job.getEducation());
        vo.setDescription(job.getDescription());
        vo.setRequirements(job.getRequirements());
        vo.setSkills(job.getSkills());
        vo.setCategory(job.getCategory());
        vo.setStatus(job.getStatus());
        vo.setViewCount(job.getViewCount());
        vo.setIsFavorite(isFavorited);
        vo.setCreateTime(job.getCreateTime());

        return R.ok(vo);
    }

    @Override
    public R listJobs(Integer page, Integer size, String keyword, String category, String location) {
        Page<Job> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Job::getStatus, 1);

        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like(Job::getTitle, keyword)
                    .or().like(Job::getDescription, keyword)
                    .or().like(Job::getSkills, keyword)
                    .or().like(Job::getCompany, keyword));
        }
        if (category != null && !category.isBlank()) {
            wrapper.eq(Job::getCategory, category);
        }
        if (location != null && !location.isBlank()) {
            wrapper.like(Job::getLocation, location);
        }
        wrapper.orderByDesc(Job::getCreateTime);
        jobMapper.selectPage(pageObj, wrapper);

        List<JobVO> voList = pageObj.getRecords().stream().map(job -> {
            JobVO vo = new JobVO();
            vo.setId(job.getId());
            vo.setTitle(job.getTitle());
            vo.setCompany(job.getCompany());
            vo.setLocation(job.getLocation());
            vo.setSalary(job.getSalary());
            vo.setExperience(job.getExperience());
            vo.setEducation(job.getEducation());
            vo.setDescription(job.getDescription());
            vo.setRequirements(job.getRequirements());
            vo.setSkills(job.getSkills());
            vo.setCategory(job.getCategory());
            vo.setStatus(job.getStatus());
            vo.setViewCount(job.getViewCount());
            vo.setIsFavorite(false);
            vo.setCreateTime(job.getCreateTime());
            return vo;
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("records", voList);
        result.put("total", pageObj.getTotal());
        result.put("pages", pageObj.getPages());
        result.put("current", pageObj.getCurrent());
        result.put("size", pageObj.getSize());
        return R.ok(result);
    }

    @Override
    public R getAllJobs() {
        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Job::getStatus, 1);
        wrapper.orderByDesc(Job::getCreateTime);
        List<Job> jobs = jobMapper.selectList(wrapper);

        List<JobVO> voList = jobs.stream().map(job -> {
            JobVO vo = new JobVO();
            vo.setId(job.getId());
            vo.setTitle(job.getTitle());
            vo.setCompany(job.getCompany());
            vo.setLocation(job.getLocation());
            vo.setSalary(job.getSalary());
            vo.setExperience(job.getExperience());
            vo.setEducation(job.getEducation());
            vo.setDescription(job.getDescription());
            vo.setRequirements(job.getRequirements());
            vo.setSkills(job.getSkills());
            vo.setCategory(job.getCategory());
            vo.setStatus(job.getStatus());
            vo.setCreateTime(job.getCreateTime());
            return vo;
        }).collect(Collectors.toList());

        return R.ok(voList);
    }
}