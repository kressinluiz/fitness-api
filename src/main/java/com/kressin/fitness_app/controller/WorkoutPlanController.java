package com.kressin.fitness_app.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kressin.fitness_app.dto.CreateWorkoutPlanRequest;
import com.kressin.fitness_app.dto.UpdateWorkoutPlanRequest;
import com.kressin.fitness_app.dto.WorkoutPlanResponse;
import com.kressin.fitness_app.mapper.WorkoutPlanMapper;
import com.kressin.fitness_app.service.WorkoutPlanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/planner")
@Tag(name = "Planner", description = "Planner management")
public class WorkoutPlanController {
    private final WorkoutPlanService service;

    public WorkoutPlanController(WorkoutPlanService service) {
        this.service = service;
    }

    @Operation(summary = "Create a workout plan")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Workout plan created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
    })
    @PostMapping
    public ResponseEntity<WorkoutPlanResponse> addWorkoutPlan(@Valid @RequestBody CreateWorkoutPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addWorkoutPlan(WorkoutPlanMapper.toCreateCommand(request)));
    }

    @Operation(summary = "Get a workout plan")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workout plan found"),
            @ApiResponse(responseCode = "404", description = "Workout plan not found"),
    })
    @GetMapping("/{id}")
    public WorkoutPlanResponse getWorkoutPlan(@PathVariable Long id) {
        return service.getWorkoutPlan(id);
    }

    @Operation(summary = "Get all workout plans")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workout plans found"),
    })
    @GetMapping
    public Page<WorkoutPlanResponse> getWorkoutPlans(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) String search) {
        return service.getAllWorkoutPlans(pageable, search);
    }

    @Operation(summary = "Update a workout plan")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workout plan updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Workout plan not found"),
    })
    @PatchMapping("/{id}")
    public WorkoutPlanResponse updateWorkoutPlan(@PathVariable Long id,
            @Valid @RequestBody UpdateWorkoutPlanRequest request) {
        return service.updateWorkoutPlan(WorkoutPlanMapper.toUpdateCommand(request, id));
    }

    @Operation(summary = "Delete a workout plan")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Workout plan deleted"),
            @ApiResponse(responseCode = "404", description = "Workout plan not found"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkoutPlan(@PathVariable Long id) {
        service.deleteWorkoutPlan(id);
        return ResponseEntity.noContent().build();
    }
}
