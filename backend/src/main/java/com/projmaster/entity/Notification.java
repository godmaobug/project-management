package com.projmaster.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false, length = 50)
    private String entityType;

    @Column(nullable = false)
    private Long entityId;

    @Column(nullable = false)
    private Boolean read = false;

    private LocalDateTime readAt;

    @CreatedDate
    private LocalDateTime createdAt;

    public enum NotificationType {
        TASK_ASSIGNED,      // 任务分配
        STATUS_CHANGED,     // 状态变更
        MENTION,            // @提及
        DUE_SOON,           // 即将到期
        COMMENT,            // 评论通知
        BUG_ASSIGNED,       // Bug分配
        ITERATION_STARTED   // 迭代开始
    }
}
