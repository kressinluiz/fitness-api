package com.kressin.fitness_app.exception;

public class ExerciseSetNotFoundException extends RuntimeException {
    public ExerciseSetNotFoundException(Long id) {
        super("ExerciseSet with id %d not found".formatted(id));
    }
}
