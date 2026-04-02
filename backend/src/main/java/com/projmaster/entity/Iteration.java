package com.projmaster.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "iterations")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Iteration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String goal;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IterationStatus status = IterationStatus.PLANNING;

    @Column(nullable = false)
    private Double totalStoryPoints = 0.0;

    @Column(nullable = false)
    private Double completedStoryPoints = 0.0;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "iteration")
    private List<Requirement> requirements = new ArrayList<>();

    @OneToMany(mappedBy = "iteration")
    private List<Task> tasks = new ArrayList<>();

    public enum IterationStatus {
        PLANNING,   // 规划中
        ACTIVE,     // 进行中
        COMPLETED   // 已完成
    }

    public Double getProgress() {
        if (totalStoryPoints == 0) return 0.0;
        return (completedStoryPoints / totalStoryPoints) * 100;
    }
}
