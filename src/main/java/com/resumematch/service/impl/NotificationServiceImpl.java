package com.resumematch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.resumematch.common.Constants;
import com.resumematch.common.R;
import com.resumematch.entity.Notification;
import com.resumematch.mapper.NotificationMapper;
import com.resumematch.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    @Override
    public R listMyNotifications(Long userId, Integer page, Integer size, Integer type) {
        Page<Notification> pageObj = new Page<>(page, size);
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        if (type != null) {
            wrapper.eq(Notification::getType, type);
        }
        wrapper.orderByDesc(Notification::getCreateTime);
        notificationMapper.selectPage(pageObj, wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("records", pageObj.getRecords());
        result.put("total", pageObj.getTotal());
        return R.ok(result);
    }

    @Override
    public R getUnreadCount(Long userId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        wrapper.eq(Notification::getIsRead, Constants.MSG_UNREAD);
        long count = notificationMapper.selectCount(wrapper);
        Map<String, Long> result = new HashMap<>();
        result.put("count", count);
        return R.ok(result);
    }

    @Override
    public R markAsRead(Long id, Long userId) {
        Notification notification = notificationMapper.selectById(id);
        if (notification == null) {
            return R.fail("通知不存在");
        }
        if (!notification.getUserId().equals(userId)) {
            return R.fail("无权操作该通知");
        }
        notification.setIsRead(Constants.MSG_READ);
        notificationMapper.updateById(notification);
        return R.ok("已标为已读");
    }

    @Override
    @Transactional
    public R markAllAsRead(Long userId) {
        LambdaQueryWrapper<Notification> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Notification::getUserId, userId);
        wrapper.eq(Notification::getIsRead, Constants.MSG_UNREAD);
        Notification update = new Notification();
        update.setIsRead(Constants.MSG_READ);
        notificationMapper.update(update, wrapper);
        return R.ok("已全部标为已读");
    }

    @Override
    public R deleteNotification(Long id) {
        notificationMapper.deleteById(id);
        return R.ok("通知已删除");
    }
}
