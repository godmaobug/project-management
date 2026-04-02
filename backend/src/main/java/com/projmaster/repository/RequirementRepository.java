package com.projmaster.repository;

import com.projmaster.entity.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long>, JpaSpecificationExecutor<Requirement> {

    List<Requirement> findByProjectId(Long projectId);

    List<Requirement> findByProjectIdAndStatus(Long projectId, Requirement.RequirementStatus status);

    List<Requirement> findByAssigneeId(Long assigneeId);

    List<Requirement> findByIterationId(Long iterationId);

    @Query("SELECT r FROM Requirement r WHERE r.project.id = :projectId AND r.iteration IS NULL")
    List<Requirement> findUnplannedByProjectId(@Param("projectId") Long projectId);

    long countByProjectId(Long projectId);

    long countByProjectIdAndStatus(Long projectId, Requirement.RequirementStatus status);
}
