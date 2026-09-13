package com.resumematch.controller.user;

import com.resumematch.common.R;
import com.resumematch.service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/my")
    public R<?> listMyNotifications(HttpServletRequest request,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(required = false) Integer type) {
        Long userId = (Long) request.getAttribute("userId");
        return notificationService.listMyNotifications(userId, page, size, type);
    }

    @GetMapping("/unread-count")
    public R<?> getUnreadCount(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return notificationService.getUnreadCount(userId);
    }

    @PutMapping("/{id}/read")
    public R<?> markAsRead(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        return notificationService.markAsRead(id, userId);
    }

    @PutMapping("/read-all")
    public R<?> markAllAsRead(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return notificationService.markAllAsRead(userId);
    }

    @DeleteMapping("/{id}")
    public R<?> deleteNotification(@PathVariable Long id) {
        return notificationService.deleteNotification(id);
    }
}
