package com.kressin.fitness_app.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateExercisePlanRequest(
        @NotNull Long id,
        Boolean shouldDelete,
        Long exerciseId,
        @Valid List<UpdateExerciseSetRequest> sets) {
}
