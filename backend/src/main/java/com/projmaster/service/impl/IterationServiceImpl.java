package com.projmaster.service.impl;

import com.projmaster.dto.request.IterationCreateRequest;
import com.projmaster.dto.response.IterationResponse;
import com.projmaster.entity.Iteration;
import com.projmaster.entity.Project;
import com.projmaster.entity.Task;
import com.projmaster.exception.ResourceNotFoundException;
import com.projmaster.repository.IterationRepository;
import com.projmaster.repository.ProjectRepository;
import com.projmaster.repository.TaskRepository;
import com.projmaster.service.IterationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IterationServiceImpl implements IterationService {

    private final IterationRepository iterationRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Override
    public List<IterationResponse> getIterationsByProject(Long projectId) {
        return iterationRepository.findByProjectId(projectId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public IterationResponse getIterationById(Long id) {
        Iteration iteration = iterationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("迭代不存在: " + id));
        return toResponse(iteration);
    }

    @Override
    @Transactional
    public IterationResponse createIteration(IterationCreateRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("项目不存在"));

        Iteration iteration = new Iteration();
        iteration.setName(request.getName());
        iteration.setGoal(request.getGoal());
        iteration.setStartDate(request.getStartDate());
        iteration.setEndDate(request.getEndDate());
        iteration.setProject(project);
        iteration.setStatus(Iteration.IterationStatus.PLANNING);
        iteration.setTotalStoryPoints(0.0);
        iteration.setCompletedStoryPoints(0.0);

        return toResponse(iterationRepository.save(iteration));
    }

    @Override
    @Transactional
    public IterationResponse updateIteration(Long id, IterationCreateRequest request) {
        Iteration iteration = iterationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("迭代不存在: " + id));

        iteration.setName(request.getName());
        iteration.setGoal(request.getGoal());
        iteration.setStartDate(request.getStartDate());
        iteration.setEndDate(request.getEndDate());

        return toResponse(iterationRepository.save(iteration));
    }

    @Override
    @Transactional
    public void deleteIteration(Long id) {
        iterationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateIterationStatus(Long id, Iteration.IterationStatus status) {
        Iteration iteration = iterationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("迭代不存在: " + id));
        iteration.setStatus(status);
        iterationRepository.save(iteration);
    }

    @Override
    @Transactional
    public void startIteration(Long id) {
        Iteration iteration = iterationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("迭代不存在: " + id));
        
        // 检查是否有其他进行中的迭代
        iterationRepository.findByProjectIdAndStatusAndIdNot(
                iteration.getProject().getId(), 
                Iteration.IterationStatus.ACTIVE, 
                id
        ).ifPresent(activeIteration -> {
            throw new RuntimeException("已存在进行中的迭代: " + activeIteration.getName());
        });
        
        iteration.setStatus(Iteration.IterationStatus.ACTIVE);
        iterationRepository.save(iteration);
    }

    @Override
    @Transactional
    public void completeIteration(Long id) {
        Iteration iteration = iterationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("迭代不存在: " + id));
        iteration.setStatus(Iteration.IterationStatus.COMPLETED);
        iterationRepository.save(iteration);
    }

    private IterationResponse toResponse(Iteration iteration) {
        IterationResponse response = new IterationResponse();
        response.setId(iteration.getId());
        response.setName(iteration.getName());
        response.setGoal(iteration.getGoal());
        response.setStartDate(iteration.getStartDate());
        response.setEndDate(iteration.getEndDate());
        response.setProjectId(iteration.getProject().getId());
        response.setProjectName(iteration.getProject().getName());
        response.setStatus(iteration.getStatus());
        response.setTotalStoryPoints(iteration.getTotalStoryPoints());
        response.setCompletedStoryPoints(iteration.getCompletedStoryPoints());
        response.setProgress(iteration.getProgress());
        
        // 统计任务数
        long totalTasks = taskRepository.countByIterationId(iteration.getId());
        long completedTasks = taskRepository.countByIterationIdAndStatus(iteration.getId(), Task.TaskStatus.DONE);
        response.setTotalTasks((int) totalTasks);
        response.setCompletedTasks((int) completedTasks);
        
        response.setCreatedAt(iteration.getCreatedAt());
        response.setUpdatedAt(iteration.getUpdatedAt());
        
        return response;
    }
}
