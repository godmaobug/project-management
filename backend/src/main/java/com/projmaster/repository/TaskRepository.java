package com.projmaster.repository;

import com.projmaster.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    List<Task> findByProjectId(Long projectId);

    List<Task> findByAssigneeId(Long assigneeId);

    List<Task> findByAssigneeIdAndStatusIn(Long assigneeId, List<Task.TaskStatus> statuses);

    List<Task> findByIterationId(Long iterationId);

    List<Task> findByRequirementId(Long requirementId);

    List<Task> findByParentId(Long parentId);

    long countByProjectId(Long projectId);

    long countByProjectIdAndStatus(Long projectId, Task.TaskStatus status);

    long countByIterationId(Long iterationId);

    long countByIterationIdAndStatus(Long iterationId, Task.TaskStatus status);
}
