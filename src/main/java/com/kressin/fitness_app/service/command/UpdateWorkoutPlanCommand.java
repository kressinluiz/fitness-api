package com.kressin.fitness_app.service.command;

public record UpdateWorkoutPlanCommand(
    Long id,
    Long workoutId,
    UpdateWorkoutDateCommand workoutDate
) {

}
