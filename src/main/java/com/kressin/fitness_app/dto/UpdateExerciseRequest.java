package com.kressin.fitness_app.dto;

import jakarta.validation.constraints.Size;

public record UpdateExerciseRequest(
        @Size(min = 1, max = 100) String name,
        @Size(min = 1, max = 500) String description,
        String category,
        String muscleGroup) {
}
