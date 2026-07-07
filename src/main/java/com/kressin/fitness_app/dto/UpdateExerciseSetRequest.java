package com.kressin.fitness_app.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateExerciseSetRequest(
        @NotNull Long id,
        Boolean shouldDelete,
        Integer reps,
        Double weight) {
}
