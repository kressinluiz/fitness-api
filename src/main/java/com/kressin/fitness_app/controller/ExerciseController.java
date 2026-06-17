package com.kressin.fitness_app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kressin.fitness_app.dto.CreateExerciseRequest;
import com.kressin.fitness_app.dto.ExerciseResponse;
import com.kressin.fitness_app.dto.UpdateExerciseRequest;
import com.kressin.fitness_app.mapper.ExerciseMapper;
import com.kressin.fitness_app.service.ExerciseService;

@RestController
@RequestMapping("/exercise")
public class ExerciseController {
    private final ExerciseService service;

    public ExerciseController(ExerciseService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ExerciseResponse getExercise(@PathVariable Long id) {
        return service.getExercise(id);
    }

    @GetMapping
    public List<ExerciseResponse> getExercises() {
        return service.getAllExercises();
    }

    @PostMapping
    public ExerciseResponse addExercise(CreateExerciseRequest request) {
        return service.addExercise(ExerciseMapper.toCreateCommand(request));
    }

    @PutMapping
    public ExerciseResponse updateExercise(UpdateExerciseRequest request) {
        return service.updateExercise(ExerciseMapper.toUpdateCommand(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteExercise(@RequestParam Long id) {
        service.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
