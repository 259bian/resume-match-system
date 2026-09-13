package com.resumematch.controller.user;

import com.resumematch.common.R;
import com.resumematch.service.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{jobId}")
    public R<?> addFavorite(HttpServletRequest request, @PathVariable Long jobId) {
        Long userId = (Long) request.getAttribute("userId");
        return favoriteService.addFavorite(userId, jobId);
    }

    @DeleteMapping("/{jobId}")
    public R<?> removeFavorite(HttpServletRequest request, @PathVariable Long jobId) {
        Long userId = (Long) request.getAttribute("userId");
        return favoriteService.removeFavorite(userId, jobId);
    }

    @GetMapping("/my")
    public R<?> listMyFavorites(HttpServletRequest request,
                                @RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size) {
        Long userId = (Long) request.getAttribute("userId");
        return favoriteService.listMyFavorites(userId, page, size);
    }

    @GetMapping("/check/{jobId}")
    public R<?> isFavorited(HttpServletRequest request, @PathVariable Long jobId) {
        Long userId = (Long) request.getAttribute("userId");
        return favoriteService.isFavorited(userId, jobId);
    }
}
