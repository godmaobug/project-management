package com.projmaster.service.impl;

import com.projmaster.dto.request.TaskCreateRequest;
import com.projmaster.dto.response.TaskResponse;
import com.projmaster.dto.response.UserResponse;
import com.projmaster.entity.*;
import com.projmaster.exception.ResourceNotFoundException;
import com.projmaster.repository.*;
import com.projmaster.service.TaskService;
import com.projmaster.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final RequirementRepository requirementRepository;
    private final IterationRepository iterationRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public List<TaskResponse> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getTasksByIteration(Long iterationId) {
        return taskRepository.findByIterationId(iterationId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getTasksByAssignee(Long assigneeId) {
        return taskRepository.findByAssigneeId(assigneeId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getMyTasks() {
        User currentUser = userService.getCurrentUserEntity();
        return getTasksByAssignee(currentUser.getId());
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("任务不存在: " + id));
        return toResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse createTask(TaskCreateRequest request) {
        User currentUser = userService.getCurrentUserEntity();
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在"));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setType(request.getType());
        task.setPriority(request.getPriority());
        task.setStatus(Task.TaskStatus.TODO);
        task.setProject(project);
        task.setCreator(currentUser);
        task.setEstimatedHours(request.getEstimatedHours() != null ? request.getEstimatedHours() : 0.0);
        task.setActualHours(0.0);
        task.setStartDate(request.getStartDate());
        task.setDueDate(request.getDueDate());

        if (request.getRequirementId() != null) {
            Requirement requirement = requirementRepository.findById(request.getRequirementId())
                    .orElseThrow(() -> new ResourceNotFoundException("需求不存在"));
            task.setRequirement(requirement);
        }

        if (request.getIterationId() != null) {
            Iteration iteration = iterationRepository.findById(request.getIterationId())
                    .orElseThrow(() -> new ResourceNotFoundException("迭代不存在"));
            task.setIteration(iteration);
        }

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("指派人不存在"));
            task.setAssignee(assignee);
        }

        if (request.getParentId() != null) {
            Task parent = taskRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("父任务不存在"));
            task.setParent(parent);
        }

        if (request.getDependsOnId() != null) {
            Task dependsOn = taskRepository.findById(request.getDependsOnId())
                    .orElseThrow(() -> new ResourceNotFoundException("依赖任务不存在"));
            task.setDependsOn(dependsOn);
        }

        return toResponse(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long id, TaskCreateRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("任务不存在: " + id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setType(request.getType());
        task.setPriority(request.getPriority());
        task.setEstimatedHours(request.getEstimatedHours());
        task.setStartDate(request.getStartDate());
        task.setDueDate(request.getDueDate());

        if (request.getRequirementId() != null) {
            Requirement requirement = requirementRepository.findById(request.getRequirementId())
                    .orElseThrow(() -> new ResourceNotFoundException("需求不存在"));
            task.setRequirement(requirement);
        }

        if (request.getIterationId() != null) {
            Iteration iteration = iterationRepository.findById(request.getIterationId())
                    .orElseThrow(() -> new ResourceNotFoundException("迭代不存在"));
            task.setIteration(iteration);
        }

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("指派人不存在"));
            task.setAssignee(assignee);
        }

        return toResponse(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateTaskStatus(Long id, Task.TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("任务不存在: " + id));
        task.setStatus(status);
        taskRepository.save(task);
    }

    @Override
    @Transactional
    public void updateTaskAssignee(Long id, Long assigneeId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("任务不存在: " + id));
        
        if (assigneeId == null) {
            task.setAssignee(null);
        } else {
            User assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new ResourceNotFoundException("指派人不存在"));
            task.setAssignee(assignee);
        }
        taskRepository.save(task);
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setType(task.getType());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setProjectId(task.getProject().getId());
        response.setProjectName(task.getProject().getName());
        
        if (task.getRequirement() != null) {
            response.setRequirementId(task.getRequirement().getId());
            response.setRequirementTitle(task.getRequirement().getTitle());
        }
        
        if (task.getIteration() != null) {
            response.setIterationId(task.getIteration().getId());
            response.setIterationName(task.getIteration().getName());
        }
        
        response.setAssignee(toUserResponse(task.getAssignee()));
        response.setCreator(toUserResponse(task.getCreator()));
        response.setParentId(task.getParent() != null ? task.getParent().getId() : null);
        response.setEstimatedHours(task.getEstimatedHours());
        response.setActualHours(task.getActualHours());
        response.setStartDate(task.getStartDate());
        response.setDueDate(task.getDueDate());
        response.setDependsOnId(task.getDependsOn() != null ? task.getDependsOn().getId() : null);
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        
        // 加载子任务
        List<Task> subtasks = taskRepository.findByParentId(task.getId());
        response.setSubtasks(subtasks.stream().map(this::toResponse).collect(Collectors.toList()));
        
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
