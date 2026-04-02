package com.projmaster.service.impl;

import com.projmaster.dto.request.BugCreateRequest;
import com.projmaster.dto.response.BugResponse;
import com.projmaster.dto.response.UserResponse;
import com.projmaster.entity.Bug;
import com.projmaster.entity.Project;
import com.projmaster.entity.Requirement;
import com.projmaster.entity.User;
import com.projmaster.exception.ResourceNotFoundException;
import com.projmaster.repository.BugRepository;
import com.projmaster.repository.ProjectRepository;
import com.projmaster.repository.RequirementRepository;
import com.projmaster.repository.UserRepository;
import com.projmaster.service.BugService;
import com.projmaster.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BugServiceImpl implements BugService {

    private final BugRepository bugRepository;
    private final ProjectRepository projectRepository;
    private final RequirementRepository requirementRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public List<BugResponse> getBugsByProject(Long projectId) {
        return bugRepository.findByProjectId(projectId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BugResponse> getMyBugs() {
        User currentUser = userService.getCurrentUserEntity();
        return bugRepository.findByAssigneeId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BugResponse getBugById(Long id) {
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bug不存在: " + id));
        return toResponse(bug);
    }

    @Override
    @Transactional
    public BugResponse createBug(BugCreateRequest request) {
        User currentUser = userService.getCurrentUserEntity();
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在"));

        Bug bug = new Bug();
        bug.setTitle(request.getTitle());
        bug.setDescription(request.getDescription());
        bug.setReproduceSteps(request.getReproduceSteps());
        bug.setSeverity(request.getSeverity());
        bug.setEnvironment(request.getEnvironment());
        bug.setStatus(Bug.BugStatus.NEW);
        bug.setProject(project);
        bug.setReporter(currentUser);
        bug.setVersion(request.getVersion());
        bug.setModule(request.getModule());

        if (request.getRequirementId() != null) {
            Requirement requirement = requirementRepository.findById(request.getRequirementId())
                    .orElseThrow(() -> new ResourceNotFoundException("需求不存在"));
            bug.setRequirement(requirement);
        }

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("指派人不存在"));
            bug.setAssignee(assignee);
        }

        return toResponse(bugRepository.save(bug));
    }

    @Override
    @Transactional
    public BugResponse updateBug(Long id, BugCreateRequest request) {
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bug不存在: " + id));

        bug.setTitle(request.getTitle());
        bug.setDescription(request.getDescription());
        bug.setReproduceSteps(request.getReproduceSteps());
        bug.setSeverity(request.getSeverity());
        bug.setEnvironment(request.getEnvironment());
        bug.setVersion(request.getVersion());
        bug.setModule(request.getModule());

        if (request.getRequirementId() != null) {
            Requirement requirement = requirementRepository.findById(request.getRequirementId())
                    .orElseThrow(() -> new ResourceNotFoundException("需求不存在"));
            bug.setRequirement(requirement);
        }

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("指派人不存在"));
            bug.setAssignee(assignee);
        }

        return toResponse(bugRepository.save(bug));
    }

    @Override
    @Transactional
    public void deleteBug(Long id) {
        bugRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateBugStatus(Long id, Bug.BugStatus status) {
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bug不存在: " + id));
        bug.setStatus(status);
        
        if (status == Bug.BugStatus.FIXED) {
            bug.setResolvedAt(java.time.LocalDateTime.now());
        } else if (status == Bug.BugStatus.CLOSED) {
            bug.setClosedAt(java.time.LocalDateTime.now());
        }
        
        bugRepository.save(bug);
    }

    @Override
    @Transactional
    public void updateBugAssignee(Long id, Long assigneeId) {
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bug不存在: " + id));
        
        if (assigneeId == null) {
            bug.setAssignee(null);
        } else {
            User assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new ResourceNotFoundException("指派人不存在"));
            bug.setAssignee(assignee);
        }
        bugRepository.save(bug);
    }

    private BugResponse toResponse(Bug bug) {
        BugResponse response = new BugResponse();
        response.setId(bug.getId());
        response.setTitle(bug.getTitle());
        response.setDescription(bug.getDescription());
        response.setReproduceSteps(bug.getReproduceSteps());
        response.setSeverity(bug.getSeverity());
        response.setStatus(bug.getStatus());
        response.setEnvironment(bug.getEnvironment());
        response.setProjectId(bug.getProject().getId());
        response.setProjectName(bug.getProject().getName());
        
        if (bug.getRequirement() != null) {
            response.setRequirementId(bug.getRequirement().getId());
            response.setRequirementTitle(bug.getRequirement().getTitle());
        }
        
        response.setReporter(toUserResponse(bug.getReporter()));
        response.setAssignee(toUserResponse(bug.getAssignee()));
        response.setVersion(bug.getVersion());
        response.setModule(bug.getModule());
        response.setCreatedAt(bug.getCreatedAt());
        response.setUpdatedAt(bug.getUpdatedAt());
        response.setResolvedAt(bug.getResolvedAt());
        response.setClosedAt(bug.getClosedAt());
        
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
