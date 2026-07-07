package com.kressin.fitness_app.dto;

import jakarta.validation.constraints.NotNull;

public record CreateExerciseSetRequest(
        @NotNull Integer reps,
        @NotNull Double weight) {
}
