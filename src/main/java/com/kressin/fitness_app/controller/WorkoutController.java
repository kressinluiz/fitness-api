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

import com.kressin.fitness_app.dto.CreateWorkoutRequest;
import com.kressin.fitness_app.dto.UpdateWorkoutRequest;
import com.kressin.fitness_app.dto.WorkoutResponse;
import com.kressin.fitness_app.mapper.WorkoutMapper;
import com.kressin.fitness_app.service.WorkoutService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/workout")
public class WorkoutController {
    private final WorkoutService service;

    public WorkoutController(WorkoutService workoutService) {
        this.service = workoutService;
    }

    @PostMapping
    public ResponseEntity<WorkoutResponse> addWorkout(@Valid @RequestBody CreateWorkoutRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addWorkout(WorkoutMapper.toCreateCommand(request)));
    }

    @PatchMapping("/{id}")
    public WorkoutResponse updateWorkout(@PathVariable Long id, @Valid @RequestBody UpdateWorkoutRequest request) {
        return service.updateWorkout(WorkoutMapper.toUpdateCommand(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        service.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public WorkoutResponse getWorkout(@PathVariable Long id) {
        return service.getWorkout(id);
    }

    @GetMapping
    public List<WorkoutResponse> getWorkouts() {
        return service.getAllWorkouts();
    }

}
