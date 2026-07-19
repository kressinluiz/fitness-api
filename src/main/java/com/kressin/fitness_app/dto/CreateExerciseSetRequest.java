package com.kressin.fitness_app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateExerciseSetRequest(
        @NotNull @Positive Integer reps,
        @NotNull @PositiveOrZero Double weight) {
}
