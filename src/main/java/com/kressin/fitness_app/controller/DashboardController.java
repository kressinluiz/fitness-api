package com.kressin.fitness_app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kressin.fitness_app.dto.DashboardSummaryResponse;
import com.kressin.fitness_app.service.DashboardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Dashboard summary")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @Operation(summary = "Get dashboard summary")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dashboard summary found"),
    })
    @GetMapping("/summary")
    public DashboardSummaryResponse getSummary() {
        return service.getSummary();
    }
}
