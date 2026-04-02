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
@Table(name = "requirements")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Requirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String acceptanceCriteria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequirementStatus status = RequirementStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iteration_id")
    private Iteration iteration;

    @Column(length = 50)
    private String source;

    @Column(nullable = false)
    private Integer storyPoints = 0;

    @Column(nullable = false)
    private Double progress = 0.0;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL)
    private List<Attachment> attachments = new ArrayList<>();

    public enum Priority {
        URGENT,     // 紧急
        HIGH,       // 高
        MEDIUM,     // 中
        LOW         // 低
    }

    public enum RequirementStatus {
        PENDING,        // 未开始
        REVIEWING,      // 评审中
        REJECTED,       // 已拒绝
        PLANNED,        // 已规划
        IN_PROGRESS,    // 进行中
        COMPLETED       // 已完成
    }
}
