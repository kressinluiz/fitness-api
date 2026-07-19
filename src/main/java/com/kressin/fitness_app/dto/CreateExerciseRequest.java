package com.kressin.fitness_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateExerciseRequest(
        @NotBlank @Size(min = 1, max = 100) String name,
        @NotNull @Size(min = 1, max = 500) String description,
        @NotNull String category,
        @NotNull String muscleGroup) {
}
