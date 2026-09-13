package com.resumematch.controller.admin;

import com.resumematch.common.Constants;
import com.resumematch.common.R;
import com.resumematch.entity.User;
import com.resumematch.mapper.UserMapper;
import com.resumematch.service.StatisticsService;
import com.resumematch.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final StatisticsService statisticsService;
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/overview")
    public R<?> getOverview() {
        return statisticsService.getDashboardData();
    }

    @GetMapping("/user/list")
    public R<?> listUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return userService.listUsers(page, size, keyword);
    }

    @PutMapping("/user/{id}/status")
    public R<?> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        return userService.updateUserStatus(id, status);
    }

    @PutMapping("/user/{id}/role")
    public R<?> updateUserRole(@PathVariable Long id, @RequestParam Integer role) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return R.fail("用户不存在");
        }
        user.setRole(role);
        userMapper.updateById(user);
        return R.ok("角色更新成功");
    }

    @GetMapping("/charts")
    public R<?> getChartsData() {
        return statisticsService.getChartsData();
    }
}
