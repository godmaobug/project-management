package com.projmaster.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "burn_down_data")
@Data
@EntityListeners(AuditingEntityListener.class)
public class BurnDownData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iteration_id", nullable = false)
    private Iteration iteration;

    @Column(nullable = false)
    private LocalDate recordDate;

    @Column(nullable = false)
    private Double remainingPoints;

    @Column(nullable = false)
    private Double completedPoints;

    @Column(nullable = false)
    private Integer newTasks;

    @Column(nullable = false)
    private Integer completedTasks;

    @CreatedDate
    private LocalDateTime createdAt;
}
