package com.projmaster.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bugs")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String reproduceSteps;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity = Severity.NORMAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BugStatus status = BugStatus.NEW;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Environment environment = Environment.TEST;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requirement_id")
    private Requirement requirement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @Column(length = 50)
    private String version;

    @Column(length = 50)
    private String module;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime resolvedAt;

    private LocalDateTime closedAt;

    @OneToMany(mappedBy = "bug", cascade = CascadeType.ALL)
    private List<Attachment> attachments = new ArrayList<>();

    public enum Severity {
        FATAL,      // 致命
        SERIOUS,    // 严重
        NORMAL,     // 一般
        MINOR       // 轻微
    }

    public enum BugStatus {
        NEW,            // 新建
        CONFIRMED,      // 已确认
        ACCEPTED,       // 已接受
        REJECTED,       // 已拒绝
        POSTPONED,      // 已延期
        FIXED,          // 已解决
        PENDING_VERIFY, // 待验证
        CLOSED,         // 已关闭
        REOPENED        // 重新打开
    }

    public enum Environment {
        DEV,        // 开发环境
        TEST,       // 测试环境
        STAGING,    // 预发环境
        PROD        // 生产环境
    }
}
