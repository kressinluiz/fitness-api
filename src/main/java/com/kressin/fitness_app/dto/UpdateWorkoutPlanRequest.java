package com.kressin.fitness_app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateWorkoutPlanRequest(
        @NotNull Long id,
        Long workoutId,
        @Valid UpdateWorkoutDateRequest workoutDate) {
}
