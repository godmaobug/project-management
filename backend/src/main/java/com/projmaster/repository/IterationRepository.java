package com.projmaster.repository;

import com.projmaster.entity.Iteration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IterationRepository extends JpaRepository<Iteration, Long> {

    List<Iteration> findByProjectId(Long projectId);

    List<Iteration> findByProjectIdAndStatus(Long projectId, Iteration.IterationStatus status);

    Optional<Iteration> findByProjectIdAndStatusAndIdNot(Long projectId, Iteration.IterationStatus status, Long excludeId);
}
