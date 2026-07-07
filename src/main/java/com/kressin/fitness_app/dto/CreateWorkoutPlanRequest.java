package com.kressin.fitness_app.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateWorkoutPlanRequest(
        @NotNull Long workoutId,
        @NotNull @Valid CreateWorkoutDateRequest workoutDate) {
}
