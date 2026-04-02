package com.projmaster.service;

import com.projmaster.dto.request.ProjectCreateRequest;
import com.projmaster.dto.response.ProjectResponse;
import com.projmaster.entity.Project;

import java.util.List;

public interface ProjectService {

    List<ProjectResponse> getMyProjects();

    List<ProjectResponse> getAllProjects();

    ProjectResponse getProjectById(Long id);

    ProjectResponse createProject(ProjectCreateRequest request);

    ProjectResponse updateProject(Long id, ProjectCreateRequest request);

    void deleteProject(Long id);

    void archiveProject(Long id);

    void addProjectMember(Long projectId, Long userId, String role);

    void removeProjectMember(Long projectId, Long userId);

    Project getProjectEntity(Long id);
}
