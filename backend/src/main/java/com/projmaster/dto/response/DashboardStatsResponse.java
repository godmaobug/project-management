package com.projmaster.dto.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DashboardStatsResponse {

    private Integer totalProjects;
    private Integer totalRequirements;
    private Integer totalTasks;
    private Integer totalBugs;

    private Integer pendingTasks;
    private Integer inProgressTasks;
    private Integer completedTasks;

    private Integer newBugs;
    private Integer resolvedBugs;
    private Integer closedBugs;

    private List<ProjectProgress> projectProgresses;
    private List<TodoItem> todoItems;
    private List<RecentActivity> recentActivities;

    @Data
    public static class ProjectProgress {
        private Long projectId;
        private String projectName;
        private Double progress;
        private Integer totalTasks;
        private Integer completedTasks;
    }

    @Data
    public static class TodoItem {
        private Long id;
        private String title;
        private String type;  // TASK, REQUIREMENT, BUG
        private String status;
        private String priority;
        private Long projectId;
        private String projectName;
    }

    @Data
    public static class RecentActivity {
        private String action;
        private String entityType;
        private String entityTitle;
        private String userName;
        private String timeAgo;
    }
}
