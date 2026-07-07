package com.kressin.fitness_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateExerciseRequest(
        @NotBlank String name,
        @NotNull String description,
        @NotNull String category,
        @NotNull String muscleGroup) {
}
