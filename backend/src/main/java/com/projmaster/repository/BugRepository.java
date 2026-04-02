package com.projmaster.repository;

import com.projmaster.entity.Bug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugRepository extends JpaRepository<Bug, Long>, JpaSpecificationExecutor<Bug> {

    List<Bug> findByProjectId(Long projectId);

    List<Bug> findByAssigneeId(Long assigneeId);

    List<Bug> findByReporterId(Long reporterId);

    List<Bug> findByRequirementId(Long requirementId);

    long countByProjectId(Long projectId);

    long countByProjectIdAndStatus(Long projectId, Bug.BugStatus status);

    long countByStatus(Bug.BugStatus status);
}
