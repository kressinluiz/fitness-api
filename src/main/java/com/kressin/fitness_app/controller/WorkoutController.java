package com.kressin.fitness_app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kressin.fitness_app.dto.CreateWorkoutRequest;
import com.kressin.fitness_app.dto.UpdateWorkoutRequest;
import com.kressin.fitness_app.dto.WorkoutResponse;
import com.kressin.fitness_app.mapper.WorkoutMapper;
import com.kressin.fitness_app.service.WorkoutService;

@RestController
@RequestMapping("/workout")
public class WorkoutController {
    private final WorkoutService service;

    public WorkoutController(WorkoutService workoutService) {
        this.service = workoutService;
    }

    @PostMapping
    public WorkoutResponse addWorkout(@RequestBody CreateWorkoutRequest request) {
        return service.addWorkout(WorkoutMapper.toCreateCommand(request));
    }

    @PutMapping
    public WorkoutResponse updateWorkout(@RequestBody UpdateWorkoutRequest request) {
        return service.updateWorkout(WorkoutMapper.toUpdateCommand(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteWorkout(@RequestParam Long id) {
        service.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public WorkoutResponse getWorkout(@PathVariable Long id) {
        return service.getWorkout(id);
    }

    // maybe return a response with only id, name, description
    // if user hits to see details of the workout,
    // we map a getWorkout(id) with all the details.
    @GetMapping
    public List<WorkoutResponse> getWorkouts() {
        return service.getAllWorkouts();
    }

}
