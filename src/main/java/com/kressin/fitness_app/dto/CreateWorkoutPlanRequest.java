package com.kressin.fitness_app.dto;

public record CreateWorkoutPlanRequest(
        Long workoutId,
        CreateWorkoutDateRequest workoutDate) {
}
