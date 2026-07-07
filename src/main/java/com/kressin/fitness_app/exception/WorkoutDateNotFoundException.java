package com.kressin.fitness_app.exception;

public class WorkoutDateNotFoundException extends RuntimeException {
    public WorkoutDateNotFoundException(Long id) {
        super("WorkoutDate with id %d not found".formatted(id));
    }
}
