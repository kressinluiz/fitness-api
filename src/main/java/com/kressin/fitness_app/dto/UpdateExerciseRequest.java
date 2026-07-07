package com.kressin.fitness_app.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateExerciseRequest(
        @NotNull Long id,
        String name,
        String description,
        String category,
        String muscleGroup) {
}
