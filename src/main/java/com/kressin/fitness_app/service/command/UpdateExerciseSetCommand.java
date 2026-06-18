package com.kressin.fitness_app.service.command;

public record UpdateExerciseSetCommand(
    Long id,
    Integer reps,
    Double weight) {
}
