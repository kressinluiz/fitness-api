package com.kressin.fitness_app.exception;

public class WorkoutPlanNotFoundException extends RuntimeException {
    public WorkoutPlanNotFoundException(Long id) {
        super("WorkoutPlan with id %d not found".formatted(id));
    }
}
