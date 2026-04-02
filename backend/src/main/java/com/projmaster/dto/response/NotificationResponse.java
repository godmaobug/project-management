package com.projmaster.dto.response;

import com.projmaster.entity.Notification;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {

    private Long id;
    private String title;
    private String content;
    private Notification.NotificationType type;
    private String entityType;
    private Long entityId;
    private Boolean read;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
}
