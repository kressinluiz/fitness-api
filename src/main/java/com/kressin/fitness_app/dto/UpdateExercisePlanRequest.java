package com.kressin.fitness_app.dto;

import java.util.List;

import jakarta.validation.Valid;

public record UpdateExercisePlanRequest(
        Long id,
        Boolean shouldDelete,
        Long exerciseId,
        @Valid List<UpdateExerciseSetRequest> sets) {
}
