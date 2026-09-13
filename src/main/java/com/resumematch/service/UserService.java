package com.resumematch.service;

import com.resumematch.common.R;
import com.resumematch.dto.LoginDTO;
import com.resumematch.dto.PasswordDTO;
import com.resumematch.dto.RegisterDTO;
import com.resumematch.entity.User;

public interface UserService {

    R login(LoginDTO loginDTO);

    R register(RegisterDTO registerDTO);

    R getProfile(Long userId);

    R updateProfile(Long userId, User user);

    R changePassword(Long userId, PasswordDTO passwordDTO);

    R listUsers(Integer page, Integer size, String keyword);

    R updateUserStatus(Long id, Integer status);

    R getUserStats(Long userId);
}
