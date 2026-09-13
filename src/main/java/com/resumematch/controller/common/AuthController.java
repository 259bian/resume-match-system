package com.resumematch.controller.common;

import com.resumematch.common.R;
import com.resumematch.dto.LoginDTO;
import com.resumematch.dto.RegisterDTO;
import com.resumematch.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public R<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @PostMapping("/register")
    public R<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }
}
