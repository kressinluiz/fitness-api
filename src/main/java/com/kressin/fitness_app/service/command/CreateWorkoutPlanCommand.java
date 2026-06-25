package com.kressin.fitness_app.service.command;

public record CreateWorkoutPlanCommand(
        Long workoutId,
        CreateWorkoutDateCommand workoutDate) {
}
