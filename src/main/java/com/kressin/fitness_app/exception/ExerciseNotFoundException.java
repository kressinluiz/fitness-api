package com.kressin.fitness_app.exception;

public class ExerciseNotFoundException extends RuntimeException {
    public ExerciseNotFoundException(Long id) {
        super("Exercise with id %d not found".formatted(id));
    }
}
