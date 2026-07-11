package com.kressin.fitness_app.dto;

import jakarta.validation.Valid;

public record UpdateWorkoutPlanRequest(
        Long workoutId,
        @Valid UpdateWorkoutDateRequest workoutDate) {
}
