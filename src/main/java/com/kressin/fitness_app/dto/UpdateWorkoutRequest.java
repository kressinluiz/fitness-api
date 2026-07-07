package com.kressin.fitness_app.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateWorkoutRequest(
        @NotNull Long id,
        String name,
        String description,
        @Valid List<UpdateExercisePlanRequest> exercisePlans) {
}
