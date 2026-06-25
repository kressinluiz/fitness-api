package com.kressin.fitness_app.dto;

public record UpdateWorkoutPlanRequest(
    Long id,
    Long workoutId,
    UpdateWorkoutDateRequest workoutDate
) {
}
