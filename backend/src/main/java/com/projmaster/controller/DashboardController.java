package com.projmaster.controller;

import com.projmaster.dto.response.ApiResponse;
import com.projmaster.dto.response.DashboardStatsResponse;
import com.projmaster.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ApiResponse<DashboardStatsResponse> getDashboardStats() {
        return ApiResponse.success(dashboardService.getDashboardStats());
    }
}
