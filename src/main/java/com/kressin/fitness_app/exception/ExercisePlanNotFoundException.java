package com.kressin.fitness_app.exception;

public class ExercisePlanNotFoundException extends RuntimeException {
    public ExercisePlanNotFoundException(Long id) {
        super("ExercisePlan with id %d not found".formatted(id));
    }
}
