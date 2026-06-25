package com.kressin.fitness_app.controller;

import java.util.List;

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

@RestController
@RequestMapping("/planner")
public class WorkoutPlanController {
    private final WorkoutPlanService service;

    public WorkoutPlanController(WorkoutPlanService service) {
        this.service = service;
    }

    @PostMapping
    public WorkoutPlanResponse addWorkoutPlan(@RequestBody CreateWorkoutPlanRequest request) {
        return service.addWorkoutPlan(WorkoutPlanMapper.toCreateCommand(request));
    }

    @PatchMapping
    public WorkoutPlanResponse updateWorkoutPlan(@RequestBody UpdateWorkoutPlanRequest request) {
        return service.updateWorkoutPlan(WorkoutPlanMapper.toUpdateCommand(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteWorkoutPlan(@RequestParam Long id) {
        service.deleteWorkoutPlan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public WorkoutPlanResponse getWorkoutPlan(@PathVariable Long id) {
        return service.getWorkoutPlan(id);
    }

    @GetMapping
    public List<WorkoutPlanResponse> getWorkoutPlans() {
        return service.getWorkoutPlans();
    }
}
