package com.projmaster.service;

import com.projmaster.dto.response.DashboardStatsResponse;
import com.projmaster.entity.*;
import com.projmaster.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final RequirementRepository requirementRepository;
    private final TaskRepository taskRepository;
    private final BugRepository bugRepository;
    private final UserService userService;

    public DashboardStatsResponse getDashboardStats() {
        User currentUser = userService.getCurrentUserEntity();
        
        DashboardStatsResponse stats = new DashboardStatsResponse();
        
        // 获取用户参与的项目
        List<Project> myProjects = projectRepository.findByMemberId(currentUser.getId());
        List<Long> projectIds = myProjects.stream().map(Project::getId).collect(Collectors.toList());
        
        // 基本统计
        stats.setTotalProjects(myProjects.size());
        
        long totalReqs = projectIds.stream().mapToLong(requirementRepository::countByProjectId).sum();
        stats.setTotalRequirements((int) totalReqs);
        
        long totalTasks = projectIds.stream().mapToLong(taskRepository::countByProjectId).sum();
        stats.setTotalTasks((int) totalTasks);
        
        long totalBugs = projectIds.stream().mapToLong(bugRepository::countByProjectId).sum();
        stats.setTotalBugs((int) totalBugs);
        
        // 任务状态统计
        List<Task> myTasks = taskRepository.findByAssigneeId(currentUser.getId());
        stats.setPendingTasks((int) myTasks.stream().filter(t -> t.getStatus() == Task.TaskStatus.TODO).count());
        stats.setInProgressTasks((int) myTasks.stream().filter(t -> t.getStatus() == Task.TaskStatus.IN_PROGRESS).count());
        stats.setCompletedTasks((int) myTasks.stream().filter(t -> t.getStatus() == Task.TaskStatus.DONE).count());
        
        // Bug状态统计
        List<Bug> myBugs = bugRepository.findByAssigneeId(currentUser.getId());
        stats.setNewBugs((int) myBugs.stream().filter(b -> b.getStatus() == Bug.BugStatus.NEW).count());
        stats.setResolvedBugs((int) myBugs.stream().filter(b -> b.getStatus() == Bug.BugStatus.FIXED).count());
        stats.setClosedBugs((int) myBugs.stream().filter(b -> b.getStatus() == Bug.BugStatus.CLOSED).count());
        
        // 项目进度
        List<DashboardStatsResponse.ProjectProgress> progresses = new ArrayList<>();
        for (Project project : myProjects) {
            DashboardStatsResponse.ProjectProgress progress = new DashboardStatsResponse.ProjectProgress();
            progress.setProjectId(project.getId());
            progress.setProjectName(project.getName());
            
            long pTotalTasks = taskRepository.countByProjectId(project.getId());
            long pCompletedTasks = taskRepository.countByProjectIdAndStatus(project.getId(), Task.TaskStatus.DONE);
            
            progress.setTotalTasks((int) pTotalTasks);
            progress.setCompletedTasks((int) pCompletedTasks);
            progress.setProgress(pTotalTasks == 0 ? 0.0 : (double) pCompletedTasks / pTotalTasks * 100);
            
            progresses.add(progress);
        }
        stats.setProjectProgresses(progresses);
        
        // 待办事项（当前用户待处理的任务、需求、Bug）
        List<DashboardStatsResponse.TodoItem> todoItems = new ArrayList<>();
        
        // 待办任务
        taskRepository.findByAssigneeIdAndStatusIn(currentUser.getId(), 
                List.of(Task.TaskStatus.TODO, Task.TaskStatus.IN_PROGRESS))
            .forEach(task -> {
                DashboardStatsResponse.TodoItem item = new DashboardStatsResponse.TodoItem();
                item.setId(task.getId());
                item.setTitle(task.getTitle());
                item.setType("TASK");
                item.setStatus(task.getStatus().name());
                item.setPriority(task.getPriority().name());
                item.setProjectId(task.getProject().getId());
                item.setProjectName(task.getProject().getName());
                todoItems.add(item);
            });
        
        // 待办需求
        requirementRepository.findByAssigneeId(currentUser.getId())
            .stream()
            .filter(r -> r.getStatus() == Requirement.RequirementStatus.IN_PROGRESS)
            .forEach(req -> {
                DashboardStatsResponse.TodoItem item = new DashboardStatsResponse.TodoItem();
                item.setId(req.getId());
                item.setTitle(req.getTitle());
                item.setType("REQUIREMENT");
                item.setStatus(req.getStatus().name());
                item.setPriority(req.getPriority().name());
                item.setProjectId(req.getProject().getId());
                item.setProjectName(req.getProject().getName());
                todoItems.add(item);
            });
        
        // 待处理Bug
        bugRepository.findByAssigneeId(currentUser.getId())
            .stream()
            .filter(b -> b.getStatus() == Bug.BugStatus.NEW || b.getStatus() == Bug.BugStatus.CONFIRMED)
            .forEach(bug -> {
                DashboardStatsResponse.TodoItem item = new DashboardStatsResponse.TodoItem();
                item.setId(bug.getId());
                item.setTitle(bug.getTitle());
                item.setType("BUG");
                item.setStatus(bug.getStatus().name());
                item.setPriority(bug.getSeverity().name());
                item.setProjectId(bug.getProject().getId());
                item.setProjectName(bug.getProject().getName());
                todoItems.add(item);
            });
        
        stats.setTodoItems(todoItems);
        
        // 最近活动（简化处理）
        List<DashboardStatsResponse.RecentActivity> activities = new ArrayList<>();
        stats.setRecentActivities(activities);
        
        return stats;
    }
}
