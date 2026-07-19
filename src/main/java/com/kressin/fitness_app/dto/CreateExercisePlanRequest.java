package com.kressin.fitness_app.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateExercisePlanRequest(
        @NotNull Long exerciseId,
        List<@Valid CreateExerciseSetRequest> sets) {
}
