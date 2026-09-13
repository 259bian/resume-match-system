package com.resumematch.controller.user;

import com.resumematch.common.R;
import com.resumematch.dto.PasswordDTO;
import com.resumematch.entity.User;
import com.resumematch.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public R<?> getProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.getProfile(userId);
    }

    @PutMapping("/profile")
    public R<?> updateProfile(HttpServletRequest request, @RequestBody User user) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.updateProfile(userId, user);
    }

    @PutMapping("/password")
    public R<?> changePassword(HttpServletRequest request, @RequestBody PasswordDTO passwordDTO) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.changePassword(userId, passwordDTO);
    }

    @GetMapping("/stats")
    public R<?> getUserStats(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.getUserStats(userId);
    }
}
