package com.kressin.fitness_app.exception;

public class WorkoutNotFoundException extends RuntimeException {
    public WorkoutNotFoundException(Long id) {
        super("Workout with id %d not found".formatted(id));
    }
}
