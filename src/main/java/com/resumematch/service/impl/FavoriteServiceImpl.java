package com.resumematch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.resumematch.common.R;
import com.resumematch.entity.Favorite;
import com.resumematch.entity.Job;
import com.resumematch.mapper.FavoriteMapper;
import com.resumematch.mapper.JobMapper;
import com.resumematch.service.FavoriteService;
import com.resumematch.vo.JobVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final JobMapper jobMapper;

    @Override
    public R addFavorite(Long userId, Long jobId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        wrapper.eq(Favorite::getJobId, jobId);
        if (favoriteMapper.selectCount(wrapper) > 0) {
            return R.fail("已收藏该岗位");
        }

        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setJobId(jobId);
        favoriteMapper.insert(favorite);
        return R.ok("收藏成功");
    }

    @Override
    public R removeFavorite(Long userId, Long jobId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        wrapper.eq(Favorite::getJobId, jobId);
        favoriteMapper.delete(wrapper);
        return R.ok("已取消收藏");
    }

    @Override
    public R listMyFavorites(Long userId, Integer page, Integer size) {
        Page<Favorite> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        wrapper.orderByDesc(Favorite::getCreateTime);
        favoriteMapper.selectPage(pageObj, wrapper);

        List<JobVO> voList = pageObj.getRecords().stream().map(fav -> {
            Job job = jobMapper.selectById(fav.getJobId());
            if (job == null) return null;
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
            vo.setIsFavorite(true);
            vo.setCreateTime(job.getCreateTime());
            return vo;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("records", voList);
        result.put("total", pageObj.getTotal());
        return R.ok(result);
    }

    @Override
    public R isFavorited(Long userId, Long jobId) {
        LambdaQueryWrapper<Favorite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorite::getUserId, userId);
        wrapper.eq(Favorite::getJobId, jobId);
        long count = favoriteMapper.selectCount(wrapper);
        Map<String, Boolean> result = new HashMap<>();
        result.put("isFavorited", count > 0);
        return R.ok(result);
    }
}
