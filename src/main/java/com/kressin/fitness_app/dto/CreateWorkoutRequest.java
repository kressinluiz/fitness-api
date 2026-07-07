package com.kressin.fitness_app.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateWorkoutRequest(
        @NotBlank String name,
        @NotNull String description,
        @Valid List<CreateExercisePlanRequest> exercisePlans) {
}
