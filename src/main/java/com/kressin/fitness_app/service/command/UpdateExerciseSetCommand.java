package com.kressin.fitness_app.service.command;

public record UpdateExerciseSetCommand(
        Long id,
        Boolean shouldDelete,
        Integer reps,
        Double weight) {
}
