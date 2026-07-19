package com.kressin.fitness_app.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateExerciseSetRequest(
        Long id,
        Boolean shouldDelete,
        @Positive Integer reps,
        @PositiveOrZero Double weight) {
}
