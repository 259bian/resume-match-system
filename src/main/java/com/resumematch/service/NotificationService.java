package com.resumematch.service;

import com.resumematch.common.R;

public interface NotificationService {

    R listMyNotifications(Long userId, Integer page, Integer size, Integer type);

    R getUnreadCount(Long userId);

    R markAsRead(Long id, Long userId);

    R markAllAsRead(Long userId);

    R deleteNotification(Long id);
}
