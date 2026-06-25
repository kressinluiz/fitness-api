package com.kressin.fitness_app.dto;

public record WorkoutPlanResponse(
        Long id,
        WorkoutResponse workout,
        WorkoutDateResponse workoutDate) {
}
