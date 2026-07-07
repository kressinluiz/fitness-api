package com.kressin.fitness_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kressin.fitness_app.dto.CreateWorkoutPlanRequest;
import com.kressin.fitness_app.dto.UpdateWorkoutPlanRequest;
import com.kressin.fitness_app.dto.WorkoutPlanResponse;
import com.kressin.fitness_app.mapper.WorkoutPlanMapper;
import com.kressin.fitness_app.service.WorkoutPlanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/planner")
public class WorkoutPlanController {
    private final WorkoutPlanService service;

    public WorkoutPlanController(WorkoutPlanService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<WorkoutPlanResponse> addWorkoutPlan(@Valid @RequestBody CreateWorkoutPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addWorkoutPlan(WorkoutPlanMapper.toCreateCommand(request)));
    }

    @PatchMapping("/{id}")
    public WorkoutPlanResponse updateWorkoutPlan(@PathVariable Long id,
            @Valid @RequestBody UpdateWorkoutPlanRequest request) {
        return service.updateWorkoutPlan(WorkoutPlanMapper.toUpdateCommand(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkoutPlan(@PathVariable Long id) {
        service.deleteWorkoutPlan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public WorkoutPlanResponse getWorkoutPlan(@PathVariable Long id) {
        return service.getWorkoutPlan(id);
    }

    @GetMapping
    public List<WorkoutPlanResponse> getWorkoutPlans() {
        return service.getAllWorkoutPlans();
    }
}
