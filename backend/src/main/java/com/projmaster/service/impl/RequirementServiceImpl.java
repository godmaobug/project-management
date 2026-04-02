package com.projmaster.service.impl;

import com.projmaster.dto.request.RequirementCreateRequest;
import com.projmaster.dto.response.RequirementResponse;
import com.projmaster.dto.response.UserResponse;
import com.projmaster.entity.Iteration;
import com.projmaster.entity.Project;
import com.projmaster.entity.Requirement;
import com.projmaster.entity.User;
import com.projmaster.exception.ResourceNotFoundException;
import com.projmaster.repository.IterationRepository;
import com.projmaster.repository.ProjectRepository;
import com.projmaster.repository.RequirementRepository;
import com.projmaster.repository.UserRepository;
import com.projmaster.service.RequirementService;
import com.projmaster.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequirementServiceImpl implements RequirementService {

    private final RequirementRepository requirementRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IterationRepository iterationRepository;
    private final UserService userService;

    @Override
    public List<RequirementResponse> getRequirementsByProject(Long projectId) {
        return requirementRepository.findByProjectId(projectId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequirementResponse> getUnplannedRequirements(Long projectId) {
        return requirementRepository.findUnplannedByProjectId(projectId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RequirementResponse getRequirementById(Long id) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("需求不存在: " + id));
        return toResponse(requirement);
    }

    @Override
    @Transactional
    public RequirementResponse createRequirement(RequirementCreateRequest request) {
        User currentUser = userService.getCurrentUserEntity();
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在"));

        Requirement requirement = new Requirement();
        requirement.setTitle(request.getTitle());
        requirement.setDescription(request.getDescription());
        requirement.setAcceptanceCriteria(request.getAcceptanceCriteria());
        requirement.setPriority(request.getPriority());
        requirement.setProject(project);
        requirement.setCreator(currentUser);
        requirement.setStatus(Requirement.RequirementStatus.PENDING);
        requirement.setSource(request.getSource());
        requirement.setStoryPoints(request.getStoryPoints() != null ? request.getStoryPoints() : 0);
        requirement.setProgress(0.0);

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("指派人不存在"));
            requirement.setAssignee(assignee);
        }

        return toResponse(requirementRepository.save(requirement));
    }

    @Override
    @Transactional
    public RequirementResponse updateRequirement(Long id, RequirementCreateRequest request) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("需求不存在: " + id));

        requirement.setTitle(request.getTitle());
        requirement.setDescription(request.getDescription());
        requirement.setAcceptanceCriteria(request.getAcceptanceCriteria());
        requirement.setPriority(request.getPriority());
        requirement.setSource(request.getSource());
        if (request.getStoryPoints() != null) {
            requirement.setStoryPoints(request.getStoryPoints());
        }

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("指派人不存在"));
            requirement.setAssignee(assignee);
        }

        return toResponse(requirementRepository.save(requirement));
    }

    @Override
    @Transactional
    public void deleteRequirement(Long id) {
        requirementRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateRequirementStatus(Long id, Requirement.RequirementStatus status) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("需求不存在: " + id));
        requirement.setStatus(status);
        requirementRepository.save(requirement);
    }

    @Override
    @Transactional
    public void assignToIteration(Long requirementId, Long iterationId) {
        Requirement requirement = requirementRepository.findById(requirementId)
                .orElseThrow(() -> new ResourceNotFoundException("需求不存在: " + requirementId));
        
        if (iterationId == null) {
            requirement.setIteration(null);
        } else {
            Iteration iteration = iterationRepository.findById(iterationId)
                    .orElseThrow(() -> new ResourceNotFoundException("迭代不存在: " + iterationId));
            requirement.setIteration(iteration);
            requirement.setStatus(Requirement.RequirementStatus.PLANNED);
        }
        requirementRepository.save(requirement);
    }

    private RequirementResponse toResponse(Requirement requirement) {
        RequirementResponse response = new RequirementResponse();
        response.setId(requirement.getId());
        response.setTitle(requirement.getTitle());
        response.setDescription(requirement.getDescription());
        response.setAcceptanceCriteria(requirement.getAcceptanceCriteria());
        response.setPriority(requirement.getPriority());
        response.setStatus(requirement.getStatus());
        response.setProjectId(requirement.getProject().getId());
        response.setProjectName(requirement.getProject().getName());
        response.setCreator(toUserResponse(requirement.getCreator()));
        response.setAssignee(toUserResponse(requirement.getAssignee()));
        if (requirement.getIteration() != null) {
            response.setIterationId(requirement.getIteration().getId());
            response.setIterationName(requirement.getIteration().getName());
        }
        response.setSource(requirement.getSource());
        response.setStoryPoints(requirement.getStoryPoints());
        response.setProgress(requirement.getProgress());
        response.setCreatedAt(requirement.getCreatedAt());
        response.setUpdatedAt(requirement.getUpdatedAt());
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
