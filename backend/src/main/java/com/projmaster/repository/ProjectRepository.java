package com.projmaster.repository;

import com.projmaster.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByDeletedFalseAndArchivedFalse();

    List<Project> findByOwnerIdAndDeletedFalse(Long ownerId);

    @Query("SELECT p FROM Project p WHERE p.deleted = false AND " +
           "(p.visibility = 'PUBLIC' OR p.id IN " +
           "(SELECT pm.project.id FROM ProjectMember pm WHERE pm.user.id = :userId))")
    List<Project> findAccessibleProjects(@Param("userId") Long userId);

    @Query("SELECT p FROM Project p WHERE p.deleted = false AND " +
           "p.id IN (SELECT pm.project.id FROM ProjectMember pm WHERE pm.user.id = :userId)")
    List<Project> findByMemberId(@Param("userId") Long userId);
}
