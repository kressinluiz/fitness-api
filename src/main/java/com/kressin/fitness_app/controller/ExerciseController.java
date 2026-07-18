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

import com.kressin.fitness_app.dto.CreateExerciseRequest;
import com.kressin.fitness_app.dto.ExerciseResponse;
import com.kressin.fitness_app.dto.UpdateExerciseRequest;
import com.kressin.fitness_app.dto.UsageCountResponse;
import com.kressin.fitness_app.mapper.ExerciseMapper;
import com.kressin.fitness_app.service.ExerciseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/exercises")
@Tag(name = "Exercises", description = "Exercise management")
public class ExerciseController {
    private final ExerciseService service;

    public ExerciseController(ExerciseService service) {
        this.service = service;
    }

    @Operation(summary = "Create an exercise")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Exercise created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
    })
    @PostMapping
    public ResponseEntity<ExerciseResponse> addExercise(@Valid @RequestBody CreateExerciseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addExercise(ExerciseMapper.toCreateCommand(request)));
    }

    @Operation(summary = "Get an exercise")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exercise found"),
            @ApiResponse(responseCode = "404", description = "Exercise not found"),
    })
    @GetMapping("/{id}")
    public ExerciseResponse getExercise(@PathVariable Long id) {
        return service.getExercise(id);
    }

    @Operation(summary = "Get all exercises")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exercises found"),
    })
    @GetMapping
    public Page<ExerciseResponse> getExercises(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) String search) {
        return service.getAllExercises(pageable, search);
    }

    @Operation(summary = "Update an exercise")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exercise updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Exercise not found"),
    })
    @PatchMapping("/{id}")
    public ExerciseResponse updateExercise(@PathVariable Long id, @Valid @RequestBody UpdateExerciseRequest request) {
        return service.updateExercise(ExerciseMapper.toUpdateCommand(request, id));
    }

    @Operation(summary = "Delete an exercise")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Exercise deleted"),
            @ApiResponse(responseCode = "404", description = "Exercise not found"),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        service.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get exercise usage count")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exercise found"),
            @ApiResponse(responseCode = "404", description = "Exercise not found"),
    })
    @GetMapping("/{id}/usage")
    public UsageCountResponse getUsageCount(@PathVariable Long id) {
        return service.countExerciseUsage(id);
    }
}
