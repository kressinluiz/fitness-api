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

import com.kressin.fitness_app.dto.CreateWorkoutRequest;
import com.kressin.fitness_app.dto.UpdateExercisePlansOrderRequest;
import com.kressin.fitness_app.dto.UpdateWorkoutRequest;
import com.kressin.fitness_app.dto.WorkoutResponse;
import com.kressin.fitness_app.mapper.WorkoutMapper;
import com.kressin.fitness_app.service.WorkoutService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/workouts")
@Tag(name = "Workouts", description = "Workout management")
public class WorkoutController {
    private final WorkoutService service;

    public WorkoutController(WorkoutService workoutService) {
        this.service = workoutService;
    }

    @Operation(summary = "Create a workout")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Workout created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
    })
    @PostMapping
    public ResponseEntity<WorkoutResponse> addWorkout(@Valid @RequestBody CreateWorkoutRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addWorkout(WorkoutMapper.toCreateCommand(request)));
    }

    @Operation(summary = "Get a workout")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workout found"),
            @ApiResponse(responseCode = "404", description = "Workout not found"),
    })
    @GetMapping("/{id}")
    public WorkoutResponse getWorkout(@PathVariable Long id) {
        return service.getWorkout(id);
    }

    @Operation(summary = "Get all workouts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workouts found"),
    })
    @GetMapping
    public Page<WorkoutResponse> getWorkouts(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) String search) {
        return service.getAllWorkouts(pageable, search);
    }

    @Operation(summary = "Update a workout")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workout updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Workout not found"),
    })
    @PatchMapping("/{id}")
    public WorkoutResponse updateWorkout(@PathVariable Long id, @Valid @RequestBody UpdateWorkoutRequest request) {
        return service.updateWorkout(WorkoutMapper.toUpdateCommand(request, id));
    }

    @Operation(summary = "Delete a workout")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Workout deleted"),
            @ApiResponse(responseCode = "404", description = "Workout not found"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        service.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update exercise plans order for an specific workout")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Workout updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Workout not found"),
    })
    @PatchMapping("/{id}/exercise-plans/order")
    public WorkoutResponse updateWorkout(@PathVariable Long id, @RequestBody UpdateExercisePlansOrderRequest request) {
        return service.updateWorkoutExercisePlansOrder(WorkoutMapper.toUpdateCommand(id, request));
    }
}
