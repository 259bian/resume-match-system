package com.resumematch.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.resumematch.common.Constants;
import com.resumematch.common.R;
import com.resumematch.dto.ApplicationDTO;
import com.resumematch.entity.*;
import com.resumematch.ai.JobMatcher;
import com.resumematch.entity.User;
import com.resumematch.mapper.*;
import com.resumematch.service.ApplicationService;
import com.resumematch.util.ExcelUtil;
import com.resumematch.vo.ApplicationVO;
import com.resumematch.vo.MatchResultVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationMapper applicationMapper;
    private final ResumeMapper resumeMapper;
    private final JobMapper jobMapper;
    private final MatchResultMapper matchResultMapper;
    private final NotificationMapper notificationMapper;
    private final UserMapper userMapper;
    private final JobMatcher jobMatcher;
    private final ExcelUtil excelUtil;

    @Override
    @Transactional
    public R apply(Long userId, ApplicationDTO applicationDTO) {
        Resume resume = resumeMapper.selectById(applicationDTO.getResumeId());
        if (resume == null) {
            return R.fail("简历不存在");
        }

        Job job = jobMapper.selectById(applicationDTO.getJobId());
        if (job == null) {
            return R.fail("岗位不存在");
        }

        // 创建投递记录
        Application application = new Application();
        application.setUserId(userId);
        application.setJobId(applicationDTO.getJobId());
        application.setResumeId(applicationDTO.getResumeId());
        application.setStatus(Constants.APPLICATION_PENDING);
        applicationMapper.insert(application);

        // AI匹配
        JSONObject matchResult = jobMatcher.match(resume.getContent(), job.getRequirements());
        int score = matchResult.getInteger("score") != null ? matchResult.getInteger("score") : 0;
        String detail = matchResult.toJSONString();

        application.setMatchScore(score);
        applicationMapper.updateById(application);

        // 保存匹配结果
        MatchResult mr = new MatchResult();
        mr.setResumeId(resume.getId());
        mr.setJobId(job.getId());
        mr.setUserId(userId);
        mr.setScore(score);
        mr.setDetail(detail);
        mr.setSuggestion(matchResult.getString("suggestion"));
        matchResultMapper.insert(mr);

        // 创建通知
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle("投递成功");
        notification.setContent("您已成功投递岗位【" + job.getTitle() + "】，匹配度得分: " + score + "分");
        notification.setType(Constants.MSG_APPLY_SUCCESS);
        notification.setIsRead(Constants.MSG_UNREAD);
        notification.setRelatedId(application.getId());
        notificationMapper.insert(notification);

        Map<String, Object> data = new HashMap<>();
        data.put("applicationId", application.getId());
        data.put("matchScore", score);
        return R.ok(data);
    }

    @Override
    public R listMyApplications(Long userId, Integer page, Integer size, Integer status) {
        Page<Application> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<Application> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Application::getUserId, userId);
        if (status != null) {
            wrapper.eq(Application::getStatus, status);
        }
        wrapper.orderByDesc(Application::getCreateTime);
        applicationMapper.selectPage(pageObj, wrapper);

        List<ApplicationVO> voList = pageObj.getRecords().stream().map(app -> {
            ApplicationVO vo = new ApplicationVO();
            vo.setId(app.getId());
            vo.setUserId(app.getUserId());
            User user = userMapper.selectById(app.getUserId());
            if (user != null) vo.setUserName(user.getRealName());
            vo.setJobId(app.getJobId());
            vo.setResumeId(app.getResumeId());
            vo.setStatus(app.getStatus());
            vo.setMatchScore(app.getMatchScore());
            vo.setRemark(app.getRemark());
            vo.setCreateTime(app.getCreateTime());

            Job job = jobMapper.selectById(app.getJobId());
            if (job != null) {
                vo.setJobTitle(job.getTitle());
                vo.setJobName(job.getTitle());
                vo.setCompanyName(job.getCompany());
                vo.setCompany(job.getCompany());
            }

            Resume resume = resumeMapper.selectById(app.getResumeId());
            if (resume != null) {
                vo.setResumeTitle(resume.getTitle());
                vo.setResumeName(resume.getTitle());
            }
            return vo;
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("records", voList);
        result.put("total", pageObj.getTotal());
        return R.ok(result);
    }

    @Override
    public R getApplicationDetail(Long id) {
        Application application = applicationMapper.selectById(id);
        if (application == null) {
            return R.fail("投递记录不存在");
        }
        return R.ok(application);
    }

    @Override
    @Transactional
    public R updateStatus(Long id, Integer status, String remark) {
        Application application = applicationMapper.selectById(id);
        if (application == null) {
            return R.fail("投递记录不存在");
        }
        application.setStatus(status);
        if (remark != null) {
            application.setRemark(remark);
        }
        applicationMapper.updateById(application);

        // 创建通知
        String statusText;
        int notifType;
        switch (status) {
            case Constants.APPLICATION_VIEWED:
                statusText = "简历已被查看";
                notifType = Constants.MSG_SYSTEM;
                break;
            case Constants.APPLICATION_INTERVIEW:
                statusText = "恭喜您获得面试通知";
                notifType = Constants.MSG_INTERVIEW_NOTICE;
                break;
            case Constants.APPLICATION_REJECTED:
                statusText = "很遗憾，您未被录取";
                notifType = Constants.MSG_REJECTED;
                break;
            case Constants.APPLICATION_ACCEPTED:
                statusText = "恭喜您被录取";
                notifType = Constants.MSG_INTERVIEW_NOTICE;
                break;
            default:
                statusText = "投递状态已更新";
                notifType = Constants.MSG_SYSTEM;
        }

        Job job = jobMapper.selectById(application.getJobId());
        String jobTitle = job != null ? job.getTitle() : "未知岗位";

        Notification notification = new Notification();
        notification.setUserId(application.getUserId());
        notification.setTitle(statusText);
        notification.setContent("您投递的岗位【" + jobTitle + "】状态已更新为：" + statusText +
                (remark != null && !remark.isEmpty() ? "，备注：" + remark : ""));
        notification.setType(notifType);
        notification.setIsRead(Constants.MSG_UNREAD);
        notification.setRelatedId(application.getId());
        notificationMapper.insert(notification);

        return R.ok("状态更新成功");
    }

    @Override
    public R withdrawApplication(Long id, Long userId) {
        Application application = applicationMapper.selectById(id);
        if (application == null) {
            return R.fail("投递记录不存在");
        }
        if (!application.getUserId().equals(userId)) {
            return R.fail("无权操作该投递记录");
        }
        applicationMapper.deleteById(id);
        return R.ok("已撤回投递");
    }

    @Override
    public R listAllApplications(Integer page, Integer size, Integer status) {
        Page<Application> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<Application> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Application::getStatus, status);
        }
        wrapper.orderByDesc(Application::getCreateTime);
        applicationMapper.selectPage(pageObj, wrapper);

        List<ApplicationVO> voList = pageObj.getRecords().stream().map(app -> {
            ApplicationVO vo = new ApplicationVO();
            vo.setId(app.getId());
            vo.setUserId(app.getUserId());
            User user = userMapper.selectById(app.getUserId());
            if (user != null) vo.setUserName(user.getRealName());
            vo.setJobId(app.getJobId());
            vo.setResumeId(app.getResumeId());
            vo.setStatus(app.getStatus());
            vo.setMatchScore(app.getMatchScore());
            vo.setRemark(app.getRemark());
            vo.setCreateTime(app.getCreateTime());

            Job job = jobMapper.selectById(app.getJobId());
            if (job != null) {
                vo.setJobTitle(job.getTitle());
                vo.setJobName(job.getTitle());
                vo.setCompanyName(job.getCompany());
                vo.setCompany(job.getCompany());
            }

            Resume resume = resumeMapper.selectById(app.getResumeId());
            if (resume != null) {
                vo.setResumeTitle(resume.getTitle());
                vo.setResumeName(resume.getTitle());
            }
            return vo;
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("records", voList);
        result.put("total", pageObj.getTotal());
        return R.ok(result);
    }

    @Override
    public R exportApplications(HttpServletResponse response) {
        List<Application> applications = applicationMapper.selectList(null);
        excelUtil.export(response, "投递数据", "投递列表", Application.class, applications);
        return R.ok();
    }
}
