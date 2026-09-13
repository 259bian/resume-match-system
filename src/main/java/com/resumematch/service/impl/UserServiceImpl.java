package com.resumematch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.resumematch.common.Constants;
import com.resumematch.common.R;
import com.resumematch.dto.LoginDTO;
import com.resumematch.dto.PasswordDTO;
import com.resumematch.dto.RegisterDTO;
import com.resumematch.entity.Application;
import com.resumematch.entity.Favorite;
import com.resumematch.entity.Resume;
import com.resumematch.entity.User;
import com.resumematch.mapper.ApplicationMapper;
import com.resumematch.mapper.FavoriteMapper;
import com.resumematch.mapper.ResumeMapper;
import com.resumematch.mapper.UserMapper;
import com.resumematch.service.UserService;
import com.resumematch.util.JwtUtil;
import com.resumematch.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final ResumeMapper resumeMapper;
    private final ApplicationMapper applicationMapper;
    private final FavoriteMapper favoriteMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public R login(LoginDTO loginDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            return R.fail("用户名或密码错误");
        }

        String storedPassword = user.getPassword();
        boolean passwordMatch;

        if (storedPassword != null && storedPassword.startsWith("$2a$")) {
            passwordMatch = passwordEncoder.matches(loginDTO.getPassword(), storedPassword);
        } else {
            // Legacy plaintext password — verify and auto-upgrade to BCrypt
            passwordMatch = loginDTO.getPassword().equals(storedPassword);
            if (passwordMatch) {
                user.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
                userMapper.updateById(user);
                log.info("Auto-upgraded password to BCrypt for user: {}", user.getUsername());
            }
        }

        if (!passwordMatch) {
            return R.fail("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 1) {
            return R.fail("账号已被禁用，请联系管理员");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setRealName(user.getRealName());
        userVO.setPhone(user.getPhone());
        userVO.setEmail(user.getEmail());
        userVO.setAvatar(user.getAvatar());
        userVO.setRole(user.getRole());
        userVO.setStatus(user.getStatus());
        userVO.setToken(token);
        userVO.setCreateTime(user.getCreateTime());
        return R.ok(userVO);
    }

    @Override
    public R register(RegisterDTO registerDTO) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            return R.fail("用户名已存在");
        }
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRealName(registerDTO.getRealName());
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setRole(Constants.ROLE_USER);
        user.setStatus(0);
        userMapper.insert(user);
        return R.ok("注册成功");
    }

    @Override
    public R getProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return R.fail("用户不存在");
        }
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setRealName(user.getRealName());
        userVO.setPhone(user.getPhone());
        userVO.setEmail(user.getEmail());
        userVO.setAvatar(user.getAvatar());
        userVO.setRole(user.getRole());
        userVO.setStatus(user.getStatus());
        userVO.setCreateTime(user.getCreateTime());
        return R.ok(userVO);
    }

    @Override
    public R updateProfile(Long userId, User user) {
        User existing = userMapper.selectById(userId);
        if (existing == null) {
            return R.fail("用户不存在");
        }
        if (user.getRealName() != null) {
            existing.setRealName(user.getRealName());
        }
        if (user.getPhone() != null) {
            existing.setPhone(user.getPhone());
        }
        if (user.getEmail() != null) {
            existing.setEmail(user.getEmail());
        }
        userMapper.updateById(existing);
        return R.ok("个人信息更新成功");
    }

    @Override
    public R changePassword(Long userId, PasswordDTO passwordDTO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return R.fail("用户不存在");
        }

        String storedPassword = user.getPassword();
        boolean oldPasswordMatch;

        if (storedPassword != null && storedPassword.startsWith("$2a$")) {
            oldPasswordMatch = passwordEncoder.matches(passwordDTO.getOldPassword(), storedPassword);
        } else {
            oldPasswordMatch = passwordDTO.getOldPassword().equals(storedPassword);
        }

        if (!oldPasswordMatch) {
            return R.fail("原密码错误");
        }
        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userMapper.updateById(user);
        return R.ok("密码修改成功");
    }

    @Override
    public R listUsers(Integer page, Integer size, String keyword) {
        Page<User> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        //wrapper.eq(User::getRole, Constants.ROLE_USER);
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like(User::getUsername, keyword)
                    .or().like(User::getRealName, keyword)
                    .or().like(User::getPhone, keyword)
                    .or().like(User::getEmail, keyword));
        }
        wrapper.orderByDesc(User::getCreateTime);
        userMapper.selectPage(pageObj, wrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageObj.getRecords());
        result.put("total", pageObj.getTotal());
        return R.ok(result);
    }

    @Override
    public R updateUserStatus(Long id, Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return R.fail("用户不存在");
        }
        user.setStatus(status);
        userMapper.updateById(user);
        return R.ok(status == 0 ? "用户已启用" : "用户已禁用");
    }

    @Override
    public R getUserStats(Long userId) {
        LambdaQueryWrapper<Resume> resumeWrapper = new LambdaQueryWrapper<>();
        resumeWrapper.eq(Resume::getUserId, userId);
        long resumeCount = resumeMapper.selectCount(resumeWrapper);

        LambdaQueryWrapper<Application> appWrapper = new LambdaQueryWrapper<>();
        appWrapper.eq(Application::getUserId, userId);
        long applicationCount = applicationMapper.selectCount(appWrapper);

        LambdaQueryWrapper<Favorite> favWrapper = new LambdaQueryWrapper<>();
        favWrapper.eq(Favorite::getUserId, userId);
        long favoriteCount = favoriteMapper.selectCount(favWrapper);

        Map<String, Object> stats = new HashMap<>();
        stats.put("resumeCount", resumeCount);
        stats.put("applicationCount", applicationCount);
        stats.put("favoriteCount", favoriteCount);
        return R.ok(stats);
    }
}
