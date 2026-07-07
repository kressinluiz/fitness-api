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

import com.kressin.fitness_app.dto.CreateExerciseRequest;
import com.kressin.fitness_app.dto.ExerciseResponse;
import com.kressin.fitness_app.dto.UpdateExerciseRequest;
import com.kressin.fitness_app.mapper.ExerciseMapper;
import com.kressin.fitness_app.service.ExerciseService;

import jakarta.validation.Valid;

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
    public ResponseEntity<ExerciseResponse> addExercise(@Valid @RequestBody CreateExerciseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addExercise(ExerciseMapper.toCreateCommand(request)));
    }

    @PatchMapping("/{id}")
    public ExerciseResponse updateExercise(@PathVariable Long id, @Valid @RequestBody UpdateExerciseRequest request) {
        return service.updateExercise(ExerciseMapper.toUpdateCommand(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        service.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}
