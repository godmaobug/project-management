package com.projmaster.service.impl;

import com.projmaster.dto.request.ProjectCreateRequest;
import com.projmaster.dto.response.ProjectMemberResponse;
import com.projmaster.dto.response.ProjectResponse;
import com.projmaster.dto.response.UserResponse;
import com.projmaster.entity.Project;
import com.projmaster.entity.ProjectMember;
import com.projmaster.entity.User;
import com.projmaster.exception.ResourceNotFoundException;
import com.projmaster.exception.UnauthorizedException;
import com.projmaster.repository.ProjectMemberRepository;
import com.projmaster.repository.ProjectRepository;
import com.projmaster.repository.UserRepository;
import com.projmaster.service.ProjectService;
import com.projmaster.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public List<ProjectResponse> getMyProjects() {
        User currentUser = userService.getCurrentUserEntity();
        return projectRepository.findByMemberId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findByDeletedFalseAndArchivedFalse()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponse getProjectById(Long id) {
        Project project = getProjectEntity(id);
        return toResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse createProject(ProjectCreateRequest request) {
        User currentUser = userService.getCurrentUserEntity();

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setType(request.getType());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setOwner(currentUser);
        project.setVisibility(request.getVisibility());
        project.setStatus(Project.ProjectStatus.ACTIVE);
        project.setArchived(false);
        project.setDeleted(false);

        Project saved = projectRepository.save(project);

        // 创建者自动成为项目成员，角色为管理者
        ProjectMember member = new ProjectMember();
        member.setProject(saved);
        member.setUser(currentUser);
        member.setRole(ProjectMember.ProjectRole.MANAGER);
        projectMemberRepository.save(member);

        return toResponse(saved);
    }

    @Override
    @Transactional
    public ProjectResponse updateProject(Long id, ProjectCreateRequest request) {
        Project project = getProjectEntity(id);
        checkProjectPermission(project);

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setType(request.getType());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setVisibility(request.getVisibility());

        return toResponse(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        Project project = getProjectEntity(id);
        checkProjectPermission(project);
        project.setDeleted(true);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void archiveProject(Long id) {
        Project project = getProjectEntity(id);
        checkProjectPermission(project);
        project.setArchived(true);
        project.setStatus(Project.ProjectStatus.ARCHIVED);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void addProjectMember(Long projectId, Long userId, String role) {
        Project project = getProjectEntity(projectId);
        checkProjectPermission(project);

        if (projectMemberRepository.existsByProjectIdAndUserId(projectId, userId)) {
            throw new RuntimeException("该用户已是项目成员");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在: " + userId));

        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(user);
        member.setRole(ProjectMember.ProjectRole.valueOf(role));
        projectMemberRepository.save(member);
    }

    @Override
    @Transactional
    public void removeProjectMember(Long projectId, Long userId) {
        Project project = getProjectEntity(projectId);
        checkProjectPermission(project);
        projectMemberRepository.deleteByProjectIdAndUserId(projectId, userId);
    }

    @Override
    public Project getProjectEntity(Long id) {
        return projectRepository.findById(id)
                .filter(p -> !p.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在: " + id));
    }

    private void checkProjectPermission(Project project) {
        User currentUser = userService.getCurrentUserEntity();
        if (!project.getOwner().getId().equals(currentUser.getId()) 
            && !currentUser.getRole().equals(User.UserRole.SUPER_ADMIN)) {
            throw new UnauthorizedException("没有权限操作此项目");
        }
    }

    private ProjectResponse toResponse(Project project) {
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setType(project.getType());
        response.setStartDate(project.getStartDate());
        response.setEndDate(project.getEndDate());
        response.setOwner(toUserResponse(project.getOwner()));
        response.setStatus(project.getStatus());
        response.setVisibility(project.getVisibility());
        response.setArchived(project.getArchived());
        response.setCreatedAt(project.getCreatedAt());
        
        // 计算进度
        response.setProgress(calculateProgress(project.getId()));
        
        // 加载成员
        List<ProjectMember> members = projectMemberRepository.findByProjectId(project.getId());
        response.setMembers(members.stream().map(this::toMemberResponse).collect(Collectors.toList()));
        
        return response;
    }

    private Double calculateProgress(Long projectId) {
        // 这里简化处理，实际应该基于任务完成情况计算
        return 0.0;
    }

    private ProjectMemberResponse toMemberResponse(ProjectMember member) {
        ProjectMemberResponse response = new ProjectMemberResponse();
        response.setId(member.getId());
        response.setUser(toUserResponse(member.getUser()));
        response.setRole(member.getRole());
        response.setJoinedAt(member.getJoinedAt());
        return response;
    }

    private UserResponse toUserResponse(User user) {
        if (user == null) return null;
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setAvatar(user.getAvatar());
        return response;
    }
}
