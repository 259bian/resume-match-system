package com.resumematch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.resumematch.common.Constants;
import com.resumematch.common.R;
import com.resumematch.entity.Application;
import com.resumematch.entity.Job;
import com.resumematch.entity.Resume;
import com.resumematch.entity.User;
import com.resumematch.mapper.ApplicationMapper;
import com.resumematch.mapper.JobMapper;
import com.resumematch.mapper.ResumeMapper;
import com.resumematch.mapper.UserMapper;
import com.resumematch.service.StatisticsService;
import com.resumematch.vo.StatisticsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final UserMapper userMapper;
    private final ResumeMapper resumeMapper;
    private final JobMapper jobMapper;
    private final ApplicationMapper applicationMapper;

    @Override
    public R getDashboardData() {
        StatisticsVO vo = new StatisticsVO();

        // 求职者总数
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(User::getRole, Constants.ROLE_USER);
        vo.setTotalUsers(userMapper.selectCount(userWrapper));

        // 简历总数
        vo.setTotalResumes(resumeMapper.selectCount(null));

        // 在招岗位数
        LambdaQueryWrapper<Job> jobWrapper = new LambdaQueryWrapper<>();
        jobWrapper.eq(Job::getStatus, 1);
        vo.setTotalJobs(jobMapper.selectCount(jobWrapper));

        // 投递总数
        vo.setTotalApplications(applicationMapper.selectCount(null));

        // 今日投递数
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LambdaQueryWrapper<Application> todayWrapper = new LambdaQueryWrapper<>();
        todayWrapper.between(Application::getCreateTime, todayStart, todayEnd);
        vo.setTodayApplications(applicationMapper.selectCount(todayWrapper));

        // 匹配成功率 (已面试+已录取 / 总投递)
        long totalApps = vo.getTotalApplications();
        if (totalApps > 0) {
            LambdaQueryWrapper<Application> successWrapper = new LambdaQueryWrapper<>();
            successWrapper.ge(Application::getStatus, 3);
            long successCount = applicationMapper.selectCount(successWrapper);
            vo.setMatchSuccessRate(Math.round((double) successCount / totalApps * 10000.0) / 100.0);
        }

        // 投递状态分布
        Map<String, Long> statusDistribution = new LinkedHashMap<>();
        statusDistribution.put("待查看", applicationMapper.selectCount(
                new LambdaQueryWrapper<Application>().eq(Application::getStatus, Constants.APPLICATION_PENDING)));
        statusDistribution.put("已查看", applicationMapper.selectCount(
                new LambdaQueryWrapper<Application>().eq(Application::getStatus, Constants.APPLICATION_VIEWED)));
        statusDistribution.put("面试通知", applicationMapper.selectCount(
                new LambdaQueryWrapper<Application>().eq(Application::getStatus, Constants.APPLICATION_INTERVIEW)));
        statusDistribution.put("已拒绝", applicationMapper.selectCount(
                new LambdaQueryWrapper<Application>().eq(Application::getStatus, Constants.APPLICATION_REJECTED)));
        statusDistribution.put("已录取", applicationMapper.selectCount(
                new LambdaQueryWrapper<Application>().eq(Application::getStatus, Constants.APPLICATION_ACCEPTED)));
        vo.setApplicationStatusDistribution(statusDistribution);

        // 岗位分类分布
        List<Job> allJobs = jobMapper.selectList(null);
        Map<String, Long> categoryDistribution = new LinkedHashMap<>();
        for (Job job : allJobs) {
            String category = job.getCategory() != null ? job.getCategory() : "未分类";
            categoryDistribution.merge(category, 1L, Long::sum);
        }
        vo.setJobCategoryDistribution(categoryDistribution);

        // 最近7天投递趋势
        Map<String, Long> dailyTrend = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime dayStart = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime dayEnd = LocalDateTime.of(date, LocalTime.MAX);

            LambdaQueryWrapper<Application> dayWrapper = new LambdaQueryWrapper<>();
            dayWrapper.between(Application::getCreateTime, dayStart, dayEnd);
            long count = applicationMapper.selectCount(dayWrapper);
            dailyTrend.put(date.format(formatter), count);
        }
        vo.setDailyApplicationTrend(dailyTrend);

        return R.ok(vo);
    }

    @Override
    public R getChartsData() {
        Map<String, Object> chartsData = new HashMap<>();

        // 投递状态饼图数据
        List<Map<String, Object>> statusPie = new ArrayList<>();
        Map<String, Object> pending = new HashMap<>();
        pending.put("name", "待查看");
        pending.put("value", applicationMapper.selectCount(
                new LambdaQueryWrapper<Application>().eq(Application::getStatus, Constants.APPLICATION_PENDING)));
        statusPie.add(pending);

        Map<String, Object> viewed = new HashMap<>();
        viewed.put("name", "已查看");
        viewed.put("value", applicationMapper.selectCount(
                new LambdaQueryWrapper<Application>().eq(Application::getStatus, Constants.APPLICATION_VIEWED)));
        statusPie.add(viewed);

        Map<String, Object> interview = new HashMap<>();
        interview.put("name", "面试通知");
        interview.put("value", applicationMapper.selectCount(
                new LambdaQueryWrapper<Application>().eq(Application::getStatus, Constants.APPLICATION_INTERVIEW)));
        statusPie.add(interview);

        Map<String, Object> rejected = new HashMap<>();
        rejected.put("name", "已拒绝");
        rejected.put("value", applicationMapper.selectCount(
                new LambdaQueryWrapper<Application>().eq(Application::getStatus, Constants.APPLICATION_REJECTED)));
        statusPie.add(rejected);

        Map<String, Object> accepted = new HashMap<>();
        accepted.put("name", "已录取");
        accepted.put("value", applicationMapper.selectCount(
                new LambdaQueryWrapper<Application>().eq(Application::getStatus, Constants.APPLICATION_ACCEPTED)));
        statusPie.add(accepted);

        chartsData.put("statusPie", statusPie);

        // 岗位分类柱状图数据
        List<Map<String, Object>> categoryBar = new ArrayList<>();
        List<Job> allJobs = jobMapper.selectList(null);
        Map<String, Long> categoryMap = new LinkedHashMap<>();
        for (Job job : allJobs) {
            String category = job.getCategory() != null ? job.getCategory() : "未分类";
            categoryMap.merge(category, 1L, Long::sum);
        }
        List<String> categories = new ArrayList<>(categoryMap.keySet());
        List<Long> catValues = new ArrayList<>(categoryMap.values());

        Map<String, Object> catData = new HashMap<>();
        catData.put("categories", categories);
        catData.put("values", catValues);
        chartsData.put("categoryBar", catData);

        // 每日投递趋势折线图数据
        List<String> days = new ArrayList<>();
        List<Long> dayCounts = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime dayStart = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime dayEnd = LocalDateTime.of(date, LocalTime.MAX);

            LambdaQueryWrapper<Application> dayWrapper = new LambdaQueryWrapper<>();
            dayWrapper.between(Application::getCreateTime, dayStart, dayEnd);

            days.add(date.format(formatter));
            dayCounts.add(applicationMapper.selectCount(dayWrapper));
        }

        Map<String, Object> lineData = new HashMap<>();
        lineData.put("days", days);
        lineData.put("values", dayCounts);
        chartsData.put("trendLine", lineData);

        // 匹配分数分布
        LambdaQueryWrapper<Application> appWrapper = new LambdaQueryWrapper<>();
        appWrapper.isNotNull(Application::getMatchScore);
        List<Application> scoredApps = applicationMapper.selectList(appWrapper);

        Map<String, Long> scoreDistribution = new LinkedHashMap<>();
        scoreDistribution.put("0-20", 0L);
        scoreDistribution.put("21-40", 0L);
        scoreDistribution.put("41-60", 0L);
        scoreDistribution.put("61-80", 0L);
        scoreDistribution.put("81-100", 0L);

        for (Application app : scoredApps) {
            Integer score = app.getMatchScore();
            if (score == null) continue;
            if (score <= 20) scoreDistribution.merge("0-20", 1L, Long::sum);
            else if (score <= 40) scoreDistribution.merge("21-40", 1L, Long::sum);
            else if (score <= 60) scoreDistribution.merge("41-60", 1L, Long::sum);
            else if (score <= 80) scoreDistribution.merge("61-80", 1L, Long::sum);
            else scoreDistribution.merge("81-100", 1L, Long::sum);
        }
        chartsData.put("scoreDistribution", scoreDistribution);

        return R.ok(chartsData);
    }
}
